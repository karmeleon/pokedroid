package com.games.pokedroid;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.games.pokedroid.game.GUIBattle;
import com.games.pokedroid.game.Pokemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class SPMenu extends Activity
{
	private AlertDialog temp;
	private ProgressDialog loading;
	
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.spmenu);
		loading=new ProgressDialog(this);
		loading.setMessage("Loading battle...");
		loading.setCancelable(false);
	}
	
	public void onQuickBattleSelect(View v)
	{
		SharedPreferences hurr=PreferenceManager.getDefaultSharedPreferences(this);
		String saved=hurr.getString("savedTeams", "durr");
		StringTokenizer durr=new StringTokenizer(saved, "|");
		ArrayList<String> array=new ArrayList<String>();
		while(durr.hasMoreTokens())
			array.add(durr.nextToken());
		
		if(array.size()==0 || array.get(0).equals("durr"))
		{
			loading.show();
			new RandoQuicky().execute();
		}
		else
		{	
        	final String[] adaray=new String[array.size()];
        	for(int i=0; i<array.size(); i++)
        		adaray[i]=array.get(i);
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setItems(adaray, new OnClickListener()
        	{
				public void onClick(DialogInterface arg0, int arg1)
				{
					loading.show();
					new LoadyQuicky().execute(adaray[arg1]);
				}
        	});
        	temp=builder.create();
        	
    		
    		AlertDialog.Builder bu=new AlertDialog.Builder(this);
    		bu.setMessage("Do you want to use a custom team?")
    		.setPositiveButton("Sure.", new OnClickListener()
    		{
    			public void onClick(DialogInterface dialog, int which)
    			{
    				dialog.dismiss();
    				temp.show();
    			}		
    		})
    		.setNegativeButton("Nah.", new OnClickListener()
    		{
    			public void onClick(DialogInterface arg0, int arg1)
    			{
    				loading.show();
    				new RandoQuicky().execute();
    			}
    		});
    		bu.show();
		}
	}
	
	private class LoadyQuicky extends AsyncTask<String, Void, Void>
	{
		protected Void doInBackground(String... params)
		{
			String name=params[0];
			Pokemon[] team=null;
			try {
				team=(Pokemon[]) loadObject(name);
			} catch (Exception e) {}
			
			ArrayList<Pokemon> p=new ArrayList<Pokemon>();
			for(Pokemon d: team)
				p.add(d);
			Bundle b=new Bundle();
			b.putSerializable("team", p);
			Intent durr=new Intent(SPMenu.this, GUIBattle.class).putExtras(b);
			durr.putExtra("battletower", false);
			loading.dismiss();
			SPMenu.this.startActivity(durr);
			return null;
		}
	}
	
	private class RandoQuicky extends AsyncTask<Void, Void, Void>
	{
		protected Void doInBackground(Void... params)
		{
			ArrayList<Pokemon> p=new ArrayList<Pokemon>();
			for(int i=0; i<6; i++)
			{
				Pokemon temp=new Pokemon((int) (Math.random()*649));
				temp.randomize();
				p.add(temp);
			}
			Bundle b=new Bundle();
			b.putSerializable("team", p);
			Intent derp=new Intent(SPMenu.this, GUIBattle.class).putExtras(b);
			loading.dismiss();
			SPMenu.this.startActivity(derp);
			return null;
		}
	}
	
	public void onBattleTowerSelect(View v)
	{
		SharedPreferences hurr=PreferenceManager.getDefaultSharedPreferences(this);
		String saved=hurr.getString("savedTeams", "durr");
		StringTokenizer durr=new StringTokenizer(saved, "|");
		ArrayList<String> array=new ArrayList<String>();
		while(durr.hasMoreTokens())
			array.add(durr.nextToken());
		
		if(array.size()==0 || array.get(0).equals("durr"))
		{
			loading.show();
			new RandoTower().execute();
		}
		else
		{    	
        	final String[] adaray=new String[array.size()];
        	for(int i=0; i<array.size(); i++)
        		adaray[i]=array.get(i);
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setItems(adaray, new OnClickListener()
        	{
				public void onClick(DialogInterface arg0, int arg1)
				{
					loading.show();
					new LoadyTower().execute(adaray[arg1]);
				}
        	});
        	temp=builder.create();
		
			AlertDialog.Builder builder2=new AlertDialog.Builder(this);
			builder2.setMessage("Do you want to use a custom team?")
			.setPositiveButton("Sure.", new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					temp.show();
				}		
			})
			.setNegativeButton("Nah.", new OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1)
				{
					loading.show();
					new RandoTower().execute();
				}
			});
			builder2.show();
		}
	}
	
	private class RandoTower extends AsyncTask<Void, Void, Void>
	{
		protected Void doInBackground(Void... params)
		{
			ArrayList<Pokemon> p=new ArrayList<Pokemon>();
			for(int i=0; i<6; i++)
			{
				Pokemon temp=new Pokemon((int) (Math.random()*649));
				temp.randomize();
				p.add(temp);
			}
			PreBattleScreen.trainerNumber=0;
			Bundle b=new Bundle();
			b.putSerializable("team", p);
			Intent derp=new Intent(SPMenu.this, PreBattleScreen.class).putExtras(b);
			loading.dismiss();
			SPMenu.this.startActivity(derp);
			return null;
		}
	}
	
	private class LoadyTower extends AsyncTask<String, Void, Void>
	{
		protected Void doInBackground(String... params)
		{
			String name=params[0];
			Pokemon[] team=null;
			try {
				team=(Pokemon[]) loadObject(name);
			} catch (Exception e) {}
			
			PreBattleScreen.trainerNumber=0;
			
			ArrayList<Pokemon> p=new ArrayList<Pokemon>();
			for(Pokemon d: team)
				p.add(d);
			Bundle b=new Bundle();
			b.putSerializable("team", p);
			Intent durr=new Intent(SPMenu.this, PreBattleScreen.class).putExtras(b);
			durr.putExtra("battletower", false);
			loading.dismiss();
			SPMenu.this.startActivity(durr);
			return null;
		}
	}
	
    protected Object[] loadObject(String filename) throws StreamCorruptedException, IOException, ClassNotFoundException
    {
    	FileInputStream fis=openFileInput(filename);
    	ObjectInputStream in=new ObjectInputStream(fis);
    	Object[] out=(Object[]) in.readObject();
    	in.close();
    	fis.close();
    	return out;
    }
}