package fr.istic.tlc.domain;

import javax.persistence.*;

@Entity
public class Carpooling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long pollId;
    private String driver;
    private String departure_localisation;
    private String departure_time;
    private String arriving_time;
    private int places;
    private int places_restantes;
    private String[] carpoolers_mails;

    public Carpooling(){}

    public Carpooling(Long pollId, String driver, String departure_localisation, String departure_time, String arriving_time, int places, String[] carpoolers_mails){
        this.pollId = pollId;
        this.driver = driver;
        this.departure_localisation = departure_localisation;
        this.departure_time = departure_time;
        this.arriving_time = arriving_time;
        this.places = places;
        this.places_restantes = places;
        this.carpoolers_mails = carpoolers_mails;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getPollId() {
        return pollId;
    }
    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDeparture_localisation() {
		return departure_localisation;
	}
	public void setDeparture_localisation(String departure_localisation) {
		this.departure_localisation = departure_localisation;
	}

    public String getArriving_time() {
		return arriving_time;
	}
	public void setArriving_time(String arriving_time) {
		this.arriving_time = arriving_time;
	}

    public String getDeparture_time() {
        return departure_time;
    }
    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public int getPlaces() {
        return places;
    }
    public void setPlaces(int places) {
        this.places = places;
    }

    public int getPlaces_restantes() {
        return places_restantes;
    }
    public void setPlaces_restantes(int places_restantes) {
        this.places_restantes = places_restantes;
    }

    public String[] getCarpoolers_mails() {
        return carpoolers_mails;
    }
    public void setCarpoolers_mails(String[] carpoolers_mails) {
        this.carpoolers_mails = carpoolers_mails;
    }

    public boolean isOnCarpooling(String mail) {
      boolean found = false;
      for (int i = 0; i < this.carpoolers_mails.length && !found; i++) {
        if (this.carpoolers_mails[i].compareTo(mail) == 0) {
          found = true;
        }
      }
      return found;
    }


    @Override
    public String toString() {
        return "Carpooling";
    }
}
