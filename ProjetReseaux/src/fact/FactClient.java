package fact;
import java.io.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

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

	@SuppressWarnings("resource")
	public int askFact() {
		InetAddress address;
		Socket socket;
		try {
			address = InetAddress.getByName(this.address);
			socket = new Socket(address, this.port);
			PrintStream output = new PrintStream(socket.getOutputStream());
			Scanner input = new Scanner(socket.getInputStream());
			output.println(this.fact);
			this.fact = input.nextInt();
			socket.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.fact;
	}

	public static void main(String[] args) {
		try {
			int input;
			System.out.println("Entrez le nombre dont vous voulez la factorielle :\n");
			Scanner sc = new Scanner(System.in);
			input = sc.nextInt();
			while(input < 0) {
				System.out.println("Veuillez entrer une valeur positive ou nulle ! \n");
				input = Integer.parseInt(sc.nextLine());
			}
			FactClient client = new FactClient(args[0], Integer.parseInt(args[1]), input);
			System.out.println("Resultat final :" + client.askFact());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}