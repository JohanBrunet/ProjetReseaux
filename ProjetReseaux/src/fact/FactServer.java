package fact;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Classe permettant de créer un serveur pour le calcul d'une factorielle.
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

	/**
	 * Constructeur du serveur.
	 * Initialise le port de connexion et le cache.
	 * La valeur 1 associée à fact(0) est initialisée dans le cache à la création du serveur.
	 * 
	 * @param port
	 * 		Le port utilisé pour la connexion.
	 */
	public FactServer(int port) {
		this.port = port;
		cache = new Hashtable<Integer, Integer>();
		cache.put(0, 1);
	}

	/**
	 * Méthode permettant de calculer la factorielle demandée par le client.
	 * <ul> Deux méthodes de calcul :
	 * 		<li> Le calcul demandé est dans le cache, on récupère la valeur associée et on la renvoie </li>
	 * 		<li> Le calcul demandé n'est pas dans le cache : on le calcul par création successive de clients factices 
	 * 			(ils demandent à chaque fois fact(n-1) </li>
	 * </ul>
	 */
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

	/**
	 * Création du serveur.
	 * @param argv
	 * 		Les arguments de la ligne de commande.
	 */
	public static void main(String[] args) {
		FactServer server = new FactServer (Integer.parseInt(args[0]));
		server.computeFact();
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
	 * Vérifier si le nombre demandé est déjà présent dans le cache (pour ne pas refaire le calcul.
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