package com.games.pokedroid.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.games.pokedroid.SQLSource;
import com.games.pokedroid.game.Pokemon.Nature;

public class TrainerAI
{
/*	public int switchTarget=0;
	public int trainerNum;
	
	public ArrayList<Integer> powers = new ArrayList<Integer>();
	public ArrayList<Integer> baseStatsOrder = new ArrayList<Integer>();
	
	private static Battlefield bf;
	
	public static int trainer;

	private static Attack attack=new Attack();
	
	private final int NAME=1;
	private final int HP=2;
	private final int ATTACK=3;
	private final int DEFENSE=4;
	private final int SP_ATK=5;
	private final int SP_DEF=6;
	private final int SPEED=7;
	private final int TYPE1=8;
	private final int TYPE2=9;
	private final int ABILITY1=10;
	private final int ABILITY2=11;
	private final int DWABILITY=12;
	private final int EVOSTAGE=13;
	private final int EVOLVEDFROM = 14;
	
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
	
	public TrainerAI(int num)
	{
		trainerNum = num;
		trainer = trainerNum;	//this is needed so executeAI doesn't get angry
		bf = new Battlefield(GUIBattle.team1, GUIBattle.team2);
		
		baseStatsOrder.add(HP);
		baseStatsOrder.add(ATTACK);
		baseStatsOrder.add(DEFENSE);
		baseStatsOrder.add(SP_ATK);
		baseStatsOrder.add(SP_DEF);
		baseStatsOrder.add(SPEED);
		
		generateAITeam();
	}
	
	public void generateAITeam()
	{
		if (trainerNum == 1)
			generateAI1();
		else if (trainerNum <= 10)
			generateAI2();
		else if (trainerNum <= 25)
			generateAI3();
		else
			generateAI4();
	}
	
	public boolean isInArrayList(ArrayList<Integer> ids, int id)
	{
		if (ids.isEmpty())
			return false;
		for (int i = 0; i < ids.size(); i++)
		{
			if (ids.get(i)==id || ids.get(i)==SQLSource.getInt(id, EVOLVEDFROM, "Pokemon"))
				return true;
			if (SQLSource.getInt(id, EVOLVEDFROM, "Pokemon") != 0
					&& ids.get(i)==SQLSource.getInt(SQLSource.getInt(id, EVOLVEDFROM, "Pokemon"),EVOLVEDFROM,"Pokemon"))
				return true;
		}
		return false;
	}
	
	/*
	 * Sets all IVs to 31 (Hidden Power IV setting to be implemented later)
	 
	
	public void setIVS(Pokemon poke)
	{
		poke.spDefIV = 31;
		poke.spAtkIV = 31;
		poke.spdIV = 31;
		poke.defIV = 31;
		poke.atkIV = 31;
		poke.HPIV = 31;
		
		poke.recomputeHP();
		poke.recomputeStats();
	}
	
	/*
	 * Sets EVs for all Trainers under 50
	 
	
	public void setEVS(Pokemon poke)
	{
		setBaseStatsOrder(poke);
		
		if (trainerNum == 1)
		{
			int totalIV = 510;
			
			poke.atkEV = (int) Math.random()*256;
			totalIV -= poke.atkEV;
			
			poke.defEV = (int) Math.random()*256;
			totalIV -= poke.defEV;
			
			if (totalIV < 255)
				poke.spdEV = (int) Math.random()*totalIV;
			else
				poke.spdEV = (int) Math.random()*256;
			totalIV -= poke.spdEV;
			
			if (totalIV < 255)
				poke.spAtkEV = (int) Math.random()*totalIV;
			else
				poke.spAtkEV = (int) Math.random()*256;
			totalIV -= poke.spAtkEV;
			
			if (totalIV < 255)
				poke.spDefEV = (int) Math.random()*totalIV;
			else
				poke.spDefEV = (int) Math.random()*256;
			totalIV -= poke.spDefEV;
			
			if (totalIV < 255)
				poke.HPEV = (int) Math.random()*totalIV;
			else
				poke.HPEV = (int) Math.random()*256;
		}
		else if (trainerNum <= 10)
		{
			poke.atkEV=84;
    		poke.defEV=84;
    		poke.spdEV=84;
    		poke.spAtkEV=84;
    		poke.spDefEV=84;
    		poke.HPEV=84;
    		
    		if (baseStatsOrder.get(0) == ATTACK)
    			poke.atkEV += 4;
    		if (baseStatsOrder.get(0) == DEFENSE)
    			poke.defEV += 4;
    		if (baseStatsOrder.get(0) == SPEED)
    			poke.spdEV += 4;
    		if (baseStatsOrder.get(0) == SP_ATK)
    			poke.spAtkEV += 4;
    		if (baseStatsOrder.get(0) == SP_DEF)
    			poke.spDefEV += 4;
    		if (baseStatsOrder.get(0) == HP)
    			poke.HPEV += 4;
		}
		else if (trainerNum <= 25)
		{
    		if (baseStatsOrder.get(0) == ATTACK)
    			poke.atkEV = 202;
    		if (baseStatsOrder.get(0) == DEFENSE)
    			poke.defEV = 202;
    		if (baseStatsOrder.get(0) == SPEED)
    			poke.spdEV = 202;
    		if (baseStatsOrder.get(0) == SP_ATK)
    			poke.spAtkEV = 202;
    		if (baseStatsOrder.get(0) == SP_DEF)
    			poke.spDefEV = 202;
    		if (baseStatsOrder.get(0) == HP)
    			poke.HPEV = 202;
    		if (baseStatsOrder.get(1) == ATTACK)
    			poke.atkEV = 152;
    		if (baseStatsOrder.get(1) == DEFENSE)
    			poke.defEV = 152;
    		if (baseStatsOrder.get(1) == SPEED)
    			poke.spdEV = 152;
    		if (baseStatsOrder.get(1) == SP_ATK)
    			poke.spAtkEV = 152;
    		if (baseStatsOrder.get(1) == SP_DEF)
    			poke.spDefEV = 152;
    		if (baseStatsOrder.get(1) == HP)
    			poke.HPEV = 152;
    		if (baseStatsOrder.get(2) == ATTACK)
    			poke.atkEV = 102;
    		if (baseStatsOrder.get(2) == DEFENSE)
    			poke.defEV = 102;
    		if (baseStatsOrder.get(2) == SPEED)
    			poke.spdEV = 102;
    		if (baseStatsOrder.get(2) == SP_ATK)
    			poke.spAtkEV = 102;
    		if (baseStatsOrder.get(2) == SP_DEF)
    			poke.spDefEV = 102;
    		if (baseStatsOrder.get(2) == HP)
    			poke.HPEV = 102;
    		if (baseStatsOrder.get(3) == ATTACK)
    			poke.atkEV = 52;
    		if (baseStatsOrder.get(3) == DEFENSE)
    			poke.defEV = 52;
    		if (baseStatsOrder.get(3) == SPEED)
    			poke.spdEV = 52;
    		if (baseStatsOrder.get(3) == SP_ATK)
    			poke.spAtkEV = 52;
    		if (baseStatsOrder.get(3) == SP_DEF)
    			poke.spDefEV = 52;
    		if (baseStatsOrder.get(3) == HP)
    			poke.HPEV = 52;
    	}
		else if (trainerNum <= 50)
		{
			if (poke.baseSpd >= 100)
			{
				poke.spdEV = 252;
				if (baseStatsOrder.get(0) == SPEED)
				{
					if (baseStatsOrder.get(1) == HP)
						poke.HPEV = 252;
					if (baseStatsOrder.get(1) == ATTACK)
						poke.atkEV = 252;
					if (baseStatsOrder.get(1) == DEFENSE)
						poke.defEV = 252;
					if (baseStatsOrder.get(1) == SP_ATK)
						poke.spAtkEV = 252;
					if (baseStatsOrder.get(1) == SP_DEF)
						poke.spDefEV = 252;
					if (baseStatsOrder.get(2) == HP)
						poke.HPEV = 4;
					if (baseStatsOrder.get(2) == ATTACK)
						poke.atkEV = 4;
					if (baseStatsOrder.get(2) == DEFENSE)
						poke.defEV = 4;
					if (baseStatsOrder.get(2) == SP_ATK)
						poke.spAtkEV = 4;
					if (baseStatsOrder.get(2) == SP_DEF)
						poke.spDefEV = 4;
				}
			}
			else
			{
				//Strategy, strategy, lots and lots of strategy
			}
		}
		
		poke.recomputeHP();
		poke.recomputeStats();
	}
	
	/*
	 * Because Shawn's checkLegality method is getting in the way.
	 
	
	public void setCheatingEVS(Pokemon poke)
	{
		setEVS(poke);
		poke.atkEV += trainerNum;
		poke.defEV += trainerNum;
		poke.spdEV += trainerNum;
		poke.spAtkEV += trainerNum;
		poke.spDefEV += trainerNum;
		poke.HPEV += trainerNum;
		
		poke.recomputeHP();
		poke.atk = poke.Statinate(poke.atkIV, poke.atkEV, poke.baseAtk, "Attack");
		poke.def = poke.Statinate(poke.defIV, poke.defEV, poke.baseDef, "Defense");
		poke.spd = poke.Statinate(poke.spdIV, poke.spdEV, poke.baseSpd, "Speed");
		poke.spAtk = poke.Statinate(poke.spAtkIV, poke.spAtkEV, poke.baseSpAtk, "Special Attack");
		poke.spDef = poke.Statinate(poke.spDefIV, poke.spDefEV, poke.baseSpDef, "Special Defense");
	}
	
	public void setBaseStatsOrder(Pokemon poke)
	{	
		clearIntArrayList(baseStatsOrder);
		
		int[] baseAtk = {poke.baseAtk, ATTACK};
		int[] baseDef = {poke.baseDef, DEFENSE};
		int[] baseHP = {poke.baseHP, HP};
		int[] baseSpDef = {poke.baseSpDef, SP_DEF};
		int[] baseSpAtk = {poke.baseSpAtk, SP_ATK};
		int[] baseSpeed = {poke.baseSpd, SPEED};
		
		ArrayList<int[]> orderedArrays = new ArrayList<int[]>();
		orderedArrays.add(baseAtk);
		
		if (baseDef[0] > baseAtk[0] || (baseDef[0] == baseAtk[0] && Math.random() < .5))
		{
			orderedArrays.add(0, baseDef);
		}
		else
			orderedArrays.add(baseDef);
		
		for (int i = 0; i < orderedArrays.size(); i++)
		{
			if (baseHP[0] > orderedArrays.get(i)[0] || (baseHP[0] == orderedArrays.get(i)[0] && Math.random() < .5))
			{
				orderedArrays.add(i, baseHP);
				i = orderedArrays.size();
			}
		}
		if (orderedArrays.size() < 3)
			orderedArrays.add(baseHP);
		
		for (int i = 0; i < orderedArrays.size(); i++)
		{
			if (baseSpDef[0] > orderedArrays.get(i)[0] || (baseSpDef[0] == orderedArrays.get(i)[0] && Math.random() < .5))
			{
				orderedArrays.add(i, baseSpDef);
				i = orderedArrays.size();
			}
		}
		if (orderedArrays.size() < 4)
			orderedArrays.add(baseSpDef);
		
		for (int i = 0; i < orderedArrays.size(); i++)
		{
			if (baseSpAtk[0] > orderedArrays.get(i)[0] || (baseSpAtk[0] == orderedArrays.get(i)[0] && Math.random() < .5))
			{
				orderedArrays.add(i, baseSpAtk);
				i = orderedArrays.size();
			}
		}
		if (orderedArrays.size() < 5)
			orderedArrays.add(baseSpAtk);		
		
		for (int i = 0; i < orderedArrays.size(); i++)
		{
			if (baseSpeed[0] > orderedArrays.get(i)[0] || (baseSpeed[0] == orderedArrays.get(i)[0] && Math.random() < .5))
			{
				orderedArrays.add(i, baseSpeed);
				i = orderedArrays.size();
			}
		}
		if (orderedArrays.size() < 6)
			orderedArrays.add(baseSpeed);
		
		for (int i = 0; i < orderedArrays.size(); i++)
		{
			baseStatsOrder.add(orderedArrays.get(i)[1]);
		}
		
	}
	
	public int sumOfBaseStats(int id)
	{
		int sum = 0;
		sum += SQLSource.getInt(id, ATTACK, "Pokemon");
		sum += SQLSource.getInt(id, DEFENSE, "Pokemon");
		sum += SQLSource.getInt(id, SPEED, "Pokemon");
		sum += SQLSource.getInt(id, SP_DEF, "Pokemon");
		sum += SQLSource.getInt(id, SP_ATK, "Pokemon");
		sum += SQLSource.getInt(id, HP, "Pokemon");
		return sum;
	}
	
	/*
	 * Sets random moves
	 
	
	public void setMoves(Pokemon poke)
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int tempAttackId = 0;
		
		for (int i = 0; i < poke.Movepool.size(); i++)
		{
			temp.add(poke.Movepool.get(i));
		}
		
		if (poke.Movepool.size() == 1)
		{
			poke.attack1 = new Move(temp.get(0));
			poke.attack2 = null;
			poke.attack3 = null;
			poke.attack4 = null;			
		}
		
		else if (poke.Movepool.size() == 2)
		{
			poke.attack1 = new Move(temp.get(0));
			poke.attack2 = new Move(temp.get(1));
			poke.attack3 = null;
			poke.attack4 = null;
		}
		
		else if (poke.Movepool.size() == 3)
		{
			poke.attack1 = new Move(temp.get(0));
			poke.attack2 = new Move(temp.get(1));
			poke.attack3 = new Move(temp.get(2));
			poke.attack4 = null;
		}
		
		else if (trainerNum <= 10)
		{
			tempAttackId = (int) Math.floor(Math.random()*temp.size());
			poke.attack1 = new Move(temp.get(tempAttackId));
			temp.remove(tempAttackId);
			
			tempAttackId = (int) Math.floor(Math.random()*temp.size());
			poke.attack2 = new Move(temp.get(tempAttackId));
			temp.remove(tempAttackId);
		
			tempAttackId = (int) Math.floor(Math.random()*temp.size());
			poke.attack3 = new Move(temp.get(tempAttackId));
			temp.remove(tempAttackId);
			
			tempAttackId = (int) Math.floor(Math.random()*temp.size());
			poke.attack4 = new Move(temp.get(tempAttackId));
			temp.remove(tempAttackId);
			
			if (trainerNum > 5)
			{
				ArrayList<Integer> moveTypes = new ArrayList<Integer>();
				
				moveTypes.add(poke.attack1.type);
				moveTypes.add(poke.attack2.type);
				moveTypes.add(poke.attack3.type);
				moveTypes.add(poke.attack4.type);
				
				while (moreThan2ofAttackType(moveTypes))
				{
					int indexToChange = (int) Math.floor(Math.random()*4);
					while (indexToChange == indexOfUniqueType(moveTypes))
						indexToChange = (int) Math.floor(Math.random()*4);
					if (indexToChange == 0)
					{
						tempAttackId = (int) Math.floor(Math.random()*temp.size());
						poke.attack1 = new Move(temp.get(tempAttackId));
						moveTypes.set(indexToChange, new Move(tempAttackId).type);
					}
					if (indexToChange == 1)
					{
						tempAttackId = (int) Math.floor(Math.random()*temp.size());
						poke.attack2 = new Move(temp.get(tempAttackId));
						moveTypes.set(indexToChange, new Move(tempAttackId).type);
					}
					if (indexToChange == 2)
					{
						tempAttackId = (int) Math.floor(Math.random()*temp.size());
						poke.attack3 = new Move(temp.get(tempAttackId));
						moveTypes.set(indexToChange, new Move(tempAttackId).type);
					}
					if (indexToChange == 3)
					{
						tempAttackId = (int) Math.floor(Math.random()*temp.size());
						poke.attack4 = new Move(temp.get(tempAttackId));
						moveTypes.set(indexToChange, new Move(tempAttackId).type);
					}
				}
			}
		}
		
		else if (trainerNum <= 25)
		{
			poke.randomize();
			setEVS(poke);
/*
			sortPowers(poke);
			int tempPower;
			int moveNum = 1;
			
			if (powers.get(0) == null)
				poke.randomize();
			else
			{
				while (moveNum < 5)
				{	
					ArrayList<Move> possibleMoves = new ArrayList<Move>();
					tempPower = (int) powers.get(0);
					for (int i = 0; i < poke.Movepool.size(); i++)
					{
						if ((int) projectedPower(poke, (double) new Move(poke.Movepool.get(i)).getPower(), new Move(poke.Movepool.get(i)).effect, new Move(poke.Movepool.get(i)).id) == tempPower)
							possibleMoves.add(new Move(poke.Movepool.get(i)));
					}
					if (moveNum == 1)
						poke.attack1 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
					if (moveNum == 2)
						poke.attack2 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
					if (moveNum == 3)
						poke.attack3 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
					if (moveNum == 4)
						poke.attack4 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
					powers.remove(0);
					moveNum++;
				}
			
				ArrayList<Integer> moveTypes = new ArrayList<Integer>();
			
				moveTypes.add(poke.attack1.type);
				moveTypes.add(poke.attack2.type);
				moveTypes.add(poke.attack3.type);
				moveTypes.add(poke.attack4.type);
			
				while (moreThan2ofAttackType(moveTypes) && powers.size() > 0)
				{
					ArrayList<Move> possibleMoves = new ArrayList<Move>();
					tempPower = powers.get(0);
					for (int i = 0; i < poke.Movepool.size(); i++)
					{
						if ((int) projectedPower(poke, (double) new Move(poke.Movepool.get(i)).getPower(), new Move(poke.Movepool.get(i)).effect, new Move(poke.Movepool.get(i)).id) == tempPower)
							possibleMoves.add(new Move(poke.Movepool.get(i)));
					}
				
					int indexToChange = 3;
					while (indexToChange == indexOfUniqueType(moveTypes) && indexToChange > 0)
						indexToChange--;
				
					if (indexToChange == 0)
					{
						poke.attack1 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
						moveTypes.add(indexToChange, poke.attack1.type);
					}
					if (indexToChange == 1)
					{
						poke.attack2 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
						moveTypes.add(indexToChange, poke.attack2.type);
					}
					if (indexToChange == 2)
					{
						poke.attack3 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
						moveTypes.add(indexToChange, poke.attack3.type);
					}
					if (indexToChange == 3)
					{
						poke.attack4 = possibleMoves.get((int) Math.floor(Math.random()*possibleMoves.size()));
						moveTypes.add(indexToChange, poke.attack4.type);
					}
				}
			}
			
			clearPowers();

		}
	}
	
	/*
	 * Clears any ArrayList
	 
	
	public void clearIntArrayList(ArrayList<Integer> list)
	{
		while (!list.isEmpty())
		{
			list.remove(0);
		}
	}
	
	/*
	 * Sorts the moves of the Pokemon based on projected power
	
	public void sortPowers(Pokemon poke)
	{
		ArrayList<Integer> powers = new ArrayList<Integer>();
		for (int i = 0; i < poke.Movepool.size(); i++)
		{
			int temp = (int) projectedPower(poke, (double) new Move(poke.Movepool.get(i)).getPower(), new Move(poke.Movepool.get(i)).effect, new Move(poke.Movepool.get(i)).id);
			if (powers.isEmpty())
				powers.add(temp);
			else
			{
				int index = 0;
				while (index >= powers.size())
				{
					if (powers.get(i) < temp)
						powers.add(i, temp);
					index++;
				}
			}
		}

		for (int i = 0; i < powers.size(); i++)
		{
			this.powers.add(powers.get(i));
		}
	}
	
	/*
	 * Projects the power of all moves of the Pokemon
	 
	
	public double projectedPower(Pokemon poke, double power, int effect, int id)
	{
		if (power == 0)
			power = 40;
		Long TRINUM = new Long("10219533275");
		Long TRIDENOM = new Long("6577474816");
		int crit = 1, critBonus = 1;
		if (poke.chosenAbility == 122)
			crit = 3;
		else
			crit = 2;
		double pow = 1;
		
		if (poke.type1 == new Move(id).type || poke.type2 == new Move(id).type)
			power *= 1.5;
			
		switch(effect)
		{
			case 2: case 45:
			//sleep (2)
			//hits 2 times (45)
				pow *= 2;
				break;
			case 4: case 9: case 51: case 52: case 53: case 54: case 55: case 90:
			//heals user by half damage dealt (4, 9)
			//raises user stats 2 stages (51-55)
			//counter (90)
				pow *= 1.5;
				break;
			case 5:
			//burn
				pow *= 3581;
				pow /= 3894;
				pow *= 1.125;
				pow *= 38825;
				pow /= 31152;
				break;
			case 6:
			//freeze
				pow *= 618;
				pow /= 649;
				pow *= 125;
				pow /= 61;
				break;
			case 7: case 68:
			//paralysis
				pow *= 590;
				pow /= 649;
				pow *= 1.25;
				pow *= 1.25;
				break;
			case 37:
			//tri-attack
				pow *= (TRINUM/TRIDENOM);
				break;
			case 11: case 12: case 14: case 36: case 66: case 82:
			//raises user stats one stage (11, 12, 14, 82)
			//lowers enemy special attacks by 50% (36)
			//lowers enemy attacks by 50% (66)
				pow *= 5;
				pow /= 4;
				break;
			case 17:
			//raises user pseudo-stats one stage
				pow *= 7;
				pow /= 6;
				break;
			case 19: case 20: case 21: case 69: case 70: case 71: case 72: case 73:
			//lowers enemy stats one stage
				pow *= 6;
				pow /= 5;
				break;
			case 24: case 25: case 74:
			//lowers enemy pseudo-stats one stage
				pow *= 8;
				pow /= 7;
				break;
			case 27:
			//bide effect (essentially)
				pow *= 2;
				pow /= 3;
				break;
			case 28:
			//thrash effect (essentially)
				pow *= 965;
				pow /= 1298;
				break;
			case 30: case 43:
			//hits multiple times (30)
			//wrap, bind, etc. (43)
				pow *= 3.5;
				break;
			case 76:
			//charges for turn, then has chance of flinching
				pow /= 2;
			case 32:
			//flinching
				pow *= 1.2;
				if (poke.baseSpd >= 100)
					pow *= 1.2;
				break;
			case 34:
			//badly poisoning
				pow *= 552;
				pow /= 649;
				pow *= 19;
				pow /= 16;
				break;
			case 3:
			//poison
				pow *= 552;
				pow /= 649;
				pow *= 9;
				pow /= 8;
				break;
			case 40: case 81:
			//turn required to charge up (40)
			//turn required to recharge (81)
				pow /= 2;
				break;
			case 44: case 48:
			//changes critBonus
				critBonus++;
				if (effect==48)
					critBonus++;
				break;
			case 46: case 49:
			//if miss, half damage recoil (46)
			//one-fourth recoil damage (49)
				pow *= 3;
				pow /= 4;
				break;
			case 50: case 77:
			//confusion
				if (SQLSource.getInt(id, getPower, "moves") == 0)
					pow = 40;
				else
				{
					pow += 40;
					pow /= 2;
				}
				pow *= 5;
				pow /= 4;
				break;
			case 59: case 60: case 61: case 63:
			//lowers enemy stats 2 stages
				pow *= 4;
				pow /= 3;
				break;
			case 39: case 84: case 91:
			//OHKO (39)
			//metronome (84)
			//encore (91)
				pow *= 0;
				break;
			case 86: case 88: case 89:
			//splash (with a slightly larger chance of being chosen than metronome) (86)
			//damage equal/close to level (88, 89)
				pow /= 100000;
				break;
		}//DO EFFECTS 8, 67, 78, 80, 83, AND 85 AT SOME POINT
		
		double chance = 1;
		
		if (SQLSource.getInt(id, getEffectAccuracy, "moves") != 10000)
		{
			chance *= SQLSource.getInt(id, getEffectAccuracy, "moves");
			chance /= 100;
		}
		
		if (critBonus >= 5)
			critBonus = 5;
		
		crit--;
		
		power *= SQLSource.getInt(id, getAccuracy, "moves");
		power /= 100;
		
		switch(critBonus)
		{
			case 2:
				pow *= (1 + crit/8);
				break;
			case 3:
				pow *= (1 + crit/4);
				break;
			case 4:
				pow *= (1 + crit/3);
				break;
			case 5:
				pow *= (1 + crit/2);
				break;
			default:
				pow *= (1 + crit/16);
		}
		
		power = power * (1-chance) + power * pow * chance;
		
		return power;
	}
	
	/*
	 * Checks to see if 3 moves are the same type
	 
	
	public boolean moreThan2ofAttackType(ArrayList<Integer> types)
	{
		if (types.get(0) == types.get(1))
		{
			if (types.get(1) == types.get(2) || types.get(1) == types.get(3))
				return true;
		}
		
		else
		{
			if (types.get(2) == types.get(3) && (types.get(1) == types.get(2) || types.get(0) == types.get(2)))
				return true;
		}
		
		return false;
	}
	
	/*
	 * Finds the index of the move that has a different type than the other 3
	 * Precondition: moreThan2ofAttackType returns true
	 
	
	public int indexOfUniqueType(ArrayList<Integer> types)
	{
		int index;
		if (types.get(0) != types.get(1) || types.get(0) != types.get(2))
			index = 0;
		else if (types.get(0) != types.get(1))
			index = 1;
		else if (types.get(0) != types.get(2))
			index = 2;
		else
			index = 3;
		return index;
	}
	
	/*
	 * Sets nature for all AI Pokemon
	 
	
	public void setNature(Pokemon poke)
	{
		int natureChoice = 0;
		String inc = "None", dec = "None";
		
		setBaseStatsOrder(poke);
		
		if (trainerNum == 1)
		{
			natureChoice = (int) Math.floor(Math.random()*25);
			poke.nature = Nature.getFromId(natureChoice);
		}
		
		if (trainerNum <= 5)
		{
			ArrayList<Integer> natures = findNatureWith("None", "None");
			if (natures.size() == 0)
				poke.nature = Nature.getFromId(1);
			else
			{
				natureChoice = natures.get((int) Math.floor(Math.random()*natures.size()));
				poke.nature = Nature.getFromId(natureChoice);
			}
		}
		
		if (trainerNum <= 10)
		{
    		if (baseStatsOrder.get(0) == ATTACK)
    			inc = "Attack";
    		if (baseStatsOrder.get(0) == DEFENSE)
    			inc = "Defense";
    		if (baseStatsOrder.get(0) == SPEED)
    			inc = "Speed";
    		if (baseStatsOrder.get(0) == SP_ATK)
    			inc = "Special Attack";
    		if (baseStatsOrder.get(0) == SP_DEF)
    			inc = "Special Defense";
    		if (baseStatsOrder.get(0) == HP)
    		{
    	   		if (baseStatsOrder.get(1) == ATTACK)
        			inc = "Attack";
        		if (baseStatsOrder.get(1) == DEFENSE)
        			inc = "Defense";
        		if (baseStatsOrder.get(1) == SPEED)
        			inc = "Speed";
        		if (baseStatsOrder.get(1) == SP_ATK)
        			inc = "Special Attack";
        		if (baseStatsOrder.get(1) == SP_DEF)
        			inc = "Special Defense";
    		}
    		
    		if (baseStatsOrder.get(5) == ATTACK)
    			dec = "Attack";
    		if (baseStatsOrder.get(5) == DEFENSE)
    			dec = "Defense";
    		if (baseStatsOrder.get(5) == SPEED)
    			dec = "Speed";
    		if (baseStatsOrder.get(5) == SP_ATK)
    			dec = "Special Attack";
    		if (baseStatsOrder.get(5) == SP_DEF)
    			dec = "Special Defense";
    		if (baseStatsOrder.get(5) == HP)
    		{
    	   		if (baseStatsOrder.get(4) == ATTACK)
        			dec = "Attack";
        		if (baseStatsOrder.get(4) == DEFENSE)
        			dec = "Defense";
        		if (baseStatsOrder.get(4) == SPEED)
        			dec = "Speed";
        		if (baseStatsOrder.get(4) == SP_ATK)
        			dec = "Special Attack";
        		if (baseStatsOrder.get(4) == SP_DEF)
        			dec = "Special Defense";
    		}
    		
    		ArrayList<Integer> natures = findNatureWith(inc, dec);
			if (natures.size() == 0)
				poke.nature = Nature.getFromId(1);
			else
			{
				natureChoice = natures.get((int) Math.floor(Math.random()*natures.size()));
				poke.nature = Nature.getFromId(natureChoice);
			}
		}
	}
	
	/*
	 * Makes searching for natures much much easier
	 
	
	public ArrayList<Integer> findNatureWith(String inc, String dec)
	{
		ArrayList<Integer> natures = new ArrayList<Integer>();
		
		for (int i = 0; i < 25; i++)
		{
			Nature nature = Nature.getFromId(i);
			if (nature.getStatIncreased().equals(inc) && nature.getStatDecreased().equals(dec))
				natures.add(i);
		}
		
		return natures;
	}
	
	/*
	 * Generates the AI for the first trainer
	 
	
	public void generateAI1()
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		GUIBattle.team2 = new Pokemon[6];
		for (int i = 0; i < 6; i++)
		{
			int id = (int) Math.ceil(Math.random()*649);
			while (isInArrayList(ids, id) || id == 0)
				id = (int) Math.ceil(Math.random()*649);
			Pokemon temp = new Pokemon(id);
			setIVS(temp);
			setEVS(temp);
			setNature(temp);
			setMoves(temp);
			GUIBattle.team2[i] = temp;
			ids.add(id);
		}
	}
	
	/*
	 * Trainers 2-10
	 
	
	public void generateAI2()
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		GUIBattle.team2 = new Pokemon[6];
		for (int i = 0; i < 6; i++)
		{
			int id = (int) Math.ceil(Math.random()*649);
		    while (SQLSource.getInt(id, EVOSTAGE, "Pokemon") != 3 || isInArrayList(ids, id) || id == 0)
				id = (int) Math.ceil(Math.random()*649);
			Pokemon temp = new Pokemon(id);
			setIVS(temp);
			setEVS(temp);
			setNature(temp);
			setMoves(temp);
			GUIBattle.team2[i] = temp;
			ids.add(id);
		}
	}
	
	/*
	 * Trainers 11-25...
	 
	
	public void generateAI3()
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		GUIBattle.team2 = new Pokemon[6];
		for (int i = 0; i < 6; i++)
		{
			int id = (int) Math.ceil(Math.random()*649);
		    while (SQLSource.getInt(id, EVOSTAGE, "Pokemon") != 3 || isInArrayList(ids, id)
		    		|| sumOfBaseStats(id) < 400 || id == 0)
				id = (int) Math.ceil(Math.random()*649);
		    Pokemon temp = new Pokemon(id);
		    setIVS(temp);
		    setEVS(temp);
		    setNature(temp);
		    setMoves(temp);
		    GUIBattle.team2[i] = temp;
		    ids.add(id);
		}
	}
	
	/*
	 * Trainers 26 and beyond...
	 * Cheating begins at trainer 51. 
	 
	
	public void generateAI4()
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		GUIBattle.team2 = new Pokemon[6];
		for (int i = 0; i < 6; i++)
		{
			int id = (int) Math.ceil(Math.random()*649);
		    while (SQLSource.getInt(id, EVOSTAGE, "Pokemon") != 3 || isInArrayList(ids, id)
		    		|| sumOfBaseStats(id) < 460 || id == 0)
				id = (int) Math.ceil(Math.random()*649);
		    Pokemon temp = new Pokemon(id);
		    setIVS(temp);
		    if (trainerNum <= 50)
		    	setEVS(temp);
		    else
		    	setCheatingEVS(temp);
		    setNature(temp);
		    setMoves(temp);
		    GUIBattle.team2[i] = temp;
		    ids.add(id);
		}
	}
	
	/*
	 * Returns a move. Time for fun.
	 
	
	public static Move executeAI()
	{
		boolean fainted = false;
		int attackid = 0;
		Move AttackSelected = null;
		
		ArrayList<Integer> damages = new ArrayList<Integer>();
		Pokemon temp = new Pokemon(GUIBattle.team1[GUIBattle.activeAlly].id);
		
		if (attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack1, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false) != 0
				&& GUIBattle.team2[GUIBattle.activeEnemy].attack1 != null)
			damages.add(attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack1, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false));
		else
			damages.add(30);//should be -1. is 30 for testing purposes.
		if (attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack2, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false) != 0
				&& GUIBattle.team2[GUIBattle.activeEnemy].attack2 != null)
			damages.add(attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack2, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false));
		else
			damages.add(30);//should be -1. is 30 for testing purposes.
		if (attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack3, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false) != 0
				&& GUIBattle.team2[GUIBattle.activeEnemy].attack3 != null)
			damages.add(attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack3, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false));
		else
			damages.add(30);//should be -1. is 30 for testing purposes.
		if (attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack4, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false) != 0
				&& GUIBattle.team2[GUIBattle.activeEnemy].attack4 != null)
			damages.add(attack.computeDamage(GUIBattle.team2[GUIBattle.activeEnemy].attack4, GUIBattle.team2[GUIBattle.activeEnemy], temp, bf, false));
		else
			damages.add(30);//should be -1. is 30 for testing purposes.
		
		if (trainer == 1)
		{
			attackid = (int) Math.random()*4;
		}
		else if (trainer <= 10)
		{
			setTempStats(temp);
			
			int success = returnMostDamage(damages);
			
			if (Math.random()<.8)
				attackid = success;
			else
			{
				damages.set(success, 0);
				success = returnMostDamage(damages);
				if (Math.random()<.7)
					attackid = success;
				else
				{
					damages.set(success, 0);
					success = returnMostDamage(damages);
					if (Math.random()<.6)
						attackid = success;
					else
					{
						damages.set(success, 0);
						attackid = returnMostDamage(damages);
					}
				}
			}
		}
		else if (trainer <= 25)
		{
			setTempStats(temp);
			
			int success = returnMostDamage(damages);
			
			if (Math.random()<.95)
				attackid = success;
			else
			{
				damages.set(success, 0);
				success = returnMostDamage(damages);
				if (Math.random()<.95)
					attackid = success;
				else
				{
					damages.set(success, 0);
					success = returnMostDamage(damages);
					if (Math.random()<.95)
						attackid = success;
					else
					{
						damages.set(success, 0);
						attackid = returnMostDamage(damages);
					}
				}
			}
		}
		
		if(attackid == 0)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack1;
		else if(attackid == 1)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack2;
		else if(attackid == 2)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack3;
		else if(attackid == 3)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack4;
		
		if (GUIBattle.team2[GUIBattle.activeEnemy].hasFainted)
		{
			//something happens here... as in it's in a different method (see onAIFaint())
		}
		
		if (AttackSelected == null)
			return GUIBattle.team2[GUIBattle.activeEnemy].attack1;
		
		return AttackSelected;
	}
	
	/*
	 * Returns the index of the move that would deal the most damage
	 

	public static int returnMostDamage(ArrayList<Integer> damages)
	{
		int highest = 0;
		if (!ifInArrayList(-1, damages))
		{
			for (int i = 1; i < damages.size(); i++)
			{
				if (damages.get(i) > damages.get(highest))
					highest = i;
				else if (damages.get(i) == damages.get(highest))
				{
					
					/*
					 * Yeah, I'll work on making this less random eventually...
					 
			
					if (highest == 2)
					{
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack3.CurrentPP < GUIBattle.team2[GUIBattle.activeEnemy].attack4.CurrentPP)
							highest = i;
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack3.CurrentPP == GUIBattle.team2[GUIBattle.activeEnemy].attack4.CurrentPP)
							if (Math.random() < .5)
								highest = i;
					}
					else if (highest == 1 && i == 2)
					{
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack2.CurrentPP < GUIBattle.team2[GUIBattle.activeEnemy].attack3.CurrentPP)
							highest = i;
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack2.CurrentPP == GUIBattle.team2[GUIBattle.activeEnemy].attack3.CurrentPP)
							if (Math.random() < .5)
								highest = i;
					}
					else if (highest == 1 && i == 3)
					{
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack2.CurrentPP < GUIBattle.team2[GUIBattle.activeEnemy].attack4.CurrentPP)
							highest = i;
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack2.CurrentPP == GUIBattle.team2[GUIBattle.activeEnemy].attack4.CurrentPP)
							if (Math.random() < .5)
								highest = i;
					}
					else if (highest == 0 && i == 1)
					{
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack1.CurrentPP < GUIBattle.team2[GUIBattle.activeEnemy].attack2.CurrentPP)
							highest = i;
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack1.CurrentPP == GUIBattle.team2[GUIBattle.activeEnemy].attack2.CurrentPP)
							if (Math.random() < .5)
								highest = i;
					}
					else if (highest == 0 && i == 2)
					{
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack1.CurrentPP < GUIBattle.team2[GUIBattle.activeEnemy].attack3.CurrentPP)
							highest = i;
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack1.CurrentPP == GUIBattle.team2[GUIBattle.activeEnemy].attack3.CurrentPP)
							if (Math.random() < .5)
								highest = i;
					}
					else
					{
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack1.CurrentPP < GUIBattle.team2[GUIBattle.activeEnemy].attack4.CurrentPP)
							highest = i;
						if (GUIBattle.team2[GUIBattle.activeEnemy].attack1.CurrentPP == GUIBattle.team2[GUIBattle.activeEnemy].attack4.CurrentPP)
							if (Math.random() < .5)
								highest = i;
					}
				}
			}
		}
		else
		{
			
			/*
			 * This will honestly be a pain and requires that all move effects are entirely functional
			 * For now it return moves fairly randomly, and does not have a check for Dream Eater.
			 * Quite shameful honestly...
			 
			
			ArrayList<Integer> statusInflicting = new ArrayList<Integer>();
			for (int i = 0; i < damages.size(); i++)
			{
				if (damages.get(i) == -1)
					statusInflicting.add(i);
			}
			if (Math.random() < .3)
				highest = statusInflicting.get((int) Math.random()*statusInflicting.size());
			else
			{
				for (int i = 0; i < statusInflicting.size(); i++)
				{
					damages.set(statusInflicting.get(i), 1);
				}
			}
			highest = returnMostDamage(damages);
		}
		
		return highest;
	}
	
	/*
	 * Sets stats for temporary Pokemon for simple calculations
	 * Does not give AI the chance to see the EVs and IVs of the enemy Pokemon
	 * There is a possibility that at some point, this will predict EV spreads. Damn son.
	
	public static void setTempStats(Pokemon poke)
	{
		poke.atkEV = 0;
		poke.atkIV = 31;
		poke.defEV = 0;
		poke.defIV = 31;
		poke.spdEV = 0;
		poke.spdIV = 31;
		poke.HPEV = 0;
		poke.HPIV = 31;
		poke.spAtkEV = 0;
		poke.spAtkIV = 31;
		poke.spDefEV = 0;
		poke.spDefIV = 31;
	}
	
	public static boolean ifInArrayList(int num, ArrayList<Integer> nums)
	{
		for (int i = 0; i < nums.size(); i++)
		{
			if (num == nums.get(i))
				return true;
		}
		return false;
	}
	
	/*
	 * When the AI faints, it selects a new Pokemon to switch to.
	 * This will later be switched into 2 methods when switching eventually becomes fully implemented.
	
	public static int onAIFaint(Pokemon user, Pokemon opponent, Battlefield bf, Pokemon[] AIteam)
	{
		int index = -2;
		float approxEffectiveness = 1f;
		float finalEffectiveness = 0f;
		
		for (int i = 0; i < AIteam.length; i++)
		{
			if (AIteam[i].hasFainted)
			{
				//Move on.
			}
			else if (index == -2)
			{
				index = i;
			}
			else
			{
				
				/*
				 * Will look at moves as well as types soon enough. Also EVs, natures, IVs, opponent projected stats, etc.
				 
				
				if (Pokemon.getEffectiveness(opponent.type1, user.type2) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type1, user.type2);
				if (Pokemon.getEffectiveness(opponent.type1, user.type1) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type1, user.type1);
				if (Pokemon.getEffectiveness(opponent.type2, user.type1) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type2, user.type2);
				if (Pokemon.getEffectiveness(opponent.type2, user.type2) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type2, user.type2);
				
				if (approxEffectiveness == finalEffectiveness && Math.random() < .5)
				{
					finalEffectiveness = approxEffectiveness;
					index = i;
				}
				if (approxEffectiveness > finalEffectiveness)
				{
					finalEffectiveness = approxEffectiveness;
					index = i;
				}
			}
		}
			
		return index;
	}
*/
	
