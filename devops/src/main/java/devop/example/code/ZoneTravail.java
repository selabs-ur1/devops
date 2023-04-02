package devop.example.code;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ZoneTravail {
	// Les attributs Attributs
	
	private JTextArea texte = new JTextArea();
	private String presse_papier = "";
		
	public void start() {
		
		// La fenetre principale
		
		JFrame FRAME_PRINCIPAL = new JFrame();	
		FRAME_PRINCIPAL.setTitle("Mini editeur version1");
		FRAME_PRINCIPAL.setBackground(Color.WHITE);
		FRAME_PRINCIPAL.setLocationRelativeTo(null);
		FRAME_PRINCIPAL.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME_PRINCIPAL.setSize(500, 250);	
			
			
		//Conteneur principal
		
		JPanel j_panel = new JPanel();
		j_panel.setBackground(Color.WHITE);
		j_panel.setLayout(new BoxLayout(j_panel, BoxLayout.Y_AXIS));
		FRAME_PRINCIPAL.setContentPane(j_panel);
			
		// La barre de menu
		
		JMenuBar menu_barre = new JMenuBar();
		menu_barre.setBackground(Color.white);
		FRAME_PRINCIPAL.setJMenuBar(menu_barre);
			
		// Fichier et operation
		
		JMenu menu_sortie = new JMenu("Sortie");
		JMenu menu_action = new JMenu("Actions");
		menu_barre.add(menu_sortie);
		menu_barre.add(menu_action);
			
		//Les sous menus
		
		JMenuItem menu_quitter = new JMenuItem("Quitter");
		menu_sortie.add(menu_quitter);
		JMenuItem menu_copier = new JMenuItem("Copier");
		menu_action.add(menu_copier);
		JMenuItem menu_couper = new JMenuItem("Couper");
		menu_action.add(menu_couper);
		JMenuItem menu_coller = new JMenuItem("Coller");
		menu_action.add(menu_coller);		
		
		// le texte
		
		texte.setLineWrap(true);
		j_panel.add(texte);
		
		// La fenetre
		
		FRAME_PRINCIPAL.setVisible(true);
		
		// zone de travail
		
		ZoneTravail zone = this;
		
	
		
		//Action du menu Copier
		
		menu_copier.addActionListener((ActionListener) new ActionListener() {
			@Override
		    public void actionPerformed(ActionEvent e) {
				executerCommande(new CommandeCopier(zone));
		    }
		});
		
		
	//Menu Quitter
		
		menu_quitter.addActionListener((ActionListener) new ActionListener() {
			@Override
		    public void actionPerformed(ActionEvent e) {
				FRAME_PRINCIPAL.dispose();
		    }
		});
		
		
		
		//Action du menu Couper
		
		menu_couper.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executerCommande(new CommandeCouper(zone));
			}
		});
		
		
		//Action du menu Coller
		
		menu_coller.addActionListener((ActionListener) new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	executerCommande(new CommandeColler(zone));
		    }
		});
	}
	
	
	
	public void executerCommande(Commande c) {
		c.executer();
	}
	
	
	//Getters
	
	public String getPressPapier() {
		return presse_papier;
	}
	public JTextArea gettexte() {
		return texte;
	}
	
	//Setters
	public void setPressPapier(String presse_papier) {
		this.presse_papier = presse_papier;
	}
}
