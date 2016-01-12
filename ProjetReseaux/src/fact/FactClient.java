package fact;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe permettant de créer un client pour la demande de calcul de factorielle au serveur
 * 
 * @author Johan Brunet
 *
 */
public class FactClient {

	public class Listen extends Thread {

		Socket socket;
		InputStream sInput;

		public Listen(Socket socket) {
			try {
				this.socket = socket;
				sInput  = socket.getInputStream();
			} catch (Exception e) {}
		}

		@SuppressWarnings("resource")
		public void run() {
			Scanner sc = new Scanner(sInput);
			while (true) {
				if (sc.hasNext()) {
					String msg = sc.nextLine();
					System.out.println(msg);
				}
			}
		}
	}

	private String address;
	private int port;
	private int fact;
	
	public FactClient(String address, int port, int fact) {
		this.address = address;
		this.port = port;
		this.fact = fact;
	}
	
	public void run() {
		InetAddress address;
		Socket socket;
		Listen listen;

		try {
			address = InetAddress.getByName(this.address);
			socket = new Socket(address, this.port);
			listen = new Listen(socket);
			listen.start();
			PrintStream output = new PrintStream(socket.getOutputStream());
			while (true) {
				output.println(this.fact);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		try {
			FactClient client = new FactClient(argv[0], Integer.parseInt(argv[1]), Integer.parseInt(argv[2]));
			client.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

