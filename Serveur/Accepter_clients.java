package Serveur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
	   private ArrayList<Socket> sockets;
	   private int nbClientsMax;
	   private ArrayList<Table> Tables;
	   //
	   /**
	    * Constructeur de la classe
	    * 
	    */
		public Accepter_clients(ServerSocket s, int nbClientsMax){
			serverSocket = s;
			this.nbClientsMax = nbClientsMax;
			 sockets = new ArrayList<Socket>(nbClientsMax);
		}
		
		/**
		 * Attends qu'un certain nombre de clients se connectent
		 * 
		 */
		private void attendreConnexions() {
			sockets.clear();
			for(int i=0; i<nbClientsMax; i++) {
				Socket socket = null;
    			try {
    				socket = serverSocket.accept();
    				sockets.add(socket);
        			System.out.println(" Un nouveau client s'est connecté !");
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
		}
		
		/**
		 * Attente de joueurs puis affectation des joueurs à une Table
		 * puis lancement de la partie
		 * 
		 */
		public void run() {
			int i=1;
        	while(true) {
    			System.out.println("\nTable n°"+i+":");
        		attendreConnexions();

        		Table tTable = new Table(sockets);
        		tTable.start();
        		i++;
        	}
		}
		
		public void creationTables(Integer nbTables) {
			Tables = new ArrayList<Table>(nbTables);
			for (int i = 0; i<nbTables; i++) {
				Tables.add(new Table("Perma"+i));
			}
		}
		
		public void AjoutTable(String nom, Joueur proprio) {
			Tables.add(new TablePerso(nom, proprio));
		}
}