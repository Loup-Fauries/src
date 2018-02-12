package Serveur;

import java.io.*;
import java.net.*;
import Model.*;

class Accepter_clients implements Runnable {

	   private ServerSocket socketserver;
	   private Socket socket, socket2;
	   private int nbrclient = 1;
	   PrintStream     fluxSortieSocket;
	   BufferedReader  fluxEntreeSocket;
	   String reponse;
		public Accepter_clients(ServerSocket s){
			socketserver = s;
		}
		
		public void run() {

	        try {
	        	while(true){
	        		socket = socketserver.accept();
	        		socket2 = socketserver.accept();// Un client se connecte on l'accepte
			  
	        		System.out.println("Le client numéro "+nbrclient+ " est connecté !");		//Action
	                nbrclient++;
	                fluxEntreeSocket = new BufferedReader (new InputStreamReader(socket.getInputStream()));
	                fluxSortieSocket = new PrintStream(socket.getOutputStream());
	                reponse = fluxEntreeSocket.readLine();
	                if(reponse.equals("1")){
	                	fluxSortieSocket.println("Okcommencer");
	                	Jeudecarte cartejouer = new Jeudecarte();
	                	cartejouer.creationpaquet();
	                	cartejouer.melanger();
	                	Jeudecarte carteJoueur = new Jeudecarte();
	                	Jeudecarte carteDealeur = new Jeudecarte();
	                	System.out.println("Tirage en cour\n\n");
	                	carteJoueur.tirerVisible(cartejouer);//manque la rendre visible
	                	carteJoueur.tirerVisible(cartejouer);//manque la rendre visible
	                	fluxSortieSocket.println(carteJoueur.toString());
	                	reponse = fluxEntreeSocket.readLine();
	                	if(reponse.equals("Okmainjoueur")){
	                		carteDealeur.tirerVisible(cartejouer);//manque la rendre visible
	                		carteDealeur.tirer(cartejouer);
	                		fluxSortieSocket.println(carteDealeur.toString());
		                	reponse = fluxEntreeSocket.readLine();
		                	if(reponse.equals("Okmaindealeur")){
		                		if (carteJoueur.cartesValeur() == 21){
		                			fluxSortieSocket.println("stand");
		                		}else{
		                			fluxSortieSocket.println("hit");
		                			reponse = fluxEntreeSocket.readLine();
		                			while(reponse.equals("1")){
		                				
		                				carteJoueur.tirerVisible(cartejouer);
		                				fluxSortieSocket.println(carteJoueur.toString());
		                				reponse = fluxEntreeSocket.readLine();
		        	                	if(reponse.equals("Okmainjoueur")){
		        	                		fluxSortieSocket.println(carteDealeur.toString());
		        	                		reponse = fluxEntreeSocket.readLine();
		        		                	if(reponse.equals("Okmaindealeur")){
		        		                		if (carteJoueur.cartesValeur() >= 21){
		        		                			fluxSortieSocket.println("stand");
		        		                		}else{
		        		                			fluxSortieSocket.println("hit");
		        		                			reponse = fluxEntreeSocket.readLine();
		        		                			
		        		                		}
		        		                	}
		        	                	}
		                			}
		                			
		                		}
		                		
		                		int valJoueur = carteJoueur.cartesValeur();
		                		if(valJoueur > 21){
		                			fluxSortieSocket.println("\nLes jeux sont fini.\nScore personnel: " + valJoueur + "\nVous avez dépasser 21, vous avez perdu.");
		                		}else{
		                			//tirage croupier
		                			carteDealeur.getCarte(1).setVisible(true);
		                			while(carteDealeur.cartesValeur() < 17){
		                				carteDealeur.tirerVisible(cartejouer);
		                			}
		                		
		                			fluxSortieSocket.println("\nLes jeux sont faits, voici les cartes.\nCarte dans votre main : " + carteJoueur.toString() + "\n Voici la main du casino : " + carteDealeur.toString() + "\n");
		                		
		                			//gagnant
		                			int valServeur = carteDealeur.cartesValeur();
		                			
		                			
		                			if(valServeur > 21){
		                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nLe casino a dépasser 21, vous avez gagnez.");
		                			}else{
		                				if(valJoueur > valServeur){
			                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez gagnez.");
		                				}else{
		                					if(valJoueur < valServeur){
				                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez perdu.");
		                					}else{
		                						if(valJoueur == 21 && valServeur ==21){
		                							int nbCartesJoueur = carteJoueur.tailleduJeu();
		    		                				int nbCartesServeur = carteDealeur.tailleduJeu();
		                							if(nbCartesJoueur == 2 && nbCartesServeur == 2){
						                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez fait une égalité.");
		                							}else{
		                								if(nbCartesJoueur == 2){
		        			                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez gagnez et fait un BlackJack.");
		                								}else{
		                									if(nbCartesServeur == 2){
			        			                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez perdu et le casino a fait un BlackJack.");
		                									}else{
		    					                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez fait une égalité.");
		                									}
		                								}
		                							}
		                						}else{
					                				fluxSortieSocket.println("Score personnel: " + valJoueur + "\nScore casino : " + valServeur + "\nVous avez fait une égalité.");
		                						}
		                					}		                					
		                				}
		                			}
		                		}
		                	}
	                	}
	                	
	                	
	                }
	                socket.close();
	        	}
	        
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}

	}