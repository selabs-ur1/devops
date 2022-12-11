package fr.istic.tlc.resources;

import fr.istic.tlc.services.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.mail.iap.ConnectionException;
import java.io.IOException;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.domain.Account;
import fr.istic.tlc.domain.Carpooling;
import fr.istic.tlc.domain.MealPreference;
import io.quarkus.panache.common.Sort;
import net.gjerull.etherpad.client.EPLiteClient;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class PollResourceEx {

	@Autowired
	PollRepository pollRepository;

	@ConfigProperty(name = "doodle.usepad")
	boolean usePad = true;
	@ConfigProperty(name = "doodle.internalPadUrl", defaultValue="http://etherpad:9001/")
	String padUrl = "";
	@ConfigProperty(name = "doodle.externalPadUrl", defaultValue="http://etherpad.diverse-team.fr/")
	String externalPadUrl = "";
	@ConfigProperty(name = "doodle.padApiKey")
	String apikey = "";
	EPLiteClient client;

	@GetMapping("/polls")
	public ResponseEntity<List<Poll>> retrieveAllpolls() {
		// On récupère la liste de tous les poll qu'on trie ensuite par titre
		List<Poll> polls = pollRepository.findAll(Sort.by("title", Sort.Direction.Ascending)).list();
		return new ResponseEntity<>(polls, HttpStatus.OK);
	}

	@GetMapping("polls/{id}/mealpreferences")
    public ResponseEntity<Object> getAllMealPreferencesFromPoll(@PathVariable int id) throws JsonMappingException, JsonProcessingException, ConnectionException, IOException, InterruptedException {
		Poll poll = pollRepository.findById(id);
		if (poll == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		System.out.println("polls/slug/mealpreferences - PollResourceEx.java");
		List<MealPreference> re = poll.getPollMealPreferences();
		return new ResponseEntity<>(re, HttpStatus.OK);
	}

	@GetMapping("/polls/{slug}")
	public ResponseEntity<Poll> retrievePoll(@PathVariable String slug, @RequestParam(required = false) String token) {
		// On vérifie que le poll existe
		Poll poll = pollRepository.findBySlug(slug);
		if (poll == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		// Si un token est donné, on vérifie qu'il soit bon
		if (token != null && !poll.getSlugAdmin().equals(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		poll.setSlugAdmin("");
		return new ResponseEntity<>(poll, HttpStatus.OK);
	}

	@GetMapping("/polls/{slug}/carpoolings")
    public ResponseEntity<Object> getCarpoolingPropositionsFromPoll(@PathVariable String slug)
        throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

		System.out.println("polls/"+slug+"/carpoolings");

		// On vérifie que le poll existe
       	Poll optPoll = pollRepository.findBySlug(slug);
        if(optPoll== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optPoll.getCarpoolingPropositionsFromPoll(), HttpStatus.OK);
    }


	@GetMapping("/polls/{slug}/pad")
	public ResponseEntity<String> retrievePadURL(@PathVariable("slug") String slug) {
		// On vérifie que le poll existe
		Poll poll = pollRepository.findBySlug(slug);
		if (poll == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(poll.getPadURL(), HttpStatus.OK);
	}

	@DeleteMapping("/polls/{slug}")
	@Transactional
	public ResponseEntity<Poll> deletePoll(@PathVariable("slug") String slug, @RequestParam String token) {
		// On vérifie que le poll existe
		Poll poll = pollRepository.findBySlug(slug);
		if (poll == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		// On vérifie que le token soit bon
		if (!poll.getSlugAdmin().equals(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// On supprime tous les choix du poll
		// Fait automatiquement par le cascade type ALL

		// On supprime tous les commentaires du poll
		// Fait automatiquement par le cascade type ALL

		// On supprime le pad
		if (client == null) {
			client = new EPLiteClient(padUrl, apikey);
		}

		client.deletePad(getPadId(poll));
		poll.deleteTlkURL();
		// On supprime le poll de la bdd
		pollRepository.deleteById(poll.getId());
		return new ResponseEntity<>(poll, HttpStatus.OK);
	}

	@PostMapping("/polls")
	@Transactional
	public ResponseEntity<Poll> createPoll(@Valid @RequestBody Poll poll) {

		// On enregistre le poll dans la bdd
		/*String padId = Utils.getInstance().generateSlug(15);
		if (this.usePad) {
			if (client == null) {
				client = new EPLiteClient(padUrl, apikey);
			}
			client.createPad(padId);
			initPad(poll.getTitle(), poll.getLocation(), poll.getDescription(), client, padId);
			poll.setPadURL(externalPadUrl + "p/" + padId);
		}*/
		pollRepository.persist(poll);
		poll.createTlkURL();
		return new ResponseEntity<>(poll, HttpStatus.CREATED);
	}

	@PutMapping("/polls/{slug}")
	@Transactional
	public ResponseEntity<Object> updatePoll(@Valid @RequestBody Poll poll, @PathVariable String slug,
			@RequestParam String token) {
		// On vérifie que le poll existe
		Poll optionalPoll = pollRepository.findBySlug(slug);
		if (optionalPoll == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		// On vérifie que le token soit bon
		if (!optionalPoll.getSlugAdmin().equals(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// On met au poll le bon id et les bons slugs
		Poll ancientPoll = optionalPoll;
		// On se connecte au pad
		String padId = getPadId(ancientPoll);

		// On sauvegarde les anciennes données pour mettre à jour le pad
		String title = ancientPoll.getTitle();
		String location = ancientPoll.getLocation();
		String description = ancientPoll.getDescription();

		// On met à jour l'ancien poll
		if (poll.getTitle() != null) {
			ancientPoll.setTitle(poll.getTitle());
		}
		if (poll.getLocation() != null) {
			ancientPoll.setLocation(poll.getLocation());
		}
		if (poll.getDescription() != null) {
			ancientPoll.setDescription(poll.getDescription());
		}
		ancientPoll.setHas_meal(poll.isHas_meal());
		// On update le pad
		String ancientPad = (String) client.getText(padId).get("text");
		ancientPad = ancientPad.replaceFirst(title, ancientPoll.getTitle());
		ancientPad = ancientPad.replaceFirst(location, ancientPoll.getLocation());
		ancientPad = ancientPad.replaceFirst(description, ancientPoll.getDescription());
		client.setText(padId, ancientPad);
		// On enregistre le poll dans la bdd
		Poll updatedPoll = pollRepository.getEntityManager().merge(ancientPoll);
		return new ResponseEntity<>(updatedPoll, HttpStatus.OK);
	}

	private static void initPad(String pollTitle, String pollLocation, String pollDescription, EPLiteClient client,
			String padId) {
		final String str = pollTitle + '\n' + "Localisation : " + pollLocation + '\n' + "Description : "
				+ pollDescription + '\n';
		client.setText(padId, str);
	}

	private static String getPadId(Poll poll) {
		//return poll.getPadURL().substring(poll.getPadURL().length() - 6);
		return poll.getPadURL().substring(poll.getPadURL().lastIndexOf('/') + 1);
	}
	@PostMapping("/polls/createUser/{firstname}/{lastname}/{mail}/{password}")
	public ResponseEntity<Account> createUser(@PathVariable String firstname, @PathVariable String lastname, @PathVariable String mail, @PathVariable String password) {
		return new ResponseEntity<>(Poll.createAccount(firstname, lastname, mail, password), HttpStatus.OK);
	}

	@PostMapping("/polls/connection/{mail}/{password}")
	public ResponseEntity<Account> connection(@PathVariable String mail, @PathVariable String password) {
		return new ResponseEntity<>(Poll.connection(mail, password), HttpStatus.OK);
	}

	@GetMapping("/polls/getAccount")
	public ResponseEntity<Account> getAccount() {
		return new ResponseEntity<>(Poll.getAccount(), HttpStatus.OK);
	}

	@GetMapping("/polls/logout")
	public ResponseEntity<Boolean> logout() {
		return new ResponseEntity<>(Poll.logout(), HttpStatus.OK);
	}

	@PutMapping("/polls/addPoll/{mail}/{slug}/{admin}")
	public ResponseEntity<Boolean> addPoll(@PathVariable String mail, @PathVariable String slug, @PathVariable Integer admin) {
		Poll poll = pollRepository.findBySlug(slug);
		return new ResponseEntity<>(poll.addPoll(mail, poll, admin), HttpStatus.OK);
	}

	@GetMapping("/polls/getPollAdmin/{mail}")
	public ResponseEntity<Object> getPollAdmin(@PathVariable String mail) {
		List<Poll> res = new ArrayList<Poll>();
		List<Long> idPolls = new ArrayList<Long>();
		idPolls = Poll.getPollAdmin(mail);
		for(int i=0 ; i<idPolls.size() ; i++){
			res.add(pollRepository.findById(idPolls.get(i)));
		}
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/polls/getPollMember/{mail}")
	public ResponseEntity<Object> getPollMember(@PathVariable String mail) {
		List<Poll> res = new ArrayList<Poll>();
		List<Long> idPolls = new ArrayList<Long>();
		idPolls = Poll.getPollMember(mail);
		for(int i=0 ; i<idPolls.size() ; i++){
			res.add(pollRepository.findById(idPolls.get(i)));
		}
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PutMapping("/polls/changeIcs/{mail}/{ics}")
	public ResponseEntity<Boolean> changeIcs(@PathVariable String mail, @PathVariable String ics) {
		return new ResponseEntity<>(Poll.changeIcs(mail, ics), HttpStatus.OK);
	}

	@GetMapping("/polls/getIcs/{mail}")
	public ResponseEntity<Object> getIcs(@PathVariable String mail) {
		return new ResponseEntity<>(Poll.getIcs(mail), HttpStatus.OK);
	}
}
