package com.games.pokedroid.server.game;

import java.util.Random;

import com.games.pokedroid.game.Pokemon.Item;
import com.games.pokedroid.server.MasterServerThread;

public class Attack
{
	
	Random gen = new Random();
	
	
	public static Move userLastUsedMove=null;
	public static Move lastUsedMove=null;
	public static Move opponentLastUsedMove=null;
	
	private static boolean isUserAttacking=false;
	
    public static int whoGoesFirst; //0 is user, 1 is AI
    
    public static boolean attackHits=false;
    public static boolean effectHits=false;
    
    public static boolean attackIsSleepTalk=false;
    
    public static int switchTarget=-1;
    public static final long MESSAGE_DELAY=1500;
    
		
    public Attack()
    {
    	
    }
    
	/**
	 * Starts a new phase of battle.
	 * @param c Context of the calling Activity for different purposes
	 * @param userAttack Move used by the player
	 * @param opponentAttack Move used by the opponent
	 * @param user Pokemon player is currently using
	 * @param yourteam player's team
	 * @param opponent Pokemon the AI is currently using
	 * @param opponentteam AI's team
	 * @param battlefield Battlefield in use
	 */
	public void attackPhase(Move userAttack, Move opponentAttack, Pokemon user, Pokemon[] yourteam,  Pokemon opponent, Pokemon[] opponentteam, Battlefield b){

        
        //This segment of code determines if the user or the AI will act first
		if(userAttack.isSwitch&&opponentAttack.isSwitch){
			if(user.effectiveSpeed>opponent.effectiveSpeed){
				isUserAttacking=true;
				switchPokemon(user, yourteam, userAttack.switchTarget, b, 1);
	    		user=MasterServerThread.team1[MasterServerThread.activeAlly];
	            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	            yourteam=MasterServerThread.team1;
	            opponentteam=MasterServerThread.team2;
	        	isUserAttacking=false;
				switchPokemon(opponent, opponentteam, opponentAttack.switchTarget,b, 2);
			}
			else{
				isUserAttacking=false;
				switchPokemon(opponent, opponentteam, opponentAttack.switchTarget,b, 2);
				user=MasterServerThread.team1[MasterServerThread.activeAlly];
	            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	            yourteam=MasterServerThread.team1;
	            opponentteam=MasterServerThread.team2;
	        	isUserAttacking=true;
				switchPokemon(user, yourteam, userAttack.switchTarget, b, 1);

			}
		}
		else if(userAttack.isSwitch){
			if(opponentAttack.effect==129){//Pursuit check
				isUserAttacking=false;
				//message(opponent.name+" used "+opponentAttack.name+"!");
				BattlePhase(opponentAttack, opponent,user, b, opponentteam, yourteam);
	    		user=MasterServerThread.team1[MasterServerThread.activeAlly];
	            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	            yourteam=MasterServerThread.team1;
	            opponentteam=MasterServerThread.team2;
			}
			isUserAttacking=true;
			if(!user.hasFainted)
			switchPokemon(user, yourteam, userAttack.switchTarget, b, 1);
			if(opponentAttack.effect!=129){
				isUserAttacking=false;
				////message(opponent.name+" used "+opponentAttack.name+"!");
	    		user=MasterServerThread.team1[MasterServerThread.activeAlly];
	            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	            yourteam=MasterServerThread.team1;
	            opponentteam=MasterServerThread.team2;
				BattlePhase(opponentAttack, opponent, user, b, opponentteam, yourteam);
			}
		}
		else if(opponentAttack.isSwitch){
			if(userAttack.effect==129){
				isUserAttacking=true;
				//message(user.name+" used "+userAttack.name+"!");
				BattlePhase(userAttack,user,opponent, b, yourteam, opponentteam);
	    		user=MasterServerThread.team1[MasterServerThread.activeAlly];
	            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	            yourteam=MasterServerThread.team1;
	            opponentteam=MasterServerThread.team2;
			}
			isUserAttacking=false;
			if(!opponent.hasFainted)
			switchPokemon(opponent, opponentteam, opponentAttack.switchTarget,b, 2);
			if(userAttack.effect!=129){
				isUserAttacking=true;
				//message(user.name+" used "+userAttack.name+"!");
	    		user=MasterServerThread.team1[MasterServerThread.activeAlly];
	            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	            yourteam=MasterServerThread.team1;
	            opponentteam=MasterServerThread.team2;
			
				BattlePhase(userAttack, user, opponent, b, yourteam, opponentteam);
			}
		}
		else{
        if(userAttack.getPriority()>opponentAttack.getPriority())
        	whoGoesFirst=0;
        else if(opponentAttack.getPriority()>userAttack.getPriority())
        	whoGoesFirst=1;
        else if(user.effectiveSpeed>opponent.effectiveSpeed)
        	whoGoesFirst=0;
        else if(opponent.spd>user.spd)
        	whoGoesFirst=1;
        else
        	if(Math.random()>=.5)
        		whoGoesFirst=1;
        	else
        		whoGoesFirst=0;
        
       
        if(whoGoesFirst==0){
        	
        	isUserAttacking=true;
        	
        	//message(user.name+" used "+userAttack.name+"!");
        	
        	BattlePhase(userAttack, user, opponent, b, yourteam, opponentteam);
    		
        	isUserAttacking=false;
    		userLastUsedMove=userAttack;
    		lastUsedMove=userAttack;
           
    		user=MasterServerThread.team1[MasterServerThread.activeAlly];
            opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
            yourteam=MasterServerThread.team1;
            opponentteam=MasterServerThread.team2;
    		
            if(!opponent.hasFainted)
    		{
    			//message(opponent.name+" used "+opponentAttack.name+"!");
            	
    			BattlePhase(opponentAttack, opponent, user, b, opponentteam, yourteam);
    			
    			opponentLastUsedMove=opponentAttack;
    			lastUsedMove=opponentAttack;
    		}

        	
        }
       
        else
        {
	        isUserAttacking=false;
        	//message(opponent.name+" used "+opponentAttack.name+"!");
        	
	        BattlePhase(opponentAttack, opponent, user, b, opponentteam, yourteam);
			opponentLastUsedMove=opponentAttack;
			lastUsedMove=opponentAttack;
	        
			user=MasterServerThread.team1[MasterServerThread.activeAlly];
	        opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
	        yourteam=MasterServerThread.team1;
	        opponentteam=MasterServerThread.team2;
	        
			if(!user.hasFainted)
			{
				isUserAttacking=true;
				
	        	//message(user.name+" used "+userAttack.name+"!");
	        	
				BattlePhase(userAttack, user, opponent, b, yourteam, opponentteam);
				userLastUsedMove=userAttack;
				lastUsedMove=userAttack;
				isUserAttacking=false;
			}
        }
 	}
       
        user=MasterServerThread.team1[MasterServerThread.activeAlly];
        opponent=MasterServerThread.team2[MasterServerThread.activeEnemy];
        yourteam=MasterServerThread.team1;
        opponentteam=MasterServerThread.team2;
        
        endPhase(user, opponent, b, yourteam, opponentteam);
        }
        	
       
	
