package Model;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Classe représentant un paquet de carte
 * 
 * @version: 1.0
 */
public class Jeudecarte {

	private LinkedList<Carte> carteEnJeu;
	
	/**
	 * Constructeur de la classe
	 * 
	 */
	public Jeudecarte(){
		this.carteEnJeu = new LinkedList<Carte>();
	}
	
	/**
	 * Création d'un paquet de 52 cartes
	 * 
	 */
	public void creationpaquet(){
		for (Couleur c : Couleur.values())
            for (Face f : Face.values())
                carteEnJeu.add(new Carte(f, c));
	}
	
	/**
	 * Mélange du paquet
	 * 
	 */
	public void melanger(){
		Collections.shuffle(carteEnJeu);
	}
	
	/**
	 * Supprime une carte du paquet
	 * 
	 */
	public void removeCarte(int i){
		carteEnJeu.remove(i);
	}
	
	/**
	 * Retourne la ième carte du paquet
	 * 
	 */
	public Carte getCarte(int i){
		return carteEnJeu.get(i);
	}
	
	/**
	 * Ajoute une carte au paquet
	 * 
	 */
	public void addCarte(Carte addCarte){
		carteEnJeu.add(addCarte);
	}
	
	/**
	 * Tire la première carte du paquet (donc la supprime du paquet)
	 * 
	 */
	public Carte tirer(){
		return carteEnJeu.remove(0);
	}
	
	/**
	 * Tire une carte face visible
	 * 
	 */
	public Carte tirerVisible() {
		Carte carte = tirer();
		carte.setVisible(true);
		return carte;
	}
	
	/**
	 * Affiche toutes les cartes du paquet
	 * 
	 */
	public String toString(){
		String listedecarte = "";
		for(Carte cart : carteEnJeu){
			listedecarte += " " + cart.toString() + " , ";
		}
		return listedecarte;
	}
	
	/**
	 * Déplace le paquet de carte dans un autre
	 * 
	 */
	public void moveAllToJeu(Jeudecarte jeu){
		int taille = carteEnJeu.size();
		for(int i = 0; i<taille; i++){
			jeu.addCarte(this.getCarte(0));
			this.removeCarte(0);
		}
	}
	
	/**
	 * Retourne la taille du paquet de carte
	 * 
	 */
	public int tailleduJeu(){
		return carteEnJeu.size();
	}
	
	/**
	 * Retourne la valeur de toutes les cartes du paquet
	 * 
	 */
	public int cartesValeur(){
		int valeur = 0;
		int nbas = 0;
		for(Carte carte: carteEnJeu) {
			if(Face.AS.equals(carte.getFace())){
				nbas += 1;
			}else{
				valeur += carte.getValeur();
			}
		}
		for(int i=nbas; i>0; i--){
			if(valeur <= 10){
				valeur += 11;
			}else{
				valeur += 1;
			}
		}
		return valeur;
	}
}
