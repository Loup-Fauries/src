package Serveur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Model.Croupier;

public class Accepter_clients implements Runnable {
	   private ServerSocket serverSocket;
	   private ArrayList<Socket> sockets;
	   private int nbClientsMax;
	   
		public Accepter_clients(ServerSocket s, int nbClientsMax){
			serverSocket = s;
			this.nbClientsMax = nbClientsMax;
			 sockets = new ArrayList<Socket>(nbClientsMax);
		}
		
		private void attendreConnexions() {
			sockets.clear();
			for(int i=0; i<nbClientsMax; i++) {
				Socket socket = null;
    			try {
    				socket = serverSocket.accept();
    				sockets.add(socket);
        			System.out.println("Le client numéro "+i+ " est connecté !");
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
		}
		
		
		public void run() {
        	while(true) {
        		attendreConnexions();
        		
        		Croupier tCroupier = new Croupier(sockets);
        		tCroupier.start();
        	}
		}
}