	/**
	 * Runs the attack logic. Does not count order of attack and such; you have to do that yourself.
	 * @param attackID the ID of the attack being used
	 * @param user Pokemon using the attack
	 * @param target the other Pokemon on the field
	 * @param bf Battlefield being used
	 * @return Pokemon after this attack in the form {user, target}. These should be responsibly written to the correct variables so the next phase of battle will work right.
	 */
	private void BattlePhase(Move move, Pokemon user, Pokemon target, Battlefield bf, Pokemon[] userTeam, Pokemon[] targetTeam)
	{
		int damage;
		attackHits=accuracyRoll(move, user, target, bf);
		effectHits=effectRoll(move, user, target);
		boolean crit=critRoll(move, user, target);
		if(user.isAsleep&&user.sleepCount==user.wakeCount){
			user.isAsleep=false;
			message(user.name + " woke up!");
		}
		if(user.isFrozen&&Math.random()<.2){
			user.isFrozen=false;
			message(user.name + " unfroze!");
			
		}
		if(user.isAsleep&&(move.effect!=214||move.effect!=93)){
			message(user.name + " is asleep");
		}
		else if (user.isFrozen){
			message(user.name + " is frozen!");
		}
		else{
			if(user.isRecharging){
				message(user.name + " is recharging!");
			}
			else{
			if(user.isConfused&&user.confusedCount==user.unConfuseCount){
				message(user.name+ " is no longer confused!");
				user.confusedCount=0;
				user.unConfuseCount=0;
				user.isConfused=false;
			}
			else if (user.isConfused){
				message(user.name+ " is confused!");
			}
			 
			if(!user.isConfused||(user.isConfused&&Math.random()<.5)){
				if(!user.isInfatuated||(user.isInfatuated&&Math.random()<.5)){
					if(user.isInfatuated)
					message(user.name + " is in love with " + target.name + ".");
					if(!user.isParalyzed||(user.isParalyzed&&Math.random()<.5)){
						if(!user.isCharging&&move.moveFlags.contains(new Integer(2))){
							message(user.name + " is charging an attack!");
							user.isCharging=true;
						}
						else{
							
						
						message(user.name+" used "+move.name+"!");
						if(!attackHits||(Pokemon.getEffectiveness(move.type, target.type1)*Pokemon.getEffectiveness(move.type, target.type2)==0&&move.moveTarget==1)){
							if(move.effect==46){
								user.damage(computeDamage(move, user, target, bf, false));
								message(user.name+" kept going and crashed!");
							}
							if(attackHits&&Pokemon.getEffectiveness(move.type, target.type1)*Pokemon.getEffectiveness(move.type, target.type2)==0)
								message("It doesn't affect " + target.name+"...");
						}
						else{
							if(move.DamageType==move.NONDAMAGING){
								switch(move.effect){
									
									case 2:{}
									case 3:{}
									case 5:{}
									case 6:{}
									case 34:{}
									case 50:{}
									case 67:{}
									case 68:{
										StatusChange(move, target, effectHits);
										break;
									}
									
					
									case 10:{
										if(((whoGoesFirst==1&&isUserAttacking)||(whoGoesFirst==0&&!isUserAttacking))&&lastUsedMove.moveFlags.contains(7)){
											move=lastUsedMove;
											message(user.name+ " used " + move.name + "!");
											BattlePhase(move, user, target, bf, userTeam, targetTeam);
											return;
											
										}
										else{
											message("But it failed!");
											
												
												if(user.attack1.compareTo(new Move(10))==0)
													user.attack1.CurrentPP--;
												if(user.attack2.compareTo(new Move(10))==0)
													user.attack2.CurrentPP--;
												if(user.attack3.compareTo(new Move(10))==0)
													user.attack3.CurrentPP--;
												if(user.attack4.compareTo(new Move(10))==0)
													user.attack4.CurrentPP--;
												
												if(isUserAttacking){
													MasterServerThread.team1=userTeam;
													MasterServerThread.team2=targetTeam;
													MasterServerThread.team1[MasterServerThread.activeAlly]=user;
													MasterServerThread.team2[MasterServerThread.activeEnemy]=target;
												}
												else{
													MasterServerThread.team1=targetTeam;
													MasterServerThread.team2=userTeam;
													MasterServerThread.team1[MasterServerThread.activeEnemy]=target;
													MasterServerThread.team2[MasterServerThread.activeAlly]=user;
												}
												
											return;
										}
										
									}
									case 11:{}
									case 12:{}
									case 13:{}
									case 14:{}
									case 15:{}
									case 16:{}
									case 17:{}
									case 51:{}
									case 52:{}
									case 53:{}
									case 54:{}
									case 55:{}
									case 56:{}
									case 57:{
										StatLevelChange(move.effect, user);
										break;
									}
									case 19:{}
									case 20:{}
									case 21:{}
									case 22:{}
									case 23:{}
									case 24:{}
									case 25:{}
									case 59:{}
									case 60:{}
									case 61:{}
									case 62:{}
									case 63:{}
									case 64:{}
									case 65:{}
									case 69:{}
									case 70:{}
									case 71:{}
									case 72:{}
									case 73:{}
									case 74:{}
									case 75:{
										StatLevelChange(move.effect, target);
										break;
									}
									case 26:{
										user.resetStats();
										target.resetStats();
										message("All stat changes returned to normal!");
										break;
									}
									case 31:{
										double ConversionRandom=Math.random();
										if(ConversionRandom>=.75){
											user.changeType(user.attack1.type);
											message(user.name+"'s type changed to " + user.attack1.type + "!");
										}
										else if(ConversionRandom>=.5){
											user.changeType(user.attack2.type);
											message(user.name+"'s type changed to " + user.attack1.type + "!");
										}
										else if(ConversionRandom>=.25){
											user.changeType(user.attack3.type);
											message(user.name+"'s type changed to " + user.attack1.type + "!");
										}
										else{
											user.changeType(user.attack4.type);
											message(user.name+"'s type changed to " + user.attack1.type + "!");
										}
										break;
									}
									case 33:{
										user.heal(user.maxHP/2);
										break;
									}
									case 38:{
										if(user.isAsleep)
											message("But it failed!");
										else{
										user.heal(user.maxHP);
										message(user.name+ " recovered all its health!");
										user.changeStatus(2);
										message(user.name+" went to sleep!");
										}
										break;
									}
									case 48:{
										if(user.isFocused!=true){
											user.isFocused=true;
											message(user.name+" focused its energy!");
										}
										else{
											user.isFocused=true;
											message(user.name+" is already focused!");
										}
										break;
									}
									case 58:{
										user=new Pokemon(user, target);
										message(user.name+ " transformed into" + target.name+"!");
										break;
									}
									case 66:{
										if(isUserAttacking&&Battlefield.REFLECT==false){
											message(user.name + " put up a barrier!");
											Battlefield.REFLECT=true;
										}
										else if(!isUserAttacking&&Battlefield.OPPONENTREFLECT==false){
											message(user.name+ " put up a barrier!");
											Battlefield.OPPONENTREFLECT=true;
										}
										else{
											message("But it failed!");
										}
										break;
									}
									case 113:{
										if(isUserAttacking)
											if(Battlefield.OPPONENTSPIKES==3)
												message("But it failed!");
											else{
												message(user.name + " laid down a layer of spikes!");
												Battlefield.OPPONENTSPIKES++;
											}
										else
											if(Battlefield.SPIKES==3)
												message("But it failed!");
											else{
												message(user.name + " laid down a layer of spikes!");
												Battlefield.SPIKES++;
											}
										break;
									}
									case 116:{
										message("A sandstorm brews!");
										Battlefield.weather=Battlefield.SANDSTORM;
										if(user.heldItem==Item.SMOOTH_ROCK)
											Battlefield.weatherCount=8;
										else
											Battlefield.weatherCount=5;
										break;
									}
									case 121:{
										if(effectHits)
											if(user.gender==1&&target.gender==2||user.gender==2&&target.gender==1){
												target.changeStatus(move.effect);
												message(target.name+ " was attracted to " + user.name + "!");
											}
											else
												message("But it failed!");
										else
											message("But it failed!");
										break;
												
									}
									case 137:{
										message("Rain begins to fall!");
										Battlefield.weather=Battlefield.RAIN;
										if(user.heldItem==Item.DAMP_ROCK)
											Battlefield.weatherCount=8;
										else
											Battlefield.weatherCount=5;
										break;
									}
									case 138:{
										message("The sun begins to shine brightly!");
										Battlefield.weather=Battlefield.SUNNY;
										if(user.heldItem==Item.HEAT_ROCK)
											Battlefield.weatherCount=8;
										else
											Battlefield.weatherCount=5;
										break;
									}
									case 165:{
										message("Hail begins to fall!");
										Battlefield.weather=Battlefield.HAIL;
										if(user.heldItem==Item.ICY_ROCK)
											Battlefield.weatherCount=8;
										else
											Battlefield.weatherCount=5;
										break;
									}
									case 214:{
										if(user.isAsleep){
											double sleepTalkCheck=Math.random();
											boolean work=false;
											attackIsSleepTalk=false;
											while(!work){
												if(sleepTalkCheck<.25&&move.compareTo(user.attack1)!=0){
													BattlePhase((Move) user.attack1, user, target, bf, userTeam, targetTeam );
													work=true;
												}
												else if(sleepTalkCheck<.5&&move.compareTo(user.attack2)!=0){
													BattlePhase((Move) user.attack2, user, target, bf, userTeam, targetTeam );
													work=true;
												}
												else if(sleepTalkCheck<.75&&move.compareTo(user.attack3)!=0){
													BattlePhase((Move) user.attack3, user, target, bf, userTeam, targetTeam );
													work=true;
												}
												else{
													BattlePhase((Move) user.attack4, user, target, bf, userTeam, targetTeam );
													work=true;
												}
											}
											attackIsSleepTalk=false;
										}
										else{
											message("But it failed!");
										}
										
										break;
											
									}
									case 250:{
										if(isUserAttacking)
											if(Battlefield.OPPONENTTOXICSPIKES==2)
												message("But it failed!");
											else{
												message(user.name+ " laid down a layer of Toxic Spikes!");
												Battlefield.OPPONENTTOXICSPIKES++;
											}
										else
											if(Battlefield.TOXICSPIKES==2)
												message("But it failed!");
											else{
												message(user.name+ " laid down a layer of Toxic Spikes!");
												Battlefield.TOXICSPIKES++;
											}
										break;
									}
									case 267:{
										if(isUserAttacking)
											if(Battlefield.OPPONENTSTEALTHROCKS)
												message("But it failed!");
											else{
												message(user.name + " laid down rocks!");
												Battlefield.OPPONENTSTEALTHROCKS=true;
											}
										else
											if(Battlefield.STEALTHROCKS)
												message("But it failed!");
											else{
												message(user.name + " laid down rocks!");
												Battlefield.STEALTHROCKS=true;
											}
										break;
									}
								}
							}
						
						//If the move is not a non-damaging move, it will be sent here.
						else{
						
							if(target.hasFainted){
								message("But there was not target!");
							}
						
							else{
								damage=computeDamage(move, user, target, bf, crit);
								if(Pokemon.getEffectiveness(move.type, target.type1)*Pokemon.getEffectiveness(move.type, target.type2)>1)
									message("It's super effective!");
								if(Pokemon.getEffectiveness(move.type, target.type1)*Pokemon.getEffectiveness(move.type, target.type2)<1)
									message("It's not very effective...");
								if(isUserAttacking)
									message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
								else
									message(target.name+" lost " + damage + " HP!");
								
								target.damage(damage);
								
								switch(move.effect){
									case 2:{}
									case 3:{}
									case 5:{}
									case 6:{}
									case 7:{}
									case 50:{}
									case 77:{
										StatusChange(move, target, effectHits);
										break;
									}
									case 4:{
										user.heal(damage/2);
										break;
									}
									case 9:{
										if(target.isAsleep)
											user.heal(damage/2);
										break;
									}
									case 10:{
										if(((whoGoesFirst==1&&isUserAttacking)||(whoGoesFirst==0&&!isUserAttacking))&&lastUsedMove.moveFlags.contains(7)){
											move=lastUsedMove;
											BattlePhase(move, user, target, bf, userTeam, targetTeam);
											return;
											
										}
										break;
									}
									case 11:{}
									case 12:{}
									case 13:{}
									case 14:{}
									case 15:{}
									case 16:{}
									case 17:{}
									case 51:{}
									case 52:{}
									case 53:{}
									case 54:{}
									case 55:{}
									case 56:{}
									case 57:{
										StatLevelChange(move.effect, user);
										break;
									}
									case 19:{}
									case 20:{}
									case 21:{}
									case 22:{}
									case 23:{}
									case 24:{}
									case 25:{}
									case 59:{}
									case 60:{}
									case 61:{}
									case 62:{}
									case 63:{}
									case 64:{}
									case 65:{}
									case 69:{}
									case 70:{}
									case 71:{}
									case 72:{}
									case 73:{}
									case 74:{}
									case 75:{
										StatLevelChange(move.effect, target);
										break;
									}
									case 26:{
										user.resetStats();
										target.resetStats();
										message("All stat changes returned to normal!");
										break;
									} 
									case 27:{
										user.bideLock=0;
										user.isBideLocked=false;
										break;
									}
									case 28:{
										if(!user.isThrashLocked){
											user.isThrashLocked=true;
											user.thrashBreak=(int)Math.ceil(Math.random()*2)+1;
											user.thrashLockCount++;
											message(user.name + " is thrashing about!");
										}
										else if(user.thrashLockCount!=user.thrashBreak){
											user.thrashLockCount++;
											message(user.name + " is thrashing about!");
										}
										else{
											user.isThrashLocked=false;
											user.thrashBreak=0;
											user.thrashLockCount=0;
											message(user.name + " calmed down.");
											StatusChange(move, user, true);
										}
										break;
									}
									case 30:{
										if(!user.hasFainted){
											int x = (int)(Math.ceil(Math.random()*1000));
											if(x<375){
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												message("The attack hit two times!");
											}
											else if(x<750){
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												message("The attack hit three times!");
											}
											else if(x<875){
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												message("The attack hit four times!");
											}
											else{
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												target.damage(damage);
												message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
												message("The attack hit five times!");
											}
										}
										break;
									}
									case 43:{
										if(!target.isWrapped){
										target.isWrapped=true;
										target.wrapBreak=(int)Math.random()*3+2;
										message(target.name + " is trapped by " + move.name  + "!");
										}
										break;
									}
									case 45:{
										if(!user.hasFainted){
											target.damage(damage);
											message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
											message("The attack hit twice!");
										}
										break;
									}
									case 49:{
										if(user.chosenAbility!=Pokemon.ROCK_HEAD)
										user.damage(damage/4);
										break;
									}
									case 78:{
										if(!user.hasFainted){
											target.damage(damage);
											message(target.name+" lost " + (int)(Math.ceil((100*damage/target.maxHP))) + "% of its health!");
											message("The attack hit twice!");
											StatusChange(move, target, effectHits);
										}
										break;
									}
									case 130:{
										if(isUserAttacking){
											if(Battlefield.TOXICSPIKES>0)
												Battlefield.TOXICSPIKES=0;
											if(Battlefield.SPIKES>0)
												Battlefield.SPIKES=0;
											if(Battlefield.STEALTHROCKS)
												Battlefield.STEALTHROCKS=false;
											if(Battlefield.STEALTHROCKS||Battlefield.SPIKES>0||Battlefield.TOXICSPIKES>0)
												message("Cleared away hazards!");
										}
										else{
											if(Battlefield.OPPONENTTOXICSPIKES>0)
												Battlefield.OPPONENTTOXICSPIKES=0;
											if(Battlefield.OPPONENTSPIKES>0)
												Battlefield.OPPONENTSPIKES=0;
											if(Battlefield.OPPONENTSTEALTHROCKS)
												Battlefield.OPPONENTSTEALTHROCKS=false;
											if(Battlefield.OPPONENTSTEALTHROCKS||Battlefield.OPPONENTSPIKES>0||Battlefield.OPPONENTTOXICSPIKES>0)
												message("Cleared away hazards!");
										}
										break;
									}
								}
								if(move.moveFlags.contains(new Integer(3)))
									user.isRecharging=true;
							}
						}
						
							if(move.effect==8){
								message(user.name + " exploded!");
								user.damage(user.maxHP);
							}
							if(!attackIsSleepTalk)
								user.confusedCount++;	
						}
						}
					}
					else{
						message(user.name+ " is fully paralyzed!");
						if(user.isThrashLocked){
							user.isThrashLocked=false;
							user.thrashBreak=0;
							user.thrashLockCount=0;
							user.isCharging=false;
						}
					}
				}
				else{
					message(user.name+ " won't attack the foe!");
					if(user.isThrashLocked){
						user.isThrashLocked=false;
						user.thrashBreak=0;
						user.thrashLockCount=0;
						user.isCharging=false;
					}
				}
			}
			else{
				message(user.name + " hurt itself in its confusion!");
				user.damage(computeDamage(new Move(10019), user, target, bf, false));
				if(user.isThrashLocked){
					user.isThrashLocked=false;
					user.thrashBreak=0;
					user.thrashLockCount=0;
					user.isCharging=false;
				}
					
			}
			}
		}
			
			if(isUserAttacking){
				MasterServerThread.team1=userTeam;
				MasterServerThread.team2=targetTeam;
				MasterServerThread.team1[MasterServerThread.activeAlly]=user;
				MasterServerThread.team2[MasterServerThread.activeEnemy]=target;
			}
			else{
				MasterServerThread.team1=targetTeam;
				MasterServerThread.team2=userTeam;
				MasterServerThread.team1[MasterServerThread.activeEnemy]=target;
				MasterServerThread.team2[MasterServerThread.activeAlly]=user;
			}
		}	
	
