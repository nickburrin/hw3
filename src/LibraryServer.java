import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

public class LibraryServer {
	static int numBooks = 0;
	static String ipAddr;
	static int portUDP;
	static int portTCP;
	static int len = 1024;
	Library lib;

	LibraryServer(int numBooks) {
		lib = new Library(numBooks);
	}

	public static void main(String[] args) throws Exception {
		ServerSocket TCPServer = null;
		DatagramSocket UDPSocket = null;
		Socket s;
		try {
			/*Initialize the server from command line*/
			//			Scanner in = new Scanner(System.in);
			//			String tokenizer = 

			readInputFile(args[0]);

			LibraryServer server = new LibraryServer(numBooks);
			TCPServer = new ServerSocket(portTCP);
			TCPServer.setSoTimeout(50);
			UDPSocket = new DatagramSocket(portUDP);
			UDPSocket.setSoTimeout(50);
			try {
				while(true){
					try{
						if((s = TCPServer.accept()) != null) {
							Thread tcp = new TCPServerThread(server.lib, s);
							tcp.start();
						}
					} catch(SocketTimeoutException e){}
					
					byte[] buf = new byte[len];
					DatagramPacket datapacket = new DatagramPacket(buf, buf.length);
					try{
						UDPSocket.receive(datapacket);
						Thread udp = new UDPServerThread(server.lib, datapacket, UDPSocket);
						udp.start();
					} catch(SocketTimeoutException e){}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				TCPServer.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {}
	}

	private static void readInputFile(String file) throws IOException {
		BufferedReader dIn = null;

		try {
			dIn = new BufferedReader(new FileReader(file));
			StringTokenizer st = new StringTokenizer(dIn.readLine());
			numBooks = Integer.parseInt(st.nextToken());
			portUDP = Integer.parseInt(st.nextToken());
			portTCP = Integer.parseInt(st.nextToken());
			ipAddr = "127.0.0.1";
		} catch (FileNotFoundException e) {} catch (IOException e) {System.err.println(e);} 
		finally {
			dIn.close();
		}
	}
}