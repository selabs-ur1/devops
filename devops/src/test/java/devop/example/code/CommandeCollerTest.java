package devop.example.code;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommandeCollerTest {
    private ZoneTravail zoneTravail;
    private CommandeColler commandeColler;

    @Before
    public void setUp() {
        zoneTravail = new ZoneTravail();
        commandeColler = new CommandeColler(zoneTravail);
    }

    @Test
    public void testCollerAvecPressePapierNonVide() {
        zoneTravail.gettexte().setText("test");
        zoneTravail.gettexte().setCaretPosition(2);
        zoneTravail.setPressPapier("st");
        commandeColler.executer();
        assertEquals("testst", zoneTravail.gettexte().getText());
    }

    @Test
    public void testCollerAvecPressePapierVide() {
        zoneTravail.gettexte().setText("test");
        zoneTravail.gettexte().setCaretPosition(2);
        zoneTravail.setPressPapier("");
        commandeColler.executer();
        assertEquals("test", zoneTravail.gettexte().getText());
    }
}
