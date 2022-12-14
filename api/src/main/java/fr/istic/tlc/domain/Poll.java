package fr.istic.tlc.domain;

import fr.istic.tlc.services.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String location;
    private String description;
    private boolean has_meal;
    private String slug = Utils.getInstance().generateSlug(24);
    private String slugAdmin = Utils.getInstance().generateSlug(24);
    private String tlkURL = "https://tlk.io/"+Utils.getInstance().generateSlug(12);
    public boolean clos = false;
    

	@CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pollID")
    @OrderBy("startDate ASC")
    List<Choice> pollChoices;

    
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.REFRESH})
    Choice selectedChoice;

    @OneToMany(cascade =  {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.REFRESH})
    @JoinColumn(name = "pollID")
    List<Comment> pollComments = new ArrayList<>();

    @OneToMany(cascade =  {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.REFRESH})
    @JoinColumn(name = "pollID")
    List<MealPreference> pollMealPreferences = new ArrayList<>();

    private String padURL;

    public Poll(){}

    public Poll(String title, String location, String description, boolean has_meal, List<Choice> pollChoices) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.has_meal = has_meal;
        this.pollChoices = pollChoices;
    }

    public void addChoice(Choice choice){
        this.pollChoices.add(choice);
    }

    public void removeChoice(Choice choice){
        this.pollChoices.remove(choice);
    }

    public void addComment(Comment comment){ this.pollComments.add(comment);}

    public void removeComment(Comment comment){ this.pollComments.remove(comment);}

    public void addMealPreference(MealPreference mealPreference){ this.pollMealPreferences.add(mealPreference);}

    public void removeComment(MealPreference mealPreference){ this.pollMealPreferences.remove(mealPreference);}

    public Long getId() {
        return id;
    }

    public String getTlkURL() {
        return tlkURL;
    }

    public void setTlkURL(String tlkURL) {
        this.tlkURL = tlkURL;
    }

    public List<Comment> getPollComments() {
        return pollComments;
    }

    public List<MealPreference> getPollMealPreferences() {
        return pollMealPreferences;
    }

    public void setPollMealPreferences(List<MealPreference> pollMealPreferences) {
        this.pollMealPreferences = pollMealPreferences;
    }

    public void setPollComments(List<Comment> pollComments) {
        this.pollComments = pollComments;
    }

    public void setId(Long id) {
        this.id = id;
    }

   public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHas_meal() {
        return has_meal;
    }

    public void setHas_meal(boolean has_meal) {
        this.has_meal = has_meal;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlugAdmin() {
        return slugAdmin;
    }

    public void setSlugAdmin(String slugAdmin) {
        this.slugAdmin = slugAdmin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Choice> getPollChoices() {
        return pollChoices;
    }

    public void setPollChoices(List<Choice> pollChoices) {
        this.pollChoices = pollChoices;
    }

    public Choice getSelectedChoice() {
		return selectedChoice;
	}

	public void setSelectedChoice(Choice selectedChoice) {
		this.selectedChoice = selectedChoice;
	}

	public String getPadURL() {
        return this.padURL;
    }

    
    public void setPadURL(String padURL) {
        this.padURL=padURL;
    }
    
    public boolean isClos() {
		return clos;
	}

	public void setClos(boolean clos) {
		this.clos = clos;
	}


    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", has_meal=" + has_meal +
                ", createdAt=" + createdAt +
                '}';
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
