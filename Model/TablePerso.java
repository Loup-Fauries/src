package Model;

public class TablePerso extends Table{

	private Joueur propriétaire;
	
	public TablePerso(String nom, int taille, int mise, Joueur proprio) {
		super(nom, taille, mise);
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
				joueur.envoyer("La table est fermée.");
				joueur.deconnexion();
			}
		}
	}
	
	public void run() {
		initialiserPartie();
		destTable();
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
		}
		for (Joueur joueur : joueurs) {
			joueur.deconnexion();
		}
	}
}
