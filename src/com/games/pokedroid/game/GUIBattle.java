package com.games.pokedroid.game;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.games.pokedroid.BottomPanel;
import com.games.pokedroid.PreBattleScreen;
import com.games.pokedroid.R;
import com.games.pokedroid.R.id;
import com.games.pokedroid.R.layout;
import com.games.pokedroid.R.menu;
import com.games.pokedroid.gfx.Panel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

public class GUIBattle extends Activity
{
	public static boolean isLockedIntoMove=false;
	
	public static Bitmap near;
	public static Bitmap far;
	
	public static Pokemon[] team1;
	public static Pokemon[] team2;
	
	public static AssetManager am;
	
	private Attack attack=new Attack();
	
	static TextView battleText;
	private static BottomPanel v;
	private static Panel p;
	
	public Move userChosenAttack=null;
	
	private Messenger messenger=new Messenger();
	
	public static Move AIChosenAttack=null;
	
	public static int activeAlly=0;
	public static int activeEnemy=0;
	
	private float downX;
	private float downY;
	private float upX;
	private float upY;
	
	public final int POWER=4;
	public final int TYPE=5;
	public final int ACCURACY=6;
	public final int PHYSICALITY=12;
	
	private static boolean isViewFrozen=false;
	
	private AlertDialog quit, switcher, noSwitch, noPP, win, lose, fainted, illegal, alreadyOut;
	
	private long time;
	
	private boolean isAIDone=false;
	
	private static boolean doneChoosing=false;
	
	public static boolean hasSomeoneWon=false;
	public static boolean userWon, battleOver=false;
	
	private boolean restarting=false;
	
	public static boolean isLockedIn=false;
	public static boolean isWrapped=false;
	
	public static String playerName, opponentName;
	private int aiLevel=10;
	private boolean isBattleTower=false;
	
	public Battlefield derp;
	
	TrainerAI ai;

