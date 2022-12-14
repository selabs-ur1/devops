package fr.istic.tlc.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.tlc.dao.ChoiceRepository;
import fr.istic.tlc.dao.MealPreferenceRepository;
import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.dao.UserRepository;
import fr.istic.tlc.domain.MealPreference;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.domain.User;

@RestController
@RequestMapping("/api")
public class MealPreferenceResource {
    @Autowired
    ChoiceRepository choiceRepository;
    @Autowired
    PollRepository pollRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MealPreferenceRepository mealPreferenceRepository;

    @GetMapping("polls/{slug}/mealpreferences")
    public ResponseEntity<Object> getAllMealPreferencesFromPoll(@PathVariable String slug) {
        // On vérifie que le poll existe
        Poll optPoll = pollRepository.findBySlug(slug);
        if(optPoll == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optPoll.getPollMealPreferences(),HttpStatus.OK);
    }

    @GetMapping("polls/{slug}/mealpreference/{idMealPreference}")
    public ResponseEntity<Object> getMealPreferenceFromPoll(@PathVariable String slug, @PathVariable long idMealPreference){
        // On vérifie que le poll et la meal preference existe
        Poll optPoll = pollRepository.findBySlug(slug);
        MealPreference optMealPreference = mealPreferenceRepository.findById(idMealPreference);
        if(optPoll == null || optMealPreference == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // On vérifie que la meal preference appartienne bien au poll
        if (!optPoll.getPollMealPreferences().contains(optMealPreference)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(optMealPreference,HttpStatus.OK);
    }

    @PostMapping("polls/{slug}/mealpreference/{idUser}")
    public ResponseEntity<Object> createMealPreference(@Valid @RequestBody MealPreference mealPreference, @PathVariable String slug, @PathVariable long idUser){
        // On vérifie que le poll et le User existe
        Poll poll = pollRepository.findBySlug(slug);
        User user = userRepository.findById(idUser);
        if (poll == null || user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // On set le user dans la meal preference
        mealPreference.setUser(user);
        // On ajoute la meal preference dans le poll
        poll.addMealPreference(mealPreference);
        // On save la meal preference
        mealPreferenceRepository.persist(mealPreference);
        pollRepository.getEntityManager().merge(poll);
        return new ResponseEntity<>(mealPreference, HttpStatus.CREATED);
    }
}



