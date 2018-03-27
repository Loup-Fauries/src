package Client;


import java.io.*;
import java.net.*;

/**
 * Classe représentant le client
 * 
 * @version: 1.0
 */
public class Client {

	/**
	 * Déroulement d'une partie côté client:
	 * Connexion au serveur
	 * Affichage tour par tour de la main du client et du Croupier
	 * Choix du client, Hit ou Stand
	 * Affichage final, Gagnant et Perdant
	 * Déconnexion
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
		System.out.println("Bonjour,\n Bienvenue au casino.\n \n Menu:\n  1 - Rejoindre une table\n  2 - Table personnalisée");

	    try {
	    	socket = new Socket("localhost",2009);
			fluxSortieSocket = new PrintStream(socket.getOutputStream());
		    fluxEntreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    fluxEntreeStandard = new BufferedReader(new InputStreamReader(System.in));
		    
			reponse = fluxEntreeStandard.readLine();
			while(!reponse.equals("1") && !reponse.equals("2")){
				System.out.println("Cette option n'est pas valide.\n Menu:\n  1 - Rejoindre une table\n  2 - Table personnalisée");
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
			    
				System.out.println("\n Quelle table désirez-vous rejoindre ?");
				reponse = fluxEntreeStandard.readLine();
				while(Integer.parseInt(reponse) > Integer.parseInt(nbTable)){
					System.out.println("Cette option n'est pas valide.\n Quelle table désirez-vous rejoindre ?");
					reponse = fluxEntreeStandard.readLine();
				}
				fluxSortieSocket.println(reponse);

				System.out.println(fluxEntreeSocket.readLine());

				
			}
			else {														//Table Personnalisée
				System.out.println("\nMalheureusement cette option n'est pas encore disponible");
		    	socket.close();
		    	return;
			}
		    

		    //Début de la partie
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
		    while ((retour = fluxEntreeSocket.readLine()) != null){
				System.out.println(retour);
			}
		    
		    fluxEntreeSocket.close();
		    fluxSortieSocket.close();
		    socket.close();
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
}