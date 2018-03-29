package Serveur;

import java.io.IOException;

import java.net.*;
/**
 * Classe représentant le serveur(Casino)
 * 
 * @version: 1.0
 */
public class Serveur {

	/**
	 * Ouverture de socket puis attente des clients
	 * 
	 */
	public static void main(String[] zero){
		
		ServerSocket socket;
		try {
			socket = new ServerSocket(2009);
			Thread t = new Thread(new Accepter_clients(socket, 5));
			t.start();
			System.out.println("Le Casino (serveur centrale) est prêt !");
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}