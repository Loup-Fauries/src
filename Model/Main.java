package Model;

import java.util.ArrayList;
import java.util.List;

public class Main {
	private List<Carte> main = new ArrayList<Carte>();
	
	public void ajouter(Carte carte) {
		main.add(carte);
	}
	
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
	
	public String toString(){
		String listedecarte = "";
		for(Carte carte : main){
			listedecarte += " " + carte.toString() + " , ";
		}
		return listedecarte;
	}
	
	public int tailleMain(){
		return main.size();
	}
	
	public void retournerCartes() {
		for (Carte carte : main) {
			if(!carte.estVisible()) {
				carte.setVisible(true);
			}
		}
	}
	
	public boolean isBlackjack() {
		return (tailleMain() == 2 && cartesValeur() == 21);
	}
}
