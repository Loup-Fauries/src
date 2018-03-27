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
		
		/**
		 * Attends qu'un certain nombre de clients se connectent
		 * 
		 */
		private void accueilJoueur() {
			String reponse;
			socket = null;
			try {
				socket = serverSocket.accept();
    			System.out.println(" Un nouveau client s'est connecté !");
    			reponse = recevoir(socket);
    			if(reponse.equals("1")) {
        			System.out.println("  Celui ci désire rejoindre une table existante");
    				envoieTable(socket);
    				reponse = recevoir(socket);
    				
        			Tables.get(Integer.parseInt(reponse)-1).ajoutJoueur(socket);
        			Tables.get(Integer.parseInt(reponse)-1).start();
    			}
    			else {
        			System.out.println("  Celui ci désire créer une partie personnaliséé");
    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Attente de joueurs puis affectation des joueurs à une Table
		 * puis lancement de la partie
		 * 
		 */
		public void run() {
			creationTables(5);
			
        	while(true) {
        		accueilJoueur();
        	}
		}
		
		public void creationTables(Integer nbTables) {
			Tables = new ArrayList<Table>(nbTables);
			for (int i = 0; i<nbTables; i++) {
				Tables.add(new Table("Perma "+i, nbClientsMax));
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
		
		public void AjoutTable(String nom, int taille, Joueur proprio) {
			Tables.add(new TablePerso(nom, taille, proprio));
		}
		
		
		/**
		 * Envoie d'un message au joueur
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
		 * Réception d'un message envoyé par un joueur
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
		
		
		
		
		
		
		
}