	public static int trainer;
	public static Battlefield bf;
	
	public int[][][] stats = new int[6][6][5];
	
	ArrayList<Integer> ids = new ArrayList<Integer>();
	
	private final int BASE = 0;
	private final int IV = 1;
	private final int EV = 2;
	private final int STAT = 3;
	private final int NATURE = 4;
	
	private final int HP = 0;
	private final int ATTACK = 1;
	private final int DEFENSE = 2;
	private final int SP_ATK = 3;
	private final int SP_DEF = 4;
	private final int SPEED = 5;
	
	private final int EVOLVEDFROM = 14;
	
	public TrainerAI(int num)
	{
		if (num <= 1)
			trainer = 1;
		else
			trainer = num;
		
		bf = new Battlefield(GUIBattle.team1, GUIBattle.team2);
		
		for (int i = 0; i < stats.length; i++)
			for (int j = 0; j < stats[i].length; j++)
				for (int k = 0; k < stats[i][j].length; k++)
					stats[i][j][k] = 0;
		
		generateAITeam();
	}
	
	public void generateAITeam()
	{
		//if (trainer == 1)
			generateAI1();
		//else
			//generateAI2();
	}
	
	public void generateAI1()
	{
		GUIBattle.team2 = new Pokemon[6];
		for (int i = 0; i < 6; i++)
		{
			int id = (int) Math.ceil(Math.random()*649);
			while (isOnTeam(ids, id) || id == 0)
				id = (int) Math.ceil(Math.random()*649);
			Pokemon temp = new Pokemon(id);
			determineStats(i, temp);
			determineNature(i, temp);
			setStats(i, temp);
			setInformation(i, temp);
			setMoves(temp);
			GUIBattle.team2[i] = temp;
			ids.add(id);
		}
	}
	
