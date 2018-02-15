package Serveur;

import java.io.IOException;
import java.net.*;

public class Serveur {

	public static void main(String[] zero){
		
		ServerSocket socket;
		try {
			socket = new ServerSocket(2009);
			Thread t = new Thread(new Accepter_clients(socket, 2));
			t.start();
			System.out.println("Le Casino (serveur centrale) est prêt !");
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}