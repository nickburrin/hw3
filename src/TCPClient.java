import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

public class TCPClient {
	Scanner din;
    PrintStream pout;
    Socket server;
    String ipa;
	int len = 1024;
	int clientID;
	List<String> commands = new ArrayList<String>();
    
    public void getSocket(int port) throws IOException {
        server = new Socket(ipa, port);
        din = new Scanner(server.getInputStream());
        pout = new PrintStream(server.getOutputStream());
    }

    public void sendCommand(String line) throws IOException {
    	StringTokenizer st = new StringTokenizer(line);
    	
    	if(st.countTokens() < 2){
    		System.out.println("Did not enter a valid number of input tokens");
    	} else{
	    	String first = st.nextToken();
			String book = st.nextToken();
	    	int localPort = 0;
	    	
	    	if(first.equalsIgnoreCase("sleep") && (st.countTokens() == 0))
	    	{
	    		// 2 total tokens for these lines but countTokens() accounts for remaining tokens, and we have already called nextToken twice
	    		try {
					Thread.sleep(Integer.parseInt(book));//variable book is the integer for the amount of sleep in input
	    		} catch (NumberFormatException | InterruptedException e) {
	    			e.printStackTrace();
				}
	    	} else if(st.countTokens() == 3){
	    		//5 total tokens for these lines but countTokens() accounts for remaining tokens, and we have already called nextToken twice
	    		try{
	    			String command = st.nextToken();
	    			if(!command.equalsIgnoreCase("reserve") && !command.equalsIgnoreCase("return")){//the command is not reserve or return
	    				System.out.println("fail " + first + " " + book);
	    				return;
	    			}
	    			else{
		    			localPort = Integer.parseInt(st.nextToken());
		    			String protocol = st.nextToken();	//Will determine TCP or UDP when we have both in single client
		    			
		    			if(protocol.equalsIgnoreCase("U") || protocol.equalsIgnoreCase("T")){//checking for appropriate protocol
			    			getSocket(localPort);
			    			String parsed = first + " " + book + " " + command;
			    			pout.println(parsed);
			    			pout.flush();
			    			String retValue = din.nextLine();
			    			System.out.println(retValue);
			    			server.close();
		    			}else{
		    				System.out.println("fail " + first + " " + book);
		    			}
	    			}
	    		} catch(NumberFormatException e){System.out.println("Did not enter a valid port");}
	    	} else{
	    		System.out.println("fail " + first + " " + book);
	    		return;
	    	}
    	}
    }
    
    public static void main(String[] args) throws IOException {
    	if (args.length != 1) {//make sure there is only one argument included
			System.out.println("Wrong nubmer of arguments");
			return;
		}
    	TCPClient myClient = new TCPClient();// create a TCP client
		myClient.readInputFile(args[0]);// read from input file
        
        try {
        	for (String msg : myClient.commands) {
				try {
					// send command
					myClient.sendCommand(msg);
				}  catch (IOException e) {
			        System.err.println(e);
			    }
			}
        } catch (Exception e) {
            System.err.println("Client aborted: " + e);
        }
    }
    
    private void readInputFile(String file) throws IOException {
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