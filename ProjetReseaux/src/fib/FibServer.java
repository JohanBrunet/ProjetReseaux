package fib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;


public class FibServer extends Thread{
	private static Hashtable<Integer, Integer> cache;
	private int port;
	private InputStream input;
	private OutputStream output;
	private int valueToCompute;
	private int computedValue;
	private int id;

	/**
	 * Le port du serveur 1
	 */
	public static int SERVER_1_PORT;
	
	/**
	 * Le port du serveur 2
	 */
	public static int SERVER_2_PORT;
	
	/**
	 * Constructeur du serveur.
	 * Initialise le port de connexion et le cache.
	 * La valeur 1 associée à fib(1) est initialisée dans le cache à la création du serveur.
	 * 
	 * @param port
	 * 		Le port utilisé pour la connexion.
	 */
	public FibServer(int id, int port) {
		this.port = port;
		this.id = id;
		cache = new Hashtable<Integer, Integer>();
		cache.put(1, 1);
		cache.put(0, 0);
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
	public void run() {
		try {
			ServerSocket sServer = new ServerSocket(this.port);
			while (true) {
				Socket socket = sServer.accept();
				FibClientThread threadClient = new FibClientThread(socket, this.port);
				System.out.println("Un thread a été crée");
				threadClient.start();
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
		System.out.println("Serveurs en ligne");
		// Récupère les arguments de l'utilisateur
		
		try {
			SERVER_1_PORT = Integer.parseInt(args[1]);
			SERVER_2_PORT = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.out.println("Erreur dans les numéros de ports");
			return;
		}

		// Créé et démarre les deux serveurs de calcul
		new FibServer(1, SERVER_1_PORT).start();
		new FibServer(2, SERVER_2_PORT).start();
	}

	/**
	 * Ajout d'une paire (clé,valeur) dans le cache.
	 * @param key
	 * 		Le nombre dont on a calculé la factorielle.
	 * @param value
	 * 		La valeur de la factorielle.
	 */
	synchronized static void addToCache(Integer key, Integer value) {
		cache.put(key, value);
	}

	/**
	 * Obtenir la valeur associée à une clé (la valeur de la factorielle du nombre donné).
	 * @param key
	 * 		Le nombre dont on veut la factorielle.
	 * @return La factorielle du nombre passé en paramètre.
	 */
	synchronized static Integer getInCache(Integer key) {
		return cache.get(key);
	}

	/**
	 * Vérifier si le nombre demandé est déjà présent dans le cache (pour ne pas refaire le calcul.)
	 * @param key
	 * 		Le nombre à chercher dans le cache.
	 * @return true si le nombre est présent dans le cache, false sinon.
	 */
	synchronized static boolean isInCache(Integer key) {
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
	private int computedValue;
	private int computedValue2;
	private PrintStream output;
	/**
	 * Constructeur de la classe FibClientThread.
	 * Permet d'initialiser le socket et le port servant à la connexion.
	 * @param socket
	 * 		Le socket sur lequel se connecter.
	 * @param port
	 * 		Le numéro du port sur lequel on se connecte.
	 */
	public FibClientThread(Socket socket, int port) {
		this.socket = socket;
		this.port = port;
	}

	@SuppressWarnings("resource")
	public void run() {
		try {
			Scanner scan = new Scanner(this.socket.getInputStream());
			this.valueToCompute = scan.nextInt();
			System.out.println(this.valueToCompute + " est la valeur reçue");
			this.output = new PrintStream(socket.getOutputStream());

			if (FibServer.isInCache(this.valueToCompute)) {
				this.computedValue = FibServer.getInCache(this.valueToCompute);
				System.out.println("Est en cache");
				output.println(this.computedValue);
			}
			else {
				System.out.println("N'est pas en cache");
				FibClient clientFactice = new FibClient(InetAddress.getLocalHost().getHostAddress(), FibServer.SERVER_1_PORT, this.valueToCompute-1);
				this.computedValue = clientFactice.askFib();
				FibClient clientFactice2 = new FibClient(InetAddress.getLocalHost().getHostAddress(), FibServer.SERVER_2_PORT, this.valueToCompute-2);
				this.computedValue2 = clientFactice2.askFib();
				this.computedValue = this.computedValue + this.computedValue2;
				System.out.println(this.computedValue + ": addition des valeurs retournées par les clients ");
				FibServer.addToCache(this.valueToCompute, this.computedValue);
				output.println(this.computedValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
