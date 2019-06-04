package com.games.pokedroid.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Main
{
	private final static int PORT=1070;
	public static int connectedUsers=0;
	public static boolean battleStarted=false;
	
	public static ServerThread user1, user2;
	public static MasterServerThread mts;

	public static void main(String[] args) throws IOException
	{
		System.out.println("Server has started... Trying to listen on port "+PORT);
        ServerSocket serverSocket = null;
        boolean listening = true;
        
        InetAddress i=InetAddress.getLocalHost();
        System.out.println("Your local IP address is "+i.getHostAddress()+" unless " +
        		"you have multiple network adapters (virtual, Bluetooth, etc.), in " +
        		"which case you'll need to check ipconfig /all on Windows or ifconfig " +
        		"on Linux/OSX.");
        
        try
        {
            serverSocket = new ServerSocket(PORT);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port "+PORT+".");
            System.exit(-1);
        }

        System.out.println("Server successfuly listening on port "+PORT+".");
        
        while (listening)
        {
        	if(connectedUsers==0)
        	{
        		user1=new ServerThread(serverSocket.accept(), 1);
        		user1.start();
        	}
        	if(connectedUsers==1)
        	{
        		user2=new ServerThread(serverSocket.accept(), 2);
        		user2.start();
        		mts=new MasterServerThread(user1, user2);
        		mts.start();
        	}
        	else
        		new ServerThread(serverSocket.accept(), 3);	//TODO: extra users
        }

        try
        {
			serverSocket.close();
		}
        catch (IOException e)
        {
			e.printStackTrace();
		}
	}
}