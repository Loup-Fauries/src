package Model;

import java.util.ArrayList;

/**
 * Classe représentant le Table et la table
 * dans cette version leur rôle ne sont pas dissociés
 * 
 * @version: 1.0
 */
public class Table extends Thread{
	private String nom;
	private int tailleTable;
	public int mise;
	protected Croupier croupier;
	protected ArrayList<Joueur> joueurs;
	protected ArrayList<Joueur> lobby;
	
	/**
	 * Constructeur de la classe 
	 * 
	 */
	
	public Table(String nom, int taille, int mise, Croupier croupier) {
		this.nom = nom;
		this.tailleTable = taille;
		this.mise = mise;
		this.croupier = croupier;
		joueurs = new ArrayList<Joueur>(taille);
		lobby = new ArrayList<Joueur>();
	}
	
	public void ajoutJoueur(Joueur joueur) {
		lobby.add(joueur);
		//joueurs.add(joueur);
	}
	
	public boolean tablePleine() {
		if (tailleTable == joueurs.size())
			return true;
		else {
			return false;
		}
	}

	public int joueurRestant() {
		return tailleTable-joueurs.size();
	}
	
	public String toString(){
		String listedecarte = nom + " - " + mise + " jetons";
		int rest = joueurRestant();
		if (tablePleine()) {
			listedecarte += " (pleine)";
		}
		else {
			if (rest==1) {
				listedecarte += " (1 place restante)";
			}
			else {
				listedecarte += " (" + rest + " places restantes)";
			}
		}
		return listedecarte;
	}
	
	public void controle(Joueur joueur) {
		if (joueur.solde() < mise) {
			joueur.envoyer("Vous n'avez plus suffisamment de jeton pour jouer à cette table.");
			joueur.deconnexion();
		}
	}
	
	public void rejouer(Joueur joueur) {
		String reponse;
		joueur.envoyer("rejouer");
		joueur.envoyer("Voulez-vous continuer de jouer ?");
		reponse = joueur.recevoir();
		System.out.println(reponse);
		if (reponse.equals("non")) {
			joueur.deconnexion();
			joueurs.remove(joueur);
		}
	}
	
	/**
	 * Initialisation d'une partie:
	 * Envoie d'un message aux joueurs
	 * Création d'un jeu de cartes et mélange de ce dernier
	 * Premier tirage de carte pour tous les joueurs
	 * 
	 */
	public void initialiserPartie() {
		croupier.initialisation();
		for (Joueur joueur : joueurs) {
			joueur.viderMain();
			joueur.envoyer(" Bienvenu autour de la table " + nom);
			joueur.envoyer("Okcommencer");
		}
		for(int i = 0; i != 2; i++) {
			for (Joueur joueur : joueurs) {
				joueur.ajouterCarte(croupier.TirerCarteVisible());
			}
		}
		croupier.TirageMainDealer();
	}
	
	/**
	 * Traite la réponse d'un joueur Hit ou Stand:
	 * Si Hit alors on lui donne une autre carte et vérifie son score
	 * Si Stand le joueur est couché et attendra la fin de la partie
	 * 
	 * @param reponse
	 * 					réponse du joueur (Hit ou Stand)
	 * @param joueur
	 * 					joueur dont on traite la réponse
	 * 
	 * @return un boolean qui correspond à la réponse du joueur
	 */
	public boolean traiterReponse(String reponse, Joueur joueur) {
		switch(reponse) {
		case "1":
			joueur.ajouterCarte(croupier.TirerCarteVisible());
			joueur.envoyer(joueur.afficherMain());
			joueur.envoyer(croupier.retourMain());
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
	 * Déroulement d'un tour:
	 * On informe le joueur que c'est son tour
	 * Puis on attend sa réponse
	 * 
	 * @return un int qui correspond au nombre de joueurs couché à la fin d'un tour
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
	 * Déroulement d'une partie:
	 * Envoie aux joueurs leur main de départ, et calcule des scores initiaux
	 * Joue tour par tour jusqu'à ce que la partie soit terminée
	 * Indique qui a gagné et perdu face au Table
	 * 
	 */
	public void run() {
		while(lobby.size()==0)
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		while(lobby.size()!=0 || joueurs.size()!= 0) {
			while(lobby.size() != 0 && joueurRestant() != 0) {
				joueurs.add(lobby.remove(0));
			}
			initialiserPartie();
			for (Joueur joueur : joueurs) {
				joueur.envoyer(joueur.afficherMain());
				joueur.envoyer(croupier.retourMain());
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
			
			croupier.mainDealer.retournerCartes();
			while(croupier.mainDealer.cartesValeur() < 17) {
				croupier.mainDealer.ajouter(croupier.TirerCarteVisible());
			}
			
			int scoreDealer = croupier.mainDealer.cartesValeur();
			for (Joueur joueur : joueurs) {
				int scoreJoueur = joueur.score();
				if(scoreJoueur > 21) {
					joueur.envoyer("\nLes jeux sont fini.\nScore personnel: "+ joueur.score()
							+ "\nVous avez dépasser 21, vous avez perdu.");
					joueur.perdre(mise);
					controle(joueur);
					joueur.envoyer("Votre solde: "+ joueur.solde() + " jetons");
					rejouer(joueur);
					continue;
				}
				joueur.envoyer("\nLes jeux sont faits, voici les cartes."
						+ "\nCarte dans votre main : " + joueur.afficherMain()
						+ "\n Voici la main du casino : " + croupier.retourMain() + "\n");
				joueur.envoyer("Score personnel: " + scoreJoueur
						+ "\nScore casino : " + scoreDealer );
				if(scoreDealer > 21) {
					joueur.envoyer("Le casino a dépassé 21, vous avez gagné.");
					joueur.gagner(mise);
					joueur.envoyer("Votre solde: "+ joueur.solde() + " jetons");
					rejouer(joueur);
					continue;
				}
				if(scoreJoueur > scoreDealer) {
					joueur.envoyer("Vous avez gagné.");
					joueur.gagner(mise);
				}else if(scoreJoueur < scoreDealer) {
					joueur.envoyer("Vous avez perdu.");
					joueur.perdre(mise);
					controle(joueur);
				}else {
					if(joueur.blackjack() && !croupier.mainDealer.isBlackjack()) {
						joueur.envoyer("Vous avez gagné et fait un BlackJack.");
						joueur.gagner(mise);
					}else if(!joueur.blackjack() && croupier.mainDealer.isBlackjack()) {
						joueur.envoyer("Vous avez perdu et le casino a fait un BlackJack.");
						joueur.perdre(mise);
						controle(joueur);
					}else {
						joueur.envoyer("Vous avez fait une égalité.");
					}
				}
				joueur.envoyer("Votre solde: "+ joueur.solde() + " jetons");
				
				rejouer(joueur);
				
			}
		}
	}
}