	private static int switchTarget;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.drawing);
		
		battleOver=false;
		userWon=false;
		hasSomeoneWon=false;
		
		Intent intent=getIntent();
		Bundle bund=intent.getExtras();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        playerName=prefs.getString("name", "Senor Default");
        
        if(intent.getIntExtra("level", -1)!=-1)
        	aiLevel=intent.getIntExtra("level", 0);
        else
        	aiLevel=10;
        
        isBattleTower=intent.getBooleanExtra("battleTower", false);
        
        if(intent.getStringExtra("ainame")!=null)
        	opponentName=intent.getStringExtra("ainame");
        else
        	opponentName="Senor AI";
		if(bund!=null)
		{
			ArrayList<Pokemon> durr=(ArrayList<Pokemon>) bund.get("team");
			team1=new Pokemon[durr.size()];
			for(int i=0; i<durr.size(); i++)
				team1[i]=durr.get(i);
	    	team2=new Pokemon[6];
	    	for(int i=0; i<6; i++)
	    		team2[i]=new Pokemon((int) (Math.random()*649));
		}
		else
		{
	    	team1=new Pokemon[6];
	    	for(int i=0; i<6; i++)
	    	{
	    		team1[i]=new Pokemon((int) (Math.random()*649));
	    		team1[i].attack1=new Move((int) (Math.random()*467));
	    		team1[i].attack2=new Move((int) (Math.random()*467));
	    		team1[i].attack3=new Move((int) (Math.random()*467));
	    		team1[i].attack4=new Move((int) (Math.random()*467));
	    	}
	    	team2=new Pokemon[6];
	    	for(int i=0; i<6; i++)
	    	{
	    		team2[i]=new Pokemon((int) (Math.random()*649));
	    		team2[i].attack1=new Move((int) (Math.random()*467));
	    		team2[i].attack2=new Move((int) (Math.random()*467));
	    		team2[i].attack3=new Move((int) (Math.random()*467));
	    		team2[i].attack4=new Move((int) (Math.random()*467));
	    	}
		}
		
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit the battle?")
        .setTitle("Quit?")
        .setPositiveButton("Yup.", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	if(isBattleTower)
            		PreBattleScreen.onLose();
            	dialog.dismiss();
            	finish();
            }
        })
        .setNegativeButton("Nope.avi", new DialogInterface.OnClickListener()
        {
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
        quit=builder.create();
        
        builder.setMessage("Switch to this Pokemon?")
        .setTitle("Switch?")
        .setPositiveButton("Yup.", new DialogInterface.OnClickListener()
        {
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				battle();
			}
		})
        .setNegativeButton("Nope.avi", new DialogInterface.OnClickListener()
        {
			public void onClick(DialogInterface dialog, int which)
			{
				userChosenAttack=null;
				dialog.dismiss();
			}
		});
        switcher=builder.create();
        
        builder.setMessage("This is your first battle! Welcome! This is nearly identical to the official games, so you should be able to figure it out. To see more info on a move or Pokemon, hit the menu button.")
        .setCancelable(false)
        .setTitle("")
        .setNegativeButton("", null)
        .setPositiveButton("Alrighty!", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	dialog.dismiss();
            }
        });

        
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("firstbattlerun2", true))
		{
			builder.show();
	        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
	        editor.putBoolean("firstbattlerun2", false);
	        editor.commit();
		}
		
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		b.setMessage("Your opponent is using a move or ability that prevents switching.")
		.setPositiveButton("Aww, that isn't very nice!", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
		});
		noSwitch=b.create();
		
		b.setMessage("You don't have any PP for that move!")
		.setPositiveButton("Whoops.", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
		});
		noPP=b.create();
		
		b.setMessage("YOU'RE WINNER !")
		.setPositiveButton("Yay!", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				finish();
			}
		});
		win=b.create();
		
		b.setMessage("You're such a loser.")
		.setPositiveButton("Aww :(", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				if(isBattleTower)
					PreBattleScreen.onLose();
				finish();
			}
		});
		lose=b.create();
		
		b.setMessage("That Pokemon has already fainted!")
		.setPositiveButton("Oops!", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
		});
		fainted=b.create();
		
		b.setMessage("Your team isn't valid. Fix that. Check moves, EVs, IVs, and that kind of thing.")
		.setPositiveButton("Okay.", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				finish();
			}
		});
		illegal=b.create();
		
		b.setMessage("That Pokemon is already out!")
		.setPositiveButton("Whoops!", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
			
		});
		alreadyOut=b.create();
		
		for(Pokemon i:team1)
			if(!i.checkLegality())
				illegal.show();
		
		boolean legal=true;
		do
		{
			ai=new TrainerAI(aiLevel);
			for(Pokemon i:team2)
				if(!i.checkLegality())
				{
					legal=false;
					Log.e("pokedroid","AI generated illegal team");
				}
		} while(!legal);
		
		battleText=(TextView) findViewById(R.id.battleText);
		v=(BottomPanel) findViewById(R.id.attackView);
		p=(Panel) findViewById(R.id.topPanel);
		v.am=getAssets();
		
		am=this.getAssets();
	}
	
	public void onResume()
	{	
		super.onResume();
		battleOver=false;
		userWon=false;
		
		Log.e("pokedroid","onResume"); //TODO
		p.fadeIn();
		v.fadeIn();
		
		if(restarting)
		{
			v.surfaceCreated(null);
			p.surfaceCreated(null);
		}
		
		v.setOnTouchListener(new OnTouchListener()	//filters so that only tap events show up
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					downX=event.getX();
					downY=event.getY();
					time=System.currentTimeMillis();
					return true;
				}
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					upX=event.getX();
					upY=event.getY();
					if(Math.abs(downX-upX)<20 && Math.abs(downY-upY)<20 && (System.currentTimeMillis()-time)<500)
						dealWithTouch(downX, downY);
					return true;
				}
				return false;
			}
		});
		
    	try
    	{
    		BufferedInputStream buf = new BufferedInputStream(am.open("rear/"+team1[activeAlly].id+".png"));
    		near = BitmapFactory.decodeStream(buf);
    		buf.close();
    	}   
    	catch (IOException e)
    	{
    	   Log.e("pokedroid",e.toString());
    	   Toast.makeText(this, "Dammit, dammit dammit, dammit!", Toast.LENGTH_LONG).show();
    	}
    	
    	try
    	{
    		BufferedInputStream buf = new BufferedInputStream(am.open("front/"+team2[activeAlly].id+".png"));
    		far = BitmapFactory.decodeStream(buf);
    		buf.close();
    	}   
    	catch (IOException e)
    	{
    	   Log.e("pokedroid",e.toString());
    	   Toast.makeText(this, "Dammit, dammit dammit, dammit!", Toast.LENGTH_LONG).show();
    	}
    	derp=new Battlefield(team1, team2);
    	showMessage("What will "+team1[activeAlly].name+" do?");
    	startAIProcess();
	}

	/**
	 * Gets the AI thinking about its next turn.
	 */
	private void startAIProcess()
	{
		new AITask().execute();
	}

	/**
	 * Sends all required values to the Attack class so a new attack phase can begin. This should be called after both the user and AI are done.
	 */
	private void battle()
	{
		new AttackTask().execute(this);
	}
	
	public void onWin()
	{
		battleOver=true;
	}
	
	public void onLose()
	{
		lose.show();
	}
	
	public void endBattle()
	{
		if(userWon)
		{
			if(isBattleTower)
				PreBattleScreen.incrementAndRandomize();
			win.show();
		}
		else
			onLose();
	}
	
	public static int onPlayerFaint()
	{
		doneChoosing=false;
		v.requestFocus();
		v.clearScreens();
		v.animate(v.SWITCH);
		isViewFrozen=true;
		while(!doneChoosing)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return switchTarget;
	}
	
	public static void noSwitchForYou()
	{
		isWrapped=true;
	}
	
	public static void noChooseForYou()
	{
		isLockedIn=true;
	}
	
	/**
	 * Switches Pokemon on the field.
	 * @param side The side whose Pokemon is being switched. 1 for player's side, 2 for the other side.
	 * @param target the index in the respective array of the Pokemon being switched to.
	 */
	public static void switchPokemon(int side, int target, Messenger m)
	{
		switch (side)
		{
		case 1:
		{
			//queueMessage(team1[activeAlly].name+", come back!", m);
	    	try
	    	{
	    		BufferedInputStream buf = new BufferedInputStream(am.open("nothing.png"));
	    		near = BitmapFactory.decodeStream(buf);
	    		buf.close();
	    	}   
	    	catch (IOException e)
	    	{
	    	   Log.e("pokedroid",e.toString());
	    	}
	    	p.scaleAlly();
			activeAlly=target;
			//queueMessage("Go, "+team1[activeAlly].name+"!", m);
	    	try
	    	{
	    		BufferedInputStream buf = new BufferedInputStream(am.open("rear/"+team1[activeAlly].id+".png"));
	    		near = BitmapFactory.decodeStream(buf);
	    		buf.close();
	    	}   
	    	catch (IOException e)
	    	{
	    	   Log.e("pokedroid",e.toString());
	    	}
	    	p.scaleAlly();
	    	Battlefield.YourPokemon=team1[activeAlly];
	    	isViewFrozen=false;
		}
		break;
		case 2:
		{
			//queueMessage("Foe withdrew "+team2[activeEnemy].name+".", m);
	    	try
	    	{
	    		BufferedInputStream buf = new BufferedInputStream(am.open("nothing.png"));
	    		far = BitmapFactory.decodeStream(buf);
	    		buf.close();
	    	}   
	    	catch (IOException e)
	    	{
	    	   Log.e("pokedroid",e.toString());
	    	}
	    	p.scaleEnemy();
			activeEnemy=target;
			//queueMessage("Foe sent out "+team2[activeEnemy].name+"!", m);
	    	try
	    	{
	    		BufferedInputStream buf = new BufferedInputStream(am.open("front/"+team2[activeAlly].id+".png"));
	    		far = BitmapFactory.decodeStream(buf);
	    		buf.close();
	    	}   
	    	catch (IOException e)
	    	{
	    	   Log.e("pokedroid",e.toString());
	    	}
	    	p.scaleEnemy();
	    	Battlefield.OpponentPokemon=team2[activeEnemy];
		}
		break;
		default:
		{
			queueMessage("Some dumbass called switchPokemon() wrong.", m);
		}
		}
	}
	
	public static void queueMessage(String message, Messenger mess)
	{
		Message m=Message.obtain();
		Bundle data=new Bundle();
		data.putString("message", message);
		m.setData(data);
		mess.sendMessageDelayinated(m, Attack.MESSAGE_DELAY);
	}
	
	public void showMessage(String message)
	{
		Message m=Message.obtain();
		Bundle data=new Bundle();
		data.putString("message", message);
		m.setData(data);
		messenger.sendMessageDelayinated(m, Attack.MESSAGE_DELAY);
	}
	
	protected void dealWithTouch(float x, float y)
	{
		switch(v.currentView)
		{
		case 0:
		{
			if(!isLockedIntoMove){
				if(x>=v.viewWidth*.05f && x<=v.viewWidth*.95f && y>=v.viewHeight*.1f && y<=v.viewHeight*.65f)	//attack button
					v.animate(v.ATTACK);
			if(!isWrapped)
			{
				if(x>=v.viewWidth*.05f && x<=v.viewWidth*.95f && y>=v.viewHeight*.7f && y<=v.viewHeight*.9f)	//switch button
					v.animate(v.SWITCH);
			}
			else
				noSwitch.show();
			
			break;
			}
			else
				userChosenAttack=Attack.lastUsedMove;
		}
		case 1:
		{
			//choose an attack
			if(x>=v.viewWidth*.01f && x<=v.viewWidth*.49f && y>=v.viewHeight*.1f && y<=v.viewHeight*.45f)
				userChosenAttack=team1[activeAlly].attack1;
			
			if(x>=v.viewWidth*.51f && x<=v.viewWidth*.99f && y>=v.viewHeight*.1f && y<=v.viewHeight*.45f)
				userChosenAttack=team1[activeAlly].attack2;
			
			if(x>=v.viewWidth*.01f && x<=v.viewWidth*.49f && y>=v.viewHeight*.5f && y<=v.viewHeight*.9f)
				userChosenAttack=team1[activeAlly].attack3;
			
			if(x>=v.viewWidth*.51f && x<=v.viewWidth*.99f && y>=v.viewHeight*.5f && y<=v.viewHeight*.9f)
				userChosenAttack=team1[activeAlly].attack4;
			
			if(userChosenAttack==null)
				return;
			if(userChosenAttack.CurrentPP==0)
				noPP.show();
			else
				battle();
		}
		break;
		case 2:
		{
			//switch pokemon
			if(userChosenAttack!=null && !isViewFrozen)
				return;
			if(isViewFrozen && doneChoosing)
				return;
			int u=-1;
			if(y<=v.viewHeight*.155f)
			{
				alreadyOut.show();
				return;
			}
			else if(y<=v.viewHeight*.32f)
				u=1;
			else if(y<=v.viewHeight*.485f)
				u=2;
			else if(y<=v.viewHeight*.655f)
				u=3;
			else if(y<=v.viewHeight*.825f)
				u=4;
			else
				u=5;
			if(team1[u].hasFainted)
			{
				fainted.show();
				return;
			}
			if(isViewFrozen)
			{
				isViewFrozen=false;
				switchTarget=u;
				doneChoosing=true;
			}
			else
			{
				userChosenAttack=new Move(1);
				userChosenAttack.makeSwitch(u);
				switcher.show();
			}
		}
		break;
		case 3:
		{
			int tar=-1;
			if(x>=v.viewWidth*.01f && x<=v.viewWidth*.49f && y>=v.viewHeight*.1f && y<=v.viewHeight*.45f)
				tar=1;
			
			if(x>=v.viewWidth*.51f && x<=v.viewWidth*.99f && y>=v.viewHeight*.1f && y<=v.viewHeight*.45f)
				tar=2;
			
			if(x>=v.viewWidth*.01f && x<=v.viewWidth*.49f && y>=v.viewHeight*.5f && y<=v.viewHeight*.9f)
				tar=3;
			
			if(x>=v.viewWidth*.51f && x<=v.viewWidth*.99f && y>=v.viewHeight*.5f && y<=v.viewHeight*.9f)
				tar=4;
			
			String info="fuck.";
			
			switch (tar)
			{
			case 1:
				info=team1[activeAlly].attack1.name+"\nPP: "+team1[activeAlly].attack1.CurrentPP+
				"/"+team1[activeAlly].attack1.MaxPP+"\nPower: "+
				team1[activeAlly].attack1.BasePower+"\nAccuracy: "+team1[activeAlly].attack1.BaseAccuracy+"\nType: "+
				typeToString(team1[activeAlly].attack1.type)+"\n"+
				team1[activeAlly].attack1.description;
				break;
			case 2:
				info=team1[activeAlly].attack2.name+"\nPP: "+team1[activeAlly].attack2.CurrentPP+
				"/"+team1[activeAlly].attack2.MaxPP+"\nPower: "+
				team1[activeAlly].attack2.BasePower+"\nAccuracy: "+team1[activeAlly].attack2.BaseAccuracy+"\nType: "+
				typeToString(team1[activeAlly].attack2.type)+"\n"+
				team1[activeAlly].attack2.description;
				break;
			case 3:
				info=team1[activeAlly].attack3.name+"\nPP: "+team1[activeAlly].attack3.CurrentPP+
				"/"+team1[activeAlly].attack3.MaxPP+"\nPower: "+
				team1[activeAlly].attack3.BasePower+"\nAccuracy: "+team1[activeAlly].attack3.BaseAccuracy+"\nType: "+
				typeToString(team1[activeAlly].attack3.type)+"\n"+
				team1[activeAlly].attack3.description;
				break;
			case 4:
				info=team1[activeAlly].attack4.name+"\nPP: "+team1[activeAlly].attack4.CurrentPP+
				"/"+team1[activeAlly].attack4.MaxPP+"\nPower: "+
				team1[activeAlly].attack4.BasePower+"\nAccuracy: "+team1[activeAlly].attack4.BaseAccuracy+"\nType: "+
				typeToString(team1[activeAlly].attack4.type)+"\n"+
				team1[activeAlly].attack4.description;
			}
			
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(info)
	        .setCancelable(false)
	        .setPositiveButton("Mmkay.", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int id)
	            {
	            	dialog.dismiss();
	            }
	        });
	        builder.create().show();
	        v.animate(v.MAIN);
		}
		}
	}
	
	public String typeToString(int type)
	{
		switch (type)
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
	
	public void onBackPressed()
	{
		if(isViewFrozen)
		{
			quit.show();
			return;
		}
		switch (v.currentView)
		{
		case 0:
			quit.show();
			break;
		case 1:
		case 2:
			v.animate(v.MAIN);
			break;
		case 3:
			v.currentView=v.ATTACK;
		}
	}
	
	/**
	 * Changes visible sprite
	 * @param id id number of Pokemon sprite to be changed to
	 * @param side true for ally, false for enemy
	 */
	public void changeSprite(int id, boolean side)
	{
		if(side)
		{
	    	try
	    	{
	    		BufferedInputStream buf = new BufferedInputStream(am.open("rear/"+id+".png"));
	    		near = BitmapFactory.decodeStream(buf);
	    		buf.close();
	    	}   
	    	catch (IOException e)
	    	{
	    	   Log.e("pokedroid",e.toString());
	    	}
		}
		else
		{
	    	try
	    	{
	    		BufferedInputStream buf = new BufferedInputStream(am.open("front/"+id+".png"));
	    		far = BitmapFactory.decodeStream(buf);
	    		buf.close();
	    	}   
	    	catch (IOException e)
	    	{
	    	   Log.e("pokedroid",e.toString());
	    	}
		}
	}
	
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.battlemenu, menu);
        return true;
    }
	
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.aboutbutton:
            aboutMenu();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void aboutMenu()
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(this);
    	b.setMessage("More info on which ones?")
    	.setPositiveButton("Team Pokemon", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				teamInfo();
			}
    	})
    	.setNegativeButton("Attacks", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				attackInfo();
			}
    	})
    	.setNeutralButton("Nevermind.", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
    	})
    	.show();
    }
    
    private void attackInfo()
    {
    	String[] moves=new String[4];
    	moves[0]=team1[activeAlly].attack1.name;
    	moves[1]=team1[activeAlly].attack2.name;
    	moves[2]=team1[activeAlly].attack3.name;
    	moves[3]=team1[activeAlly].attack4.name;
    	
    	final AlertDialog.Builder c=new AlertDialog.Builder(this);
    	
    	AlertDialog.Builder b=new AlertDialog.Builder(this);
    	b.setTitle("Which attack?")
    	.setItems(moves, new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				String info="";
				switch (arg1)
				{
				case 0:
					info=team1[activeAlly].attack1.toString();
					break;
				case 1:
					info=team1[activeAlly].attack2.toString();
					break;
				case 2:
					info=team1[activeAlly].attack3.toString();
					break;
				case 3:
					info=team1[activeAlly].attack4.toString();
				}
				c.setMessage(info)
				.setPositiveButton("Okay.", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						arg0.dismiss();
					}
				})
				.show();
			}
    	})
    	.show();
    }
    
    private void teamInfo()
    {
    	String[] names = new String[team1.length];
    	for(int i=0; i<team1.length; i++)
    		names[i]=team1[i].name;
    	
    	final AlertDialog.Builder c=new AlertDialog.Builder(this);
    	
    	AlertDialog.Builder b=new AlertDialog.Builder(this);
    	b.setTitle("Choose a Pokemon")
    	.setNegativeButton("Close", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
    	})
    	.setItems(names, new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				c.setMessage(team1[arg1].toString())
				.setPositiveButton("Okay.", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						arg0.dismiss();
					}
				})
				.show();
			}
    	})
    	.show();
    }
    
	public void generateRandomTeam()
	{
		team2=new Pokemon[6];
		
		for(int i=0; i<team2.length; i++)
		{
			team2[i]=new Pokemon((int)(Math.random()*649));
			team2[i].attack1=new Move((int)(Math.random()*467));
			team2[i].attack2=new Move((int)(Math.random()*467));
			team2[i].attack3=new Move((int)(Math.random()*467));
			team2[i].attack4=new Move((int)(Math.random()*467));
			//team2[i].attackinate();
		}
		
		if(team1==null)
		{
			team1=new Pokemon[6];
			for(int i=0; i<team1.length; i++)
			{
				team1[i]=new Pokemon((int)(Math.random()*649));
				team1[i].attack1=new Move((int)(Math.random()*467));
				team1[i].attack2=new Move((int)(Math.random()*467));
				team1[i].attack3=new Move((int)(Math.random()*467));
				team1[i].attack4=new Move((int)(Math.random()*467));
				//team1[i].attackinate();
			}
			
		}
	}
	
	public void onPause()
	{
		super.onPause();
		p.surfaceDestroyed(null);
		v.surfaceDestroyed(null);
		restarting=true;
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		v.fadeOut();
		p.fadeOut();
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
	}
	
    private class AITask extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(Void... derp)
        {
        	AIChosenAttack=TrainerAI.executeAI();
			return null;
        }

		protected void onPostExecute(Void args)
        {
			isAIDone=true;
        }
    }
    
    private class AttackTask extends AsyncTask<Context, Void, Void>
    {
		protected Void doInBackground(Context... params)
		{
			isLockedIn=false;
			isWrapped=false;
			battleOver=false;
			int tries=0;
			while(!isAIDone)		//checks to see if the user and AI are done every 50ms
			{
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
				tries++;
				if(tries>200)
				{
					Log.e("pokedroid", "the ai got confused and hung. restarting.");
					new AITask().execute();
					tries=0;
				}
			}
			v.currentView=v.NONE;
			isAIDone=false;
			Move mv=userChosenAttack;
			attack.attackPhase(params[0], userChosenAttack, AIChosenAttack, team1[activeAlly], team1, team2[activeEnemy], team2, derp, messenger);
			v.clearScreens();
			userChosenAttack=null;
			AIChosenAttack=null;
			if(hasSomeoneWon)
				onWin();
			if(!isLockedIn)
			{
				v.currentView=v.MAIN;
				Message m=Message.obtain();
				Bundle data=new Bundle();
				data.putString("message", "What will "+team1[activeAlly].name+" do?");
				m.setData(data);
				messenger.sendMessageDelayinated(m, Attack.MESSAGE_DELAY);
				startAIProcess();
				v.clearScreens();
			}
			else
			{
				userChosenAttack=mv;
				startAIProcess();
				battle();
			}
			return null;
		}
		
		public void onPostExecute(Void params)
		{
			if(battleOver)
				endBattle();
		}
    }
    
    public class Messenger extends Handler
    {
    	public void sendMessageDelayinated(Message m, long time)
    	{
    		sendMessage(m);
    		try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	public void handleMessage(Message m)
    	{
    		Bundle b=m.getData();
    		String message=b.getString("message");
    		battleText.setText(message);
    	}
    }
}