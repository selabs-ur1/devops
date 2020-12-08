package fr.istic.tlc.resources.features;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.istic.tlc.dao.ChoiceRepository;
import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.dao.UserRepository;
import fr.istic.tlc.domain.Choice;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.domain.User;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Path("/api")
public class ExportResource {
	@Inject
	ChoiceRepository choiceRepository;
	@Inject
	PollRepository pollRepository;
	@Inject
	UserRepository userRepository;

	
	@ConfigProperty(name = "doodle.tmpfolder")
	String EXCEL_FILE_LOCATION = "/tmp/excelFiles";


	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/polls/{slug}/results")
	public Response downloadResultsExcel(@PathParam("slug") String slug) throws IOException {
		Poll poll = pollRepository.findBySlug(slug);
		if (poll == null) {
			return null;
		}
		String filePath = createExcelFile(poll, slug);
		return getHttpEntityToDownload(filePath, "vnd.ms-excel");
	}

	/*
	 * @RequestMapping(value = "/polls/{slug}/print", method = RequestMethod.GET,
	 * produces = APPLICATION_PDF) public @ResponseBody HttpEntity<byte[]>
	 * downloadResultsPdf(@PathVariable String slug) throws IOException { Poll poll
	 * = pollRepository.findBySlug(slug); if (poll == null) { return new
	 * ResponseEntity<>(HttpStatus.NOT_FOUND); } String filePath = "./Test.xls";
	 * //Utils.excel2pdf(); //convertToPdf(filePath); return
	 * getHttpEntityToDownload(filePath,"pdf"); }
	 */

	/*
	 * private String convertToPdf(String filePath){ return "";
	 * 
	 * }
	 */

	static int beginningColumnCell = 0;
	static int beginningRowCell = 3;
	static int fontSize = 9;
	static Colour borderColour = Colour.WHITE;

	private String createExcelFile(Poll poll, String slug) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy-HH.mm.ss");
		Date date = new Date();
		String fileName = EXCEL_FILE_LOCATION + File.separator + slug + "-" + dateFormat.format(date) + ".xls";

		File folder = new File(EXCEL_FILE_LOCATION);
		if (!folder.exists()) {
			folder.mkdir();
		}

