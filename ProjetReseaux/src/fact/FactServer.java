package fact;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Classe permettant de créer un serveur pour le calcul de factorielle
 * 
 * @author Johan Brunet, Quentin Bouygues
 *
 */
public class FactServer {

	private static Hashtable<Integer, Integer> cache = new Hashtable<Integer, Integer>();
	private int port;

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
	
	synchronized private static void addToCache(Integer key, Integer value) {
		cache.put(key, value);
	}
	
	synchronized private static Integer getInCache(Integer key) {
		return cache.get(key);
	}
	
	synchronized private static boolean isInCache(Integer key) {
		if (cache.containsKey(key)) {
			return true;
		}
		return false;
	}
}

class FactClientThread extends Thread {

	private Socket socket;
	private int port;

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
					//TODO création nouveau client pour calcul de fact(n-1)
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