	public void generateAI2()
	{
		/*
		 * Realism goes here
		 */
	}
	
	public boolean isOnTeam(ArrayList<Integer> ids, int id)
	{
		if (ids.isEmpty())
			return false;
		for (int i = 0; i < ids.size(); i++)
		{
			if (ids.get(i)==id || ids.get(i)==SQLSource.getInt(id, EVOLVEDFROM, "Pokemon"))
				return true;
			if (SQLSource.getInt(id, EVOLVEDFROM, "Pokemon") != 0
					&& ids.get(i)==SQLSource.getInt(SQLSource.getInt(id, EVOLVEDFROM, "Pokemon"),EVOLVEDFROM,"Pokemon"))
				return true;
		}
		return false;
	}
	
	public void determineStats(int teamMember, Pokemon poke)
	{	
		stats[teamMember][HP][BASE] = poke.baseHP;
		stats[teamMember][ATTACK][BASE] = poke.baseAtk;
		stats[teamMember][DEFENSE][BASE] = poke.baseDef;
		stats[teamMember][SP_ATK][BASE] = poke.baseSpAtk;
		stats[teamMember][SP_DEF][BASE] = poke.baseSpDef;
		stats[teamMember][SPEED][BASE] = poke.baseSpd;
		
		stats[teamMember][HP][IV] = 31;
		stats[teamMember][ATTACK][IV] = 31;
		stats[teamMember][DEFENSE][IV] = 31;
		stats[teamMember][SP_ATK][IV] = 31;
		stats[teamMember][SP_DEF][IV] = 31;
		stats[teamMember][SPEED][IV] = 31;
		
		stats[teamMember][HP][EV] = 88;
		stats[teamMember][ATTACK][EV] = 84;
		stats[teamMember][DEFENSE][EV] = 84;
		stats[teamMember][SP_ATK][EV] = 84;
		stats[teamMember][SP_DEF][EV] = 84;
		stats[teamMember][SPEED][EV] = 84;
	}
	
