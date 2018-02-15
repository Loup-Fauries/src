package Model;

import java.util.Collections;
import java.util.LinkedList;

public class Jeudecarte {

	private LinkedList<Carte> carteEnJeu;
	
	public Jeudecarte(){
		this.carteEnJeu = new LinkedList<Carte>();
	}
	
	public void creationpaquet(){
		for (Couleur c : Couleur.values())
            for (Face f : Face.values())
                carteEnJeu.add(new Carte(f, c));
	}
	
	public void melanger(){
		Collections.shuffle(carteEnJeu);
	}
	
	public void removeCarte(int i){
		carteEnJeu.remove(i);
	}
	
	public Carte getCarte(int i){
		return carteEnJeu.get(i);
	}
	
	public void addCarte(Carte addCarte){
		carteEnJeu.add(addCarte);
	}
	
	public Carte tirer(){
		return carteEnJeu.remove(0);
	}
	
	public Carte tirerVisible() {
		Carte carte = tirer();
		carte.setVisible(true);
		return carte;
	}
	
	public String toString(){
		String listedecarte = "";
		for(Carte cart : carteEnJeu){
			listedecarte += " " + cart.toString() + " , ";
		}
		return listedecarte;
	}
	
	public void moveAllToJeu(Jeudecarte jeu){
		int taille = carteEnJeu.size();
		for(int i = 0; i<taille; i++){
			jeu.addCarte(this.getCarte(i));
		}
		for(int i = 0; i<taille; i++){
			this.removeCarte(0);
		}
	}
	
	public int tailleduJeu(){
		return carteEnJeu.size();
	}
	
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
