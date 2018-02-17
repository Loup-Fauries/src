package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la main d'un joueur
 * 
 * @version: 1.0
 */
public class Main {
	private List<Carte> main = new ArrayList<Carte>();
	
	/**
	 * Ajoute une carte à la main
	 * 
	 */
	public void ajouter(Carte carte) {
		main.add(carte);
	}
	
	/**
	 * Retourne la valeur de la main
	 * 
	 */
	public int cartesValeur(){
		int valeur = 0;
		int nbas = 0;
		for(Carte carte: main) {
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
	
	/**
	 * Forme une chaine de caractères constituée de la main d'un joueur
	 * 
	 */
	public String toString(){
		String listedecarte = "";
		for(Carte carte : main){
			listedecarte += " " + carte.toString() + " , ";
		}
		return listedecarte;
	}
	
	/**
	 * Retourne le nombre de carte dans main
	 * 
	 */
	public int tailleMain(){
		return main.size();
	}
	
	/**
	 * Retourne les cartes de la main face visible
	 * 
	 */
	public void retournerCartes() {
		for (Carte carte : main) {
			if(!carte.estVisible()) {
				carte.setVisible(true);
			}
		}
	}
	
	/**
	 * Retourne true si la main vaut : Blackjack
	 * 
	 */
	public boolean isBlackjack() {
		return (tailleMain() == 2 && cartesValeur() == 21);
	}
}
