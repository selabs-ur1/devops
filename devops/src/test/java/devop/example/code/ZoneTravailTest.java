package devop.example.code;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import org.junit.Before;
import org.junit.Test;

public class ZoneTravailTest {
   private ZoneTravail zone;

	@Before
	public void setUp() {
		zone = new ZoneTravail();
		zone.start();
	}

	@Test
	public void testSetTexte() {
		JTextArea texte = zone.gettexte();
		zone.executerCommande(new CommandeInsererTexte(zone, "Hello World!"));
		assertEquals("Hello World!", texte.getText());
	}

	@Test
	public void testCopierColler() {
		JTextArea texte = zone.gettexte();
		zone.executerCommande(new CommandeInsererTexte(zone, "Hello World!"));
		texte.setSelectionStart(0);
		texte.setSelectionEnd(5);
		zone.executerCommande(new CommandeCopier(zone));
		zone.executerCommande(new CommandeColler(zone));
		assertEquals("HelloHello World!", texte.getText());
	}

	@Test
	public void testCouperColler() {
		JTextArea texte = zone.gettexte();
		zone.executerCommande(new CommandeInsererTexte(zone, "Hello World!"));
		texte.setSelectionStart(0);
		texte.setSelectionEnd(5);
		zone.executerCommande(new CommandeCouper(zone));
		zone.executerCommande(new CommandeColler(zone));
		assertEquals(" World!", texte.getText());
	}

	@Test
	public void testSetPressePapier() {
		zone.setPressPapier("Hello");
		assertEquals("Hello", zone.getPressPapier());
	}

	@Test
	public void testSetPressePapierNull() {
		zone.setPressPapier(null);
		assertNull(zone.getPressPapier());
	}

	@Test
	public void testExecuterCommande() {
		Commande c = new CommandeInsererTexte(zone, "Hello World!");
		zone.executerCommande(c);
		assertEquals("Hello World!", zone.gettexte().getText());
		assertTrue(c.estExecutee());
	}
}
