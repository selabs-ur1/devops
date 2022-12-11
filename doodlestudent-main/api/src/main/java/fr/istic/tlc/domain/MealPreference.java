package fr.istic.tlc.domain;

import javax.persistence.*;

@Entity
public class MealPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;
    private String auteur;

    @ManyToOne
    User user;

    public MealPreference(){}

    public MealPreference(String content){
        this.content=content;
    }

    public MealPreference(String content, String auteur) {
        this.auteur = auteur;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.auteur = user.getUsername();
    }

    @Override
    public String toString() {
        return "MealPreference{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", user=" + user +
                '}';
    }
}
