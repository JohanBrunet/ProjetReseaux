package fact;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe permettant de créer un client pour la demande de calcul de factorielle au serveur
 * 
 * @author Johan Brunet, Quentin Bouygues
 *
 */
public class FactClient {

	private String address;
	private int port;
	private int fact;
	
	public FactClient(String address, int port, int fact) {
		this.address = address;
		this.port = port;
		this.fact = fact;
	}
	
	public void askFact() {
		InetAddress address;
		Socket socket;

		try {
			address = InetAddress.getByName(this.address);
			socket = new Socket(address, this.port);
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
			client.askFact();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

