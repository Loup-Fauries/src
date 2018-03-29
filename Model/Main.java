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
	 * @param carte
	 * 				Carte que l'on veut ajouter à la main du joueur
	 */
	public void ajouter(Carte carte) {
		main.add(carte);
	}
	
	/**
	 * Retourne la valeur de la main
	 * 
	 * @return un int qui correspond à la valeur de la main du joueur
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
	 * Forme une chaine de caractères constitués de la main d'un joueur
	 * 
	 * @return une chaine de caractères, constitués de la main d'un joueur
	 */
	public String toString(){
		String listedecarte = "";
		for(Carte carte : main){
			if(carte.equals(this.getCarte(this.tailleMain()-1)))
				listedecarte += " " + carte.toString();
			else
				listedecarte += " " + carte.toString() + " , ";
		}
		return listedecarte;
	}
	
	/**
	 * Retourne la ième carte de la main
	 * 
	 * @param i
	 * 			numéro de la carte que l'on souhaite connaitre
	 * 
	 * @return une instance de Carte, qui correspond à la ième carte de la main
	 */
	public Carte getCarte(int i){
		return main.get(i);
	}
	
	/**
	 * Retourne le nombre de cartes dans la main du joueur
	 * 
	 * @return un int qui représente le nombre de cartes dans la main du joueur
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
	 * @return un boolean, indiquant si la main vaut Blackjack
	 */
	public boolean isBlackjack() {
		return (tailleMain() == 2 && cartesValeur() == 21);
	}
	
	public void vider() {
		main.clear();
	}
}
