package Model;

public class Croupier {

	protected Main mainDealer;
	protected Jeudecarte cartejouer;
	
	public Croupier() {
		mainDealer = new Main();
		cartejouer = new Jeudecarte();
	}
	
	public void initialisation() {
		cartejouer.creationpaquet();
		cartejouer.melanger();
	}
	
	public Carte TirerCarteVisible() { 
		return cartejouer.tirerVisible();
	}
	
	public void TirageMainDealer() {
		mainDealer.ajouter(cartejouer.tirerVisible());
		mainDealer.ajouter(cartejouer.tirer());
	}
	
	public String retourMain(){
		return mainDealer.toString();
	}
}
