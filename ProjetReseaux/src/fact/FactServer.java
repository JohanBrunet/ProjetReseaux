package fact;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Classe permettant de créer un serveur pour le calcul de factorielle
 * 
 * @author Johan Brunet, Quentin Bouygues
 *
 */
public class FactServer {

	private Hashtable<Integer, Integer> cache = new Hashtable<Integer, Integer>();
	private int port;
	private InputStream input;
	private OutputStream output;

	public FactServer(int port) {
		this.port = port;
	}

	@SuppressWarnings("resource")
	public void run() {
		try {
			ServerSocket sServer = new ServerSocket(this.port);
			while (true) {
				Socket socket = sServer.accept();
				FactClientThread client = new FactClientThread(socket, this.port);
				client.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String [] argv) {
		FactServer client = new FactServer (50000);
		client.run();
	}
	
	synchronized private void addToCache(Integer key, Integer value) {
		cache.put(key, value);
	}
	
	synchronized private Integer getInCache(Integer key) {
		return cache.get(key);
	}
	
	synchronized private boolean isInCache(Integer key) {
		if (cache.containsKey(key)) {
			return true;
		}
		return false;
	}
}

class FactClientThread extends Thread {

	private Socket socket;
	private int port;

	/**
	 * Constructeur de la classe FactClientThread.
	 * Permet d'initialiser le socket et le port servant à la connexion.
	 * @param socket
	 * 		Le socket sur lequel se connecter.
	 * @param port
	 * 		Le numéro du port sur lequel on se connecte.
	 */
	public FactClientThread(Socket socket, int port) {
		this.socket = socket;
		this.port = port;
	}

	public void run() {
		Scanner sc;
		try {
			sc = new Scanner(this.socket.getInputStream());
			while (true) {
				if (sc.hasNext()) {
					int factTmp = sc.nextInt();
					
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


