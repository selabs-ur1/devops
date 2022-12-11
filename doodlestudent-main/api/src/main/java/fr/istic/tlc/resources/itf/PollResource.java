package fr.istic.tlc.resources.itf;

import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.domain.Poll;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/poll")
public interface PollResource  extends PanacheRepositoryResource<PollRepository, Poll,Long> {
}
