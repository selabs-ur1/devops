package devop.example.code;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

public class CommandeCopierTest {
    private ZoneTravail zone;

    @Before
    public void setUp() throws Exception {
       ZoneTravail zone = new ZoneTravail();
        //zone.gettexte().setTexte("Hello world!");
    }

    @Test
    public void testCopierAvecSelection() {
        // Sélectionnez le texte "Hello" dans la zone de travail
        zone.gettexte().setSelectionStart(0);
        zone.gettexte().setSelectionEnd(5);

        CommandeCopier cmdCopier = new CommandeCopier(zone);
        cmdCopier.executer();

        // Vérifiez que le texte "Hello" a été copié dans le presse-papiers
        assertEquals("Hello", zone.getPressPapier());
    }

    @Test
    public void testCopierSansSelection() {
        // Désélectionnez tout le texte dans la zone de travail
        zone.gettexte().setSelectionStart(0);
        zone.gettexte().setSelectionEnd(0);

        CommandeCopier cmdCopier = new CommandeCopier(zone);
        cmdCopier.executer();

        // Vérifiez que le presse-papiers est toujours vide
        assertNull(zone.getPressPapier());

        // Vérifiez qu'un message d'erreur est affiché
        assertEquals(JOptionPane.YES_OPTION, JOptionPane.showConfirmDialog(null, "Vous n'avez effectué aucune selection"));
    }
}
