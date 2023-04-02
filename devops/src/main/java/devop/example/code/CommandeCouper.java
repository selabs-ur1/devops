package devop.example.code;

import javax.swing.JOptionPane;

public class CommandeCouper extends Commande {

	public CommandeCouper(ZoneTravail zone) {
		super(zone);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executer() {
		System.out.println("Commande couper");
		if(this.getZoneTravail().gettexte().getSelectedText() != null) {
			this.getZoneTravail().setPressPapier(this.getZoneTravail().gettexte().getSelectedText());
			
			String chaine_entiere = this.getZoneTravail().gettexte().getText();
			String nouveau_chaine = chaine_entiere.substring(0, this.getZoneTravail().gettexte().getSelectionStart()) + 
					chaine_entiere.substring(this.getZoneTravail().gettexte().getSelectionEnd());
			this.getZoneTravail().gettexte().setText(nouveau_chaine);
		} else {
			JOptionPane.showMessageDialog(null, "Vous n'avez effectué aucune selectionné");
		}
		
	}

}
