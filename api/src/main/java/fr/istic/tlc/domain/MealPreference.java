package fr.istic.tlc.domain;

import javax.persistence.*;

@Entity
public class MealPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne
    User user;

    public MealPreference(){}

    public MealPreference(String content){
        this.content=content;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
