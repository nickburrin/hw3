import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client {
	static PrintStream pout;
    static Socket server;
	static DatagramPacket sPacket, rPacket;
	static Scanner din;
	static String ipa;
	static int port;
	static int len = 1024;
	static ArrayList<String> commands = new ArrayList<String>();
	static int clientID;
	static int commandType;
	static final int UDP = 1;
	static final int TCP = 0;
	
	public static void getSocket(int port) throws IOException {
        server = new Socket(ipa, port);
        din = new Scanner(server.getInputStream());
        pout = new PrintStream(server.getOutputStream());
    }
	
	private static boolean legalMessage(String msg) {
		StringTokenizer st = new StringTokenizer(msg);
		
		if(st.countTokens() < 2){
			System.out.println("Did not enter a valid number of input tokens");
			return false;
		} else{
			String first = st.nextToken();
			String book = st.nextToken();

			if(first.equalsIgnoreCase("sleep") && (st.countTokens() == 0))
			{
				// 2 total tokens for these lines but countTokens() accounts for remaining tokens, and we have already called nextToken twice
				try {
					Thread.sleep(Integer.parseInt(book));//variable book is the integer for the amount of sleep in input
				} catch (NumberFormatException | InterruptedException e) {
					e.printStackTrace();
				} 
				
				return false;
			} else if(st.countTokens() == 3){
				//5 total tokens for these lines but countTokens() accounts for remaining tokens, and we have already called nextToken twice
				try{
					String command = st.nextToken();
					if(!command.equalsIgnoreCase("reserve") && !command.equalsIgnoreCase("return")){//the command is not reserve or return
						System.out.println("fail " + first + " " + book);
						return false;
					}
					else{
						port = Integer.parseInt(st.nextToken());
						String protocol = st.nextToken();	//Will determine TCP or UDP when we have both in single client

						if(protocol.equalsIgnoreCase("U") || protocol.equalsIgnoreCase("T")){//checking for appropriate protocol
							if(protocol.equalsIgnoreCase("U")){ commandType = UDP; }
							else{ commandType = TCP; }
							
							String parsed = first + " " + book + " " + command;
							msg = parsed;
							return true;
						}else{
							System.out.println("fail " + first + " " + book);
							return false;
						}
					}
				} catch(NumberFormatException e){System.out.println("Did not enter a valid port");}
			} else{
				System.out.println("fail " + first + " " + book);
				return false;
			}
		}
		
		return false;
	}
	
	public static void main(String args[]) throws Exception {
		readInputFile(args[0]);

		DatagramSocket datasocket = null;
		
		try {
			datasocket = new DatagramSocket();
		//	datasocket.setSoTimeout(500);

			for (String msg : commands) {
				
				try {
					// send command
					if(legalMessage(msg)){
						if(commandType == UDP){
							byte[] buffer = new byte[msg.length()];
							buffer = msg.getBytes();
							sPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipa), port);
							datasocket.send(sPacket);//send as a data packet
	
							// receive command
							byte[] rbuffer = new byte[len];
							rPacket = new DatagramPacket(rbuffer, rbuffer.length);
							datasocket.receive(rPacket);
							System.out.println(new String(rPacket.getData()));
						}
						else{
							getSocket(port);
			    			pout.println(msg);
			    			pout.flush();
			    			String retValue = din.nextLine();
			    			System.out.println(retValue);
			    			server.close();
						}
					}
				}  catch (IOException e) {
					System.err.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			datasocket.close();
		}
	}
	
	private static void readInputFile(String file) throws IOException {
		BufferedReader dIn = null;
		try {
			dIn = new BufferedReader(new FileReader(file));
			StringTokenizer st = new StringTokenizer(dIn.readLine());
			clientID = Integer.parseInt(st.nextToken());
			ipa = st.nextToken();
			String line = dIn.readLine();
			while (line != null) {
				if(!line.contains("sleep")){
					line = "c" + clientID + " " + line;
				}
				commands.add(line);
				line = dIn.readLine();
			}
		} catch (FileNotFoundException e) {} catch (IOException e) {System.err.println(e);} 
		finally {
			dIn.close();
		}
	}
}