		// Create an Excel file
		WritableWorkbook Wbook = null;
		try {
			System.out.println("Création du fichier");
			// Create an Excel file in the file location
			File file = new File(fileName);

			if (!file.createNewFile()) {
				System.out.println("Erreur lors de la création du fichier");
			}
			Wbook = Workbook.createWorkbook(file);

			// Create an Excel sheet
			WritableSheet mainSheet = Wbook.createSheet("SONDAGE", 0);
			Wbook.setColourRGB(Colour.BLUE, 53, 37, 230);

			// Format objects
			WritableCellFormat formatTitle = new WritableCellFormat();
			WritableFont fontTitle = new WritableFont(WritableFont.TAHOMA, 16, WritableFont.BOLD);
			fontTitle.setColour(Colour.BLUE);
			formatTitle.setFont(fontTitle);

			Label label;
			label = new Label(0, 0, "Sondage \"" + poll.getTitle() + "\"", formatTitle);
			mainSheet.addCell(label);
			label = new Label(0, 1, "http://localhost:3000/polls/" + poll.getSlug());
			mainSheet.addCell(label);

			// On récupere les users qui ont voté dans ce sondage
			List<User> users = retrieveUsers(poll);

			// On ecrit les users sur la première colonne
			writeUsers(poll, Wbook, users);

			// On ecrit les choix avec les votes de chaque users
			writeChoices(poll, Wbook, users);

			System.out.println("Enregistrement du fichier");
			// On ecrit les donnée du workbook dans un format excel
			Wbook.write();

		} catch (Exception e) {
			System.out.println("Erreur lors de la création du fichier :( " + e.toString());
			e.printStackTrace();
		} finally {

			if (Wbook != null) {
				try {
					Wbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return fileName;
	}

	private List<User> retrieveUsers(Poll poll) {
		List<User> users = new ArrayList<>();
		// On parcours les choix du poll pour récupérer les users ayant voté
		if (!poll.getPollChoices().isEmpty()) {
			for (Choice choice : poll.getPollChoices()) {
				if (!choice.getUsers().isEmpty()) {
					for (User user : choice.getUsers()) {
						// On vérifie que le user ne soit pas déjà dans la liste
						if (!users.contains(user)) {
							users.add(user);
						}
					}
				}
			}
		}
		return users;
	}

	private void writeChoices(Poll poll, WritableWorkbook Wbook, List<User> users) throws jxl.write.WriteException {
		Label label;
		Number number;
		WritableSheet mainSheet = Wbook.getSheet(0);

		List<fr.istic.tlc.domain.Choice> choices = poll.getPollChoices();
		// List<Choice> choices =
		// choiceRepository.findAll(Sort.by(Sort.Direction.ASC,"startDate"));

		// Format objects
		WritableCellFormat formatVoteYes = new WritableCellFormat();
		formatVoteYes.setAlignment(Alignment.CENTRE);
		formatVoteYes.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatVoteYes.setBorder(Border.ALL, BorderLineStyle.THIN, borderColour);
		formatVoteYes.setBackground(Colour.LIGHT_GREEN);
		WritableFont fontVoteYes = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.NO_BOLD);
		fontVoteYes.setColour(Colour.BLACK);
		formatVoteYes.setFont(fontVoteYes);
		// Format objects
		Wbook.setColourRGB(Colour.LIGHT_ORANGE, 255, 195, 195);
		WritableCellFormat formatVoteNo = new WritableCellFormat();
		formatVoteNo.setAlignment(Alignment.CENTRE);
		formatVoteNo.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatVoteNo.setBorder(Border.ALL, BorderLineStyle.THIN, borderColour);
		formatVoteNo.setBackground(Colour.LIGHT_ORANGE);
		WritableFont fontVoteNo = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.NO_BOLD);
		fontVoteNo.setColour(Colour.BLACK);
		formatVoteNo.setFont(fontVoteNo);

		// On ecrit les colonnes des choix
		for (int i = 0; i < choices.size(); i++) {
			mainSheet.setColumnView(1 + beginningColumnCell + i, 14);
			// On ecrit la date
			writeChoiceDate(Wbook, choices, i);

			// On ecrit les votes
			List<User> listUsersVotes = choices.get(i).getUsers();
			for (int x = 0; x < users.size(); x++) {
				if (listUsersVotes.contains(users.get(x))) {
					label = new Label(1 + beginningColumnCell + i, 3 + beginningRowCell + x, "OK", formatVoteYes);
				} else {
					label = new Label(1 + beginningColumnCell + i, 3 + beginningRowCell + x, "-", formatVoteNo);
				}
				mainSheet.addCell(label);
			}
			// on ecrit le nombre total de vote pour le choix
			number = new Number(1 + beginningColumnCell + i, 3 + beginningRowCell + users.size(),
					listUsersVotes.size());
			mainSheet.addCell(number);

		}
	}

