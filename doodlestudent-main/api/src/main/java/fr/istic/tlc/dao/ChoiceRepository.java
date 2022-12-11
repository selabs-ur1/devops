package fr.istic.tlc.dao;


import javax.enterprise.context.ApplicationScoped;

import fr.istic.tlc.domain.Choice;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


@ApplicationScoped
public class ChoiceRepository implements PanacheRepository<Choice> {
}
