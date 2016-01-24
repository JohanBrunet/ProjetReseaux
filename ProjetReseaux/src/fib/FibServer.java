package fib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

import fib.FibClientThread;
import fib.FibServer;

public class FibServer {
	private Hashtable<Integer, Integer> cache;
	private int port;
	private InputStream input;
	private OutputStream output;
	private int valueToCompute;
	private int computedValue;

	/**
	 * Constructeur du serveur.
	 * Initialise le port de connexion et le cache.
	 * La valeur 1 associée à fib(1) est initialisée dans le cache à la création du serveur.
	 * 
	 * @param port
	 * 		Le port utilisé pour la connexion.
	 */
	public FibServer(int port) {
		this.port = port;
		cache = new Hashtable<Integer, Integer>();
		cache.put(1, 1);
	}

	/**
	 * Méthode permettant de calculer la suite de Fibonacci demandée par le client.
	 * <ul> Deux méthodes de calcul :
	 * 		<li> Le calcul demandé est dans le cache, on récupère la valeur associée et on la renvoie </li>
	 * 		<li> Le calcul demandé n'est pas dans le cache : on le calcule par création successive de clients factices 
	 * 			(ils demandent à chaque fois fib(n-1) et fib(n-2) </li>
	 * </ul>
	 */
	@SuppressWarnings("resource")
	public void computeFib() {
		try {
			ServerSocket sServer = new ServerSocket(this.port);
			Socket socket = sServer.accept();
			Scanner scan = new Scanner(socket.getInputStream());
			while (scan.hasNext()) {
				this.valueToCompute = Integer.parseInt(scan.nextLine());
				if (isInCache(this.valueToCompute)) {
					this.computedValue = this.computedValue + getInCache(this.valueToCompute);
				}
				else {
					if(this.valueToCompute-1 == 0) {
						this.computedValue++;
					}
					else {
						FibClientThread clientFactice = new FibClientThread(socket, this.port, this.valueToCompute-1);
						FibClientThread clientFactice2 = new FibClientThread(socket, this.port, this.valueToCompute-2);
						clientFactice.run();
						clientFactice2.run();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Création du serveur.
	 * @param argv
	 * 		Les arguments de la ligne de commande.
	 */
	public static void main(String[] args) {
		FibServer server = new FibServer (Integer.parseInt(args[0]));
		server.computeFib();
	}

	/**
	 * Ajout d'une paire (clé,valeur) dans le cache.
	 * @param key
	 * 		Le nombre dont on a calculé la factorielle.
	 * @param value
	 * 		La valeur de la factorielle.
	 */
	synchronized private void addToCache(Integer key, Integer value) {
		cache.put(key, value);
	}

	/**
	 * Obtenir la valeur associée à une clé (la valeur de la factorielle du nombre donné).
	 * @param key
	 * 		Le nombre dont on veut la factorielle.
	 * @return La factorielle du nombre passé en paramètre.
	 */
	synchronized private Integer getInCache(Integer key) {
		return cache.get(key);
	}

	/**
	 * Vérifier si le nombre demandé est déjà présent dans le cache (pour ne pas refaire le calcul.)
	 * @param key
	 * 		Le nombre à chercher dans le cache.
	 * @return true si le nombre est présent dans le cache, false sinon.
	 */
	synchronized private boolean isInCache(Integer key) {
		if (cache.containsKey(key)) {
			return true;
		}
		return false;
	}
}

class FibClientThread extends Thread {

	private Socket socket;
	private int port;
	private int valueToCompute;

	/**
	 * Constructeur de la classe FibClientThread.
	 * Permet d'initialiser le socket et le port servant à la connexion.
	 * @param socket
	 * 		Le socket sur lequel se connecter.
	 * @param port
	 * 		Le numéro du port sur lequel on se connecte.
	 */
	public FibClientThread(Socket socket, int port, int valueToCompute) {
		this.valueToCompute = valueToCompute;
		this.socket = socket;
		this.port = port;
	}

	public void run() {
		new FibClient(this.socket.getInetAddress().toString(), this.port, this.valueToCompute);
	}
}
