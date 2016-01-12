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

		Socket socket;
		InputStream sInput;
		OutputStream sOutput;
		String name;

		public ClientThread(Socket socket, String name) {
			try {
				this.socket = socket;
				sOutput = socket.getOutputStream();
				sInput  = socket.getInputStream();
				this.name = name;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("resource")
		public void run() {
			Scanner sc = new Scanner(sInput);
			while (true) {
				if (sc.hasNext()) {
					String msg = sc.nextLine();
					System.out.println(name + ": " + msg);
					broadcast(name + ": " + msg);
				}				
			}
		}
	}

	// Server part

	ArrayList<ClientThread> socks = new ArrayList<ClientThread>();
	int port;
	int num = 0;

	public FactServer(int port) {
		this.port = port;
	}

	public synchronized void broadcast(String msg) {
		for (ClientThread e : socks) {
			PrintStream output = new PrintStream(e.sOutput);
			output.println(msg);
		}
	}

	@SuppressWarnings("resource")
	public void run() {
		try {
			ServerSocket sServer = new ServerSocket(port);
			while (true) {
				Socket s = sServer.accept();
				ClientThread c = new ClientThread(s, "Thread " + num);;
				System.out.println("Connection of client " + num);
				num++;
				socks.add(c);
				c.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String [] argv) {
		FactServer c = new FactServer (50000);
		c.run();
	}
}


