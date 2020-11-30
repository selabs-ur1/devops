package fr.istic.tlc.resources.itf;

import fr.istic.tlc.dao.MealPreferenceRepository;
import fr.istic.tlc.domain.MealPreference;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/mealpreference")
public interface MealPreferenceResource  extends PanacheRepositoryResource<MealPreferenceRepository,MealPreference,Long> {
}
