package fib;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

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
	
	public void askFact() {
		InetAddress address;
		Socket socket;

		try {
			address = InetAddress.getByName(this.address);
			socket = new Socket(address, this.port);
			PrintStream output = new PrintStream(socket.getOutputStream());
			while (true) {
				output.println(this.fib);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		try {
			FibClient client = new FibClient(argv[0], Integer.parseInt(argv[1]), Integer.parseInt(argv[2]));
			client.askFact();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
