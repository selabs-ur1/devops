package devop.example.code;

import javax.swing.JOptionPane;

public class CommandeCopier extends Commande {

	public CommandeCopier(ZoneTravail zone) {
		super(zone);
	}

	@Override
	public void executer() {
		System.out.println("Commande copier");
		if(this.getZoneTravail().gettexte().getSelectedText() != null) {
			this.getZoneTravail().setPressPapier(this.getZoneTravail().gettexte().getSelectedText());
		} else {
			JOptionPane.showMessageDialog(null, "Vous n'avez effectué aucune selection");
		}		
	}
	

}
