package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Joueur extends Thread{
	private boolean couche;
	private Main main;
	private Socket service;
	private PrintStream     fluxSortieSocket;
	private BufferedReader  fluxEntreeSocket;
	
	public Joueur(Socket service) {
		this.service= service;
		this.couche = false;
		this.main = new Main();
	}
	
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
	
	public void ajouterCarte(Carte carte) {
		main.ajouter(carte);
	}
	
	public String afficherMain() {
		return main.toString();
	}
	
	public boolean estCouche() {
		return couche;
	}
	
	public void setCouche(boolean b) {
		couche = b;
	}
	
	public int score() {
		return main.cartesValeur();
	}
	
	public boolean blackjack() {
		return main.isBlackjack();
	}
	
	public void deconnexion() {
		try {
			service.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
