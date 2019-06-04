package com.games.pokedroid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.games.pokedroid.game.GUIBattle;
import com.games.pokedroid.game.Pokemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PreBattleScreen extends Activity
{
	private static int battleTowerLevel=0;
	
	private TextView battleNumber, streak, nextTrainer, trainerBio, difficulty;
	private ImageView trainerImage;
	private ArrayList<Pokemon> team;
	private String aiName;
	public static int trainerNumber;
	private static boolean hasLostYet=false;
	private AlertDialog loser;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		hasLostYet=false;
		setContentView(R.layout.prebattle);
		
		Intent intent=getIntent();
		Bundle extra=intent.getExtras();
		team=(ArrayList<Pokemon>) extra.get("team");
		
		battleNumber=(TextView)	findViewById(R.id.battlenumber);
		streak=(TextView) findViewById(R.id.streak);
		nextTrainer=(TextView) findViewById(R.id.prebattletrainername);
		trainerBio=(TextView) findViewById(R.id.biotext);
		difficulty=(TextView) findViewById(R.id.difficulty);
		
		trainerImage=(ImageView) findViewById(R.id.prebattleimage);
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		b.setMessage("Your streak is over. You won "+(battleTowerLevel)+" battles.")
		.setPositiveButton("Okay.", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				battleTowerLevel=0;
				finish();
			}
		});
		loser=b.create();
		incrementAndRandomize();
	}
	
	public static void onLose()
	{
		hasLostYet=true;
	}
	
	public static void incrementAndRandomize()
	{
		battleTowerLevel++;
		trainerNumber=(int) (Math.random()*104+1);
	}
	
	public void onResume()
	{
		super.onResume();
		if(hasLostYet)
			loser.show();
		battleNumber.setText("Battle #"+battleTowerLevel);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int streak=prefs.getInt("streak", 0);
        if(streak<battleTowerLevel)
        {
        	SharedPreferences.Editor editor=prefs.edit();
        	editor.putInt("streak", battleTowerLevel);
        	editor.commit();
        	streak=battleTowerLevel;
        }
        this.streak.setText("Record streak: "+streak);
        
        aiName=SQLSource.getString(trainerNumber, 1, "trainerinfo");
        nextTrainer.setText(aiName);
        
        if(battleTowerLevel==1)
        	difficulty.setText("This one should be really, really easy.");
        else if(battleTowerLevel<=10)
        	difficulty.setText("This should still be pretty easy.");
        else if(battleTowerLevel<=25)
        	difficulty.setText("They're starting to become a bit more difficult.");
        else if(battleTowerLevel<=50)
        	difficulty.setText("No one would blame you for losing.");
        else
        	difficulty.setText("You're gonna lose.");
        trainerBio.setText(SQLSource.getString(trainerNumber, 2, "trainerinfo"));
        
		Bitmap result=null;
		
    	try
    	{
    		BufferedInputStream buf = new BufferedInputStream(getAssets().open("trainers/"+trainerNumber+".jpg"));
    		result = BitmapFactory.decodeStream(buf);
    		buf.close();
    	}   
    	catch (IOException e)
    	{
    	   Log.e("pokedroid",e.toString());
    	}
        
        trainerImage.setImageBitmap(result);
	}
	
	public void onBattleStart(View v)
	{
		Intent intent=new Intent(PreBattleScreen.this, GUIBattle.class);
		Bundle b=new Bundle();
		b.putSerializable("team", team);
		intent.putExtras(b);
		intent.putExtra("battleTower", true);
		intent.putExtra("level", battleTowerLevel);
		intent.putExtra("ainame", aiName);
		PreBattleScreen.this.startActivity(intent);
	}
	
	public void onGiveUp(View v)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		Resources res=getResources();
		String[] messages=res.getStringArray(R.array.ExitMessages);
		int chosen=(int) (Math.random()*messages.length);
		String chosenExit=messages[chosen];
		builder.setMessage(chosenExit)
		.setPositiveButton("I'm outta here.", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				battleTowerLevel=0;
				finish();
			}
		})
		.setNegativeButton("I'll stay.", new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
		});
		builder.show();
	}
}