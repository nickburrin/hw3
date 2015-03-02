import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

public class Server {
	static int numBooks = 0;
	static String ipAddr;
	static int portUDP;
	static int portTCP;
	static int len = 1024;
	Library lib;

	Server(int numBooks) {
		lib = new Library(numBooks);
	}

	public static void main(String[] args) throws Exception {
		ServerSocket TCPServer = null;
		DatagramSocket UDPSocket = null;
		Socket s;
		try {
			Scanner scan = new Scanner(System.in);
			readInput(scan.nextLine());

			Server server = new Server(numBooks);
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
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {}
	}

	static void readInput(String nextLine) throws IOException {
		StringTokenizer st = new StringTokenizer(nextLine);
		numBooks = Integer.parseInt(st.nextToken());
		portUDP = Integer.parseInt(st.nextToken());
		portTCP = Integer.parseInt(st.nextToken());
		ipAddr = "127.0.0.1";
	}
}