package fr.istic.tlc.domain;

import com.google.gson.Gson;
import com.sun.mail.iap.ConnectionException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import fr.istic.tlc.services.Utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

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

    // API's ports
    //private int meal_port = 3001;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String location;
    private String description;
    private boolean has_meal;
    private String slug = Utils.getInstance().generateSlug(24);
    private String slugAdmin = Utils.getInstance().generateSlug(24);
    public boolean clos = false;
    private static String mailAccount = "";


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

    public void addComment(Comment comment)
        throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> newComment = new HashMap<>();
        newComment.put("content", comment.getContent());
        newComment.put("auteur", comment.getAuteur());
        System.out.println("pollId" + getId());
        newComment.put("pollId", getId().toString());

        String requestBody = objectMapper.writeValueAsString(newComment);
        System.out.println("Resquest body = " + requestBody);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.242:80/poll"))
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                .header("Content-Type", "application/json")
                                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .thenApply(HttpResponse::body)
                                            .thenAccept(System.out::println)
                                            .join();
    }

    public void removeComment(Comment comment){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.242:80/poll/comment/"+comment.getId()))
                                .DELETE()
                                .header("Content-Type", "application/json")
                                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .thenApply(HttpResponse::body)
                                            .thenAccept(System.out::println)
                                            .join();
    }

    public void addMealPreference(MealPreference mealPreference)
        throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

        System.out.println("addMealPreference() - Poll.java");

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> newMeal = new HashMap<>();
        newMeal.put("content", mealPreference.getContent());
        newMeal.put("auteur", mealPreference.getUser().getUsername());
        //System.out.println("pollId" + getId());
        newMeal.put("pollId", getId().toString());

        String requestBody = objectMapper.writeValueAsString(newMeal);
        //System.out.println("Resquest body = " + requestBody);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.112:80/poll"))
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                .header("Content-Type", "application/json")
                                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .thenApply(HttpResponse::body)
                                            .thenAccept(System.out::println)
                                            .join();
    }

    public void removeComment(MealPreference mealPreference){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.112:80/poll/mealpreference/"+mealPreference.getId()))
                                .DELETE()
                                .header("Content-Type", "application/json")
                                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .thenApply(HttpResponse::body)
                                            .thenAccept(System.out::println)
                                            .join();
    }

    public Long getId() {
        return id;
    }

    public void createTlkURL() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://148.60.11.242:81/chat/"+getId()))
                                    .POST(HttpRequest.BodyPublishers.ofString(""))
                                    .header("Content-Type", "application/json")
                                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                        .join();
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
        }
    }

    public String getTlkURL() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://148.60.11.242:81/chat/"+getId()))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                                    .join();

            String res = response.body();

            return res;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
        }

        return null;
    }

    public void setTlkURL(String tlkURL) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> newChat = new HashMap<>();
            newChat.put("url", tlkURL);

            String requestBody = objectMapper.writeValueAsString(newChat);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://148.60.11.242:81/chat/"+getId()))
                                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                                    .header("Content-Type", "application/json")
                                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                        .join();
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
        }
    }

    public void deleteTlkURL() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://148.60.11.242:81/chat/"+getId()))
                                    .DELETE()
                                    .header("Content-Type", "application/json")
                                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                        .join();
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
        }
    }

    public List<Comment> getPollComments()
        throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

        HttpClient client = HttpClient.newHttpClient();

        //System.out.println("getPollComments() - Poll.java");
        //System.out.println("request : " + "http://localhost:3000/poll/"+getId());

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.112:80/poll/"+getId()))
                                .header("Content-Type", "application/json")
                                .build();

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                                .join();

        String json = response.body();
        //System.out.println("json response : " + json);

        Type listType = new TypeToken<ArrayList<Comment>>(){}.getType();
        ArrayList<Comment> list = new Gson().fromJson(json, listType);

        return list;
    }

    public List<MealPreference> getPollMealPreferences()
        throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

        //System.out.println("getPollMealPreferences() - Poll.java");

        HttpClient client = HttpClient.newHttpClient();

        //System.out.println("PollId = " + getId());

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.112:80/poll/"+getId()+"/mealpreferences"))
                                .header("Content-Type", "application/json")
                                .build();

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                                .join();
        String json = response.body();
        //System.out.println("meal preferences json = " + json);

        Type listType = new TypeToken<ArrayList<MealPreference>>(){}.getType();
        ArrayList<MealPreference> meal_preferences_from_poll = new Gson().fromJson(json, listType);

        return meal_preferences_from_poll;
    }

    public void setPollMealPreferences(List<MealPreference> pollMealPreferences)
        throws JsonMappingException, JsonProcessingException, ConnectionException, IOException, InterruptedException {
        for( MealPreference mealPreference : pollMealPreferences){
            addMealPreference(mealPreference);
        }
    }

    public List<Carpooling> getCarpoolingPropositionsFromPoll()
        throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {

        //System.out.println("getPollMealPreferences() - Poll.java");

        HttpClient client = HttpClient.newHttpClient();

        //System.out.println("PollId = " + getId());

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://148.60.11.112:81/poll/"+getId()+"/carpooling"))
                                .header("Content-Type", "application/json")
                                .build();

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                                .join();
        String json = response.body();
        //System.out.println("meal preferences json = " + json);

        Type listType = new TypeToken<ArrayList<Carpooling>>(){}.getType();
        ArrayList<Carpooling> carpoolings_from_poll = new Gson().fromJson(json, listType);

        return carpoolings_from_poll;
    }

    public String addCarpooling(Carpooling carpooling)
    throws IOException, InterruptedException, JsonProcessingException, JsonMappingException, ConnectionException {
      System.out.println("addCarpooling() - Poll.java");

      ObjectMapper objectMapper = new ObjectMapper();

      String requestBody = objectMapper.writeValueAsString(carpooling);

      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
                              .uri(URI.create("http://148.60.11.112:81/poll"))
                              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                              .header("Content-Type", "application/json")
                              .build();

      HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                              .join();
      String json = response.body();
      System.out.println("response = " + json);
      return json;
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

    public static Account createAccount(String firstname, String lastname, String mail, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> newAccount = new HashMap<>();
            newAccount.put("firstname", firstname);
            newAccount.put("lastname", lastname);
            newAccount.put("mail", mail);
            newAccount.put("password", password);

            String requestBody = objectMapper.writeValueAsString(newAccount);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account"))
                                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .join();

            String json = response.body();
            Type accountType = new TypeToken<Account>(){}.getType();
            Account account = new Gson().fromJson(json, accountType);
            mailAccount = account.getMail();

            return account;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return null;
        }
    }

    public static Account connection(String mail, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> data = new HashMap<>();
            data.put("username", mail);
            data.put("password", password);

            String requestBody = objectMapper.writeValueAsString(data);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/auth/login"))
                                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .join();

            String json = response.body();
            Type accountType = new TypeToken<Account>(){}.getType();
            Account account = new Gson().fromJson(json, accountType);
            mailAccount = account.getMail();

            return account;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return null;
        }
    }

    public static Account getAccount() {
        if(mailAccount.compareTo("") == 0){
            return new Account();
        }
		try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account/"+mailAccount))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .join();

            String json = response.body();
            Type accountType = new TypeToken<Account>(){}.getType();
            Account account = new Gson().fromJson(json, accountType);

            return account;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return null;
        }
	}

    public static boolean logout() {
        mailAccount = "";
        return true;
	}

    public boolean addPoll(String mail, Poll poll, Integer admin) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> data = new HashMap<>();
            data.put("mail", mail);
            data.put("pollId", poll.id+"");
            data.put("admin", admin+"");

            String requestBody = objectMapper.writeValueAsString(data);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account/poll"))
                                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                                    .header("Content-Type", "application/json")
                                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .join();
            return true;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return false;
        }
    }

    public static List<Long> getPollAdmin(String mail){
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account/polls/admin/"+mail))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                                    .join();

            String json = response.body();

            Type listType = new TypeToken<ArrayList<Long>>(){}.getType();
            ArrayList<Long> idPollAdmin = new Gson().fromJson(json, listType);

            return idPollAdmin;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return null;
        }
    }

    public static List<Long> getPollMember(String mail){
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account/polls/member/"+mail))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                                    .join();

            String json = response.body();

            Type listType = new TypeToken<ArrayList<Long>>(){}.getType();
            ArrayList<Long> idPollMember = new Gson().fromJson(json, listType);

            return idPollMember;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return null;
        }
    }

    public static Boolean changeIcs(String mail, String agendaIcs) {
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> data = new HashMap<>();
            data.put("mail", mail);
            data.put("agendaIcs", agendaIcs);

            String requestBody = objectMapper.writeValueAsString(data);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account/agenda"))
                                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                                    .header("Content-Type", "application/json")
                                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                            .join();

            return true;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return false;
        }
	}

    public static String getIcs(String mail) {
		try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:3010/account/agenda/"+mail))
                                    .header("Content-Type", "application/json")
                                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                    .join();

            String json = response.body();

            Type type = new TypeToken<String>(){}.getType();
            String res = new Gson().fromJson(json, type);

            return res;
        } catch (Exception e){
            System.out.println("Erreur lors de l'échange avec le service. " + e);
            return null;
        }
	}
}