	public void determineNature(int teamMember, Pokemon poke)
	{
		stats[teamMember][0][NATURE] = (int) Math.floor(Math.random());
	}
	
	public void setStats(int teamMember, Pokemon poke)
	{
		poke.nature = Nature.getFromId(stats[teamMember][0][NATURE]);
		
		stats[teamMember][HP][STAT] = poke.Statinate(stats[teamMember][HP][IV], stats[teamMember][HP][EV], 
													 stats[teamMember][HP][BASE], "HP");
		stats[teamMember][ATTACK][STAT] = poke.Statinate(stats[teamMember][ATTACK][IV], stats[teamMember][ATTACK][EV], 
														 stats[teamMember][ATTACK][BASE], "Attack");
		stats[teamMember][DEFENSE][STAT] = poke.Statinate(stats[teamMember][DEFENSE][IV], stats[teamMember][DEFENSE][EV], 
														  stats[teamMember][DEFENSE][BASE], "Defense");
		stats[teamMember][SP_ATK][STAT] = poke.Statinate(stats[teamMember][SP_ATK][IV], stats[teamMember][SP_ATK][EV], 
														 stats[teamMember][SP_ATK][BASE], "Special Attack");
		stats[teamMember][SP_DEF][STAT] = poke.Statinate(stats[teamMember][SP_DEF][IV], stats[teamMember][SP_DEF][EV], 
													     stats[teamMember][SP_DEF][BASE], "Special Defense");
		stats[teamMember][SPEED][STAT] = poke.Statinate(stats[teamMember][SPEED][IV], stats[teamMember][SPEED][EV], 
													  stats[teamMember][SPEED][BASE], "Speed");
	}
	
