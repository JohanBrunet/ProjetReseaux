package fact;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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
				this.sOutput = socket.getOutputStream();
				this.sInput  = socket.getInputStream();
				this.name = name;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("resource")
		public void run() {
			Scanner sc = new Scanner(this.sInput);
			while (true)
				if (sc.hasNext()) {
					String msg = sc.nextLine();
					System.out.println(this.name + ": factorielle " + msg);
					broadcast(this.name + ": " + msg);
				}
		}
	}

	// Server part

	ArrayList<ClientThread> socks = new ArrayList<ClientThread>();
	private int port;
	private int num = 0;

	public FactServer(int port) {
		this.port = port;
	}

	public synchronized void broadcast(String msg) {
		for (ClientThread threadClient : this.socks) {
			PrintStream output = new PrintStream(threadClient.sOutput);
			output.println(msg);
		}
	}

	@SuppressWarnings("resource")
	public void run() {
		try {
			ServerSocket sServer = new ServerSocket(port);
			while (true) {
				Socket socket = sServer.accept();
				ClientThread client = new ClientThread(socket, ">> Thread " + this.num);;
				System.out.println(">> Connection of client " + this.num);
				this.num++;
				this.socks.add(client);
				client.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		FactServer server = new FactServer(Integer.parseInt(argv[0]));
		server.run();
	}
}