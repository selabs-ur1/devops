package fr.istic.tlc.resources;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.sun.mail.iap.ConnectionException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.sun.mail.iap.ConnectionException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import fr.istic.tlc.dao.ChoiceRepository;
import fr.istic.tlc.dao.CommentRepository;
import fr.istic.tlc.dao.MealPreferenceRepository;
import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.dao.UserRepository;
import fr.istic.tlc.domain.Choice;
import fr.istic.tlc.domain.Comment;
import fr.istic.tlc.domain.Carpooling;
import fr.istic.tlc.domain.MealPreference;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.domain.User;
import fr.istic.tlc.dto.ChoiceUser;
import fr.istic.tlc.services.SendEmail;

@Path("/api/poll")
public class NewPollResourceEx {

	@Inject
	PollRepository pollRep;

	@Inject
	UserRepository userRep;

	@Inject
	ChoiceRepository choiceRep;

	@Inject
	MealPreferenceRepository mealprefRep;

	@Inject
	CommentRepository commentRep;

	@Inject
	SendEmail sendmail;

	@Path("/slug/{slug}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Poll getPollBySlug(@PathParam("slug") String slug)
		throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

		System.out.println("/api/poll/slug/{slug}");

		Poll p = pollRep.findBySlug(slug);
		if (p != null) {
			System.out.println("poll exist");
			p.getPollComments().clear();
		}

		p.setSlugAdmin("");
		return p;
	}

	@Path("/aslug/{aslug}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Poll getPollByASlug(@PathParam("aslug") String aslug) {
		return pollRep.findByAdminSlug(aslug);
	}

	@Path("/comment/{slug}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	public Comment createComment4Poll(@PathParam("slug") String slug, Comment c)
		throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {
		this.commentRep.persist(c);
		Poll p = pollRep.findBySlug(slug);
		p.addComment(c);
		this.pollRep.persistAndFlush(p);
		return c;
	}

	@PUT
	@Path("/update1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	public Poll updatePoll(Poll p) {
		System.err.println( "p " + p);
		Poll p1 = pollRep.findById(p.getId());
		List<Choice> choicesToRemove = new ArrayList<Choice>();
		for (Choice c : p1.getPollChoices()) {
			if (!p.getPollChoices().contains(c)) {

				choicesToRemove.add(c);
				System.err.println("toremove " + c.getId());
			}

		}
		for (Choice c : p.getPollChoices()) {
			if (c.getId() != null) {
				this.choiceRep.getEntityManager().merge(c);
			} else {
				this.choiceRep.getEntityManager().persist(c);
			}

		}
		for (Choice c : choicesToRemove) {
			if (c.equals(p1.getSelectedChoice())) {
				p.setSelectedChoice(null);
				p1.setSelectedChoice(null);
				p.setClos(false);
			}
			for (User u : c.getUsers()) {
				u.getUserChoices().remove(c);
			}
			c.getUsers().clear();
			this.choiceRep.delete(c);

		}

		for (Choice c : p.getPollChoices()) {
			System.err.println("tomerge " + c.getId());
		}

		Poll p2 = this.pollRep.getEntityManager().merge(p);
		return p2;

	}

	@Path("/choiceuser/{pollId}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public User addChoiceUser(@PathParam("pollId") int pollId, ChoiceUser userChoice)
	throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {
		System.out.println("addChoiceUser() - New Poll Ressource");
		User u = this.userRep.find("mail", userChoice.getMail()).firstResult();
		if (u == null) {
			u = new User();
			u.setUsername(userChoice.getUsername());
			u.setIcsurl(userChoice.getIcs());
			u.setMail(userChoice.getMail());
			this.userRep.persist(u);
		}


		if (userChoice.getPref() != null && !"".equals(userChoice.getPref())) {
			MealPreference mp = new MealPreference();
			mp.setContent(userChoice.getPref());
			mp.setUser(u);
			//this.mealprefRep.persist(mp);
			Poll poll = pollRep.findById(pollId);
			poll.addMealPreference(mp);
		}
		for (Long choiceId : userChoice.getChoices()) {
			Choice c = this.choiceRep.findById(choiceId);
			c.addUser(u);
			this.choiceRep.persistAndFlush(c);
		}
		return u;
	}

	@Path("/selectedchoice/{choiceid}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void closePoll(@PathParam("choiceid") String choiceid) {
		Choice c = choiceRep.findById(Long.parseLong(choiceid));
		Poll p = this.pollRep.find("select p from Poll as p join p.pollChoices as c where c.id= ?1", c.getId())
				.firstResult();
		p.setClos(true);
		p.setSelectedChoice(c);
		this.pollRep.persist(p);
		this.sendmail.sendASimpleEmail(p);
	}

	@Path("/{pollId}/carpooling/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void addCarpooling(@PathParam("pollId") String pollId, Carpooling carpooling)
	throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {
		System.out.println("addCarpooling(" + pollId + ") - NewPollRessourceEx");

		Poll p = this.pollRep.findById(Long.parseLong(pollId));
		if (p != null) {
			System.out.println("poll exist => POST new Carpooling");
			String response = p.addCarpooling(carpooling);
			System.out.println("Response NewPollResourceEx addCarpooler: " + response);
		}
	}

	@Path("/carpooling/{carpoolingId}/{mail}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public void addCarpooler(@PathParam("mail") String mail, @PathParam("carpoolingId") String carpoolingId) {
		//System.out.println("addCarpooler() - NewPollRessourceEx");
		System.out.println("addCarpooler("+carpoolingId+","+mail+") - NewPollRessourceEx");
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
														.uri(URI.create("http://localhost:3005/poll/carpooling/"+carpoolingId+"/"+mail))
														.header("Content-Type", "application/json")
														.build();
		HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
																	.join();

		System.out.println("json carpooling updated : " + response.body());
		Type carpoolingType = new TypeToken<Carpooling>(){}.getType();
		Carpooling cp = new Gson().fromJson(response.body(), carpoolingType);

		if (cp.isOnCarpooling(mail)) {
			System.out.println("Sending Email Fro Carpooling Reservation");
			this.sendmail.sendASimpleEmail(cp, mail);
		}
	}

	@GET()
	@Path("polls/{slug}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getAllCommentsFromPoll(@PathParam("slug") String slug)
		throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {
		Poll p = this.pollRep.findBySlug(slug);
		if (p!= null)
			return p.getPollComments();
		return null;
	}

}
