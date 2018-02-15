package Client;


import java.io.*;
import java.net.*;

public class Client {

	public static void main(String args[]){
		Socket socket;
		BufferedReader  fluxEntreeStandard;
	    PrintStream     fluxSortieSocket;
	    BufferedReader  fluxEntreeSocket;
	    String retour, reponse;
	    boolean coucher = false;
	    
		System.out.println("Bonjour,\n Bienvenue au casino.\n Voulez vous commencez une partie de BlackJack ? (appuyer sur une touche pour commencer)\n");									//Action

	    try {
	    	socket = new Socket("localhost",2009);
			fluxSortieSocket = new PrintStream(socket.getOutputStream());
		    fluxEntreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    fluxEntreeStandard = new BufferedReader(new InputStreamReader(System.in));
		    
		    retour = fluxEntreeSocket.readLine();
		    if(!retour.equals("Okcommencer")) {
		    	socket.close();
		    	return;
		    }
		    while(!coucher) {
		    	retour = fluxEntreeSocket.readLine();
				System.out.println("Voici votre main:\n" +retour);
				retour = fluxEntreeSocket.readLine();
				System.out.println("Voici la main de la banque:\n" +retour);
				
				retour = fluxEntreeSocket.readLine();
				if(retour.equals("stand")) {
					coucher = true;
					break;
				}
				retour = fluxEntreeSocket.readLine();
				System.out.println("C'est votre tour!");
				System.out.println("Que voulez-vous faire, Hit (1) ou stand (2) ?");
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