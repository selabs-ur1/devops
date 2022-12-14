package fr.istic.tlc.services;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.domain.User;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

@ApplicationScoped
public class SendEmail {

	@Inject
	Mailer mailer;

	@Inject
	PollRepository pollRep;

	@ConfigProperty(name = "doodle.organizermail")
	String organizermail= "test@test.fr";
	public void sendASimpleEmail(Poll p )  {
		// Create a default MimeMessage object.
		System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());

		List<User> u = this.pollRep.findAllUser4Poll(p.getId());
		List<String> attendees = new ArrayList<String>();
		for (User u1 : u) {
			attendees.add(u1.getMail());
		}
		
		String ics = this.getICS1(p.getSelectedChoice().getstartDate(), p.getSelectedChoice().getendDate(), p.getTitle(), attendees, organizermail);
		Mail m = new Mail();
		m.addAttachment("meeting.ics", ics.getBytes(), "text/calendar");
	
		m.setFrom(organizermail);
		m.setTo(attendees);
		m.setCc(Arrays.asList(organizermail));
		m.setFrom(organizermail);
		m.setSubject("Réunion c" + p.getTitle() + " [créneau confirmé] ");
		m.setHtml("La date définitive pour la réunion : \""+ p.getTitle() + "\" a été validée par l\'organisateur. <BR>" + 
				"Un salon a été créé de discussion pour cette réunion est accessible à cette adresse <a [href]=\" " +p.getTlkURL() + "\" target=\"_blank\">" + p.getTlkURL() + "</a>.<BR>\n" + 
				"Un pad a été créé pour cette réunion <a [href]=\""+ p.getPadURL() + "\" target=\"_blank\">\""+ p.getPadURL() + "\"</a>.</span><BR>\n");
		
		mailer.send(m);
		
	}

	
	public String getICS1(Date start, Date end, String libelle, List<String> attendees, String organizer) {

		// Create a TimeZone
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("Europe/Paris");
		VTimeZone tz = timezone.getVTimeZone();

		// Create the event
		DateTime startd = new DateTime(start);
		DateTime endd = new DateTime(end);
		VEvent meeting = new VEvent(startd, endd, libelle);
		// add timezone info..
		meeting.getProperties().add(tz.getTimeZoneId());

		// generate unique identifier..
		UidGenerator ug = new RandomUidGenerator();
		Uid uid = ug.generateUid();
		meeting.getProperties().add(uid);


		// add attendees..
		for (String attendee : attendees) {
			Attendee p1 = new Attendee(URI.create("mailto:"+attendee));
			p1.getParameters().add(Role.REQ_PARTICIPANT);
//			dev1.getParameters().add(new Cn("Developer 1"));
			meeting.getProperties().add(p1);			
		}
		Organizer p1 = new Organizer(URI.create("mailto:"+organizer));
		meeting.getProperties().add(p1);	


		// Create a calendar
		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(Version.VERSION_2_0);
		icsCalendar.getProperties().add(new ProdId("Zimbra-Calendar-Provider"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Method.REQUEST);
		icsCalendar.getComponents().add(tz);

		// Add the event and print
		icsCalendar.getComponents().add(meeting);
				
		return icsCalendar.toString();
	}


}