	public void setInformation(int teamMember, Pokemon poke)
	{
		poke.HPIV = stats[teamMember][HP][IV];
		poke.atkIV = stats[teamMember][ATTACK][IV];
		poke.defIV = stats[teamMember][DEFENSE][IV];
		poke.spAtkIV = stats[teamMember][SP_ATK][IV];
		poke.spDefIV = stats[teamMember][SP_DEF][IV];
		poke.spdIV = stats[teamMember][SPEED][IV];
		
		poke.HPEV = stats[teamMember][HP][EV];
		poke.atkEV = stats[teamMember][ATTACK][EV];
		poke.defEV = stats[teamMember][DEFENSE][EV];
		poke.spAtkEV = stats[teamMember][SP_ATK][EV];
		poke.spDefEV = stats[teamMember][SP_DEF][EV];
		poke.spdEV = stats[teamMember][SPEED][EV]; 
		
		poke.maxHP = stats[teamMember][HP][STAT];
		poke.currentHP = stats[teamMember][HP][STAT];
		poke.atk = stats[teamMember][ATTACK][STAT];
		poke.def = stats[teamMember][DEFENSE][STAT];
		poke.spAtk = stats[teamMember][SP_ATK][STAT];
		poke.spDef = stats[teamMember][SP_DEF][STAT];
		poke.spd = stats[teamMember][SPEED][STAT];
		
		poke.calculateEffectiveSpeed();
	}
	
