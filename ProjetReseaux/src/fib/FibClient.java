package fib;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import fib.FibClient;

public class FibClient {
	private String address;
	private int port;
	private int fib;
	
	public FibClient(String address, int port, int fib) {
		this.address = address;
		this.port = port;
		this.fib = fib;
	}
	
	public int askFib() {
		InetAddress address;
		Socket socket;
		try {
			address = InetAddress.getByName(this.address);
			socket = new Socket(address, this.port);
			PrintStream output = new PrintStream(socket.getOutputStream());
			Scanner input = new Scanner(socket.getInputStream());
			output.println(this.fib);
			this.fib = input.nextInt();
			socket.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.fib;
	}
	
	public static void main(String argv[]) {
		try {
			int input;
			System.out.println("Entrez le nombre dont vous voulez le résultat de fibonacci :\n");
			Scanner sc = new Scanner(System.in);
			input = sc.nextInt();
			while(input < 0 || input%1 != 0) {
				System.out.println("Veuillez entrer une valeur positive ou nulle ! \n");
				input = Integer.parseInt(sc.nextLine());
			}
			FibClient client = new FibClient(argv[0], Integer.parseInt(argv[1]), input);
			System.out.println("Résultat : " + client.askFib());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
