package Serveur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Model.Carte;
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
	   private ArrayList<Table> Tables;
	   private PrintStream     fluxSortieSocket;
	   private BufferedReader  fluxEntreeSocket;
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
			for (int i = 1; i<=nbTables; i++) {
				Tables.add(new Table("Perma "+i, nbClientsMax, 10));
			}
		}
		
		public void envoieTable(Socket service) {
			String taille = Integer.toString(Tables.size());
			int i=1;

			envoyer(taille, service);
			
			for(Table table : Tables){
				envoyer("  "+i+": "+table.toString(), service);
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
			Tables.add(new TablePerso(nom, taille, mise, proprio));
		}
		
		
		/**
		 * Envoie d'un message au client
		 * 
		 * @param chaine
		 * 					Correspond au message à envoyer
		 */
		public void envoyer(String chaine, Socket service) {
			if (fluxSortieSocket == null) {
				try {
					fluxSortieSocket = new PrintStream(service.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fluxSortieSocket.println(chaine);
		}
		
		/**
		 * Réception d'un message envoyé par un client
		 * 
		 * @return une chaine de caractères, correspond au message reçu
		 */
		public String recevoir(Socket service) {
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
		 * acceuil le client en le faisant rejoindre une table
		 * public ou privée
		 * 
		 */
		private void accueilJoueur() {
			String reponse, nbj, nom, mise;
			socket = null;
			try {
				socket = serverSocket.accept();
    			System.out.println(" Un nouveau client s'est connecté !");
    			reponse = recevoir(socket);
    			if(reponse.equals("1")) {
        			System.out.println("  Il désire rejoindre une table existante");
    				envoieTable(socket);
    				reponse = recevoir(socket);
    				
        			Tables.get(Integer.parseInt(reponse)-1).ajoutJoueur(socket);
        			Tables.get(Integer.parseInt(reponse)-1).start();
    			}
    			else {
        			System.out.println("  Il désire créer une partie personnaliséé");

    				nbj = recevoir(socket);
    				nom = recevoir(socket);
    				mise = recevoir(socket);
    				//AjoutTable(nom, taille, mise, );
        			//Tables.get(Tables.size()-1).ajoutJoueur(socket);
        			//Tables.get(Tables.size()-1).start();
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
			
        	while(true) {
        		accueilJoueur();
        	}
		}
		
		
		
}