package fact;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

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

	public void run() {
		InetAddress address;
		Socket socket;
		Listen listen;
		Scanner scanner;
		String msg;

		try {
			address = InetAddress.getLocalHost();
			socket = new Socket(address, 50000);
			listen = new Listen(socket);
			listen.start();
			PrintStream output = new PrintStream(socket.getOutputStream());
			scanner = new Scanner(System.in);
			while (true) {
				msg = scanner.nextLine();
				output.println(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		try {
			FactClient c = new FactClient();
			c.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

