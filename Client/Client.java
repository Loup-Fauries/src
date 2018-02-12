package Client;

import java.io.*;
import java.net.*;

public class Client {

	public static void main(String[] zero){
		
		Socket socket;
		String          reponse, retour;
	    BufferedReader  fluxEntreeStandard;
	    PrintStream     fluxSortieSocket;
	    BufferedReader  fluxEntreeSocket;
	    boolean coucher = false;
		try {
			socket = new Socket("localhost",2009);
		
			System.out.println("Bonjour,\n Bienvenue au casino.\n Voulez vous commencez une partie de BlackJack ? (Répondre 1 pour oui, et 2 pour non)\n");									//Action
			fluxEntreeStandard = new BufferedReader(new InputStreamReader(System.in));
			reponse = fluxEntreeStandard.readLine();
			while(!reponse.equals("1") && !reponse.equals("2")){
				System.out.println("Choix impossible, veuillez répondre 1 pour oui ou 2 pour non.\n Voulez vous commencez une partie de BlackJack ?\n");									//Action
				reponse = fluxEntreeStandard.readLine();
			}
			fluxSortieSocket = new PrintStream(socket.getOutputStream());
		    fluxEntreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    fluxSortieSocket.println(reponse);
		    retour = fluxEntreeSocket.readLine();
			if(reponse.equals("1") && retour.equals("Okcommencer")){
				while(!coucher){
					retour = fluxEntreeSocket.readLine();
					System.out.println("Voici votre main:\n" +retour);//manque de la somme total
					reponse = "Okmainjoueur";
					fluxSortieSocket.println(reponse);
					retour = fluxEntreeSocket.readLine();
					System.out.println("Voici la main de la banque:\n" +retour);//manque de la somme total
					reponse = "Okmaindealeur";
					fluxSortieSocket.println(reponse);
					retour = fluxEntreeSocket.readLine();
					if(!retour.equals("stand")){//<21 2carte
						System.out.println("Que voulez-vous faire, Hit (1) ou stand (2) ?");
						reponse = fluxEntreeStandard.readLine();
						while(!reponse.equals("1") && !reponse.equals("2")){
							System.out.println("Nous voulons une valeur de 1 ou 2.\nQue voulez-vous faire, Hit (1) ou stand (2) ?");
							reponse = fluxEntreeStandard.readLine();
						}
						fluxSortieSocket.println(reponse);
						if(reponse.equals("2")){
							coucher = true;
						}
					}else{
						coucher = true;
					}					
				}
				while ((retour = fluxEntreeSocket.readLine()) != null){
					System.out.println(retour);
				}
			}
			socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}