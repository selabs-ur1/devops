package fr.istic.tlc.services;

import java.util.Date;
import java.util.Random;

public class Utils {
    private static Random random = new Random();
    private static String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";

    public static String generateSlug(int length) {
        StringBuilder slug = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            slug.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return slug.toString();
    }
    
	public static boolean intersect(Date start1, Date end1, Date start2, Date end2) {
		if (start1 == null || start2 == null ||end1 == null||end2 == null)
			return false;
		return end1.after(start2) && start1.before(end2);

	}
    
}
