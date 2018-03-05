package Model;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe repr�sentant le Table et la table
 * dans cette version leur r�le ne sont pas dissoci�s
 * 
 * @version: 1.0
 */
public class Table extends Thread{
	private Main mainDealer;
	private Jeudecarte cartejouer;
	private ArrayList<Joueur> joueurs;
	
	/**
	 * Constructeur de la classe 
	 * 
	 */
	public Table(ArrayList<Socket> sockets) {
		joueurs = new ArrayList<Joueur>(sockets.size());
		for (Socket socket : sockets) {
			joueurs.add(new Joueur(socket));
		}
	}
	
	/**
	 * Initialisation d'une partie:
	 * Envoie d'un message aux joueurs
	 * Cr�ation d'un jeu de cartes et m�lange de ce dernier
	 * Premier tirage de carte pour tous les joueurs
	 * 
	 */
	public void initialiserPartie() {
		for (Joueur joueur : joueurs) {
			joueur.envoyer("Okcommencer");
		}
		cartejouer = new Jeudecarte();
		cartejouer.creationpaquet();
		cartejouer.melanger();
		for(int i = 0; i != 2; i++) {
			for (Joueur joueur : joueurs) {
				joueur.ajouterCarte(cartejouer.tirerVisible());
			}
		}
		mainDealer = new Main();
		mainDealer.ajouter(cartejouer.tirerVisible());
		mainDealer.ajouter(cartejouer.tirer());
	}
	
	/**
	 * Traite la r�ponse d'un joueur Hit ou Stand:
	 * Si Hit alors on lui donne une autre carte et v�rifie son score
	 * Si Stand le joueur est couch� et attendra la fin de la partie
	 * 
	 * @param reponse
	 * 					r�ponse du joueur (Hit ou Stand)
	 * @param joueur
	 * 					joueur dont on traite la r�ponse
	 * 
	 * @return un boolean qui correspond � la r�ponse du joueur
	 */
	public boolean traiterReponse(String reponse, Joueur joueur) {
		switch(reponse) {
		case "1":
			joueur.ajouterCarte(cartejouer.tirerVisible());
			joueur.envoyer(joueur.afficherMain());
			joueur.envoyer(mainDealer.toString());
			if(joueur.score() >= 21) {
				joueur.envoyer("stand");
				joueur.setCouche(true);
			}else {
				joueur.envoyer("hit");
			}
			break;
		case "2":
			joueur.setCouche(true);
			break;
		default:
			joueur.envoyer("inconnu");
			return false;
		}	
		return true;
	}
	
	/**
	 * D�roulement d'un tour:
	 * On informe le joueur que c'est son tour
	 * Puis on attend sa r�ponse
	 * 
	 * @return un int qui correspond au nombre de joueurs couch� � la fin d'un tour
	 */
	public int unTour() {
		int nbCouche = 0;
		for(Joueur joueur: joueurs) {
			if(!joueur.estCouche()) {
				joueur.envoyer("tour");
				String reponse;
				do {
					reponse = joueur.recevoir();
				}while(!traiterReponse(reponse, joueur));
			}else {
				nbCouche++;
			}
		}
		return nbCouche;
	}
	
	/**
	 * D�roulement d'une partie:
	 * Envoie aux joueurs leur main de d�part, et calcule des scores initiaux
	 * Joue tour par tour jusqu'� ce que la partie soit termin�e
	 * Indique qui a gagn� et perdu face au Table
	 * 
	 */
	public void run() {
		initialiserPartie();
		for (Joueur joueur : joueurs) {
			joueur.envoyer(joueur.afficherMain());
			joueur.envoyer(mainDealer.toString());
			if(joueur.score() >= 21) {
				joueur.envoyer("stand");
				joueur.setCouche(true);
			}else {
				joueur.envoyer("hit");
			}
		}
		boolean fini = false;
		while(!fini) {
			int nbCouche = 0;
			nbCouche = unTour();
			fini = (nbCouche == joueurs.size());
		}
		
		mainDealer.retournerCartes();
		while(mainDealer.cartesValeur() < 17) {
			mainDealer.ajouter(cartejouer.tirerVisible());
		}
		
		int scoreDealer = mainDealer.cartesValeur();
		for (Joueur joueur : joueurs) {
			int scoreJoueur = joueur.score();
			if(scoreJoueur > 21) {
				joueur.envoyer("\nLes jeux sont fini.\nScore personnel: "+ joueur.score()
						+ "\nVous avez d�passer 21, vous avez perdu.");
				continue;
			}
			joueur.envoyer("\nLes jeux sont faits, voici les cartes."
					+ "\nCarte dans votre main : " + joueur.afficherMain()
					+ "\n Voici la main du casino : " + mainDealer.toString() + "\n");
			joueur.envoyer("Score personnel: " + scoreJoueur
					+ "\nScore casino : " + scoreDealer );
			if(scoreDealer > 21) {
				joueur.envoyer("Le casino a d�pass� 21, vous avez gagn�.");
				continue;
			}
			if(scoreJoueur > scoreDealer) {
				joueur.envoyer("Vous avez gagn�.");
			}else if(scoreJoueur < scoreDealer) {
				joueur.envoyer("Vous avez perdu.");
			}else {
				if(joueur.blackjack() && !mainDealer.isBlackjack()) {
					joueur.envoyer("Vous avez gagn� et fait un BlackJack.");
				}else if(!joueur.blackjack() && mainDealer.isBlackjack()) {
					joueur.envoyer("Vous avez perdu et le casino a fait un BlackJack.");
				}else {
					joueur.envoyer("Vous avez fait une �galit�.");
				}
			}
		}
		for (Joueur joueur : joueurs) {
			joueur.deconnexion();
		}
	}
}
