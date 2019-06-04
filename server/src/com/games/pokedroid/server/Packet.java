package com.games.pokedroid.server;

import com.games.pokedroid.server.game.Move;
import com.games.pokedroid.server.game.Pokemon;

public class Packet
{
	public static final int INVALID=-1;
	public static final int PKM=0;	//pokemon
	public static final int TEA=1;	//team
	public static final int MOV=2;	//move
	public static final int PNG=3;	//ping
	public static final int PON=4;	//pong
	public static final int HI=5;	//greeting
	public static final int BYE=6;	//closing
	public static final int MSG=7;	//message
	
	public static final int PPK=10; //prompt pokemon
	public static final int PTE=11;	//prompt team
	public static final int PMV=12;	//prompt move
	public static final int PBT=13;	//prompt battle
	public static final int PTR=14;	//prompt new turn
	public static final int PMS=15;	//prompt midturn switch
	
	public String content;
	public int type=-1;
	public int teamLength=-1;
	
	public Packet(String in)
	{
		content=in;
		type=packetType();
	}
	
	/**
	 * Creates simple packet, only works for packets with no arguments such as prompts
	 * @param type
	 */
	public Packet(int type, String args)
	{
		switch(type)
		{
		case MSG:
		{
			type=MSG;
			content="[msg]|"+args+"|[msg]";
		}
		
		case PNG:
		{
			type=PNG;
			content="[png]|"+System.currentTimeMillis()+"|[png]";
		}
		break;
		
		case PON:
		{
			type=PON;
			if(args.equals(""))
				break;
			content="[pon]|"+args+"|[pon]";
		}
		break;
		
		case HI:
		{
			type=HI;
			content="[hi!]";
		}
		break;
		
		case BYE:
		{
			type=BYE;
			content="[bye]";
		}
		break;
		
		case PPK:
		{
			type=PPK;
			content="[ppk]";
		}
		break;
			
		case PTE:
		{
			type=PTE;
			content="[pte]";
		}
		break;
		
		case PMV:
		{
			type=PMV;
			content="[pmv]";
		}
		break;
		
		case PBT:
		{
			type=PBT;
			content="[pbt]";
		}
		
		case PTR:
		{
			type=PTR;
			content="[ptr]";
		}
		
		case PMS:
		{
			type=PMS;
			content="[pms]";
		}
		
		//TODO: deal with bad types
		}
		//TODO: deal with them here too
	}

	public Packet(int ppk2)
	{
		this(ppk2, null);
	}

	public int packetType()	//TODO: remember to add all new packet types to this too
	{
		String header=content.substring(0,5);
		String footer=content.substring(content.length()-5); //TODO: should work
		
		if(header.charAt(0) != '[' || header.charAt(4) != ']' || !header.equals(footer))
			return INVALID;
		
		if(header.equals("[pkm]"))
			return PKM;
		if(header.equals("[tea]"))
			return TEA;
		if(header.equals("[mov]"))
			return MOV;
		if(header.equals("[png]"))
			return PNG;
		if(header.equals("[pon]"))
			return PON;
		if(header.equals("[hi!]"))
			return HI;
		if(header.equals("[bye]"))
			return BYE;
		if(header.equals("[ppk]"))
			return PPK;
		if(header.equals("[pte]"))
			return PTE;
		if(header.equals("[pmv]"))
			return PMV;
		if(header.equals("[msg]"))
			return MSG;
		if(header.equals("[ppk]"))
			return PPK;
		if(header.equals("[pte]"))
			return PTE;
		if(header.equals("[pmv]"))
			return PMV;
		if(header.equals("[pbt]"))
			return PBT;
		if(header.equals("[ptr]"))
			return PTR;
		if(header.equals("[pms]"))
			return PMS;
		
		return INVALID;
	}
	
	public Object getObject()
	{
		switch(type)
		{
		case PKM:
			return new Pokemon(content);
			
		case TEA:
		{
			Pokemon[] ret=new Pokemon[teamLength];
			return null; //TODO: teams
		}
		
		case MOV:
			return new Move(content);
			
		case MSG:
			return null; //TODO: extracting message content
			
		default:
			return null; //TODO: better handling of this stuff
		}
	}

	public String toString()
	{
		return content;
	}
}