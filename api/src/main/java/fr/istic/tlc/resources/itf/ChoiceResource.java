package fr.istic.tlc.resources.itf;


import fr.istic.tlc.dao.ChoiceRepository;
import fr.istic.tlc.domain.Choice;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/choice")
public interface ChoiceResource  extends PanacheRepositoryResource<ChoiceRepository, Choice,Long> {
}
