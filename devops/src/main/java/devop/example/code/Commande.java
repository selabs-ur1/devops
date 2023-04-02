package devop.example.code;

public abstract class Commande {
	private ZoneTravail zone;
	
	public Commande(ZoneTravail zone) {
		this.zone = zone;
	}
	
	public abstract void executer();
	
	// ---- Getters
	public ZoneTravail getZoneTravail() {
		return zone;
	}
}
