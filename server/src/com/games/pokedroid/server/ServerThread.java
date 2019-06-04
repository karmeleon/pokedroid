package com.games.pokedroid.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.games.pokedroid.server.game.Move;
import com.games.pokedroid.server.game.Pokemon;

public class ServerThread extends Thread
{
    private Socket socket = null;
    public int userNumber;
    private boolean running;
    
    PrintWriter out;
    BufferedReader in;

    public ServerThread(Socket socket, int i)
    {
    	super("ServerThread"+i);
    	this.socket = socket;
    	userNumber=i;
    	running=true;
    }
	
    /**
     * Stops the thread from running, effectively dropping the client.
     */
    public void terminate()
    {
    	running=false;
    }
    
    public void run()
    {
		try
		{	
			Main.connectedUsers++;
		    out = new PrintWriter(socket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    String ip=socket.getInetAddress().getHostAddress();
		    
		    System.out.println("client connected from IP "+ip);
		    
		    String inputLine;
		    String fromServer;
		    
		    if(userNumber>2)
		    {
		    	System.err.println("Too many users connected. Booting the latest connectee.");
		    	out.println("You're too slow! You're connectee number "+userNumber+".");
		    	out.println("[bye]");
		    }
		    
		    out.println("[hi!]");
		    
		    System.out.println(receivePokemon());
		    
		    /*
		    while(running)
		    {
		    	sleep(1000);
		    	out.println("[png]");
		    }
		    */
		    
		    
	        while ((inputLine = in.readLine()) != null)
	        {
	        	dealWithPacket(inputLine);
	        }
		    
		    out.close();
		    in.close();
		    socket.close();
		    Main.connectedUsers--;
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
    }
    
    private void dealWithPacket(String inputLine)
    {
    	//int packetType=PacketUtils.packetType(inputLine);
    	//TODO
    }
    
    /**
     * Tells the client to start a new battle.
     */
    public void startBattle()
    {
    	sendString(new Packet(Packet.PBT).toString());
    }
    
    /**
     * Tells the client that a new turn has begun. The next thing to call is getMove() to recieve the next move from the client.
     */
    public void newTurn()
    {
    	sendString(new Packet(Packet.PTR).toString());
    }

    /**
     * Prompts the client for its team.
     */
    public void promptForTeam()
    {
    	sendString(new Packet(Packet.PTE).toString());
    }
    
    /**
     * Asks the client to send a Move.
     * @return Move the user sent.
     */
    public Move promptForMove()
    {
    	sendString(new Packet(Packet.PMV).toString());
    	
    	return recieveMove();	//TODO: probably gonna redo how this works
    }
    
    /**
     * Tells the client that their Pokemon has fainted mid-battle.
     * @return Index in array of the desired switch target.
     * @throws Exception 
     */
    public int midTurnSwitch()
    {
    	sendString(new Packet(Packet.PMS).toString());
    	
    	return recieveInt();
    }
    
    /**
     * Recieves move from the user. This should
     * @return
     */
    public Move recieveMove()
    {
    	Packet durr = null;
		try
		{
			durr = new Packet(in.readLine());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
    	if(durr.type!=Packet.INVALID)
    		return (Move) durr.getObject();
    	return null;
    }
    
    /**
     * Sends a Move to the client.
     * @param sending Move to send.
     */
    public void sendMove(Move sending)
    {
    	out.println(new Packet(sending.writeToString()).toString());
    }
    
    public Pokemon receivePokemon()
    {
    	promptForPokemon();	//TODO
		
		try
		{
			Packet pkt=new Packet(in.readLine());
			return (Pokemon) pkt.getObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
    }
    
    public void promptForPokemon()
    {
		sendString(new Packet(Packet.PPK).toString());
	}

	/**
     * Recieves a Pokemon[] sent by the client. This is a blocking call.
     * @return the Pokemon[] the client sent.
     * @throws Exception
     */
    public Pokemon[] recieveTeam()	//TODO: make it work
    {
    	Pokemon[] team = new Pokemon[recieveInt()];
    	
    	promptForTeam();
    	
    	String currentLine;
    	
    	int count=0;
    	
    	try
    	{
			while((currentLine=in.readLine()) != null && count<team.length)
				team[count]=new Pokemon(currentLine);
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    		
    	return team;
    }
    
    /**
     * Sends a Pokemon[] to the client.
     * @param team Pokemon[] to send.
     */
    public void sendTeam(Pokemon[] team)	//TODO: make teams work
    {
    	sendInt(team.length);
    	
    	for(int i=0; i<team.length; i++)
    		out.println(team[i].writeToString());
    }
    
    /**
     * Sends an int to the client.
     * @param sending int to be sent.
     */
    public void sendInt(int sending)
    {
    	out.println(sending);
    }
    
    /**
     * Recieves the int the client sends. Note: this is a blocking call.
     * @return the int the user sent
     * @throws Exception
     */
    public int recieveInt()
    {
    	String durr = null;
		try
		{
			durr = in.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    	if(durr!=null)
    		return Integer.parseInt(durr);
    	return -1;
    }
    
    /**
     * Sends a String to the client. Used for prompts.
     * @param in String to be sent
     */
    public void sendString(String in)
    {
    	out.println(in);
    }
    
    /**
     * Sends a message, to be shown in the client's text box.
     * @param msg message to send.
     */
    public void broadcastMessage(String msg)
    {
    	sendString(new Packet(Packet.MSG, msg).toString());
    }
}