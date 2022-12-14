package fr.istic.tlc.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String mail;
    private String icsurl;


	@JsonIgnore
    @ManyToMany(mappedBy = "users")
    List<Choice> userChoices = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    List<MealPreference> userMealPreferences = new ArrayList<>();

    public User(){}

    public User(String username) {	
        this.username = username;
    }

    public void addChoice(Choice choice){
        this.userChoices.add(choice);
    }
    public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}


    public String getIcsurl() {
		return icsurl;
	}

	public void setIcsurl(String icsurl) {
		this.icsurl = icsurl;
	}

	public void removeChoice(Choice choice){
        this.userChoices.remove(choice);
    }


    public void addMealPreference (MealPreference mealPreference) {this.userMealPreferences.add(mealPreference);}

    public void removeMealPreference (MealPreference mealPreference) {this.userMealPreferences.remove(mealPreference);}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Choice> getUserChoices() {
        return userChoices;
    }

    public List<MealPreference> getUserMealPreferences() {
        return userMealPreferences;
    }

    public void setUserMealPreferences(List<MealPreference> userMealPreferences) {
        this.userMealPreferences = userMealPreferences;
    }

    public void setUserChoices(List<Choice> userChoices) {
        this.userChoices = userChoices;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
