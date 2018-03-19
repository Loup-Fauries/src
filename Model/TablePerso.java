package Model;

public class TablePerso extends Table{

	private Joueur propriétaire;
	
	public TablePerso(String nom, Joueur proprio) {
		super(nom);
		propriétaire = proprio;
	}
	
	private boolean ProprioPresent() {
		for (Joueur joueur : joueurs) {
			if(joueur == propriétaire) {
				return true;
			}
		}
		return false;
	}
	
	public void destTable() {
		if(!ProprioPresent()) {
			for (Joueur joueur : joueurs) {
				joueur.envoyer("La table est fermée, veuillez quitter la table.");
			}
		}
	}
	
	public void run() {
		initialiserPartie();
		destTable();
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
		destTable();
		boolean fini = false;
		while(!fini) {
			int nbCouche = 0;
			nbCouche = unTour();
			fini = (nbCouche == joueurs.size());
		}
		destTable();
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
				joueur.envoyer("Le casino a dépassé 21, vous avez gagné.");
				continue;
			}
			if(scoreJoueur > scoreDealer) {
				joueur.envoyer("Vous avez gagné.");
			}else if(scoreJoueur < scoreDealer) {
				joueur.envoyer("Vous avez perdu.");
			}else {
				if(joueur.blackjack() && !mainDealer.isBlackjack()) {
					joueur.envoyer("Vous avez gagné et fait un BlackJack.");
				}else if(!joueur.blackjack() && mainDealer.isBlackjack()) {
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
