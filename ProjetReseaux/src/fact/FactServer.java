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

	private Hashtable<Integer, Integer> cache;
	private int port;
	private InputStream input;
	private OutputStream output;
	private int valueToCompute;
	private int computedValue;

	public FactServer(int port) {
		this.port = port;
		cache = new Hashtable<Integer, Integer>();
		cache.put(0, 1);
	}

	@SuppressWarnings("resource")
	public void computeFact() {
		try {
			ServerSocket sServer = new ServerSocket(this.port);
			Socket socket = sServer.accept();
			Scanner scan = new Scanner(socket.getInputStream());
			while (scan.hasNext()) {
				this.valueToCompute = Integer.parseInt(scan.nextLine());
				if (isInCache(this.valueToCompute)) {
					this.computedValue = getInCache(this.valueToCompute);
				}
				else {
					FactClientThread clientFactice = new FactClientThread(socket, this.port);
					clientFactice.run();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String [] argv) {
		FactServer server = new FactServer (50000);
		server.computeFact();
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
					
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


