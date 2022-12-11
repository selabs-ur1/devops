package fr.istic.tlc.services;

import java.util.Date;
import java.util.Random;

public class Utils {
    private Random random = null;// = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
    private static Utils instance = null;
    private Utils(){
        this.random = new Random();
    }


  
    public static Utils getInstance(){
        if (instance == null)
            instance = new Utils();
        return instance;
    }


    public  String generateSlug(int length) {
        if (random == null){
            random = new Random();
        }
        StringBuilder slug = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            slug.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return slug.toString();
    }
    
	public boolean intersect(Date start1, Date end1, Date start2, Date end2) {
		if (start1 == null || start2 == null ||end1 == null||end2 == null)
			return false;
		return end1.after(start2) && start1.before(end2);

	}
    
}
