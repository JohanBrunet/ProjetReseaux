package fact;

public class FactClient {
	
	private String serverAddress;
	private int serverPort;
	
	public FactClient(String address, int port) {
		this.serverAddress = address;
		this.serverPort = port;
	}
	
	public static void main(String[] args) {
		FactClient client = new FactClient(args[0], Integer.parseInt(args[1]));
	}
}
