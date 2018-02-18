package Model;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Classe représentant un paquet de cartes
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
	 * @param i
	 * 			il s'agit de la ième carte que l'on veut supprimer
	 */
	public void removeCarte(int i){
		carteEnJeu.remove(i);
	}
	
	/**
	 * Retourne la ième carte du paquet
	 * 
	 * @param i
	 * 			il s'agit de la ième carte du paquet 
	 * 
	 * @return une instance de Carte, qui correspond à la ième carte du paquet
	 */
	public Carte getCarte(int i){
		return carteEnJeu.get(i);
	}
	
	/**
	 * Ajoute une carte au paquet
	 * 
	 * @param addCarte
	 * 					Carte que l'on veut ajouter au paquet
	 */
	public void addCarte(Carte addCarte){
		carteEnJeu.add(addCarte);
	}
	
	/**
	 * Tire la première carte du paquet (donc la supprime du paquet)
	 * 
	 * @return une instance de Carte, qui correspond à la Carte que l'on vient de tirer
	 */
	public Carte tirer(){
		return carteEnJeu.remove(0);
	}
	
	/**
	 * Tire une carte face visible
	 * 
	 * @return une instance de Carte, qui correspond à la carte que l'on vient de tirer face visible
	 */
	public Carte tirerVisible() {
		Carte carte = tirer();
		carte.setVisible(true);
		return carte;
	}
	
	/**
	 * Affiche toutes les cartes du paquet
	 * 
	 * @return un String qui correspond aux cartes qui composent le paquet
	 */
	public String toString(){
		String listedecarte = "";
		for(Carte cart : carteEnJeu){
			listedecarte += " " + cart.toString() + " , ";
		}
		return listedecarte;
	}
	
	/**
	 * Déplace le paquet de cartes dans un autre
	 * 
	 * @param jeu
	 * 				paquet vers lequel on souhaite déplacer le paquet de cartes
	 */
	public void moveAllToJeu(Jeudecarte jeu){
		int taille = carteEnJeu.size();
		for(int i = 0; i<taille; i++){
			jeu.addCarte(this.getCarte(0));
			this.removeCarte(0);
		}
	}
	
	/**
	 * Retourne la taille du paquet de cartes
	 * 
	 * @return un int qui correspond au nombre de carte dans le paquet
	 */
	public int tailleduJeu(){
		return carteEnJeu.size();
	}
	
	/**
	 * Retourne la valeur totale du paquet de cartes
	 * 
	 * @return un int qui correspond à la valeur totale du paquet de cartes
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
