package com.games.pokedroid.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.games.pokedroid.SQLSource;



public class Move implements Serializable, Comparable
{
	private static final long serialVersionUID = 1044817248686483164L;
	public int id;
	public String name;
	public String description;
	
	public int CurrentPP;
	public int MaxPP;
	
	public int type;
	
	public int BasePower;
	private int power;
	public int BaseAccuracy;
	private int Accuracy;
	
	public int effect;
	public int BaseEffectAccuracy;
	private int EffectAccuracy;
	
	public int BasePriority;
	private int Priority;
	public int DamageType;
	public ArrayList<Integer> moveFlags;
	
	public int moveTarget;
	
	public boolean justFailed=false;
	
	public boolean isSwitch=false;
	public int switchTarget=-1;
	
	//public final boolean Snatchable;
	//public final boolean Protectable;
	//public final boolean MagicCoatAble;
	//public final boolean KingsRock;
	//public final boolean BrightPowder;
	//public final boolean MakesContact;
	
	public Move(String in)
	{
		StringTokenizer s=new StringTokenizer(in);
		String header=s.nextToken("|");
		System.out.println(header);
		if(!header.equals("[mov]"))
			System.out.println("oops.");
		else
		{
			boolean switching=false;
			int switchTarget=-1;
			int id=1;
			
			if(s.nextToken("|").equals("true"))
				switching=true;
			switchTarget=Integer.parseInt(s.nextToken("|"));
			id=Integer.parseInt(s.nextToken("|"));
			this.id=id;
			name=SQLSource.getString(id, getName, "moves");
			description=SQLSource.getString(id,getDescription,"moves");
			
			MaxPP=SQLSource.getInt(id, getPP, "moves");
			CurrentPP=MaxPP;
			type=SQLSource.getInt(id,getType,"moves");
			BasePower=SQLSource.getInt(id,getPower,"moves");
			BaseAccuracy=SQLSource.getInt(id,getAccuracy,"moves");
			BasePriority=SQLSource.getInt(id,getPriority,"moves");
			DamageType=SQLSource.getInt(id,getDamageType,"moves");
			effect=SQLSource.getInt(id,getEffect,"moves");
			BaseEffectAccuracy=SQLSource.getInt(id,getEffectAccuracy,"moves");
			switch(SQLSource.getInt(id,9,"moves")){
			case 1:
			case 8:
			case 9:
			case 10:
			case 11:{
				moveTarget=1;
				break;
			}
			case 2:
			case 4:
			case 5:
			case 7:{
				moveTarget=2;
				break;
			}
			case 6:{
				moveTarget=3;
				break;
			}
			case 12:{
				moveTarget=4;
				break;
			}
			case 3:{
				moveTarget=5;
				break;
			}
				default:
					moveTarget=0;
					break;
			}
			//moveTarget key:
			//1- opponent
			//2-user
			//3- opponent's side of the field
			//4- whole field
			//5- Helping Hand
			Priority=BasePriority;
			power=BasePower;
			Accuracy=BaseAccuracy;
			EffectAccuracy=BaseEffectAccuracy;
			moveFlags = SQLSource.getIntList(id, 1, "move_flag_map");
			
			if(switching)
				makeSwitch(switchTarget);
		}
	}
	
	public Move(int id){
		this.id=id;
		name=SQLSource.getString(id, getName, "moves");
		description=SQLSource.getString(id,getDescription,"moves");
		
		MaxPP=SQLSource.getInt(id, getPP, "moves");
		CurrentPP=MaxPP;
		type=SQLSource.getInt(id,getType,"moves");
		BasePower=SQLSource.getInt(id,getPower,"moves");
		BaseAccuracy=SQLSource.getInt(id,getAccuracy,"moves");
		BasePriority=SQLSource.getInt(id,getPriority,"moves");
		DamageType=SQLSource.getInt(id,getDamageType,"moves");
		effect=SQLSource.getInt(id,getEffect,"moves");
		BaseEffectAccuracy=SQLSource.getInt(id,getEffectAccuracy,"moves");
		switch(SQLSource.getInt(id,9,"moves")){
		case 1:
		case 8:
		case 9:
		case 10:
		case 11:{
			moveTarget=1;
			break;
		}
		case 2:
		case 4:
		case 5:
		case 7:{
			moveTarget=2;
			break;
		}
		case 6:{
			moveTarget=3;
			break;
		}
		case 12:{
			moveTarget=4;
			break;
		}
		case 3:{
			moveTarget=5;
			break;
		}
			default:
				moveTarget=0;
				break;
		}
		//moveTarget key:
		//1- opponent
		//2-user
		//3- opponent's side of the field
		//4- whole field
		//5- Helping Hand
		Priority=BasePriority;
		power=BasePower;
		Accuracy=BaseAccuracy;
		EffectAccuracy=BaseEffectAccuracy;
		moveFlags = SQLSource.getIntList(id, 1, "move_flag_map");
		
		
		
	}
	
	public void makeSwitch(int target)
	{
		isSwitch=true;
		switchTarget=target;
	}
	
	public int getPower(){
		return power;
	}
	
	public int getAccuracy(){
		return Accuracy;
	}
	
	public int getPriority(){
		return Priority;
	}
	
	public int getEffectAccuracy(){
		return EffectAccuracy;
	}
	
	public String writeToString()
	{
		StringBuilder b=new StringBuilder();
		
		b.append("[mov]|");
		b.append(isSwitch);
		b.append("|");
		b.append(switchTarget);
		b.append("|");
		b.append(id);
		b.append("|[mov]");
		
		return b.toString();
	}
	
	public String toString()
	{
		/*
		 * 					info=team1[activeAlly].attack1.name+"\nPP: "+team1[activeAlly].attack1.CurrentPP+
					"/"+team1[activeAlly].attack1.MaxPP+"\nPower: "+
					team1[activeAlly].attack1.BasePower+"\nAccuracy: "+team1[activeAlly].attack1.BaseAccuracy+"\nType: "+
					typeToString(team1[activeAlly].attack1.type)+"\n"+
					team1[activeAlly].attack1.description;
		 */
		return name+"\nPP: "+CurrentPP+"/"+MaxPP+"\nPower: "+BasePower+"\nAccuracy: "+BaseAccuracy+"\nType: "+type+"\n"+description;
	}

	public String getString(int index, int column, String table)
	{
		return SQLSource.getString(index, column, table);
	}
	
	public int getInt(int index, int column, String table)
	{
		return SQLSource.getInt(index, column, table);
	}
	
	public ArrayList<Integer> getIntList(int id, int row, String table)
	{
		return SQLSource.getIntList(id, row, table);
	}
	
	private final int getName=1;
	private final int getDescription=2;
	private final int getEffect=3;
	private final int getPower=4;
	private final int getType=5;
	private final int getAccuracy=6;
	private final int getPP=7;
	private final int getEffectAccuracy=8;
	private final int getPriority=10;
	private final int getDamageType=12;

	public final int PHYSICAL=2;
	public final int SPECIAL=3;
	public final int NONDAMAGING=1;


public int compareTo(Object obj) {
	Move temp = (Move)obj;
	return name.compareTo(temp.name);
}
}