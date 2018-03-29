package Model;

public class TablePerso extends Table{

	private Joueur propriétaire;
	
	public TablePerso(String nom, int taille, int mise, Joueur proprio, Croupier croupier) {
		super(nom, taille, mise, croupier);
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
			for (Joueur joueur : lobby) {
				joueur.envoyer("La table est fermée.");
				joueur.deconnexion();
			}
		}
	}
	
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
			destTable();
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
