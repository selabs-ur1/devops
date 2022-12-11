package fr.istic.tlc.domain;

public class Account {

    private String firstname;
    private String lastname;
    private String mail;
    private String agendaIcs;

    public Account(){}

    public Account(String firstname, String lastname, String mail){
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
    }

    public String toString(){
        return mail + " " + firstname;
    }

    public String getFirstname(){
        return firstname;
    }
    public String getLastname(){
        return lastname;
    }
    public String getMail(){
        return mail;
    }
    public String getAgendaIcs(){
        return agendaIcs;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public void setMail(String mail){
        this.mail = mail;
    }
    public void setAgendaIcs(String ics) {
        this.agendaIcs = ics;
    }
    
}
