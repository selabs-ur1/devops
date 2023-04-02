package devop.example.code;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommandeTest {
   private ZoneTravail zoneTravail;
    private Commande commande;

    @Before
    public void setUp() {
        zoneTravail = new ZoneTravail();
        commande = new Commande(zoneTravail) {
            @Override
            public void executer() {
                // Do nothing, as we cannot create an instance of an abstract class
            }
        };
    }

    @Test
    public void testGetZoneTravail() {
        assertEquals(zoneTravail, commande.getZoneTravail());
    }
}
