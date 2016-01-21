package fact;

import java.io.OutputStream;
import java.io.PrintStream;
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

	private static Hashtable<Integer, Integer> cache;
	private int port;
	private int valueToCompute;

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
			while (true) {
				Socket socket = sServer.accept();
				FactClientThread threadClient = new FactClientThread(socket);
				threadClient.run();
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
	 * Vérifier si le nombre demandé est déjà présent dans le cache (pour ne pas refaire le calcul.
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

/**
 * Thread client
 * @author Johan Brunet
 *
 */
class FactClientThread extends Thread {

	private Socket socket;
	private int port;
	private PrintStream output;
	private int fact;
	private int computedValue;
	private int valueToCompute;

	/**
	 * Constructeur de la classe FactClientThread.
	 * Permet d'initialiser le socket et le port servant à la connexion.
	 * @param socket
	 * 		Le socket sur lequel se connecter.
	 * @param port
	 * 		Le numéro du port sur lequel on se connecte.
	 */
	public FactClientThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			Scanner scan= new Scanner(this.socket.getInputStream());
			while (scan.hasNext()) {
				this.valueToCompute = Integer.parseInt(scan.nextLine());
				if (FactServer.isInCache(this.valueToCompute)) {
					this.computedValue = FactServer.getInCache(this.valueToCompute);
				}
				else {
					FactClient clientFactice = new FactClient(socket.getInetAddress().toString(), this.port, this.fact-1);
					this.fact = clientFactice.askFact();
				}
				this.computedValue = this.computedValue * this.fact;
			}
			FactServer.addToCache(this.valueToCompute, this.computedValue);
			this.output = new PrintStream(socket.getOutputStream());
			this.output.println(this.computedValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}