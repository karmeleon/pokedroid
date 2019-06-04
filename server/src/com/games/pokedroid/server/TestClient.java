package com.games.pokedroid.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.games.pokedroid.server.game.Pokemon;

public class TestClient
{
	public static final int PORT=1070;
	
	public static void main(String[] args) throws IOException
	{
        Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        //System.out.print("Enter the server IP: ");
        
        //String i=Keyboard.readString();
        
        String i="127.0.0.1";
        
        System.out.println(new Pokemon(40).writeToString());
        
        try
        {
            kkSocket = new Socket(""+i, PORT);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        }
        catch (UnknownHostException e)
        {
            System.err.println("Unknown host: "+i);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't connect to server at "+i);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        while ((fromServer = in.readLine()) != null)
        {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("Bye."))
                break;
		  
            fromUser = stdIn.readLine();
		    if (fromUser != null)
		    {
		    	System.out.println("Client: " + fromUser);
		    	out.println(fromUser);
		    }
        }

        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
	}
}