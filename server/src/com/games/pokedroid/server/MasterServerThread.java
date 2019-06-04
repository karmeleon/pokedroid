package com.games.pokedroid.server;

import com.games.pokedroid.server.game.Attack;
import com.games.pokedroid.server.game.Battlefield;
import com.games.pokedroid.server.game.Move;
import com.games.pokedroid.server.game.Pokemon;

public class MasterServerThread extends Thread
{
	private static ServerThread user1, user2;
	
	public int waitingStatus=0;
	
	public static boolean hasSomeoneWon=false;
	public static boolean user1Locked=false, user2Locked=false, user1SwitchLock=false, user2SwitchLock=false;
	
	private Move move1=null, move2=null;
	public static Pokemon[] team1, team2;
	public static int activeAlly=0, activeEnemy=0;
	private Attack a=new Attack();
	private Battlefield b;

	public static boolean user1Won;
	
	public static final int WAITINGFORBOTH=0;
	public static final int WAITINGFOR1=1;
	public static final int WAITINGFOR2=2;
	public static final int INPROGRESS=3;
	public static final int COMPLETE=4;
	
	public MasterServerThread(ServerThread s1, ServerThread s2)
	{
		super("MasterServerThread");
		user1=s1;
		user2=s2;
	}
	
	public void run()
	{
		team1=user1.recieveTeam();
		team2=user2.recieveTeam();
		
		b=new Battlefield(team1, team2);
		
		user1.startBattle();
		user2.startBattle();
		while(!hasSomeoneWon)
		{
			new Thread()	//so both users are notified at the same time
			{
				public void run()
				{
					move1=user1.promptForMove();
				}
			}.start();
			
			new Thread()
			{
				public void run()
				{
					move2=user2.promptForMove();
				}
			}.start();
			
			while(move1==null || move2==null)
				try
				{
					sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			
			a.attackPhase(move1, move2, team1[activeAlly], team1, team2[activeEnemy], team2, b);
			
			
		}
	}
	
	public static void broadcastMessage(String message)
	{
		user1.broadcastMessage(message);
		user2.broadcastMessage(message);
	}
	
	public static void noChooseForYou()
	{
		user1Locked=true;	//TODO
	}

	public static void noSwitchForYou()
	{
		user1SwitchLock=true;	//TODO
	}

	public static int onUser1Faint()
	{
		return user1.midTurnSwitch();
	}
	
	public static int onUser2Faint()
	{
		return user2.midTurnSwitch();
	}

	public static void switchPokemon(int side, int switchTo)
	{
		// TODO Auto-generated method stub
		
	}
}