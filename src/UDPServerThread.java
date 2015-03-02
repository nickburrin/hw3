import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;


public class UDPServerThread extends Thread {
	Library lib;
	DatagramSocket socket;
	DatagramPacket receivepacket, returnpacket;
	byte[] buf = new byte[1024];
	String commandType, client, book;

	UDPServerThread(Library lib, DatagramPacket pac, DatagramSocket soc) {
		this.lib = lib;
		this.receivepacket = pac;
		this.socket = soc;
	}
	
	public String doCommand() {
		int returnStatus;
		
		if (commandType.equals("reserve")) {
			returnStatus = lib.reserveBook(client, book);
		} else {
			returnStatus = lib.returnBook(client, book);
		}
		
		if (returnStatus == 1){
			return client + " " + book;
		} else if(returnStatus == 2){
			return "free " + client + " " + book;
		} else{
			return "fail " + client + " " + book;
		}
	}

	@Override
	public void run() {
		try {
			String command = new String(receivepacket.getData());
			parseCommand(command);

			if (commandType != null){
				String response = doCommand();
				byte[] buffer = new byte[response.length()];
				buffer = response.getBytes();

				returnpacket = new DatagramPacket(buffer, buffer.length, receivepacket.getAddress(), receivepacket.getPort());
				socket.send(returnpacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void parseCommand(String command) {
		StringTokenizer st = new StringTokenizer(command);
		
		client = st.nextToken();
		book = st.nextToken();
		commandType = st.nextToken();
	}

}