package com.games.pokedroid.game;


public class Battlefield {

	public static int weather=0;
	public static final int NOWEATHER=0;
	public static final int RAIN=1;
	public static final int SUNNY=2;
	public static final int SANDSTORM=3;
	public static final int HAIL=4;
	public static final int FOG=5;
	public static int weatherCount=0;
	
	public static boolean TRICKROOM= false;
	public static boolean MAGICROOM=false;
	public static boolean WONDERROOM=false;
	public static boolean GRAVITY=false;
	
	public static boolean playerMist=false;
	public static boolean AIMist=false;
	
	
	public static boolean REFLECT =false;
	public static boolean LIGHTSCREEN =false;
	public static boolean SAFEGUARD = false;
	
	public static int SPIKES = 0;
	public static int TOXICSPIKES =0;
	public static boolean STEALTHROCKS = false;
	
	
	
	public static boolean OPPONENTREFLECT =false;
	public static boolean OPPONENTLIGHTSCREEN =false;
	public static boolean OPPONENTSAFEGUARD = false;

	public static int OPPONENTSPIKES = 0;
	public static int OPPONENTTOXICSPIKES =0;
	public static boolean OPPONENTSTEALTHROCKS = false;
	
	public static Pokemon[] team1;
	public static Pokemon[] team2;
	public static Pokemon YourPokemon;
	public static Pokemon OpponentPokemon;
	
	public Battlefield(Pokemon[] team1, Pokemon[] team2){
		Battlefield.team1=team1;
		Battlefield.team2=team2;
		YourPokemon = team1[0];
		OpponentPokemon = team2[0];
	}
	
}
