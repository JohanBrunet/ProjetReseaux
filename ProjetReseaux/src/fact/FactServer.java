package fact;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe permettant de créer un serveur pour le calcul de factorielle
 * 
 * @author Johan Brunet, Quentin Bouygues
 *
 */
public class FactServer {

	// Client part

	public class ClientThread extends Thread {

		private Socket socket;
		private InputStream sInput;
		private OutputStream sOutput;
		private String name;

		public ClientThread(Socket socket, String name) {
			try {
				this.socket = socket;
				this.sOutput = this.socket.getOutputStream();
				this.sInput  = this.socket.getInputStream();
				this.name = name;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("resource")
		public void run() {
			Scanner sc = new Scanner(this.sInput);
			while (true) {
				if (sc.hasNext()) {
					String msg = sc.nextLine();
					System.out.println(this.name + ": " + msg);
					broadcast(this.name + ": " + msg);
				}				
			}
		}
	}

	// Server part

	private ArrayList<ClientThread> socks = new ArrayList<ClientThread>();
	private int port;
	private int num = 0;

	public FactServer(int port) {
		this.port = port;
	}

	public synchronized void broadcast(String msg) {
		for (ClientThread e : this.socks) {
			PrintStream output = new PrintStream(e.sOutput);
			output.println(msg);
		}
	}

	@SuppressWarnings("resource")
	public void run() {
		try {
			ServerSocket sServer = new ServerSocket(this.port);
			while (true) {
				Socket socket = sServer.accept();
				ClientThread client = new ClientThread(socket, "Thread " + this.num);;
				System.out.println("Connection of client " + this.num);
				this.num++;
				this.socks.add(client);
				client.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant le calcul d'une factorielle.
	 * @param n
	 * 		La valeur n de laquelle on doit calculer la factorielle.
	 * @return La factorielle de n.
	 * @throws Exception 
	 */
	public int fact(int n) throws Exception {
		if (n < 0) {
			throw new Exception("Nombre négatif !");
		}
		else if (n != 0) {
			int fact = 1;
			int i = 1;
			while (i <= n) {
				fact = fact*i;
				i++;
			}
			return fact;
		}
		else {
			return 1;
		}
	}

	public static void main(String [] argv) {
		FactServer client = new FactServer (50000);
		client.run();
	}
	
	//TODO implémentation du cache sous forme de HashTable
}


