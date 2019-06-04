package com.games.pokedroid;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.games.pokedroid.game.Pokemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class TeamManager extends Activity
{
	private Context c=this;
	
	public void onCreate(Bundle xyzzy)
	{
		super.onCreate(xyzzy);
		setContentView(R.layout.teammanager);
	}
	
	public void onNewTeam(View v)
	{
		TeamManager.this.startActivity(new Intent(TeamManager.this, BuilderActivity.class));
	}
	
	public void onEditTeam(View v)
	{
		SharedPreferences hurr=PreferenceManager.getDefaultSharedPreferences(this);
		String saved=hurr.getString("savedTeams", "durr");
		StringTokenizer durr=new StringTokenizer(saved, "|");
		ArrayList<String> array=new ArrayList<String>();
		while(durr.hasMoreTokens())
			array.add(durr.nextToken());
		
		if(array.size()==0 || array.get(0).equals("durr"))
		{
			AlertDialog.Builder durr1=new AlertDialog.Builder(this);
			durr1.setMessage("You don't have any saved teams, silly!")
			.setPositiveButton("Aww.", new OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1)
				{
					arg0.dismiss();
				}
			});
			durr1.show();
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
        			String name=adaray[arg1];
        			Pokemon[] team=null;
        			try {
        				team=(Pokemon[]) loadObject(name);
        			} catch (Exception e) {}
        			
        			ArrayList<Pokemon> p=new ArrayList<Pokemon>();
        			for(int i=0; i<team.length; i++)
        				p.add(team[i]);
        			Bundle b=new Bundle();
        			b.putSerializable("team", p);
        			Intent durr=new Intent(TeamManager.this, PokemonCustomizer.class).putExtras(b);
        			TeamManager.this.startActivity(durr);
				}
        	});
        	builder.show();
		}
	}
	
	public void onDeleteTeam(View v)
	{
		SharedPreferences hurr=PreferenceManager.getDefaultSharedPreferences(this);
		String saved=hurr.getString("savedTeams", "durr");
		StringTokenizer durr=new StringTokenizer(saved, "|");
		ArrayList<String> array=new ArrayList<String>();
		while(durr.hasMoreTokens())
			array.add(durr.nextToken());
		
		if(array.size()==0 || array.get(0).equals("durr"))
		{
			AlertDialog.Builder durr1=new AlertDialog.Builder(this);
			durr1.setMessage("You don't have any saved teams, silly!")
			.setPositiveButton("Aww.", new OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1)
				{
					arg0.dismiss();
				}
			});
			durr1.show();
		}
		else
		{
        	final String[] adaray=new String[array.size()];
        	for(int i=0; i<array.size(); i++)
        		adaray[i]=array.get(i);
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle("Delete which one?")
        	.setItems(adaray, new OnClickListener()
        	{
				public void onClick(DialogInterface arg0, int arg1)
				{
					final int selected=arg1;
					arg0.dismiss();
					AlertDialog.Builder b=new AlertDialog.Builder(c);
					b.setMessage("Are you sure you wanna delete that team?")
					.setPositiveButton("Yep.", new OnClickListener()
					{
						public void onClick(DialogInterface dialog, int arg1)
						{
		        			String name=adaray[selected];
		        			String newString="";
		        			for(String i:adaray)
		        				if(!i.equals(name))
		        					newString+=i+"|";
		        			writeFileList(newString);
		        			deleteObject(name);
		        			dialog.dismiss();
						}
					})
					.setNegativeButton("Nah.", new OnClickListener()
					{
						public void onClick(DialogInterface dialog, int arg1)
						{
							dialog.dismiss();
						}
					});
					b.show();
				}
        	})
        	.setNegativeButton("Nevermind.", new OnClickListener()
        	{
        		public void onClick(DialogInterface dialog, int arg1)
        		{
        			dialog.dismiss();
        		}
        	});
        	builder.show();
		}
	}
	
	protected void writeFileList(String list)
	{
		SharedPreferences hurr=PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor edit=hurr.edit();
		edit.putString("savedTeams", list);
		edit.commit();
	}
	
	protected void deleteObject(String filename)
	{
		deleteFile(filename);
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