package fr.istic.tlc.resources.itf;

import fr.istic.tlc.dao.CommentRepository;
import fr.istic.tlc.domain.Comment;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/comment")
public interface CommentResource  extends PanacheRepositoryResource<CommentRepository,   Comment,Long> {
}
