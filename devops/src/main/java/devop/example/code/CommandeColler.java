package devop.example.code;

import javax.swing.JOptionPane;

public class CommandeColler extends Commande {

	public CommandeColler(ZoneTravail zone) {
		super(zone);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executer() {
		System.out.println("Commande coller");
		if(!this.getZoneTravail().getPressPapier().isEmpty()) {
			this.getZoneTravail().gettexte().insert(this.getZoneTravail().getPressPapier(), this.getZoneTravail().gettexte().getCaretPosition());
		} else {
			JOptionPane.showMessageDialog(null, "Presse papier est vide");
		}		
	}

}
