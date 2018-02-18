package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Classe représentant un joueur
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
		return "J'ai rien reçu";
	}
	
	/**
	 * Ajoute une carte dans la main d'un joueur
	 * 
	 */
	public void ajouterCarte(Carte carte) {
		main.ajouter(carte);
	}
	
	/**
	 * Affiche la main d'un joueur
	 * 
	 */
	public String afficherMain() {
		return main.toString();
	}
	
	/**
	 * Retoure si le joueur est couché ou non
	 * 
	 */
	public boolean estCouche() {
		return couche;
	}
	
	/**
	 * Permet de coucher un joueur
	 * 
	 */
	public void setCouche(boolean b) {
		couche = b;
	}
	
	/**
	 * Affiche le score du joueur
	 * 
	 */
	public int score() {
		return main.cartesValeur();
	}
	
	/**
	 * Retourne true si la main du joueur vaut Blackjack
	 * 
	 */
	public boolean blackjack() {
		return main.isBlackjack();
	}
	
	/**
	 * Permet de déconnecter un joueur
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