	/**
	 * This phase ends all turns by checking for the different effects caused by abilities, items, and weather
	 * @param user the User's currently active Pokemon
	 * @param opponent the Opponent's currently active Pokemon
	 * @param b the Battlefield that the two Pokemon are on
	 * @param yourteam the User's team
	 * @param opponentteam the Opponent's team
	 */
	private void endPhase(Pokemon user, Pokemon opponent, Battlefield b, Pokemon[] yourteam, Pokemon[] opponentteam){
		
		//If current weather is hail, this damages all pokemon that do not have ice as either of its types, and heals those with the ability Ice Body
		if(Battlefield.weather==Battlefield.HAIL){
			if(user.type1!=Pokemon.ICE&&user.type2!=Pokemon.ICE){
				user.damage(user.maxHP/16);
				message(user.name + " was buffeted by the hail!");
			}
			if(user.chosenAbility==Pokemon.ICE_BODY){
				user.heal(user.maxHP/8);
				message(user.name + "'s Ice Body healed it!");
			}
			if(opponent.type1!=Pokemon.ICE&&opponent.type2!=Pokemon.ICE){
				opponent.damage(opponent.maxHP/16);
				message(opponent.name + " was buffeted by the hail!");
			}
			if(opponent.chosenAbility==Pokemon.ICE_BODY){
				opponent.heal(opponent.maxHP/8);
				message(opponent.name + "'s Ice Body healed it!");
			}
		
		}
		
		//If current weather is sandstorm, this damages all pokemon that are not Rock, Ground, or Steel Typed
		if(Battlefield.weather==Battlefield.SANDSTORM){
			if(user.type1!=Pokemon.ROCK&&user.type2!=Pokemon.ROCK&&user.type1!=Pokemon.GROUND&&user.type2!=Pokemon.GROUND&&user.type1!=Pokemon.STEEL&&user.type2!=Pokemon.STEEL){
				user.damage(user.maxHP/16);
				message(user.name + " was buffeted by the sandstorm!");
			}
			if(opponent.type1!=Pokemon.ROCK&&opponent.type2!=Pokemon.ROCK&&opponent.type1!=Pokemon.GROUND&&opponent.type2!=Pokemon.GROUND&&opponent.type1!=Pokemon.STEEL&&opponent.type2!=Pokemon.STEEL){
				opponent.damage(opponent.maxHP/16);
				message(opponent.name + " was buffeted by the sandstorm!");
			}
			
			
		}
		if(!user.hasFainted){
			if(user.isBadlyPoisoned){
				user.damage(user.maxHP*user.toxicCount/16);
				user.toxicCount++;
				message(user.name+" was hurt by its poison!");
			}
			if(user.isBurned){
				user.damage(user.maxHP/8);
				message(user.name+" was hurt by its burn!");
			}
			if(user.isPoisoned){
				user.damage(user.maxHP/8);
				message(user.name+" was hurt by its poison!");
			}
			if(user.isAsleep){
				user.sleepCount++;
			}
			if(user.isThrashLocked){
				MasterServerThread.noChooseForYou();
			}
			if(user.isEncored){
				user.encoreLockCount++;
				if(user.encoreLockCount==user.encoreBreak){
					user.isEncored=false;
					user.encoreLockCount=0;
					message(user.name+"'s encore ended!");
				}
				else
					MasterServerThread.noChooseForYou();
			}
			if(user.isLeechSeeded){
				user.damage(user.maxHP/8);
				opponent.heal(user.maxHP/8);
				message(user.name+"'s HP was drained!");
			}
			if(user.isBideLocked)
				user.bideLock++;
			
			if(user.isWrapped){
				if(user.wrapCount==user.wrapBreak){
					message(user.name + " was freed from foe's hold!");
					user.isWrapped=false;
					user.wrapCount=0;
					user.wrapBreak=0;
				}
				else{
					user.wrapCount++;
					user.damage(user.maxHP/16);
					message(user.name + " was damaged by foe's hold!");
					MasterServerThread.noSwitchForYou();
				}
			}
			//if(user.heldItem==Item.CHOICE_BAND||user.heldItem==Item.CHOICE_SCARF||user.heldItem==Item.CHOICE_SPECS)
				//MasterServerThread.noChooseForYou();
		}
		
		
		if(!opponent.hasFainted){
			if(opponent.isBadlyPoisoned){
				opponent.damage(opponent.maxHP*opponent.toxicCount/16);
				opponent.toxicCount++;
				message(opponent.name+" was hurt by its poison!");
			}
			if(opponent.isBurned){
				opponent.damage(opponent.maxHP/8);
				message(opponent.name+" was hurt by its burn!");
			}
			if(opponent.isPoisoned){
				opponent.damage(opponent.maxHP/8);
				message(opponent.name+" was hurt by its poison!");
			}
			if(opponent.isAsleep){
				opponent.sleepCount++;
			}
			if(opponent.isEncored){
				opponent.encoreLockCount++;
				if(opponent.encoreLockCount==opponent.encoreBreak){
					opponent.isEncored=false;
					opponent.encoreLockCount=0;
					message(opponent.name+"'s encore ended!");
				}
			}
			if(opponent.isLeechSeeded){
				opponent.damage(opponent.maxHP/8);
				user.heal(opponent.maxHP/8);
				message(opponent.name+"'s HP was drained!");
			}
			if(opponent.isBideLocked)
				opponent.bideLock++;
			
			if(opponent.isWrapped){
				if(opponent.wrapCount==opponent.wrapBreak){
					message(opponent.name + " was freed from foe's hold!");
					opponent.isWrapped=false;
					opponent.wrapCount=0;
					opponent.wrapBreak=0;
				}
				else{
					opponent.wrapCount++;
					opponent.damage(opponent.maxHP/16);
					message(opponent.name + " was damaged by foe's hold!");
				}
			}
		}	

		
		if(user.hasFainted){
			boolean allFainted=true;
			for(int i = 0; i<yourteam.length; i++)
				if(yourteam[i].hasFainted&&allFainted)
					allFainted=true;
				else
					allFainted=false;
			if(allFainted){
				MasterServerThread.user1Won=false;
				MasterServerThread.hasSomeoneWon=true;
			}
			else{	
			int switchTo=MasterServerThread.onUser1Faint();
			switchPokemon(user,yourteam,switchTo,b, 1);
			}
		}
		
		if(opponent.hasFainted){
			int AISwitch=MasterServerThread.onUser2Faint();
			if(!(AISwitch==-2))	
			switchPokemon(opponent,opponentteam,AISwitch,b, 2);
			else{
				MasterServerThread.user1Won=true;
				MasterServerThread.hasSomeoneWon=true;
			}
		}
		
		if(Battlefield.weather!=0){
			Battlefield.weatherCount--;
			if(Battlefield.weatherCount!=0)
				switch(Battlefield.weather){
				case Battlefield.SUNNY:{
					message("The sun continues to shine.");
					break;
				}
				case Battlefield.RAIN:{
					message("The rain continues to fall.");
					break;
				}
				case Battlefield.SANDSTORM:{
					message("The sandstorm continues to blow.");
					break;
				}
				case Battlefield.HAIL:{
					message("The hail continues to fall.");
					break;
				}
				}
			else{
				switch(Battlefield.weather){
				case Battlefield.SUNNY:{
					message("The sun stopped shining.");
					break;
				}
				case Battlefield.RAIN:{
					message("The rain stopped falling.");
					break;
				}
				case Battlefield.SANDSTORM:{
					message("The sandstorm stopped.");
					break;
				}
				case Battlefield.HAIL:{
					message("The hail stopped falling.");
					break;
				}
				}
				Battlefield.weather=Battlefield.NOWEATHER;
			}
		}
			
		//MasterServerThread.team1[MasterServerThread.activeAlly]=user;
		//MasterServerThread.team2[MasterServerThread.activeEnemy]=opponent;
		//MasterServerThread.team1=yourteam;
		//MasterServerThread.team2=opponentteam;
		
		try {
			Thread.sleep(MESSAGE_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void switchPokemon(Pokemon switchee, Pokemon[] switchTeam, int switchTo, Battlefield bf, int side){
		
		switchee.resetOnSwitch();
		Pokemon temp = switchee;
		if(!switchee.hasFainted&&isUserAttacking)
		message(switchee.name+" come back!");
		if(!switchee.hasFainted&&!isUserAttacking)
			message("The opponent recalled " + switchee.name + "!");
		switchee=switchTeam[switchTo];
		switchTeam[0]=switchee;
		MasterServerThread.switchPokemon(side, switchTo);
		switchTeam[switchTo]=temp;
		if(side==1)
		message("Go, " + switchee.name+"!");
		if(side==2)
			message("Foe sent out " + switchee.name+"!");
		if(side==1){
			MasterServerThread.team1=switchTeam;
			MasterServerThread.team1[0]=switchee;
		}
		else{
			MasterServerThread.team2=switchTeam;
			MasterServerThread.team2[0]=switchee;
		}
		onSwitchIn(switchee, bf, side);
		
		if(side==1)
			MasterServerThread.team1=switchTeam;
		else
			MasterServerThread.team2=switchTeam;
	}
	
	private void onSwitchIn(Pokemon switchee, Battlefield bf, int side){
		if(side==1){
			if(Battlefield.STEALTHROCKS){
				switchee.damage((int)(switchee.maxHP*Pokemon.getEffectiveness(Pokemon.ROCK,switchee.type1)*Pokemon.getEffectiveness(Pokemon.ROCK,switchee.type2)/8));
				message(switchee.name+" was hurt by the Stealth Rocks!");
			}
			if(Battlefield.SPIKES>0){
				if(Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type1)*Pokemon.getEffectiveness(Pokemon.GROUND,switchee.type2)!=0){
					message(switchee. name + " was hurt by the spikes!");
					switchee.damage(switchee.maxHP/(10-(2*Battlefield.SPIKES)));
				}
					
			}
			if(Battlefield.TOXICSPIKES>0){
				if(switchee.type1==Pokemon.POISON||switchee.type2==Pokemon.POISON){
					message(switchee.name + " absorbed the toxic spikes");
					Battlefield.TOXICSPIKES=0;
				}
				if(Battlefield.TOXICSPIKES==1){
					if(Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type1)*Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type2)==0 || Pokemon.getEffectiveness(Pokemon.POISON, switchee.type1)*Pokemon.getEffectiveness(Pokemon.POISON, switchee.type2)==0){}
					else{
						switchee.changeStatus(3);
						message(switchee.name+ " was poisoned!");
					}
						
				}
				if(Battlefield.TOXICSPIKES==2){
					if(Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type1)*Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type2)==0 || Pokemon.getEffectiveness(Pokemon.POISON, switchee.type1)*Pokemon.getEffectiveness(Pokemon.POISON, switchee.type2)==0){}
					else
						switchee.changeStatus(34);
					message(switchee.name + " was badly poisoned!");
				}
			}
		}
			if(side==2){
				if(Battlefield.OPPONENTSTEALTHROCKS){
					switchee.damage((int)(switchee.maxHP*Pokemon.getEffectiveness(Pokemon.ROCK,switchee.type1)*Pokemon.getEffectiveness(Pokemon.ROCK,switchee.type2)/8));
					message(switchee.name+" was hurt by the Stealth Rocks!");
				}
				if(Battlefield.OPPONENTSPIKES>0){
					if(Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type1)*Pokemon.getEffectiveness(Pokemon.GROUND,switchee.type2)!=0){
						message(switchee. name + " was hurt by the spikes!");
						switchee.damage(switchee.maxHP/(10-(2*Battlefield.OPPONENTSPIKES)));
					}
						
				}
				if(Battlefield.OPPONENTTOXICSPIKES>0){
					if(switchee.type1==Pokemon.POISON||switchee.type2==Pokemon.POISON){
						message(switchee.name + " absorbed the toxic spikes");
						Battlefield.OPPONENTTOXICSPIKES=0;
					}
					if(Battlefield.OPPONENTTOXICSPIKES==1){
						if(Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type1)*Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type2)==0 || Pokemon.getEffectiveness(Pokemon.POISON, switchee.type1)*Pokemon.getEffectiveness(Pokemon.POISON, switchee.type2)==0){}
						else{
							switchee.changeStatus(3);
							message(switchee.name+ " was poisoned!");
						}
							
					}
					if(Battlefield.OPPONENTTOXICSPIKES==2){
						if(Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type1)*Pokemon.getEffectiveness(Pokemon.GROUND, switchee.type2)==0 || Pokemon.getEffectiveness(Pokemon.POISON, switchee.type1)*Pokemon.getEffectiveness(Pokemon.POISON, switchee.type2)==0){}
						else
							switchee.changeStatus(34);
						message(switchee.name + " was badly poisoned!");
					}
			}
		}
		
		 MasterServerThread.activeAlly=0;
	     MasterServerThread.activeEnemy=0;
		
		if(side==1){
			MasterServerThread.team1[MasterServerThread.activeAlly]=switchee;
		}
		else{
			MasterServerThread.team2[MasterServerThread.activeEnemy]=switchee;
		}
		
	}
	
	/**
	 * Rolls to see if a Pokemon's move hits or not.
	 * @param move attack that the Pokemon is using
	 * @param user Pokemon using the attack
	 * @param target Pokemon being targeted by the attack
	 * @param bf Battlefield for weather purposes and such
	 * @return whether the attack hits or not
	 */
	private boolean accuracyRoll(Move move, Pokemon user, Pokemon target, Battlefield bf){
		if(move.getAccuracy()==10000)
			return true;
		
		switch(move.effect){
		case 18:{}
		case 79:{
				return true;	
		}
		case 9:{
			if(!target.isAsleep){
			message("But it failed!");	
				return false;
			}
			break;
		}
		case 27:{
			if(user.bideLock!=2){
			message(user.name + " is storing energy!");
			user.isBideLocked=true;
				return false;
			}
			break;
		}
		case 39:{
			if(user.level<target.level){
				message("But it failed!");
				return false;
			}
			break;
		}
		}
		
		double p = move.getAccuracy();
		double mod=1;
		if(user.accLvl<=0)
			mod*=(3/(3+user.accLvl));
		if(user.accLvl>0)
			mod*=((3+user.accLvl)/3);
		if(user.evaLvl<=0)
			mod/=(3/(3+target.evaLvl));
		if(user.evaLvl>0)
			mod/=((3+target.evaLvl)/3);
		p*=mod;
		
		if(user.chosenAbility==Pokemon.COMPOUNDEYES)
			p+=30;
		
		if(move.effect==39)
			p=30+(user.level-target.level);
		
		if(Math.random()*100<=p)
			return true;
		
		message("But it missed!");
		return false;
	}
	
	/**
	 * Rolls the dice to see if a move's secondary effect hits. If the move's effect is supposed to always hit (10000 Accuracy) it will return true.
	 * @param move
	 * @param user
	 * @param target
	 * @return
	 */
	private boolean effectRoll(Move move, Pokemon user, Pokemon target){
		
		int accuracy = move.BaseEffectAccuracy;
		if(accuracy==10000)
			return true;
		if(user.chosenAbility==Pokemon.SERENE_GRACE)
			accuracy*=2;
		if(Math.random()*100<accuracy)
			return true;
		return false;
	}
	
	private boolean critRoll(Move move, Pokemon user, Pokemon target){
		int critlvl=1;
		double accuracy=0;
		
		if(move.effect==48)
			critlvl++;
		if(move.effect==40)
			critlvl++;
		switch(user.heldItem){
		case SCOPE_LENS:{
			critlvl++;
			break;
		}
		//case RAZOR_CLAW:{
			//critlvl++;
			//break;
		//}
		//case STICK:
		//if(user.id==83)
		//	critlvl+=2;
		//case LUCKY_PUNCH:
		//if(user.id==113)
			//critlvl+=2;
		}
		if(user.chosenAbility==Pokemon.SUPER_LUCK)
			critlvl++;
		if(user.isFocused)
			critlvl+=2;
		
		if(critlvl>5)
			critlvl=5;
		
		switch(critlvl){
		case 1:
			accuracy=6.25;
			break;
		case 2:
			accuracy=12.5;
			break;
		case 3:
			accuracy=25;
			break;
		case 4:
			accuracy=33.3;
			break;
		case 5:
			accuracy=50;
			break;
		}
		
		if(Math.random()*100<accuracy)
			return true;
		
		return false;
	}
	
	
	
	private boolean StatusChange(Move move, Pokemon target, boolean hits){
		if(!hits&&move.BaseEffectAccuracy!=10000){
			return false;
		}
		switch(move.effect){
		case(28):{}
		case 50:{
			if(target.isConfused&&move.effect!=28){
				message(target.name + " is already confused!");
				return false;
			}
			else{
				message(target.name + " became confused!");
				target.changeStatus(move.effect);
				return true;
			}
					
		}
		}
		
		
		if(target.hasStatus){
			if(move.BaseEffectAccuracy!=10000)
			message("But it failed!");
			return false;
		}
		

		target.changeStatus(move.effect);	
		String STATUS="hurr you're a durr";
		switch(move.effect){

			case 38:{}
			case 2: {
				STATUS="fell asleep!";
				break;
			}
			case 78:{}
			case 67:{}
			case 3: {
				STATUS="was poisoned!";
				break;
			}
			case 5:{
				STATUS="was burned!";
				break;
			}
			case 6:{
				STATUS="was frozen!";
				break;
			}
			case 68:{}
			case 7:{
				STATUS="is paralyzed!";
				break;
			}
			case 34:{
				STATUS="is badly poisoned!";
				break;
			}
			case 37:{
				if(Math.random()<(1/3))
					StatusChange(new Move(53), target, true);
				else if(Math.random()<(2/3))
					StatusChange(new Move(58),target,true);
				else
					StatusChange(new Move(85), target, true);
				return true;
				
			}
			
		}
		
		message(target.name+" " +STATUS);
	
		return true;
	}
	
	private boolean StatLevelChange(int effectNum, Pokemon target){
		switch(effectNum){
		case 11:{
			if (target.atkLvl==6)
				{
				message("But " + target.name+"'s Attack can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.ATTACK, 1);
				message(target.name+"'s Attack rose!");
				}
				else
					return false;
					break;
		}
		case 12:{
			if (target.defLvl==6)
			{
				message("But " + target.name+"'s Defense can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.DEFENSE, 1);
					message(target.name+"'s Defense rose!");
				}		
				else
					return false;
			break;
		}
		case 13:{
			if (target.spdLvl==6)
			{
				message("But " + target.name+"'s Speed can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SPEED, 1);
					message(target.name+"'s Speed rose!");
				}
				else
					return false;
			break;		
		}
		case 14:{
			if (target.spAtkLvl==6)
			{
				message("But " + target.name+"'s Special Attack can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_ATK, 1);
					message(target.name+"'s Special Attack rose!");
				}
				else
					return false;
			break;
		}
		case 15:{
			if (target.spDefLvl==6)
			{
				message("But " + target.name+"'s Special Defense can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_DEF, 1);
					message(target.name+"'s Special Defense rose!");
				}
				else
					return false;
			break;
		}
		case 16:{
			if(target.accLvl==6)
			{
				message("But " + target.name+"'s Accuracy can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(9,1);
					message(target.name+"'s Accuracy rose!");
				}
				else
					return false;
			break;
		}
		case 17:{
			if(target.evaLvl==6)
			{
				message("But " + target.name+"'s Evasion can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(8,1);
					message(target.name+"'s Evasion rose!");
				}
				else
					return false;
			break;
		}
		case 51:{
			if (target.atkLvl==6)
				{
				message("But " + target.name+"'s Attack can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.ATTACK, 2);
				message(target.name+"'s Attack sharply rose!");
				}
				else
					return false;
					break;
		}
		case 52:{
			if (target.defLvl==6)
			{
				message("But " + target.name+"'s Defense can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.DEFENSE, 2);
					message(target.name+"'s Defense sharply rose!");
				}		
				else
					return false;
			break;
		}
		case 53:{
			if (target.spdLvl==6)
			{
				message("But " + target.name+"'s Speed can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SPEED, 2);
					message(target.name+"'s Speed sharply rose!");
				}
				else
					return false;
			break;		
		}
		case 54:{
			if (target.spAtkLvl==6)
			{
				message("But " + target.name+"'s Special Attack can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_ATK, 2);
					message(target.name+"'s Special Attack sharply rose!");
				}
				else
					return false;
			break;
		}
		case 55:{
			if (target.spDefLvl==6)
			{
				message("But " + target.name+"'s Special Defense can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_DEF, 2);
					message(target.name+"'s Special Defense sharply rose!");
				}
				else
					return false;
			break;
		}
		case 56:{
			if(target.accLvl==6)
			{
				message("But " + target.name+"'s Accuracy can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(9,2);
					message(target.name+"'s Accuracy sharply rose!");
				}
				else
					return false;
			break;
		}
		case 57:{
			if(target.evaLvl==6)
			{
				message("But " + target.name+"'s Evasion can't go any higher!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(8,2);
					message(target.name+"'s Evasion sharply rose!");
				}
				else
					return false;
			break;
		}
		case 19:{}
		case 69:{
			if (target.atkLvl==-6)
			{
				message("But " + target.name+"'s attack can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.ATTACK, -1);
					message(target.name+"'s attack fell!");
				}
				else
					return false;
			break;
		}
		case 70:{}
		case 20:{
			if (target.defLvl==-6)
			{
				message("But " + target.name+"'s Defense can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.DEFENSE, -1);
					message(target.name+"'s Defense fell!");
				}
				else
					return false;
			break;
		}
		case 71:{}
		case 21:{
			if (target.spdLvl==-6)
			{
				message("But " + target.name+"'s Speed can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SPEED, -1);
					message(target.name+"'s Speed fell!");
				}
				else
					return false;
			break;
		}
		case 72:{}
		case 22:{
			if (target.spAtkLvl==-6)
			{
				message("But " + target.name+"'s Special Attack can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_ATK, -1);
					message(target.name+"'s Special Attack fell!");
				}
				else
					return false;
			break;
		}
		case 73:{}
		case 23:{
			if (target.spDefLvl==-6)
			{
				message("But " + target.name+"'s Special Defense can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_DEF, -1);
					message(target.name+"'s Special Defense fell!");
				}
				else
					return false;
			break;
		}
		case 74:{}
		case 24:{
			if(target.accLvl==-6)
			{
				message("But " + target.name+"'s Accuracy can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(9,-1);
					message(target.name+"'s Accuracy fell!");
				}
				else
					return false;
			break;
		}
		case 75:{}
		case 25:{
			if(target.evaLvl==-6)
			{
				message("But " + target.name+"'s Evasion can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(8,-1);
					message(target.name+"'s Evasion fell!");
				}
				else
					return false;
			break;
		}
		case 59:{
			if (target.atkLvl==-6)
			{
				message("But " + target.name+"'s attack can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.ATTACK, -2);
					message(target.name+"'s attack harshly fell!");
				}
				else
					return false;
			break;
		}
		case 60:{
			if (target.defLvl==-6)
			{
				message("But " + target.name+"'s Defense can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.DEFENSE, -2);
					message(target.name+"'s Defense harshly fell!");
				}
				else
					return false;
			break;
		}
		case 61:{
			if (target.spdLvl==-6)
			{
				message("But " + target.name+"'s Speed can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SPEED, -2);
					message(target.name+"'s Speed harshly fell!");
				}
				else
					return false;
			break;
		}
		case 62:{
			if (target.spAtkLvl==-6)
			{
				message("But " + target.name+"'s Special Attack can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_ATK, -2);
					message(target.name+"'s Special Attack harshly fell!");
				}
				else
					return false;
			break;
		}
		case 63:{
			if (target.spDefLvl==-6)
			{
				message("But " + target.name+"'s Special Defense can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(Pokemon.SP_DEF, -2);
					message(target.name+"'s Special Defense harshly fell!");
				}
				else
					return false;
			break;
		}
		case 64:{
			if(target.accLvl==-6)
			{
				message("But " + target.name+"'s Accuracy can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(9,-2);
					message(target.name+"'s Accuracy harshly fell!");
				}
				else
					return false;
			break;
		}
		case 65:{
			if(target.evaLvl==-6)
			{
				message("But " + target.name+"'s Evasion can't go any lower!");
				return false;
				}
			else
				if(effectHits){
					target.statBuff(8,-2);
					message(target.name+"'s Evasion harshly fell!");
				}
				else
					return false;
			break;
		}
		
		}
		return true;
	}
	
	/**
	 * Computes damage. Does not account for accuracy; this should only be called when a hit is generated. Accounts for criticals and physical/special damage. 
	 * @param attack Move being used.
	 * @param user Pokemon using the attack.
	 * @param target Pokemon targeted.
	 * @param bf 
	 * @return damage done by attack. DOES NOT INFLICT DAMAGE!!!!!!!!!! Damage must be inflicted somewhere else. Returns -1 if this attack has a base power of 0. This means that this attack must be dealt with elsewhere in logic.
	 */
 	public int computeDamage(Move attack, Pokemon user, Pokemon target, Battlefield bf, boolean crit){
		switch(attack.effect){
		case 27:{
			if(user.bideLock==2)
			message(user.name+" unleashed the stored energy!");
			return(user.bideDamage*2);
		}
		case 88:{
			if(Pokemon.getEffectiveness(attack.type,target.type1)!=0&&Pokemon.getEffectiveness(attack.type,target.type2)!=0)
				return (user.level);
			else
				return(0);
		}
		case 42:{
			return 40;
		}
		case 131:{
			return 20;
		}
		case 39:{
			return target.maxHP;
		}
		case 89:{
			return user.level*(gen.nextInt(11)+5)/10;
		}
		
		default:
 		

		int CH=1;
		if(crit==true){
			if(user.chosenAbility==Pokemon.SNIPER)
				CH=3;
			else
				CH=2;
			message("It's a critical hit!");
		}
		
		float TYPE1=Pokemon.getEffectiveness(attack.type, target.type1);
		float TYPE2=Pokemon.getEffectiveness(attack.type, target.type2);
		
		int Power;
		int HH=1;//Helping Hand
		float BP=attack.getPower();//Move's base power
		switch(attack.effect){
		case 122:{}
		case 124:{
			BP=102;
		}
		break;
		case 197:{
			if(target.weight<=22)
				BP=20;
			else if(target.weight<=55)
				BP=40;
			else if(target.weight<=110)
				BP=60;
			else if(target.weight<=220)
				BP=80;
			else if(target.weight<=440)
				BP=100;
			else
				BP=120;
			break;
			
		}
		}
		float 	IT=1f; //Item Multiplier
		int CHG=1; //Charge
		float MS=1f; //Mud Sport
		float WS=1f; //Water Sport
		float UA=1f; //User Ability
		float FA=1f; //Foe Ability
		
		
		switch(user.heldItem){
		case MUSCLE_BAND:
			IT=1.1f;
			break;
		case WISE_GLASSES:
			IT=1.1f;
			break;
			/*
		case user.TYPEPLATES DO THIS:
			IT=1.2;
		break;
			*/
		case ADAMANT_ORB:
			if(user.id==483)
			IT=1.2f;
		break;
		case LUSTROUS_ORB:
			if(user.id==484)
				IT=1.2f;
			break;
		case GRISEOUS_ORB:
			if(user.id==660)
				IT=1.2f;
		break;
		default:
			IT=1;
			break;
			
		}
		
		
		
		if(isUserAttacking&&userLastUsedMove!=null&&userLastUsedMove.type==Pokemon.ELECTRIC)
			CHG=2;
		else if(isUserAttacking==false&&opponentLastUsedMove!=null&&opponentLastUsedMove.type==Pokemon.ELECTRIC)
			CHG=2;
		
		if(user.WaterSported==true&&attack.type==Pokemon.FIRE)
			WS=.5f;
		if(user.MudSported==true&&attack.type==Pokemon.ELECTRIC)
			MS=.5f;
		
		switch(user.chosenAbility){
		
			case Pokemon.RIVALRY:{
			if(user.gender==target.gender)
				UA=1.25f;
			else
				UA=.75f;
		
			break;
			}
		
			case Pokemon.RECKLESS:{
				//if(MOVEHASRECOIL)
					UA=1.2f;
					break;
			}
			
			case Pokemon.IRON_FIST:{
				if(attack.moveFlags.contains(new Integer(8)))
					UA=1.2f;
				break;
			}
			
			case Pokemon.BLAZE:{
				if(attack.type==Pokemon.FIRE && user.currentHP<(int)(user.maxHP/3))
					UA=1.5f;
				break;
			}
			
			case Pokemon.OVERGROW:{
				if(attack.type==Pokemon.GRASS && user.currentHP<(int)(user.maxHP/3))
					UA=1.5f;
				break;
			}
			
			case Pokemon.TORRENT:{
				if(attack.type==Pokemon.WATER && user.currentHP<(int)(user.maxHP/3))
					UA=1.5f;
				break;
			}
			
			case Pokemon.SWARM:{
				if(attack.type==Pokemon.BUG && user.currentHP<(int)(user.maxHP/3))
					UA=1.5f;
				break;
			}
			
			case Pokemon.TECHNICIAN:{
				if(attack.BasePower<=60)
					UA=1.5f;
				break;
				
			}
		
		}
		
		switch(target.chosenAbility)
		{
		case Pokemon.THICK_FAT:{
			if(attack.type==Pokemon.ICE||attack.type==Pokemon.FIRE)
				FA=.5f;
			break;		
		}
		case Pokemon.HEAT_PROOF:{
			if(attack.type==Pokemon.FIRE)
				FA=.5f;
			break;
		}
		case Pokemon.DRY_SKIN:{
			if(attack.type==Pokemon.FIRE)
				FA=1.25f;
			break;
		}
		}
		//Multiplies each part of the base power and its modifiers, rounding down to the nearest int each time.
		Power=HH;
		Power*=BP;
		Power*=IT;
		Power*=CHG;
		Power*=MS;
		Power*=WS;
		Power*=UA;
		Power*=FA;
		
		//The attack and special attack stats and their modifiers
		
		int AtkStat;
		int Stat=0;
		float SM=1f;
		float AM=1f;
		float IM=1f;
		
		if(attack.DamageType==attack.PHYSICAL)
		{
			Stat=user.atk;
			int j=user.atkLvl;
			if(user.chosenAbility==Pokemon.SIMPLE)
				j*=2;
			if(target.chosenAbility==Pokemon.UNAWARE||j==0)
				SM=1;
			else if(j>0)
				SM=1+(j/2);
			else
				SM=2/(j);
			
			if(SM>4)
				SM=4;
			if(SM<.25)
				SM=.25f;
			if(crit&&SM<1)
				SM=1;
			
			switch(user.chosenAbility)
			{
			case Pokemon.PURE_POWER:
				AM=2;
			break;
			
			case Pokemon.HUGE_POWER:
				AM=2;
			break;
			
			case Pokemon.FLOWER_GIFT:
				if(bf.weather==bf.SUNNY)
					AM=1.5f;
			break;
				
			case Pokemon.GUTS:
				if(user.hasStatus)
					AM=1.5f;
			break;
			
			case Pokemon.HUSTLE:
				AM=1.5f;
			break;
			
			case Pokemon.SLOW_START:
				if(user.turnCount<5)
					AM=.5f;
			break;
			
			}
			
			if(user.heldItem==Item.CHOICE_BAND)
				IM=1.5f;
			if(user.heldItem==Item.LIGHT_BALL&&user.id==25)
				IM=2;
			if(user.heldItem==Item.THICK_CLUB&&(user.id==104||user.id==105))
				IM=2;
			
		}
		
		if(attack.DamageType==attack.SPECIAL)
		{
			Stat=user.spAtk;
			int j=user.spAtkLvl;
			if(user.chosenAbility==Pokemon.SIMPLE)
				j*=2;
			if(target.chosenAbility==Pokemon.UNAWARE||j==0)
				SM=1;
			else if(j>0)
				SM=1+(j/2);
			else
				SM=2/(j);
			
			if(SM>4)
				SM=4f;
			if(SM<.25)
				SM=.25f;
			if(crit&&SM<1)
				SM=1f;
			
			switch(user.chosenAbility)
			{
			case Pokemon.PLUS:
				break;
			case Pokemon.MINUS:
				break;
			case Pokemon.SOLAR_POWER:
				if(bf.weather==bf.SUNNY)
					AM=1.5f;
				break;
			}	
				
				if(user.heldItem==Item.CHOICE_SPECS)
					IM=1.5f;
				if(user.heldItem==Item.LIGHT_BALL&&user.id==25)
					IM=2;
				if(user.heldItem==Item.SOUL_DEW&&(user.id==380||user.id==381))
					IM=2;
				if(user.heldItem==Item.DEEPSEATOOTH&&user.id==366)
					IM=2;
				 
		}
		

		AtkStat=Stat;
		AtkStat*=SM;
		AtkStat*=AM;
		AtkStat*=IM;
		
		
		//Defense statistic
		
		
		int DefStat=999999;
		int DefenseStat=0;
		float DSM=1f;
		float DefMod=1f;
		if(attack.DamageType==attack.PHYSICAL)
		{
			DefenseStat=user.def;
			int j=user.defLvl;
			if(target.chosenAbility==Pokemon.SIMPLE)
				j*=2;
			if(user.chosenAbility==Pokemon.UNAWARE||j==0)
				DSM=1f
				
				
				
				
				
				
				;
			else if(j>0)
				DSM=1+(j/2);
			else
				DSM=2/(j);
			
			if(DSM>4)
				DSM=4;
			if(DSM<.25)
				DSM=.25f;
			if(crit&&DSM>1)
				DSM=1;
			
			DefStat=DefenseStat;
			DefStat*=DSM;
			
			if(target.chosenAbility==Pokemon.MARVEL_SCALE&&target.hasStatus)
				DefStat*=1.5;
		//	if(target.heldItem==Pokemon.METAL_POWDER&&pokemonisDitto)
			//	DefStat*=1.5;
			
			if(DefStat==0)
				DefStat=1;
			
		}
		
		if(attack.DamageType==attack.SPECIAL)
		{
			DefenseStat=user.spDef;
			int j=user.spDefLvl;
			if(target.chosenAbility==Pokemon.SIMPLE)
				j*=2;
			if(user.chosenAbility==Pokemon.UNAWARE||j==0)
				DSM=1;
			else if(j>0)
				DSM=1+(j/2);
			else
				DSM=2/(j);
			
			if(SM>4)
				DSM=4;
			if(SM<.25)
				DSM=.25f;
			if(crit&&DSM>1)
				DSM=1;
			
			DefStat=DefenseStat;
			DefStat*=DSM;
			
			
			
			if(target.chosenAbility==Pokemon.FLOWER_GIFT)
				DefStat*=1.5;
			
			
			if(target.heldItem==Item.DEEPSEASCALE&&target.id==366)
				DefStat*=2;
			if(target.heldItem==Item.METAL_POWDER&&target.id==132)
				DefStat*=1.5;
			if(target.heldItem==Item.SOUL_DEW&&(target.id==380||target.id==381))
				DefStat*=1.5;
			
			
			if((target.type1==Pokemon.ROCK||target.type2==Pokemon.ROCK)&&bf.weather==bf.SANDSTORM)
				DefStat*=1.5;
			
			if(DefStat==0)
				DefStat=1;
		}
		
		
		//This section of code determines MOD1
		
		float BRN=1;
		float RL=1;
		float SR=1;
		float FF=1;
		
		if(user.isBurned&&attack.DamageType==attack.PHYSICAL&&user.chosenAbility!=user.GUTS)
			BRN=.5f;
		if(attack.DamageType==attack.PHYSICAL&&bf.REFLECT==true)
			RL=.5f;
		if(attack.DamageType==attack.SPECIAL&&bf.LIGHTSCREEN==true)
			RL=.5f;
		
		switch(attack.type){
			case Pokemon.FIRE:
			{
				if(bf.weather==bf.SUNNY)
					SR=1.5f;
				if(bf.weather==bf.RAIN)
					SR=.5f;
			}
			break;
			case Pokemon.WATER:
			{
				{
					if(bf.weather==Battlefield.SUNNY)
						SR=.5f;
					if(bf.weather==Battlefield.RAIN)
						SR=1.5f;
				}
			}
			break;
		}
		if(user.FlashFireActive=true&&attack.type==Pokemon.FIRE)
			FF=1.5f;
		
		if(crit)
			RL=1;
		
		float mod1=1;
		mod1*=BRN;
		mod1*=RL;
		mod1*=SR;
		mod1*=FF;
		
		int mod2=1;
		
		if(user.heldItem==Item.LIFE_ORB)
			mod2*=1.3;
		if(user.heldItem==Item.METRONOME)
			mod2*=(1+.1);//*ConsecutiveUses);
		
		//if(isUsingMeFirst)
			//mod2*=1.5;
		
		int mod3=1;
		float SRF=1;
		float EB=1;
		float TL=1;
		float TRB=1;
		if((target.chosenAbility==Pokemon.SOLID_ROCK||target.chosenAbility==Pokemon.FILTER)&&(TYPE1*TYPE2>1))
			SRF=.75f;
		if(user.heldItem==Item.EXPERT_BELT)
			EB=1.2f;
		if(user.chosenAbility==Pokemon.TINTED_LENS&&(TYPE1*TYPE2<1))
			TL=2;
		//TRB statement here
		
		mod3*=SRF;
		mod3*=EB;
		mod3*=TL;
		mod3*=TRB;
		
		int R;
		R=(int)(((Math.random()*38+217)*100)/255);
		
		float STAB=1;
		if(user.type1==attack.type||user.type2==attack.type)
			if(user.chosenAbility==Pokemon.ADAPTABILITY)
				STAB=2f;
			else
				STAB=1.5f;
		
		int Damage;
		Damage=user.level;
		Damage*=2;
		Damage/=5;
		Damage+=2;
		Damage*=Power;
		Damage*=AtkStat;
		Damage/=50;
		Damage/=DefStat;
		Damage*=mod1;
		Damage+=2;
		Damage*=CH;
		Damage*=mod2;
		Damage*=R;
		Damage/=100;
		Damage*=STAB;
		Damage*=TYPE1;
		Damage*=TYPE2;
		Damage*=mod3;
		
		return Damage;
	
		}
 	}
	
	/**
	 * Writes message to message box. This will be immediately overwritten by the next message if you don't use Thread.sleep() to wait a bit.
	 * @param message message to be written
	 */
	public static void message(String message)
	{
		MasterServerThread.broadcastMessage(message);
	}
}