	private void writeChoiceDate(WritableWorkbook Wbook, List<Choice> choices, int i) throws jxl.write.WriteException {
		Label label;
		WritableSheet mainSheet = Wbook.getSheet(0);
		String month[] = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre",
				"Novembre", "Décembre" };
		String dayOfWeek[] = { "Lun.", "Mar.", "Mer.", "Jeu.", "Ven.", "Sam.", "Dim." };
		Wbook.setColourRGB(Colour.BLUE, 53, 37, 230);
		// Format objects
		WritableCellFormat formatDate = new WritableCellFormat();
		formatDate.setAlignment(Alignment.CENTRE);
		formatDate.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatDate.setBorder(Border.ALL, BorderLineStyle.THIN, borderColour);
		formatDate.setBackground(Colour.BLUE);
		WritableFont fontDate = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.NO_BOLD);
		fontDate.setColour(Colour.WHITE);
		formatDate.setFont(fontDate);

		// On recupère la date de début
		Choice choice = choices.get(i);
		Date startDate = choice.getstartDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int startYear = calendar.get(Calendar.YEAR);
		String startMonth = month[calendar.get(Calendar.MONTH)];
		int startDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		String startDayOfWeek = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
		int startHourInt = calendar.get(Calendar.HOUR_OF_DAY);
		String startHour = (startHourInt < 10 ? "0" : "") + startHourInt;
		int startMinuteInt = calendar.get(Calendar.MINUTE);
		String startMinute = (startMinuteInt < 10 ? "0" : "") + startMinuteInt;
		// On recupère la date de fin
		Date endDate = choice.getendDate();
		calendar.setTime(endDate);
		int endHourInt = calendar.get(Calendar.HOUR_OF_DAY);
		String endHour = (endHourInt < 10 ? "0" : "") + endHourInt;
		int endMinuteInt = calendar.get(Calendar.MINUTE);
		String endMinute = (endMinuteInt < 10 ? "0" : "") + endMinuteInt;

		label = new Label(1 + beginningColumnCell + i, beginningRowCell, startMonth + " " + startYear, formatDate);
		mainSheet.addCell(label);
		label = new Label(1 + beginningColumnCell + i, 1 + beginningRowCell, startDayOfWeek + " " + startDayOfMonth,
				formatDate);
		mainSheet.addCell(label);
		label = new Label(1 + beginningColumnCell + i, 2 + beginningRowCell,
				startHour + ":" + startMinute + " - " + endHour + ":" + endMinute, formatDate);
		mainSheet.addCell(label);
	}

	private void writeUsers(Poll poll, WritableWorkbook Wbook, List<User> users) throws jxl.write.WriteException {
		Label label;

		WritableSheet mainSheet = Wbook.getSheet(0);
		mainSheet.setColumnView(beginningColumnCell, 25);

		// Format objects
		WritableCellFormat formatUser = new WritableCellFormat();
		formatUser.setAlignment(Alignment.RIGHT);
		formatUser.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatUser.setBorder(Border.ALL, BorderLineStyle.THIN, borderColour);

		formatUser.setBackground(Colour.GRAY_25);
		WritableFont fontUser = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.NO_BOLD);
		fontUser.setColour(Colour.BLACK);
		formatUser.setFont(fontUser);

		// On ecrit la premier colonne avec users et label "Nombre"
		for (int i = 0; i < users.size(); i++) {
			label = new Label(beginningColumnCell, 3 + beginningRowCell + i, users.get(i).getUsername(), formatUser);
			mainSheet.addCell(label);
		}
		label = new Label(beginningColumnCell, 3 + beginningRowCell + users.size(), "Nombre");
		mainSheet.addCell(label);
	}

	private Response getHttpEntityToDownload(String filePath, String fileType) throws IOException {
		File file = getFile(filePath);

		// header.set("Content-Disposition", "inline; filename=" + file.getName());
		return Response.ok(((Object) file), MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
	}

	private File getFile(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("file with path: " + filePath + " was not found.");
		}
		return file;
	}

	/*
	 * @ControllerAdvice public class GlobalExceptionHandler {
	 * 
	 * @ExceptionHandler(value = FileNotFoundException.class) public void
	 * handle(FileNotFoundException ex, HttpServletResponse response) throws
	 * IOException { System.out.println("handling file not found exception");
	 * response.sendError(404, ex.getMessage()); }
	 * 
	 * @ExceptionHandler(value = IOException.class) public void handle(IOException
	 * ex, HttpServletResponse response) throws IOException {
	 * System.out.println("handling io exception"); response.sendError(500,
	 * ex.getMessage()); } }
	 */

}
