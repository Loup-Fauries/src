package Model;

import java.net.Socket;
import java.util.ArrayList;

public class Croupier extends Thread{
	private Main mainDealer;
	private Jeudecarte cartejouer;
	private ArrayList<Joueur> joueurs;
	
	public Croupier(ArrayList<Socket> sockets) {
		joueurs = new ArrayList<Joueur>(sockets.size());
		for (Socket socket : sockets) {
			joueurs.add(new Joueur(socket));
		}
	}
	
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
						+ "\nVous avez dépasser 21, vous avez perdu.");
				continue;
			}
			joueur.envoyer("\nLes jeux sont faits, voici les cartes."
					+ "\nCarte dans votre main : " + joueur.afficherMain()
					+ "\n Voici la main du casino : " + mainDealer.toString() + "\n");
			joueur.envoyer("Score personnel: " + scoreJoueur
					+ "\nScore casino : " + scoreDealer );
			if(scoreDealer > 21) {
				joueur.envoyer("Le casino a dépasser 21, vous avez gagnez.");
				continue;
			}
			if(scoreJoueur > scoreDealer) {
				joueur.envoyer("Vous avez gagnez.");
			}else if(scoreJoueur < scoreDealer) {
				joueur.envoyer("Vous avez perdu.");
			}else {
				if(joueur.blackjack() && !mainDealer.isBlackjack()) {
					joueur.envoyer("Vous avez gagnez et fait un BlackJack.");
				}else if(!joueur.blackjack() && !mainDealer.isBlackjack()) {
					joueur.envoyer("Vous avez perdu et le casino a fait un BlackJack.");
				}else {
					joueur.envoyer("Vous avez fait une égalité.");
				}
			}
		}
		for (Joueur joueur : joueurs) {
			joueur.deconnexion();
		}
	}
}