	public void setMoves(Pokemon poke)
	{
		do
		{
			poke.FillMovepool(poke.id);
			poke.attack1=new Move(poke.Movepool.get((int) (poke.Movepool.size()*Math.random())));
			poke.attack2=new Move(poke.Movepool.get((int) (poke.Movepool.size()*Math.random())));
			poke.attack3=new Move(poke.Movepool.get((int) (poke.Movepool.size()*Math.random())));
			poke.attack4=new Move(poke.Movepool.get((int) (poke.Movepool.size()*Math.random())));
		} while(!poke.checkMoves());
	}
	
	public static Move executeAI()
	{
		boolean fainted = false;
		int attackid = 0;
		Move AttackSelected = null;
		
		attackid = (int) Math.random()*4;
		
		if(attackid == 0)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack1;
		else if(attackid == 1)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack2;
		else if(attackid == 2)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack3;
		else if(attackid == 3)
			AttackSelected = GUIBattle.team2[GUIBattle.activeEnemy].attack4;
		
		if (AttackSelected == null)
			return GUIBattle.team2[GUIBattle.activeEnemy].attack1;
		
		return AttackSelected;
	}
	
	public static int onAIFaint(Pokemon user, Pokemon opponent, Battlefield bf, Pokemon[] AIteam)
	{
		int index = -2;
		float approxEffectiveness = 1f;
		float finalEffectiveness = 0f;
		
		for (int i = 0; i < AIteam.length; i++)
		{
			if (AIteam[i].hasFainted)
			{
				//Move on.
			}
			else if (index == -2)
			{
				index = i;
			}
			else
			{
				
				/*
				 * Will look at moves as well as types soon enough. Also EVs, natures, IVs, opponent projected stats, etc.
				 */
				
				if (Pokemon.getEffectiveness(opponent.type1, user.type2) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type1, user.type2);
				if (Pokemon.getEffectiveness(opponent.type1, user.type1) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type1, user.type1);
				if (Pokemon.getEffectiveness(opponent.type2, user.type1) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type2, user.type2);
				if (Pokemon.getEffectiveness(opponent.type2, user.type2) != 0)
					approxEffectiveness *= Pokemon.getEffectiveness(opponent.type2, user.type2);
				
				if (approxEffectiveness == finalEffectiveness && Math.random() < .5)
				{
					finalEffectiveness = approxEffectiveness;
					index = i;
				}
				if (approxEffectiveness > finalEffectiveness)
				{
					finalEffectiveness = approxEffectiveness;
					index = i;
				}
			}
		}
			
		return index;
	}
}