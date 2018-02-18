package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Classe repr�sentant un joueur
 * 
 * @version: 1.0
 */
public class Joueur extends Thread{
	private boolean couche;
	private Main main;
	private Socket service;
	private PrintStream     fluxSortieSocket;
	private BufferedReader  fluxEntreeSocket;
	
	/**
	 * Constructeur de la classe
	 * 
	 */
	public Joueur(Socket service) {
		this.service= service;
		this.couche = false;
		this.main = new Main();
	}
	
	/**
	 * Envoie d'un message au joueur
	 * 
	 * @param chaine
	 * 					Correspond au message � envoyer
	 */
	public void envoyer(String chaine) {
		if (fluxSortieSocket == null) {
			try {
				fluxSortieSocket = new PrintStream(service.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fluxSortieSocket.println(chaine);
	}
	
	/**
	 * R�ception d'un message envoy� par un joueur
	 * 
	 * @return une chaine de caract�res, correspond au message re�u
	 */
	public String recevoir() {
		if (fluxEntreeSocket == null) {
			try {
				fluxEntreeSocket = new BufferedReader (new InputStreamReader(service.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return fluxEntreeSocket.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "J'ai rien re�u";
	}
	
	/**
	 * Ajoute une carte dans la main d'un joueur
	 * 
	 * @param carte
	 * 				carte que l'on veut ajouter � la main du joueur
	 */
	public void ajouterCarte(Carte carte) {
		main.ajouter(carte);
	}
	
	/**
	 * Affiche la main d'un joueur
	 * 
	 * @return une chaine de caract�res constitut� des cartes de la main du joueur
	 */
	public String afficherMain() {
		return main.toString();
	}
	
	/**
	 * Retourne si le joueur est couch� ou non
	 * 
	 * @return un boolean permettant de savoir si le joueur est couch�
	 */
	public boolean estCouche() {
		return couche;
	}
	
	/**
	 * Permet de coucher un joueur
	 * 
	 * @param b
	 * 			Valeur � affecter � l'attribut "couche"
	 */
	public void setCouche(boolean b) {
		couche = b;
	}
	
	/**
	 * Affiche le score du joueur
	 * 
	 * @return un int qui correspond au score du joueur
	 */
	public int score() {
		return main.cartesValeur();
	}
	
	/**
	 * Retourne true si la main du joueur vaut Blackjack
	 * 
	 * @ return un boolean indiquant si le joueur a fait blackjack ou non
	 */
	public boolean blackjack() {
		return main.isBlackjack();
	}
	
	/**
	 * Permet de d�connecter un joueur
	 * 
	 */
	public void deconnexion() {
		try {
			service.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
