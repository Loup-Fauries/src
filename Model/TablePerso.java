package Model;

public class TablePerso extends Table{

	private Joueur propri�taire;
	
	public TablePerso(String nom, int taille, Joueur proprio) {
		super(nom, taille);
		propri�taire = proprio;
	}
	
	private boolean ProprioPresent() {
		for (Joueur joueur : joueurs) {
			if(joueur == propri�taire) {
				return true;
			}
		}
		return false;
	}
	
	public void destTable() {
		if(!ProprioPresent()) {
			for (Joueur joueur : joueurs) {
				joueur.envoyer("La table est ferm�e, veuillez quitter la table.");
			}
		}
	}
	
	public void run() {
		initialiserPartie();
		destTable();
		for (Joueur joueur : joueurs) {
			joueur.envoyer(joueur.afficherMain());
			joueur.envoyer(croupier.mainDealer.toString());
			if(joueur.score() >= 21) {
				joueur.envoyer("stand");
				joueur.setCouche(true);
			}else {
				joueur.envoyer("hit");
			}
		}
		destTable();
		boolean fini = false;
		while(!fini) {
			int nbCouche = 0;
			nbCouche = unTour();
			fini = (nbCouche == joueurs.size());
		}
		destTable();
		croupier.mainDealer.retournerCartes();
		while(croupier.mainDealer.cartesValeur() < 17) {
			croupier.mainDealer.ajouter(croupier.TirerCarteVisible());
		}
		
		int scoreDealer = croupier.mainDealer.cartesValeur();
		for (Joueur joueur : joueurs) {
			int scoreJoueur = joueur.score();
			if(scoreJoueur > 21) {
				joueur.envoyer("\nLes jeux sont fini.\nScore personnel: "+ joueur.score()
						+ "\nVous avez d�passer 21, vous avez perdu.");
				continue;
			}
			joueur.envoyer("\nLes jeux sont faits, voici les cartes."
					+ "\nCarte dans votre main : " + joueur.afficherMain()
					+ "\n Voici la main du casino : " + croupier.mainDealer.toString() + "\n");
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
				if(joueur.blackjack() && !croupier.mainDealer.isBlackjack()) {
					joueur.envoyer("Vous avez gagn� et fait un BlackJack.");
				}else if(!joueur.blackjack() && croupier.mainDealer.isBlackjack()) {
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
