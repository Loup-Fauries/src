package Client;


import java.io.*;
import java.net.*;

/**
 * Classe repr�sentant le client
 * 
 * @version: 1.0
 */
public class Client {

	/**
	 * D�roulement d'une partie c�t� client:
	 * Connexion au serveur
	 * Affichage tour par tour de la main du client et du Croupier
	 * Choix du client, Hit ou Stand
	 * Affichage final, Gagnant et Perdant
	 * D�connexion
	 * 
	 */
	public static void main(String args[]){
		Socket socket;
		BufferedReader  fluxEntreeStandard;
	    PrintStream     fluxSortieSocket;
	    BufferedReader  fluxEntreeSocket;
	    String nbTable, retour, reponse;
	    boolean coucher = false;

	    //Menu
		System.out.println("Bonjour,\n Bienvenue au casino.\n \n Menu:\n  1 - Rejoindre une table\n  2 - Table personnalis�e");

	    try {
	    	socket = new Socket("localhost",2009);
			fluxSortieSocket = new PrintStream(socket.getOutputStream());
		    fluxEntreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    fluxEntreeStandard = new BufferedReader(new InputStreamReader(System.in));
		    
			reponse = fluxEntreeStandard.readLine();
			while(!reponse.equals("1") && !reponse.equals("2")){
				System.out.println("Cette option n'est pas valide.\n Menu:\n  1 - Rejoindre une table\n  2 - Table personnalis�e");
				reponse = fluxEntreeStandard.readLine();
			}
			
			fluxSortieSocket.println(reponse);
			if(reponse.equals("1")) {									//Rejoindre une table
				System.out.println("\n Liste des tables:");
			    nbTable = fluxEntreeSocket.readLine();
			    for(int i = 1; i <= Integer.parseInt(nbTable); i++) {
				    retour = fluxEntreeSocket.readLine();
			    	System.out.println(retour);
			    }
			    
				System.out.println("\n Quelle table d�sirez-vous rejoindre ?");
				reponse = fluxEntreeStandard.readLine();
				while(Integer.parseInt(reponse) > Integer.parseInt(nbTable) || Integer.parseInt(reponse) == 0){
					System.out.println("Cette option n'est pas valide.\n Quelle table d�sirez-vous rejoindre ?");
					reponse = fluxEntreeStandard.readLine();
				}
				fluxSortieSocket.println(reponse);
				
			}
			else {														//Table Personnalis�e
				System.out.println("\nCombien de joueurs peuvent jouer autour de votre table ?");
				reponse = fluxEntreeStandard.readLine();
				fluxSortieSocket.println(reponse);

				System.out.println("\nQuel nom voulez vous donnez � votre table ?");
				reponse = fluxEntreeStandard.readLine();
				fluxSortieSocket.println(reponse);

				System.out.println("\nQuelle est la mise minimal de votre table ?");
				reponse = fluxEntreeStandard.readLine();
				fluxSortieSocket.println(reponse);
								
			}
		    
			boolean continu= true;
			while(continu){
				System.out.println("\n \n \n \n \n \n \n \n \n \n \n" + fluxEntreeSocket.readLine());
			    //D�but de la partie
			    retour = fluxEntreeSocket.readLine();
			    if(!retour.equals("Okcommencer")) {
			    	socket.close();
			    	return;
			    }
			    while(!coucher) {
			    	retour = fluxEntreeSocket.readLine();
					System.out.println("\nVoici votre main:\n" +retour);
					retour = fluxEntreeSocket.readLine();
					System.out.println("Voici la main de la banque:\n" +retour);
					
					retour = fluxEntreeSocket.readLine();
					if(retour.equals("stand")) {
						coucher = true;
						break;
					}
					retour = fluxEntreeSocket.readLine();
					System.out.println("\nC'est votre tour !");
					System.out.println("Que voulez-vous faire, Hit (1) ou Stand (2) ?");
					reponse = fluxEntreeStandard.readLine();
					while(!reponse.equals("1") && !reponse.equals("2")){
						System.out.println("Nous voulons une valeur de 1 ou 2.\nQue voulez-vous faire, Hit (1) ou stand (2) ?");
						reponse = fluxEntreeStandard.readLine();
					}
					fluxSortieSocket.println(reponse);
					if(reponse.equals("2")) {
						coucher = true;
						break;
					}
			    }
			    retour = fluxEntreeSocket.readLine();
			    while (!retour.equals("rejouer")){
					System.out.println(retour);
					retour = fluxEntreeSocket.readLine();
				}
			    retour = fluxEntreeSocket.readLine();
			    System.out.println(retour);
				reponse = fluxEntreeStandard.readLine();
				while(!reponse.equals("oui") && !reponse.equals("non")){
					System.out.println("Ce n'est pas la r�ponse attendu, voulez-vous recommencer une partie ?");
					reponse = fluxEntreeStandard.readLine();	
				}
				
				if (reponse.equals("non")){
					continu=false;
				}
					
				fluxSortieSocket.println(reponse);

			}
		    fluxEntreeSocket.close();
		    fluxSortieSocket.close();
		    socket.close();
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
}