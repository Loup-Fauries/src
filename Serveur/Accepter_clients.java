package Serveur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Model.Carte;
import Model.Croupier;
import Model.Joueur;
import Model.Table;
import Model.TablePerso;

/**
 * Classe permettant la connexion d'un client(ouverture de socket...)
 * 
 * @version: 2.0
 */
public class Accepter_clients implements Runnable {
	   private ServerSocket serverSocket;
	   private Socket socket;
	   private int nbClientsMax;
	   private ArrayList<Joueur> joueurs;
	   private ArrayList<Table> Tables;
	   private ArrayList<Croupier> croupiers;
	   //
	   /**
	    * Constructeur de la classe
	    * 
	    */
		public Accepter_clients(ServerSocket s, int nbClientsMax){
			serverSocket = s;
			this.nbClientsMax = nbClientsMax;
			socket = null;
		}
		
		
		public void creationTables(Integer nbTables) {
			Tables = new ArrayList<Table>(nbTables);
			croupiers = new ArrayList<>(nbTables);
			for (int i = 1; i<=nbTables; i++) {
				croupiers.add(new Croupier());
				Tables.add(new Table("Perma "+i, nbClientsMax, 10, croupiers.get(i-1)));
			}
		}
		
		public void envoieTable(Joueur joueur) {
			String taille = Integer.toString(Tables.size());
			int i=1;

			joueur.envoyer(taille);
			
			for(Table table : Tables){
				joueur.envoyer("  "+i+": "+table.toString());
				i++;
			}
			
		}
		
		public String listeTable(){
			String listedetable = "";
			int i=1;
			for(Table table : Tables){
				listedetable += i + ": " + table.toString();
				i++;
			}
			return listedetable;
		}
		
		public void AjoutTable(String nom, int taille, int mise, Joueur proprio) {
			croupiers.add(new Croupier());
			Tables.add(new TablePerso(nom, taille, mise, proprio, croupiers.get(croupiers.size()-1)));
		}
		
	
		
		/**
		 * acceuil le client en le faisant rejoindre une table
		 * public ou privée
		 * 
		 */
		private void accueilJoueur() {
			String reponse, nbj, nom, mise;
			Joueur joueur;
			socket = null;
			try {
				socket = serverSocket.accept();
    			System.out.println(" Un nouveau client s'est connecté !");
				joueur = new Joueur(socket);
    			reponse = joueur.recevoir();
    			if(reponse.equals("1")) {
        			System.out.println("  Il désire rejoindre une table existante");
    				envoieTable(joueur);
    				reponse = joueur.recevoir();
    				
        			Tables.get(Integer.parseInt(reponse)-1).ajoutJoueur(joueur);
    			}
    			else {
        			System.out.println("  Il désire créer une partie personnaliséé");

    				nbj = joueur.recevoir();
    				nom = joueur.recevoir();
    				mise = joueur.recevoir();
    				AjoutTable(nom, Integer.parseInt(nbj), Integer.parseInt(mise), joueur);
        			Tables.get(Tables.size()-1).ajoutJoueur(joueur);
        			Tables.get(Tables.size()-1).start();
    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		 * Création des tables initiales,
		 * puis acceuil des joueurs dans le casino
		 * 
		 */
		public void run() {
			creationTables(5);
			for(Table table : Tables){
				table.start();
			}
        	while(true) {
        		accueilJoueur();
        	}
		}
		
		
		
}