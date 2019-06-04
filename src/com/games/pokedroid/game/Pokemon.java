package com.games.pokedroid.game;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.games.pokedroid.SQLSource;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Pokemon implements Serializable
{
	private static final long serialVersionUID = -8410965589110976427L;
	
	public String name;
	public int level;	//n
	public int type1;
	public int type2;
	public int ability1;
	public int ability2;
	public int DWAbility;
	public int maxHP;	//computable
	public int currentHP;	//n
	public int id;
	public boolean hasFainted=false;	//n
	public Move attack1;	//n
	public Move attack2;	//n
	public Move attack3;	//n
	public Move attack4;	//n
	public int pp1;	//n
	public int pp2;	//n
	public int pp3;	//n
	public int pp4;	//n
	public Item heldItem=Item.NO_ITEM;	//n
	public Nature nature=Nature.HARDY;	//n
	public int chosenAbility;	//n
	public int gender;	//n
	public int weight;
	public ArrayList<Integer> Movepool=new ArrayList<Integer>();
	
	public boolean legal;
	
	public int spDef;	//all computable
	public int spAtk;
	public int spd;
	public int effectiveSpeed;
	public int def;
	public int atk;
	
	public int baseSpDef;
	public int baseSpAtk;
	public int baseSpd;
	public int baseDef;
	public int baseAtk;
	public int baseHP;
	
	public int spDefIV=0;	//all n
	public int spAtkIV=0;
	public int spdIV=0;
	public int defIV=0;
	public int atkIV=0;
	public int HPIV=0;
	
	public int spDefEV=0;	//all n
	public int spAtkEV=0;
	public int spdEV=0;
	public int defEV=0;
	public int atkEV=0;
	public int HPEV=0;
	
	public boolean hasStatus=false;		//all n
	public boolean isParalyzed=false;
	public boolean isPoisoned=false;
	public boolean isBadlyPoisoned=false;
	public boolean isConfused=false;
	public boolean isBurned=false;
	public boolean isFrozen=false;
	public boolean isInfatuated=false;
	public boolean isAsleep=false;
	public boolean isLeechSeeded=false;
	public boolean isFlinched=false;
	public boolean isRaging=false;
	public boolean isWrapped=false;

	
	public boolean isDefenseCurled=false;
	public boolean isMinimized=false;
	public boolean isFocused=false;
	
	public boolean isChoiceLocked=false;
	
	
	public int isMimicing=0;
	public int MimicPP=12341234;
	
	public boolean isCharging=false;
	public boolean isRecharging=false;
	public boolean isEncored=false;
	public boolean isBideLocked=false;
	public boolean isThrashLocked=false;
	public boolean isUnderwater=false;
	public boolean isUnderGround=false;
	public boolean isFlying=false;
	public boolean isBouncing=false;	
	
	public int turnCount=0;
	public int confusedCount=0;
	public int unConfuseCount=0;
	public int sleepCount=0;
	public int wakeCount=0;
	public int toxicCount=0;
	public int thrashLockCount=0;
	public int thrashBreak=0;
	public int bideLock=0;
	public int bideDamage=0;
	public int encoreLockCount=0;
	public final int encoreBreak=4;	//y
	public int wrapCount=0;
	public int wrapBreak=0;
	
	public int spDefLvl=0; //all n
	public int spAtkLvl=0;
	public int spdLvl=0;
	public int defLvl=0;
	public int atkLvl=0;
	public int accLvl=0;
	public int evaLvl=0;
	
	public static boolean FlashFireActive=false;
	public static boolean WaterSported=false;
	public static boolean MudSported=false;
	
	
	public Pokemon(int id)
	{
		this.id=id;
		name=getString(id, NAME,"Pokemon");
		baseHP=getInt(id, HP,"Pokemon");
		currentHP=maxHP=baseHP;
		gender=MALE;

		level=100;
		
		HPIV=31;
		atkIV=31;
		defIV=31;
		spdIV=31;
		spDefIV=31;
		spAtkIV=31;
		
		baseSpDef=getInt(id, SP_DEF,"Pokemon");
		baseSpAtk=getInt(id, SP_ATK,"Pokemon");
		baseSpd=getInt(id, SPEED,"Pokemon");
		baseDef=getInt(id, DEFENSE,"Pokemon");
		baseAtk=getInt(id, ATTACK,"Pokemon");
		
		weight=getInt(id,15,"Pokemon");
		
		ability1=getInt(id, ABILITY1,"Pokemon");
		ability2=getInt(id, ABILITY2, "Pokemon");
		DWAbility=getInt(id, DWABILITY, "Pokemon");
		type1=getInt(id, TYPE1, "Pokemon");
		type2=getInt(id, TYPE2, "Pokemon");
		
		FillMovepool(this.id);
		attack1=new Move(10000);
		attack2=new Move(10000);
		attack3=new Move(10000);
		attack4=new Move(10000);
		
		recomputeStats();
		recomputeHP();
    }
	
	public Pokemon(String in)
	{
		Scanner s=new Scanner(in);
		s.useDelimiter("\\x7c");
		//the | character
		String temp=s.next();
		if(!temp.equals("[pkm]"))
		{
			System.err.println(temp+" is not a [pkm] tag");
			return;
		}
		id=s.nextInt();
		level=s.nextInt();
		currentHP=s.nextInt();
		hasFainted=s.nextBoolean();
		
		StringBuilder m=new StringBuilder();
		m.append(s.next());
		String str;
		
		do
		{
			str=s.next();
			m.append("|");
			m.append(str);
		} while(!str.equals("[mov]"));
		
		attack1=new Move(m.toString());
		
		m=new StringBuilder();
		m.append(s.next());
		str="";
		
		do
		{
			str=s.next();
			m.append("|");
			m.append(str);
		} while(!str.equals("[mov]"));
		
		attack2=new Move(m.toString());
		
		m=new StringBuilder();
		m.append(s.next());
		str="";
		
		do
		{
			str=s.next();
			m.append("|");
			m.append(str);
		} while(!str.equals("[mov]"));
		
		attack3=new Move(m.toString());
		
		m=new StringBuilder();
		m.append(s.next());
		str="";
		
		do
		{
			str=s.next();
			m.append("|");
			m.append(str);
		} while(!str.equals("[mov]"));
		
		attack4=new Move(m.toString());
		
		heldItem=Pokemon.Item.getFromId(s.nextInt());
		nature=Pokemon.Nature.getFromId(s.nextInt());
		chosenAbility=s.nextInt();
		gender=s.nextInt();
		
		spDefIV=s.nextInt();
		spAtkIV=s.nextInt();
		spdIV=s.nextInt();
		defIV=s.nextInt();
		atkIV=s.nextInt();
		HPIV=s.nextInt();
		
		spDefEV=s.nextInt();
		spAtkEV=s.nextInt();
		spdEV=s.nextInt();
		defEV=s.nextInt();
		atkEV=s.nextInt();
		HPEV=s.nextInt();
		
		hasStatus=s.nextBoolean();
		isParalyzed=s.nextBoolean();
		isPoisoned=s.nextBoolean();
		isBadlyPoisoned=s.nextBoolean();
		isConfused=s.nextBoolean();
		isBurned=s.nextBoolean();
		isFrozen=s.nextBoolean();
		isInfatuated=s.nextBoolean();
		isAsleep=s.nextBoolean();
		
		isLeechSeeded=s.nextBoolean();
		isFlinched=s.nextBoolean();
		isWrapped=s.nextBoolean();
		isDefenseCurled=s.nextBoolean();
		isMinimized=s.nextBoolean();
		isFocused=s.nextBoolean();
		isChoiceLocked=s.nextBoolean();
		isCharging=s.nextBoolean();
		isRecharging=s.nextBoolean();
		isEncored=s.nextBoolean();
		isBideLocked=s.nextBoolean();
		isThrashLocked=s.nextBoolean();
		isUnderwater=s.nextBoolean();
		isUnderGround=s.nextBoolean();
		isFlying=s.nextBoolean();
		isBouncing=s.nextBoolean();
		
		turnCount=s.nextInt();
		confusedCount=s.nextInt();
		unConfuseCount=s.nextInt();
		sleepCount=s.nextInt();
		wakeCount=s.nextInt();
		toxicCount=s.nextInt();
		thrashLockCount=s.nextInt();
		thrashBreak=s.nextInt();
		bideLock=s.nextInt();
		bideDamage=s.nextInt();
		encoreLockCount=s.nextInt();
		wrapCount=s.nextInt();
		
		spDefLvl=s.nextInt();
		spAtkLvl=s.nextInt();
		spdLvl=s.nextInt();
		defLvl=s.nextInt();
		atkLvl=s.nextInt();
		accLvl=s.nextInt();
		evaLvl=s.nextInt();
		
		name=getString(id, NAME,"Pokemon");
		baseHP=getInt(id, HP,"Pokemon");
		
		baseSpDef=getInt(id, SP_DEF,"Pokemon");
		baseSpAtk=getInt(id, SP_ATK,"Pokemon");
		baseSpd=getInt(id, SPEED,"Pokemon");
		baseDef=getInt(id, DEFENSE,"Pokemon");
		baseAtk=getInt(id, ATTACK,"Pokemon");
		
		weight=getInt(id,15,"Pokemon");
		
		ability1=getInt(id, ABILITY1,"Pokemon");
		ability2=getInt(id, ABILITY2, "Pokemon");
		DWAbility=getInt(id, DWABILITY, "Pokemon");
		type1=getInt(id, TYPE1, "Pokemon");
		type2=getInt(id, TYPE2, "Pokemon");
		
		recomputeStats();
		recomputeHP();
		calculateEffectiveSpeed();
	}

	public void randomize()
	{
		atkEV=85;
		defEV=85;
		spdEV=85;
		spAtkEV=85;
		spDefEV=85;
		HPEV=85;
		nature=Nature.getFromId((int) Math.floor(Math.random()*25));
		do
		{
			FillMovepool(id);
			attack1=new Move(Movepool.get((int) (Movepool.size()*Math.random())));
			attack2=new Move(Movepool.get((int) (Movepool.size()*Math.random())));
			attack3=new Move(Movepool.get((int) (Movepool.size()*Math.random())));
			attack4=new Move(Movepool.get((int) (Movepool.size()*Math.random())));
		} while(!checkMoves());
	}
	
	public boolean checkMoves()
	{
		int damaging=0;
		if(attack1.BasePower!=0)
			damaging++;
		if(attack2.BasePower!=0)
			damaging++;
		if(attack3.BasePower!=0)
			damaging++;
		if(attack4.BasePower!=0)
			damaging++;
		
		if(attack1.id==attack2.id || attack1.id==attack3.id || attack1.id==attack4.id || attack2.id==attack3.id || attack2.id==attack4.id || attack3.id==attack4.id)
			return false;
		
		return (damaging>=2);
	}
	
	public void FillMovepool(int getMovesFrom){
		
		if(id==235)
			for (int i = 1; i<=559; i++)
				Movepool.add(new Integer(i));
		else{
		
		ArrayList<Integer> moveIDs=getIntList(getMovesFrom, 2, "pokemon_moves");
		
		if(moveIDs.isEmpty()==false)
		for(int i = 0; i<moveIDs.size();i++)
			Movepool.add(moveIDs.get(i).intValue());
		int evolvedFrom=getInt(getMovesFrom,14,"Pokemon");
		if(evolvedFrom!=0)
			FillMovepool(evolvedFrom);
	
		}
	}
	/**
	 * Rolls for a status change.
	 * @param chance percent chance (1-100) of this status change occurring
	 * @param stat stat to be changed:<p>1-paralysis<p>2-poison<p>3-confuse<p>4-burn<p>5-freeze<p>6-infatuation<p>7-sleep
	 * @return 2 if successful stat change, 1 if already affected, 0 if RNG is mean to you, -1 if you fucked up the stat number.
	 */
	
	public boolean checkLegality()
	{
		legal=true;
		int totalIV=spDefIV+spAtkIV+spdIV+atkIV+defIV+HPIV;
		int totalEV=spDefEV+spAtkEV+spdEV+atkEV+defEV+HPEV;
		
		if(totalIV>186 || totalEV>510)
			legal=false;
		if(attack1==attack2 || attack1==attack3 || attack1==attack4 || attack2==attack3 || attack2==attack4 || attack3==attack4)
			legal=false;
		return legal;
	}
	
	public int Statinate(int IV, int EV, int base, String StatName)
	{
		int stat;
		if(StatName.equals("HP"))
			stat=(((IV+(2*base)+(EV/4)+100)*level)/100)+10;
			
		else
			stat=(((IV+(2*base)+(EV/4))*level)/100)+5;
		
		if(StatName.equals(nature.getStatIncreased()))
			stat*=1.1;
		if(StatName.equals(nature.getStatDecreased()))
			stat*=.9;
		
		return stat;
		
	}
	
	
	public void recomputeStats()
	{
		
		atk=Statinate(atkIV, atkEV, baseAtk,"Attack");
		def=Statinate(defIV, defEV, baseDef,"Defense");
		spd=Statinate(spdIV, spdEV, baseSpd,"Speed");
		calculateEffectiveSpeed();
		spAtk=Statinate(spAtkIV, spAtkEV, baseSpAtk, "Special Attack");
		spDef=Statinate(spDefIV, spDefEV, baseSpDef, "Special Defense");
		
		
		
		checkLegality();
	}
	
	public void recomputeHP()
	{
		maxHP=currentHP=Statinate(HPIV, HPEV, baseHP, "HP");
	}
	
	public String typeToString(int typenum)
	{
		if(typenum==1)
		{
			switch (type1)
			{
			case 0:
				return "Normal";
			case 1:
				return "Fire";
			case 2:
				return "Water";
			case 3:
				return "Electric";
			case 4:
				return "Grass";
			case 5:
				return "Ice";
			case 6:
				return "Fighting";
			case 7:
				return "Poison";
			case 8:
				return "Ground";
			case 9:
				return "Flying";
			case 10:
				return "Psychic";
			case 11:
				return "Bug";
			case 12:
				return "Rock";
			case 13:
				return "Ghost";
			case 14:
				return "Dragon";
			case 15:
				return "Dark";
			case 16:
				return "Steel";
			case 17:
				return "";
			default:
				return "Dammit.";
			}
		}
		
		if(typenum==2)
		{
			switch (type2)
			{
			case 0:
				return "Normal";
			case 1:
				return "Fire";
			case 2:
				return "Water";
			case 3:
				return "Electric";
			case 4:
				return "Grass";
			case 5:
				return "Ice";
			case 6:
				return "Fighting";
			case 7:
				return "Poison";
			case 8:
				return "Ground";
			case 9:
				return "Flying";
			case 10:
				return "Psychic";
			case 11:
				return "Bug";
			case 12:
				return "Rock";
			case 13:
				return "Ghost";
			case 14:
				return "Dragon";
			case 15:
				return "Dark";
			case 16:
				return "Steel";
			case 17:
				return "";
			default:
				return "Dammit.";
			}
		}
		return "What the hell did you do this time?";
	}
	
	/**
	 * Damages this Pokemon the specified amount. This also takes care of fainting.
	 * @param amount Amount of damage to inflict.
	 * @return true if this damage causes fainting, false otherwise.
	 */
	public boolean damage(int amount)
	{
		currentHP-=amount;
		if(currentHP<=0)
		{
			currentHP=0;
			hasFainted=true;
			//Attack.message(name + " fainted!");
			//TODO: deal with messages
			return true;
		}
		return false;
	}
	
	public void heal(double amount){
		int temp =currentHP;
		currentHP+=(int)Math.ceil(amount);
		if(currentHP>maxHP)
			currentHP=maxHP;
		//Attack.message(name+" recovered " + (currentHP-temp) + " HP!");
		//TODO: deal with messages
	}
	
	public void changeStatus(int statusToChange){
		switch(statusToChange){

		case 2:{
			wakeCount=(int)Math.ceil(Math.random()*5);
		}
		case 38: {
			isAsleep=true;
			if(statusToChange==38)
			wakeCount=2;
			break;
		}
		case 67:{}
		case 3: {
			isPoisoned=true;
			break;
		}
		case 5:{
			isBurned=true;
			break;
		}
		case 6:{
			isFrozen=true;
			break;
		}
		case 7:{}
		case 68:{
			isParalyzed=true;
			break;
		}
		case 34:{
			isBadlyPoisoned=true;
			toxicCount=1;
			break;
		}
		case 28:{}
		case 50:{
			isConfused=true;
			unConfuseCount=(int)Math.ceil(Math.random()*4);
			return;
		}
		case 121:{
			isInfatuated=true;
			return;
		}
		}
		hasStatus=true;
	}
	
	/**
	 * (De)Buffs the specified stat the specified number of levels.
	 * @param stat The stat number of the stat to be (de)buffed:<p>3: attack<p>4: defense<p>5: spAtk<p>6: spDef<p>7: speed<p>8: evade<p>9: accuracy
	 * @param levels Number of levels to buff the specified stat. Positive numbers are buffs, negatives are debuffs.
	 */
	public void statBuff(int stat, int levels)
	{
		if(stat==3)
			atkLvl+=levels;
		if(stat==4)
			defLvl+=levels;
		if(stat==5)
			spAtkLvl+=levels;
		if(stat==6)
			spDefLvl+=levels;
        if(stat==7){
        	spdLvl+=levels;
        	calculateEffectiveSpeed();
        }
		if(stat==8)
			evaLvl+=levels;
		if(stat==9)
			accLvl+=levels;
	}
	
	public void calculateEffectiveSpeed(){
		effectiveSpeed=spd;
		if(spdLvl<0)
			effectiveSpeed*=(2/(2-spdLvl));
		if(spdLvl>0)
			effectiveSpeed*=(2+spdLvl)/2;
		if(isParalyzed)
			effectiveSpeed/=2;
		if(heldItem==Item.CHOICE_SCARF)
			effectiveSpeed*=1.5;
		if(Battlefield.weather==Battlefield.RAIN&&chosenAbility==SWIFT_SWIM||Battlefield.weather==Battlefield.SUNNY&&chosenAbility==CHLOROPHYLL||Battlefield.weather==Battlefield.SANDSTORM&&chosenAbility==SAND_RUSH)
			effectiveSpeed*=2;
	}
	
	public String writeToString()
	{
		StringBuilder b=new StringBuilder();
		
		b.append("[pkm]|");
		b.append(id);
		b.append("|");
		b.append(level);
		b.append("|");
		b.append(currentHP);
		b.append("|");
		b.append(hasFainted);
		b.append("|");
		
		b.append(attack1.writeToString());
		b.append("|");
		b.append(attack2.writeToString());
		b.append("|");
		b.append(attack3.writeToString());
		b.append("|");
		b.append(attack4.writeToString());
		
		b.append("|");
		b.append(heldItem.getID());
		b.append("|");
		b.append(nature.getID());
		b.append("|");
		b.append(chosenAbility);
		b.append("|");
		b.append(gender);
		b.append("|");
		
		b.append(spDefIV);
		b.append("|");
		b.append(spAtkIV);
		b.append("|");
		b.append(spdIV);
		b.append("|");
		b.append(defIV);
		b.append("|");
		b.append(atkIV);
		b.append("|");
		b.append(HPIV);
		b.append("|");
		
		b.append(spDefEV);
		b.append("|");
		b.append(spAtkEV);
		b.append("|");
		b.append(spdEV);
		b.append("|");
		b.append(defEV);
		b.append("|");
		b.append(atkEV);
		b.append("|");
		b.append(HPEV);
		
		b.append("|");
		b.append(hasStatus);
		b.append("|");
		b.append(isParalyzed);
		b.append("|");
		b.append(isPoisoned);
		b.append("|");
		b.append(isBadlyPoisoned);
		b.append("|");
		b.append(isConfused);
		b.append("|");
		b.append(isBurned);
		b.append("|");
		b.append(isFrozen);
		b.append("|");
		b.append(isInfatuated);
		b.append("|");
		b.append(isAsleep);
		
		b.append("|");
		b.append(isLeechSeeded);
		b.append("|");
		b.append(isFlinched);
		b.append("|");
		b.append(isWrapped);
		b.append("|");
		b.append(isDefenseCurled);
		b.append("|");
		b.append(isMinimized);
		b.append("|");
		b.append(isFocused);
		b.append("|");
		b.append(isChoiceLocked);
		b.append("|");
		b.append(isCharging);
		b.append("|");
		b.append(isRecharging);
		b.append("|");
		b.append(isEncored);
		b.append("|");
		b.append(isBideLocked);
		b.append("|");
		b.append(isThrashLocked);
		b.append("|");
		b.append(isUnderwater);
		b.append("|");
		b.append(isUnderGround);
		b.append("|");
		b.append(isFlying);
		b.append("|");
		b.append(isBouncing);
		b.append("|");
		
		b.append(turnCount);
		b.append("|");
		b.append(confusedCount);
		b.append("|");
		b.append(unConfuseCount);
		b.append("|");
		b.append(sleepCount);
		b.append("|");
		b.append(wakeCount);
		b.append("|");
		b.append(toxicCount);
		b.append("|");
		b.append(thrashLockCount);
		b.append("|");
		b.append(thrashBreak);
		b.append("|");
		b.append(bideLock);
		b.append("|");
		b.append(bideDamage);
		b.append("|");
		b.append(encoreLockCount);
		b.append("|");
		b.append(wrapCount);
		b.append("|");
		
		b.append(spDefLvl);
		b.append("|");
		b.append(spAtkLvl);
		b.append("|");
		b.append(spdLvl);
		b.append("|");
		b.append(defLvl);
		b.append("|");
		b.append(atkLvl);
		b.append("|");
		b.append(accLvl);
		b.append("|");
		b.append(evaLvl);
		
		b.append("|[pkm]");
		
		return b.toString();
	}
	
	/**
	 * Resets all stat buffs to their default values, like if Mist was used.
	 */
	public void resetStats()
	{
		atkLvl=defLvl=spAtkLvl=spDefLvl=spdLvl=evaLvl=accLvl=0;
	}
	
	public void resetOnSwitch(){
		 if(isMimicing!=0){
			 if(isMimicing==1){
				 attack1=new Move(102);
				 attack1.CurrentPP=MimicPP;
			 }
			 if(isMimicing==2){
				 attack2=new Move(102);
				 attack2.CurrentPP=MimicPP;
			 }
			 if(isMimicing==3){
				 attack3=new Move(102);
				 attack3.CurrentPP=MimicPP;
			 }
			 if(isMimicing==4){
				 attack4=new Move(102);
				 attack4.CurrentPP=MimicPP;
			 }
		 }
		 turnCount=0;
		 confusedCount=0;
		 unConfuseCount=0;
		 sleepCount=0;
		 toxicCount=0;
		 thrashLockCount=0;
		 thrashBreak=0;
		 bideLock=0;
		 bideDamage=0;
		 encoreLockCount=0;
		 isConfused=false;
		 isInfatuated=false;
		 isLeechSeeded=false;
		 isFlinched=false;
		 isDefenseCurled=false;
		 isMinimized=false;
		 isFocused=false;
		 resetStats();
	}
	
	public String toString()
	{
		return name+"\nLevel: "+level+"\nHP: "+currentHP+"/"+maxHP+"\nStats:\nAttack: "+atk+"\nDefense: "+def+"\nSpeed: "+spd+"\nSp. Atk: "+spAtk+"\nSp. Def: "+spDef;
	}
	
	public void changeType(int type){
		type1=type;
		type2=UNKNOWN;
	}
	
	public static float getEffectiveness(int AtkType, int DefType){
		float[][] TypeChart =
		{
				{1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,.5f,0f,1f,1f,.5f,1f},//normal
				{1f,.5f,.5f,1f,2f,2f,1f,1f,1f,1f,1f,2f,.5f,1f,.5f,1f,2f,1f},//fire
				{1f,2f,.5f,1f,.5f,1f,1f,1f,2f,1f,1f,1f,2f,1f,.5f,1f,1f,1f},//water
				{1f,1f,2f,.5f,.5f,1f,1f,1f,0f,2f,1f,1f,1f,1f,.5f,1f,1f,1f},//electric
				{1f,.5f,2f,1f,.5f,1f,1f,.5f,2f,.5f,1f,.5f,2f,1f,.5f,1f,.5f,1f},//grass
				{1f,.5f,.5f,1f,2f,.5f,1f,1f,2f,2f,1f,1f,1f,1f,2f,1f,.5f,1f},//ice
				{2f,1f,1f,1f,1f,2f,1f,.5f,1f,.5f,.5f,.5f,2f,0f,1f,2f,2f,1f},//fighting
				{1f,1f,1f,1f,2f,1f,1f,.5f,.5f,1f,.5f,1f,.5f,.5f,1f,1f,0f,1f},//poison
				{1f,2f,1f,2f,.5f,1f,1f,2f,1f,0f,1f,.5f,2f,1f,1f,1f,2f,1f},//ground
				{1f,1f,1f,.5f,2f,1f,2f,1f,1f,1f,1f,2f,.5f,1f,1f,1f,.5f,1f},//flying
				{1f,1f,1f,1f,1f,1f,2f,2f,1f,1f,.5f,1f,1f,1f,1f,0f,.5f,1f},//psychic
				{1f,.5f,1f,1f,2f,1f,.5f,.5f,1f,.5f,2f,1f,1f,.5f,1f,2f,.5f,1f},//bug
				{1f,2f,1f,1f,1f,2f,.5f,1f,.5f,2f,1f,2f,1f,1f,1f,1f,.5f,1f},//rock
				{0f,1f,1f,1f,1f,1f,1f,1f,1f,1f,2f,1f,1f,2f,1f,.5f,.5f,1f},//ghost
				{1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,2f,1f,.5f,1f},//dragon
				{1f,1f,1f,1f,1f,1f,.5f,1f,1f,1f,2f,.5f,1f,2f,1f,.5f,.5f,1f},//dark
				{1f,.5f,.5f,.5f,1f,2f,1f,1f,1f,1f,1f,1f,2f,1f,1f,1f,.5f,1f},//steel
				{1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f}//unknown
			
		};
		float effectiveness=0;
		effectiveness=TypeChart[AtkType][DefType];
		
		return effectiveness;
	}
	
    public Bitmap getFrontSprite(AssetManager am)	 
    {	 
        Bitmap result=null;	 
 
	    try	 
	    {	 
	            BufferedInputStream buf = new BufferedInputStream(am.open("front/"+id+".png"));	 
	            result = BitmapFactory.decodeStream(buf);	 
	            buf.close();	 
	    }	 
	    catch (IOException e)	 
	    {	 
	       Log.e("pokedroid",e.toString());	 
	    }	 
	 
	    return result;	 
    }	 
 
    public Bitmap getBackSprite(AssetManager am)	 
    {	 
        Bitmap result=null;	 
 
        try	 
    	{	 
            BufferedInputStream buf = new BufferedInputStream(am.open("rear/"+id+".png"));	 
            result = BitmapFactory.decodeStream(buf);	 
            buf.close();	 
    	}	 
        catch (IOException e)	 
        {	 
        	Log.e("pokedroid",e.toString());	 
    	}	 
 
        return result;	 
    }
	
    /**
     * OVERRIDE ME, BITCH
     * @param index
     * @param column
     * @param table
     * @return
     */
    public String getString(int index, int column, String table)
    {
    	return SQLSource.getString(index, column, table);
    }
    
    /**
     * OVERRIDE THIS TOO, BITCH
     * @param index
     * @param column
     * @param table
     * @return
     */
    public int getInt(int index, int column, String table)
    {
    	return SQLSource.getInt(index, column, table);
    }
    
    /**
     * AND THIS, HOE
     * @param index
     * @param column
     * @param table
     * @return
     */
    public ArrayList<Integer> getIntList(int index, int column, String table)
    {
    	return SQLSource.getIntList(index, column, table);
    }
    
	public static final int NORMAL = 0;
	public static final int FIRE=1;
	public static final int WATER=2;
	public static final int ELECTRIC=3;
	public static final int GRASS=4;
	public static final int ICE=5;
	public static final int FIGHTING=6;
	public static final int POISON=7;
	public static final int GROUND=8;
	public static final int FLYING = 9;
	public static final int PSYCHIC = 10;
	public static final int BUG=11;
	public static final int ROCK=12;
	public static final int GHOST = 13;
	public static final int DRAGON=14;
	public static final int DARK =15;
	public static final int STEEL=16;
	public static final int UNKNOWN = 17;
	
	public static final int ADAPTABILITY=0;
	public static final int AFTERMATH=1;
	public static final int AIR_LOCK=2;
	public static final int ANALYTIC=3;
	public static final int ANGER_POINT=4;
	public static final int ANTICIPATION=5;
	public static final int ARENA_TRAP=6;
	public static final int BAD_DREAMS=7;
	public static final int BATTLE_ARMOR=8;
	public static final int BIG_PECKS=9;
	public static final int BLAZE=10;
	public static final int CACOPHONY=11;
	public static final int CHLOROPHYLL=12;
	public static final int CLEAR_BODY=13;
	public static final int CLOUD_NINE=14;
	public static final int COLOR_CHANGE=15;
	public static final int COMPOUNDEYES=16;
	public static final int CONTRARY=17;
	public static final int CURSED_BODY=18;
	public static final int CUTE_CHARM=19;
	public static final int DAMP=20;
	public static final int DEFEATIST=21;
	public static final int DEFIANT = 22;
	public static final int DOWNLOAD=23;
	public static final int DRIZZLE=24;
	public static final int DROUGHT=25;
	public static final int DRY_SKIN=26;
	public static final int EARLY_BIRD=27;
	public static final int EFFECT_SPORE=28;
	public static final int FILTER=29;
	public static final int FLAME_BODY=30;
	public static final int FLARE_BOOST=31;
	public static final int FLASH_FIRE=32;
	public static final int FLOWER_GIFT=33;
	public static final int FORECAST=34;
	public static final int FOREWARN=35;
	public static final int FRIEND_GUARD=36;
	public static final int FRISK=37;
	public static final int GLUTTONY=38;
	public static final int GUTS=39;
	public static final int HARVEST = 40;
	public static final int HEALER=41;
	public static final int HEAT_PROOF=42;
	public static final int HEAVY_METAL=43;
	public static final int HONEY_GATHER=44;
	public static final int HUGE_POWER=45;
	public static final int HUSTLE=46;
	public static final int HYDRATION=47;
	public static final int HYPER_CUTTER=48;
	public static final int ICE_BODY=49;
	public static final int ILLUMINATE=50;
	public static final int ILLUSION=51;
	public static final int IMMUNITY = 52;
	public static final int IMPOSTER =53;
	public static final int INFILTRATOR=54;
	public static final int INNER_FOCUS=55;
	public static final int INSOMNIA=56;
	public static final int INTIMIDATE=57;
	public static final int IRON_BARBS=58;
	public static final int IRON_FIST=59;
	public static final int JUSTIFIED=60;
	public static final int KEEN_EYE=61;
	public static final int KLUTZ=62;
	public static final int LEAF_GUARD=63;
	public static final int LEVITATE=64;
	public static final int LIGHT_METAL=65;
	public static final int LIGHTNINGROD=66;
	public static final int LIMBER=67;
	public static final int LIQUID_OOZE=68;
	public static final int MAGIC_BOUNCE=69;
	public static final int MAGIC_GUARD=70;
	public static final int MAGMA_ARMOR=71;
	public static final int MAGNET_PULL=72;
	public static final int MARVEL_SCALE=73;
	public static final int MINUS=74;
	public static final int MOLD_BREAKER=75;
	public static final int MOODY=76;
	public static final int MOTOR_DRIVE=77;
	public static final int MOXIE=78;
	public static final int MULTISCALE=79;
	public static final int MULTITYPE=80;
	public static final int MUMMY=81;
	public static final int NATURAL_CURE=82;
	public static final int NO_GUARD=83;
	public static final int NORMALIZE=84;
	public static final int OBLIVIOUS=85;
	public static final int OVERCOAT=86;
	public static final int OVERGROW=87;
	public static final int OWN_TEMPO=88;
	public static final int PICK_UP=89;
	public static final int PICKPOCKET=90;
	public static final int PLUS=91;
	public static final int POISON_HEAL=92;
	public static final int POISON_POINT=93;
	public static final int POISON_TOUCH=94;
	public static final int PRANKSTER=95;
	public static final int PRESSURE=96;
	public static final int PURE_POWER=97;
	public static final int QUICK_FEET=98;
	public static final int RAIN_DISH=99;
	public static final int RATTLED=100;
	public static final int RECKLESS=101;
	public static final int REGENERATOR=102;
	public static final int RIVALRY=103;
	public static final int ROCK_HEAD=104;
	public static final int ROUGH_SKIN=105;
	public static final int RUN_AWAY=106;
	public static final int SAND_FORCE=107;
	public static final int SAND_RUSH=108;
	public static final int SAND_STREAM=109;
	public static final int SAND_VEIL=110;
	public static final int SAP_SIPPER=111;
	public static final int SCRAPPY=112;
	public static final int SERENE_GRACE=113;
	public static final int SHADOW_TAG=114;
	public static final int SHED_SKIN=115;
	public static final int SHEER_FORCE=116;
	public static final int SHELL_ARMOR=117;
	public static final int SHIELD_DUST=118;
	public static final int SIMPLE=119;
	public static final int SKILL_LINK=120;
	public static final int SLOW_START=121;
	public static final int SNIPER=122;
	public static final int SNOW_CLOAK=123;
	public static final int SNOW_WARNING=124;
	public static final int SOLAR_POWER=125;
	public static final int SOLID_ROCK=126;
	public static final int SOUNDPROOF=127;
	public static final int SPEED_BOOST=128;
	public static final int STALL=129;
	public static final int STATIC=130;
	public static final int STEADFAST=131;
	public static final int STENCH=132;
	public static final int STICKY_HOLD=133;
	public static final int STORM_DRAIN=134;
	public static final int STURDY=135;
	public static final int SUCTION_CUPS=136;
	public static final int SUPER_LUCK=137;
	public static final int SWARM=138;
	public static final int SWIFT_SWIM=139;
	public static final int SYNCHRONIZE=140;
	public static final int TANGLED_FEET=141;
	public static final int TECHNICIAN=142;
	public static final int TELEPATHY=143;
	public static final int TERAVOLT=144;
	public static final int THICK_FAT=145;
	public static final int TINTED_LENS=146;
	public static final int TORRENT=147;
	public static final int TOXIC_BOOST=148;
	public static final int TRACE=149;
	public static final int TRUANT=150;
	public static final int TURBOBLAZE=151;
	public static final int UNAWARE=152;
	public static final int UNBURDEN=153;
	public static final int UNNERVE=154;
	public static final int VICTORY_STAR=155;
	public static final int VITAL_SPIRIT=156;
	public static final int VOLT_ABSORB=157;
	public static final int WATER_ABSORB=158;
	public static final int WATER_VEIL=159;
	public static final int WEAK_ARMOR=160;
	public static final int WHITE_SMOKE=161;
	public static final int WONDER_GUARD=162;
	public static final int WONDER_SKIN=163;
	public static final int ZEN_MODE=164;
	
	public enum Item{
		
				NO_ITEM (0, "No Item"),
				ABSORB_BULB (1, "Absorb Bulb"),
				ADAMANT_ORB (2, "Adamant Orb"),
				AGUAV_BERRY (3, "Aguav Berry"),
				AIR_BALLOON (4, "Air Balloon"),
				AMULET_COIN (5, "Amulet Coin"),
				APICOT_BERRY (6, "Apicot Berry"),
				ASPEAR_BERRY (7, "Aspear Berry"),
				BABIRI_BERRY (8, "Babiri Berry"),
				BESERK_GENE (9, "Beserk Gene"),
				BIG_ROOT (10, "Big Root"),
				BINDING_BAND (11, "Binding Band"),
				BLACK_SLUDGE (12, "Black Sludge"),
				BLUK_BERRY (13, "Bluk Berry"),
				BRIGHTPOWDER (14, "Brightpowder"),
				BUG_GEM (15, "Bug Gem"),
				CELL_BATTERY (16, "Cell Battery"),
				CHARTI_BERRY (17, "Charti Berry"),
				CHERI_BERRY (18, "Cheri Berry"),
				CHESTO_BERRY (19, "Chesto Berry"),
				CHILAN_BERRY (20, "Chilan Berry"),
				CHOICE_BAND (21, "Choice Band"),
				CHOICE_SCARF (22, "Choice Scarf"),
				CHOICE_SPECS (23, "Choice Specs"),
				CHOPLE_BERRY (24, "Chople Berry"),
				COBA_BERRY (25, "Coba Berry"),
				COLBUR_BERRY (26, "Colbur Berry"),
				CORNN_BERRY (27, "Cornn Berry"),
				CUSTAP_BERRY (28, "Custap Berry"),
				DAMP_ROCK (29, "Damp Rock"),
				DARK_GEM (30, "Dark Gem"),
				DEEPSEASCALE (31, "DeepSeaScale"),
				DEEPSEATOOTH (32, "DeepSeaTooth"),
				DESTINTY_KNOT (33, "Destiny Knot"),
				DRACO_PLATE (34, "Draco Plate"),
				DRAGON_GEM (35, "Dragon Gem"),
				DREAD_PLATE (36, "Dread Plate"),
				DURIN_BERRY (37, "Durin Berry"),
				EARTH_PLATE (38, "Earth Plate"),
				EJECT_BUTTON (39, "Eject Button"),
				ELECTRIC_GEM (40, "Electric Gem"),
				ENIGMA_BERRY (41, "Enigma Berry"),
				EVIOLITE (42, "Eviolite"),
				EXPERT_BELT (43, "Expert Belt"),
				FIGHTING_GEM (44, "Fighting Gem"),
				FIGY_BERRY (45, "Figy Berry"),
				FIRE_GEM (46, "Fire Gem"),
				FIST_PLATE (47, "Fist Plate"),
				FLAME_ORB (48, "Flame Orb"),
				FLAME_PLATE (49, "Flame Plate"),
				FLOAT_STONE (50, "Float Stone"),
				FLYING_GEM (51, "Flying Gem"),
				FOCUS_BAND (52, "Focus Band"),
				FOCUS_SASH (53, "Focus Sash"),
				FULL_INCENSE (54, "Full Incense"),
				GANLON_BERRY (55, "Ganlon Berry"),
				GHOST_GEM (56, "Ghost Gem"),
				GRASS_GEM (57, "Grass Gem"),
				GREPA_BERRY (58, "Grepa Berry"),
				GRIP_CLAW (59, "Grip Claw"),
				GRISEOUS_ORB (60, "Griseous Orb"),
				GROUND_GEM (61, "Ground Gem"),
				HABAN_BERRY (62, "Haban Berry"),
				HEAT_ROCK (63, "Heat Rock"),
				HONDEW_BERRY (64, "Hondew Berry"),
				IAPAPA_BERRY (65, "Iapapa Berry"),
				ICE_GEM (66, "Ice Gem"),
				ICICLE_PLATE (67, "Icicle Plate"),
				ICY_ROCK (68, "Icy Rock"),
				INSECT_LATE (69, "Insect Plate"),
				IRON_BALL (70, "Iron Ball"),
				IRON_PLATE (71, "Iron Plate"),
				JABOCA_BERRY (72, "Jaboca Berry"),
				KASIB_BERRY (73, "Kasib Berry"),
				KEBIA_BERRY (74, "Kebia Berry"),
				KELPSY_BERRY (75, "Kelpsy Berry"),
				KINGS_ROCK (76, "King's Rock"),
				LAGGING_TAIL (77, "Lagging Tail"),
				LANSAT_BERRY (78, "Lansat Berry"),
				LAX_INCENSE (79, "Lax Incense"),
				LEFTOVERS (80, "Leftovers"),
				LEPPA_BERRY (81, "Leppa Berry"),
				LIECHI_BERRY (82, "Liechi Berry"),
				LIFE_ORB (83, "Life Orb"),
				LIGHT_BALL (84, "Light Ball"),
				LIGHT_CLAY (85, "Light Clay"),
				LUCK_INCENSE (86, "Luck Incense"),
				LUM_BERRY (87, "Lum Berry"),
				LUSTROUS_ORB (88, "Lustrous Orb"),
				MACHO_BRACE (89, "Macho Brace"),
				MAGOST_BERRY (90, "Magost Berry"),
				MAGO_BERRY (91, "Mago Berry"),
				MEADOW_PLATE (92, "Meadow Plate"),
				MENTAL_HERB (93, "Mental Herb"),
				METAL_POWDER (94, "Metal Powder"),
				METRONOME (95, "Metronome"),
				MICLE_BERRY (96, "Micle Berry"),
				MIND_PLATE (97, "Mind Plate"),
				MUSCLE_BAND (98, "Muscle Band"),
				NANAB_BERRY (99, "Nanab Berry"),
				NOMEL_BERRY (100, "Nomel Berry"),
				NORMAL_GEM (101, "Normal Gem"),
				OCCA_BERRY (102, "Occa Berry"),
				ODD_INCENSE (103, "Odd Incense"),
				ORAN_BERRY (104, "Oran Berry"),
				PAMTRE_BERRY (105, "Pamtre Berry"),
				PASSHO_BERRY (106, "Passho Berry"),
				PAYAPA_BERRY (107, "Payapa Berry"),
				PECHA_BERRY (108, "Pecha Berry"),
				PERSIM_BERRY (109, "Persim Berry"),
				PETAYA_BERRY (110, "Petaya Berry"),
				PINAP_BERRY (111, "Pinap Berry"),
				POISON_GEM (112, "Poison Gem"),
				POMEG_BERRY (113, "Pomeg Berry"),
				POWER_ANKLET (114, "Power Anklet"),
				POWER_BAND (115, "Power Band"),
				POWER_BELT (116, "Power Belt"),
				POWER_BRACER (117, "Power Bracer"),
				POWER_HERB (118, "Power Herb"),
				POWER_LENS (119, "Power Lens"),
				POWER_WEIGHT (120, "Power Weight"),
				PSYCHIC_GEM (121, "Psychic Gem"),
				PURE_INCENSE (122, "Pure Incense"),
				QUALOT_BERRY (123, "Qualot Berry"),
				QUICK_CLAW (124, "Quick Claw"),
				RABUTA_BERRY (125, "Rabuta Berry"),
				RAWST_BERRY (126, "Rawst Berry"),
				RAZZ_BERRY (127, "Razz Berry"),
				RED_CARD (128, "Red Card"),
				RINDO_BERRY (129, "Rindo Berry"),
				RING_TARGET (130, "Ring Target"),
				ROCKY_HELMET (131, "Rocky Helmet"),
				ROCK_GEM (132, "Rock Gem"),
				ROCK_INCENSE (133, "Rock Incense"),
				ROSE_INCENSE (134, "Rose Incense"),
				ROWAP_BERRY (135, "Rowap Berry"),
				SALAC_BERRY (136, "Salac Berry"),
				SCOPE_LENS (137, "Scope Lens"),
				SEA_INCENSE (138, "Sea Incense"),
				SHED_SHELL (139, "Shed Shell"),
				SHELL_BELL (140, "Shell Bell"),
				SHUCA_BERRY (141, "Shuca Berry"),
				SITRUS_BERRY (142, "Sitrus Berry"),
				SKY_PLATE (143, "Sky Plate"),
				SMOKE_BALL (144, "Smoke Ball"),
				SMOOTH_ROCK (145, "Smooth Rock"),
				SOUL_DEW (146, "Soul Dew"),
				SPELON_BERRY (147, "Spelon Berry"),
				SPLASH_PLATE (148, "Splash Plate"),
				SPOOKY_PLATE (149, "Spooky Plate"),
				STARF_BERRY (150, "Starf Berry"),
				STEEL_GEM (151, "Steel Gem"),
				STICKY_BARB (152, "Sticky Barb"),
				STONE_PLATE (153, "Stone Plate"),
				TAMATO_BERRY (154, "Tamato Berry"),
				TANGA_BERRY (155, "Tanga Berry"),
				THICK_CLUB (156, "Thick Club"),
				TOXIC_ORB (157, "Toxic Orb"),
				TOXIC_PLATE (158, "Toxic Plate"),
				WACAN_BERRY (159, "Wacan Berry"),
				WATER_GEM (160, "Water Gem"),
				WATMEL_BERRY (161, "Watmel Berry"),
				WAVE_INCENSE (162, "Wave Incense"),
				WEPEAR_BERRY (163, "Wepear Berry"),
				WHITE_HERB (164, "White Herb"),
				WIDE_LENS (165, "Wide Lens"),
				WIKI_BERRY (166, "Wiki Berry"),
				WISE_GLASSES (167, "Wise Glasses"),
				YACHE_BERRY (168, "Yache Berry"),
				ZAP_PLATE (169, "Zap Plate"),
				ZOOM_LENS (170, "Zoom Lens");
		

	
	private int id;
	private String name;
	private static final HashMap<Integer, Item> lookupId = new HashMap<Integer, Item>();
	private Item(int i, String name)
	{
		id=i;
		this.name=name;
	} 
	
	public static Item getFromId(int i) {
		return lookupId.get(i);
	}
	
	public int getID()
	{
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	static {
		for(Item item: values()) {
			lookupId.put(item.getID(), item);
		}
	}
	
	}

	public enum Nature{
		ADAMANT(0,"Adamant", "Attack", "Special Attack"),
		BASHFUL(1,"Bashful","None","None"),
		BOLD(2,"Bold", "Defense", "Attack"),
		BRAVE(3,"Brave","Attack","Speed"),
		CALM(4, "Calm", "Special Defense", "Attack"),
		CAREFUL(5, "Careful", "Special Defense", "Special Attack"),
		DOCILE(6, "Docile", "None", "None"),
		GENTLE(7, "Gentle", "Special Defense", "Defense"),
		HARDY(8, "Hardy", "None", "None"),
		HASTY(9, "Hasty", "Speed", "Defense"),
		IMPISH(10, "Impish", "Defense", "Special Attack"),
		JOLLY(11, "Jolly", "Speed", "Special Attack"),
		LAX(12, "Lax", "Defense", "Special Defense"),
		LONELY(13, "Lonely", "Attack", "Defense"),
		MILD(14, "Mild", "Special Attack", "Defense"),
		MODEST(15, "Modest", "Special Attack", "Attack"),
		NAIVE(16, "Naïve", "Speed", "Special Defense"),
		NAUGHTY(17, "Naughty", "Attack", "Special Defense"),
		QUIET(18, "Quiet", "Special Attack", "Speed"),
		QUIRKY(19, "Quirky", "None", "None"),
		RASH(20, "Rash", "Special Attack", "Special Defense"),
		RELAXED(21, "Relaxed", "Defense", "Speed"),
		SASSY(22, "Sassy", "Special Defense", "Speed"),
		SERIOUS(23, "Serious", "None", "None"),
		TIMID(24, "Timid", "Speed", "Attack");
		
		
		private int id;
		private String name;
		private String Positive;
		private String Negative;
		
		private static final HashMap<Integer, Nature> lookupId = new HashMap<Integer, Nature>();
		
		private Nature(int i, String name, String StatUp, String StatDown){
			id=i;
			this.name=name;
			Positive=StatUp;
			Negative=StatDown;
		}
		
		public static Nature getFromId(int i) {
			return lookupId.get(i);
		}
		
		public int getID(){
			return id;
		}
		public String getName(){
			return name;
		}
		public String getStatIncreased(){
			return Positive;
		}
		public String getStatDecreased(){
			return Negative;
		}
		
		static {
			for(Nature nat: values()) {
				lookupId.put(nat.getID(), nat);
			}
		}
	}
	
	public Pokemon(Pokemon ditto, Pokemon toCopy){
		maxHP=ditto.maxHP;
		currentHP=ditto.currentHP;
		atk=toCopy.atk;
		def=toCopy.def;
		spd=toCopy.spd;
		spAtk=toCopy.spAtk;
		spDef=toCopy.spDef;
		attack1=toCopy.attack1;
		attack2=toCopy.attack2;
		attack3=toCopy.attack3;
		attack4=toCopy.attack4;
		chosenAbility=toCopy.chosenAbility;
		type1=toCopy.type1;
		type2=toCopy.type2;
		nature=toCopy.nature;
	}
	public Pokemon(Pokemon original, int newForm){
		
		
	}
	
	public final static int NAME=1;
	public final static int HP=2;
	public final static int ATTACK=3;
	public final static int DEFENSE=4;
	public final static int SP_ATK=5;
	public final static int SP_DEF=6;
	public final static int SPEED=7;
	public final static int TYPE1=8;
	public final static int TYPE2=9;
	public final static int ABILITY1=10;
	public final static int ABILITY2=11;
	public final static int DWABILITY=12;
	
	public final static int EVASIVENESS=8;
	public final static int ACCURACY=9;
	
	public final static int MALE=0;
	public final static int FEMALE=1;
}