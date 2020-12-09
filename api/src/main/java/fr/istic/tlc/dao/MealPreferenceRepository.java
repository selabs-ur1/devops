package fr.istic.tlc.dao;

import javax.enterprise.context.ApplicationScoped;

import fr.istic.tlc.domain.MealPreference;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MealPreferenceRepository implements PanacheRepository<MealPreference> {
}
