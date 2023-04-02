package devop.example.code;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

public class CommandeCouperTest {
    private ZoneTravail zone;

    @Before
    public void setUp() throws Exception {
        zone = new ZoneTravail();
        zone.setTexte("Hello world!");
    }

    @Test
    public void testCouperAvecSelection() {
        // Sélectionnez le texte "Hello" dans la zone de travail
        zone.gettexte().setSelectionStart(0);
        zone.gettexte().setSelectionEnd(5);

        CommandeCouper cmdCouper = new CommandeCouper(zone);
        cmdCouper.executer();

        // Vérifiez que le texte "Hello" a été copié dans le presse-papiers
        assertEquals("Hello", zone.getPressPapier());

        // Vérifiez que le texte restant est " world!"
        assertEquals(" world!", zone.gettexte().getText());
    }

    @Test
    public void testCouperSansSelection() {
        // Désélectionnez tout le texte dans la zone de travail
        zone.gettexte().setSelectionStart(0);
        zone.gettexte().setSelectionEnd(0);

        CommandeCouper cmdCouper = new CommandeCouper(zone);
        cmdCouper.executer();

        // Vérifiez que le presse-papiers est toujours vide
        assertNull(zone.getPressPapier());

        // Vérifiez qu'un message d'erreur est affiché
        assertEquals(JOptionPane.YES_OPTION, JOptionPane.showConfirmDialog(null, "Vous n'avez effectué aucune selectionné"));
    }
}
