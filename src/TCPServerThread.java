import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServerThread extends Thread {
	Library lib;
	Socket theClient;
	String client, book, commandType;

	public TCPServerThread(Library lib, Socket s) {
		this.lib = lib;
		this.theClient = s;
	}

	private String doCommand() throws InterruptedException {
		int returnStatus;

		if (commandType.equals("reserve")) {
			returnStatus = lib.reserveBook(client,book);
		} else{
			returnStatus = lib.returnBook(client,book);
		}
		
		if (returnStatus == 1){
			return client + " " + book;
		} else if(returnStatus == 2){
			return "free " + client + " " + book;
		} else{
			return "fail " + client + " " + book;
		}
	}

	public void run() {
		Scanner sc = null;
		Scanner st = null;
		PrintWriter pout = null;

		try {
			sc = new Scanner(theClient.getInputStream());
			pout = new PrintWriter(theClient.getOutputStream());
			String command = sc.nextLine();
			st = new Scanner(command);
			
			client = st.next();
			book = st.next();
			commandType = st.next();

			String ret = doCommand();
			pout.println(ret);
			pout.flush();
			theClient.close();

		} catch (Exception e) {

		} finally {
			sc.close();
		}
	}
}