import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.BasicConfigurator;

Logger logger = Logger.getLogger(PollResourceEx.class);

@GetMapping("/polls")
@Timed(name = "getPolls", description = "A measure of how long it takes get polls.", unit = MetricUnits.MILLISECONDS)
public ResponseEntity<List<Poll>> retrieveAllpolls() {
    // On récupère la liste de tous les poll qu'on trie ensuite par titre
    List<Poll> polls = pollRepository.findAll(Sort.by("title", Sort.Direction.Ascending)).list();
    return new ResponseEntity<>(polls, HttpStatus.OK);
}

@PostMapping("/polls")
@Transactional
@Timed(name = "postPolls", description = "A measure of how long it takes post polls.", unit = MetricUnits.MILLISECONDS)
@Counted(description = "How polls posted", absolute = true, name = "countPostPolls")
public ResponseEntity<Poll> createPoll(@Valid @RequestBody Poll poll) {
    // On enregistre le poll dans la bdd
    try {
        logger.debug("msg de debogage");
        logger.info("msg d'information");
        logger.warn("msg d'avertissement");
        logger.error("msg d'erreur");
        logger.fatal("msg d'erreur fatale");
        logger.info("mon message");
        }
    catch(Exception e) {
        e.printStackTrace();
    }
    String padId = generateSlug(15);
    if (this.usePad) {
        if (client == null) {
            client = new EPLiteClient(padUrl, apikey);
        }
        client.createPad(padId);
        initPad(poll.getTitle(), poll.getLocation(), poll.getDescription(), client, padId);
        poll.setPadURL(externalPadUrl + "p/" + padId);
    } 
    pollRepository.persist(poll);
    return new ResponseEntity<>(poll, HttpStatus.CREATED);
}