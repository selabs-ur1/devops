package fr.istic.tlc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.tlc.dao.CommentRepository;
import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.domain.Comment;
import fr.istic.tlc.domain.Poll;

@RestController
@RequestMapping("/api")
public class CommentResourceEx {
    @Autowired
    PollRepository pollRepository;
    @Autowired
    CommentRepository commentRepository;

  /*  @GetMapping("users/{idUser}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsFromUser(@PathVariable long idUser) {
        // On vérifie que l'utilisateur existe
       User optUser = userRepository.findById(idUser);
        if(optUser== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optUser.getUserComments(), HttpStatus.OK);
    }*/

    @GetMapping("polls/{slug}/comments")
    public ResponseEntity<Object> getAllCommentsFromPoll(@PathVariable String slug) {
        // On vérifie que le poll existe
       Poll optPoll = pollRepository.findBySlug(slug);
        if(optPoll== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optPoll.getPollComments(),HttpStatus.OK);
    }

    @GetMapping("polls/{slug}/comments/{idComment}")
    public ResponseEntity<Object> getCommentFromPoll(@PathVariable String slug, @PathVariable long idComment){
        // On vérifie que le poll et le comment existe
       Poll optPoll = pollRepository.findBySlug(slug);
        Comment optComment = commentRepository.findById(idComment);
        if(optPoll== null || optComment== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // On vérifie que le comment appartienne bien au poll
        if (!optPoll.getPollComments().contains(optComment)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(optComment,HttpStatus.OK);
    }

   /* @PostMapping("polls/{slug}/comments/{idUser}")
    public ResponseEntity<Object> createComment(@Valid @RequestBody Comment comment, @PathVariable String slug, @PathVariable long idUser){
        // On vérifie que le poll et le User existe
       Poll poll = pollRepository.findBySlug(slug);
       User user = userRepository.findById(idUser);
        if (poll== null || user== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // On set le user dans comment
        comment.setUser(user);
        // On ajoute le commentaire dans le poll
        poll.addComment(comment);
        pollRepository.save(poll);
        // On save le commentaire
        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }*/

}


