package fr.istic.tlc.resources.itf;

import fr.istic.tlc.dao.UserRepository;
import fr.istic.tlc.domain.User;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/user")
public interface UserResource  extends PanacheRepositoryResource<UserRepository, User,Long> {
}
