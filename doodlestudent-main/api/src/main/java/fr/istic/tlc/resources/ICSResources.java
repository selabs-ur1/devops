package fr.istic.tlc.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import fr.istic.tlc.dao.ChoiceRepository;
import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.dao.UserRepository;
import fr.istic.tlc.domain.Choice;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.dto.EventDTO;
import fr.istic.tlc.dto.EventDTOAndSelectedChoice;
import fr.istic.tlc.services.Utils;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.MapTimeZoneCache;

@Path("/api/ics")
public class ICSResources {

	@Inject
	PollRepository pollRep;

	@Inject
	UserRepository userRep;

	@Inject
	ChoiceRepository choiceRep;

	@GET
	@Path("polls/{slug}/{ics}")
	@javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
	public EventDTOAndSelectedChoice parseCalendartoAppointments(@PathParam("slug") String slug,
			@PathParam("ics") String ics)
			throws IOException, ParserException, InterruptedException, ExecutionException, MessagingException {
		EventDTOAndSelectedChoice result = new EventDTOAndSelectedChoice();
		List<EventDTO> appointments = new ArrayList<>();
		List<Long> selectedChoices = new ArrayList<>();
		result.setEventdtos(appointments);
		result.setSelectedChoices(selectedChoices);

		// Get Poll
		Poll p = this.pollRep.findBySlug(slug);
		Date minDate = new Date();
		if (p != null) {
			// Get minimal date for Poll to filter ics
			
			if (p.getPollChoices().size() > 0 && minDate.after(p.getPollChoices().get(0).getstartDate()))
				minDate = p.getPollChoices().get(0).getstartDate();
		}

		// Get user to get its ICS
		// User u = this.userRep.find("mail", usermail).firstResult();
		byte[] decodedBytes = Base64.getDecoder().decode(ics);
		String decodedString = new String(decodedBytes);

		if (decodedString != null && !"".equals(decodedString)) {
			// String s =
			// "http://zimbra.inria.fr/home/olivier.barais@irisa.fr/Calendar.ics";

			// Query the ics url

			System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
			CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
			client.start();
			HttpGet request = new HttpGet(decodedString);

			Future<HttpResponse> future = client.execute(request, null);
			HttpResponse response = future.get();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			String responseString = out.toString();
			out.close();
			client.close();

			// Parse result
			StringReader sin = new StringReader(responseString);
			CalendarBuilder builder = new CalendarBuilder();
			Calendar calendar = builder.build(sin);
			ComponentList<CalendarComponent> events = calendar.getComponents(Component.VEVENT);
			List<Choice> choices =  new ArrayList<Choice>();
			if (p!= null)
				choices = p.getPollChoices();
			// Create Event to draw
			java.util.Calendar calEnd = java.util.Calendar.getInstance();
			calEnd.setTime(new Date());
			calEnd.add(java.util.Calendar.YEAR, 1);
			DateTime start = new DateTime(minDate);
			DateTime end = new DateTime(calEnd.getTime());
			for (CalendarComponent event : events) {

				Period period = new Period(start, end);
				PeriodList list = event.calculateRecurrenceSet(period);
				for (Period p1 : list) {
					if (minDate.before(p1.getStart())) {
						EventDTO a = new EventDTO();
						a.setStartDate(p1.getStart());
						a.setEndDate(p1.getEnd());
						if (((VEvent) event).getSummary() != null)
							a.setDescription(((VEvent) event).getSummary().getValue());

						// Si intersection ajoute l'ID du choice comme ID selected
						// https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap
						for (Choice choice : choices) {
							if (Utils.getInstance().intersect(choice.getstartDate(), choice.getendDate(), p1.getStart(),
									p1.getEnd())) {
								if (!selectedChoices.contains(choice.getId())) {
									selectedChoices.add(choice.getId());
								}
							}
						}
						appointments.add(a);
					}

				}
			}

		}

		return result;
	}

}
