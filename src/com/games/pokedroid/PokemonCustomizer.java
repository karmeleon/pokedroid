package com.games.pokedroid;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.games.pokedroid.game.Move;
import com.games.pokedroid.game.Pokemon;
import com.games.pokedroid.game.Pokemon.Item;
import com.games.pokedroid.game.Pokemon.Nature;

public class PokemonCustomizer extends Activity
{
	public static Pokemon[] chosen;
	
	final static int MAX_IV=186;
	final static int MAX_EV=510;
	final static int NUMBER_OF_ITEMS=1;
	final static int NUMBER_OF_NATURES=1;
	
	Item chosenItem=Item.NO_ITEM;
	
	int finalStats, finalIV, finalEV, finalBase;
	
	private Gallery chooser;
	private AlertDialog done, duplicate;
	private Spinner items, natureSpinner, genderSpinner, abiliSpinner;
	private Spinner attack1, attack2, attack3, attack4;
	private TextView attack1Pow, attack2Pow, attack3Pow, attack4Pow;
	private TextView attack1Acc, attack2Acc, attack3Acc, attack4Acc;
	private TextView attack1PP, attack2PP, attack3PP, attack4PP;
	private TextView attack1Desc, attack2Desc, attack3Desc, attack4Desc;
	private TextView itemDesc;
	private TextView FinalBase, FinalEV, FinalIV, FinalTotal;
	private ImageView Image;
	private TextView Attack, Defense, SpAtk, SpDef, Name, ID, Speed, Types;
	private TextView AtkBase, DefBase, SpdBase, HPBase, SpDefBase, SpAtkBase;
	private TextView AtkFinal, DefFinal, HPFinal, SpdFinal, SpDefFinal, SpAtkFinal;
	private EditText AtkEv, DefEv, SpAtkEv, SpDefEv, HPEv, SpdEv;
	private EditText AtkIv, DefIv, SpAtkIv, SpDefIv, HPIv, SpdIv;
	private TextView type1, type2, type3, type4;
	private ArrayList<Integer> backingArray, backingAbilityArray;
	public static int numberOfMembers=0;
	
	private Resources res;
	Context c=this;
	
	public int currentTarget=0;
	public static boolean hasCustomizedTeam=false;
	
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.customizer);
		
		Intent intent=getIntent();
		Bundle bund=intent.getExtras();
		
		if(bund!=null)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Pokemon> pokemans=(ArrayList<Pokemon>) bund.get("team");
			chosen=new Pokemon[pokemans.size()];
			for(int i=0; i<pokemans.size(); i++)
				chosen[i]=pokemans.get(i);
			numberOfMembers=pokemans.size();
		}
		else
		{
	    	chosen=new Pokemon[6];
	    	for(int i=0; i<6; i++)
	    		chosen[i]=new Pokemon((int) (Math.random()*649));
	    	numberOfMembers=chosen.length;
		}
		res = getResources();
        
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.alert_textentry, null);
        final EditText edit=(EditText)view.findViewById(R.id.username_edit);
        
        AlertDialog.Builder d=new AlertDialog.Builder(this);
        d.setTitle("Save...")
        .setView(view)
        .setPositiveButton("Alrighty!", new OnClickListener()
        {
			public void onClick(DialogInterface arg0, int arg1)
			{
				String filename=edit.getText().toString();
				try {
					saveObject(chosen, filename);
				} catch (IOException e) {}
				
				SharedPreferences hurr=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String currentSaved=hurr.getString("savedTeams", "durr");
				if(currentSaved.equals("durr"))
					currentSaved="";
				Editor durr=hurr.edit();
				durr.putString("savedTeams", currentSaved+"|"+filename);
				durr.commit();
				PokemonCustomizer.this.startActivity(new Intent(PokemonCustomizer.this, PokeDroid.class));
			}
			
	        public void saveObject(Serializable[] obj, String filename) throws IOException
	        {
	        	FileOutputStream fos = openFileOutput(filename,Context.MODE_PRIVATE);
	        	ObjectOutputStream out = new ObjectOutputStream(fos);
	        	out.writeObject(obj);
	        	out.close();
	        	fos.close();
	        }
        })
        .setNegativeButton("Nevermind.", new OnClickListener()
        {
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
        });
        done=d.create();
        
        AlertDialog.Builder build=new AlertDialog.Builder(this);
        build.setMessage("A Pokemon can only know any move once.")
        .setPositiveButton("Aw, okay.", new OnClickListener()
        {
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
        });
        duplicate=build.create();
	}
	
	public void onStart()
	{
		super.onStart();
		
		type1=(TextView) findViewById(R.id.attackType1);
		type2=(TextView) findViewById(R.id.attackType2);
		type3=(TextView) findViewById(R.id.attackType3);
		type4=(TextView) findViewById(R.id.attackType4);
		
		//stuff up top with Nixon placeholder
		
    	Image=(ImageView) findViewById(R.id.targetViewer);
    	Attack=(TextView) findViewById(R.id.cattack);
    	Defense=(TextView) findViewById(R.id.cdefense);
    	SpAtk=(TextView) findViewById(R.id.cspatk);
    	SpDef=(TextView) findViewById(R.id.cspdef);
    	Name=(TextView) findViewById(R.id.customizername);
    	ID=(TextView) findViewById(R.id.customidno);
    	Types=(TextView) findViewById(R.id.customizertype);
    	Speed=(TextView) findViewById(R.id.cspeed);
    	
    	//display the base stats
    	
    	AtkBase=(TextView) findViewById(R.id.atkbase);
    	DefBase=(TextView) findViewById(R.id.defbase);
    	SpdBase=(TextView) findViewById(R.id.spdbase);
    	HPBase=(TextView) findViewById(R.id.hpbase);
    	SpDefBase=(TextView) findViewById(R.id.spdefbase);
    	SpAtkBase=(TextView) findViewById(R.id.spatkbase);
    	
    	//display final computed stats (there are a lot!)
    	
    	AtkFinal=(TextView) findViewById(R.id.atkFinal);
    	DefFinal=(TextView) findViewById(R.id.defFinal);
    	HPFinal=(TextView) findViewById(R.id.hpFinal);
    	SpdFinal=(TextView) findViewById(R.id.spdFinal);
    	SpDefFinal=(TextView) findViewById(R.id.spdefFinal);
    	SpAtkFinal=(TextView) findViewById(R.id.spatkFinal);
    	//EVs (goddamn)
    	
    	AtkEv=(EditText) findViewById(R.id.atkEV);
    	DefEv=(EditText) findViewById(R.id.defEV);
    	SpAtkEv=(EditText) findViewById(R.id.spatkEV);
    	SpDefEv=(EditText) findViewById(R.id.spdefEV);
    	HPEv=(EditText) findViewById(R.id.hpEV);
    	SpdEv=(EditText) findViewById(R.id.spdEV);
    	
    	/*
    	 * Waiting and fading and floating away
    	 * Waiting and fading and floating away
    	 * Waiting and fading and floaaaaaaaaaaaaaatinggggggggggggg.......
    	 * 
    	 * oh yeah, IVs.
    	 */
    	
    	AtkIv=(EditText) findViewById(R.id.atkIV);
    	DefIv=(EditText) findViewById(R.id.defIV);
    	SpAtkIv=(EditText) findViewById(R.id.spatkIV);
    	SpDefIv=(EditText) findViewById(R.id.spdefIV);
    	HPIv=(EditText) findViewById(R.id.hpIV);
    	SpdIv=(EditText) findViewById(R.id.spdIV);	//why do I always forget speed?
    	
    	FinalBase=(TextView) findViewById(R.id.baseTotal);
    	FinalEV=(TextView) findViewById(R.id.EVtotal);
    	FinalIV=(TextView) findViewById(R.id.IVTotal);
    	FinalTotal=(TextView) findViewById(R.id.statTotal);	//MOSKAU MOSKAU germangermangermangerman HAHAHAHAHAH, HEY!
    	
    	itemDesc=(TextView) findViewById(R.id.itemDescription);
    	
    	attack1Pow=(TextView) findViewById(R.id.power1);
    	attack2Pow=(TextView) findViewById(R.id.power2);
    	attack3Pow=(TextView) findViewById(R.id.power3);
    	attack4Pow=(TextView) findViewById(R.id.power4);
    	
    	attack1Acc=(TextView) findViewById(R.id.acc1);
    	attack2Acc=(TextView) findViewById(R.id.acc2);
    	attack3Acc=(TextView) findViewById(R.id.acc3);
    	attack4Acc=(TextView) findViewById(R.id.acc4);
    	
    	attack1PP=(TextView) findViewById(R.id.pp1);
    	attack2PP=(TextView) findViewById(R.id.pp2);
    	attack3PP=(TextView) findViewById(R.id.pp3);
    	attack4PP=(TextView) findViewById(R.id.pp4);
    	
    	attack1Desc=(TextView) findViewById(R.id.attackDesc1);
    	attack2Desc=(TextView) findViewById(R.id.attackDesc2);
    	attack3Desc=(TextView) findViewById(R.id.attackDesc3);
    	attack4Desc=(TextView) findViewById(R.id.attackDesc4);
    	
    	genderSpinner=(Spinner) findViewById(R.id.genderSpinner);
    	ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.genderPromptArray, android.R.layout.simple_spinner_item);
    	genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        	{
    	    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    	    	{
    	    		chosen[currentTarget].gender=pos;
    	    	}

    	    	public void onNothingSelected(AdapterView<?> arg0){}
        	});
    	
    	items=(Spinner) findViewById(R.id.itemSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.SpinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        items.setAdapter(adapter);
        items.setOnItemSelectedListener(new OnItemSelectedListener()
        	{
    	    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    	    	{
    	    		
    	    		chosenItem=Item.getFromId(pos);
    	    		itemDesc.setText(SQLSource.getString(pos, 1, "item_description"));
    	    		
    	    		chosen[currentTarget].heldItem=chosenItem;
    	    		chosen[currentTarget].recomputeStats();
    	    	}

    	    	public void onNothingSelected(AdapterView<?> arg0){}
        	});
        
    	natureSpinner=(Spinner) findViewById(R.id.natureSpinner);
        ArrayAdapter<CharSequence> natureAdapter = ArrayAdapter.createFromResource(this, R.array.natureSpinnerArray, android.R.layout.simple_spinner_item);
        natureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        natureSpinner.setAdapter(natureAdapter);
        natureSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        	{
        		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        		{
        			chosen[currentTarget].nature=Nature.getFromId(pos);
        			chosen[currentTarget].recomputeStats();
        		}

        		public void onNothingSelected(AdapterView<?> arg0){}
        	});
        attack1=(Spinner) findViewById(R.id.attackSpinner1);
        attack1.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int id=backingArray.get(arg2);
				if(arg2!=0 && (id==chosen[currentTarget].attack2.id || id==chosen[currentTarget].attack3.id || id==chosen[currentTarget].attack4.id))
				{
					duplicate.show();
					arg0.setSelection(0);
					return;
				}
				attack1Desc.setText(SQLSource.getString(id, 2, "moves"));
				attack1Pow.setText(""+SQLSource.getInt(id, 4, "moves"));
				attack1Acc.setText(""+SQLSource.getInt(id, 6, "moves"));
				attack1PP.setText(""+SQLSource.getInt(id, 7, "moves"));
				type1.setText(typeToString(SQLSource.getInt(id, 5, "moves")));
				chosen[currentTarget].attack1=new Move(id);
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
       	});
        
        attack2=(Spinner) findViewById(R.id.attackSpinner2);
        attack2.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int id=backingArray.get(arg2);
				if(arg2!=0 && (id==chosen[currentTarget].attack1.id || id==chosen[currentTarget].attack3.id || id==chosen[currentTarget].attack4.id))
				{
					duplicate.show();
					arg0.setSelection(0);
					return;
				}
				attack2Desc.setText(SQLSource.getString(id, 2, "moves"));
				attack2Pow.setText(""+SQLSource.getInt(id, 4, "moves"));
				attack2Acc.setText(""+SQLSource.getInt(id, 6, "moves"));
				attack2PP.setText(""+SQLSource.getInt(id, 7, "moves"));
				type2.setText(typeToString(SQLSource.getInt(id, 5, "moves")));
				chosen[currentTarget].attack2=new Move(id);
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
        
        attack3=(Spinner) findViewById(R.id.attackSpinner3);
        attack3.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int id=backingArray.get(arg2);
				if(arg2!=0 && (id==chosen[currentTarget].attack1.id || id==chosen[currentTarget].attack2.id || id==chosen[currentTarget].attack4.id))
				{
					duplicate.show();
					arg0.setSelection(0);
					return;
				}
				attack3Desc.setText(SQLSource.getString(id, 2, "moves"));
				attack3Pow.setText(""+SQLSource.getInt(id, 4, "moves"));
				attack3Acc.setText(""+SQLSource.getInt(id, 6, "moves"));
				attack3PP.setText(""+SQLSource.getInt(id, 7, "moves"));
				type3.setText(typeToString(SQLSource.getInt(id, 5, "moves")));
				chosen[currentTarget].attack3=new Move(id);
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
        
        attack4=(Spinner) findViewById(R.id.attackSpinner4);
        attack4.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int id=backingArray.get(arg2);
				if(arg2!=0 && (id==chosen[currentTarget].attack1.id || id==chosen[currentTarget].attack2.id || id==chosen[currentTarget].attack3.id))
				{
					duplicate.show();
					arg0.setSelection(0);
					return;
				}
				attack4Desc.setText(SQLSource.getString(id, 2, "moves"));
				attack4Pow.setText(""+SQLSource.getInt(id, 4, "moves"));
				attack4Acc.setText(""+SQLSource.getInt(id, 6, "moves"));
				attack4PP.setText(""+SQLSource.getInt(id, 7, "moves"));
				type4.setText(typeToString(SQLSource.getInt(id, 5, "moves")));
				chosen[currentTarget].attack4=new Move(id);
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
        
        abiliSpinner=(Spinner) findViewById(R.id.AbiliSpinner);
        abiliSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int id=backingAbilityArray.get(arg2);
				chosen[currentTarget].chosenAbility=id;
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
        });
        
        chooser=(Gallery) findViewById(R.id.customizerGallery);
        chooser.setAdapter(new ImageAdapter(this));
        
	    chooser.setOnItemClickListener(new OnItemClickListener()
	    {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	        {
	            currentTarget=position;
	            onPokemonChange();
	        }
	    });
	    
	    AtkIv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(AtkIv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].atkIV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    DefIv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(DefIv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].defIV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    SpdIv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(SpdIv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].spdIV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    SpAtkIv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(SpAtkIv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].spAtkIV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    SpDefIv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(SpDefIv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].spDefIV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    HPIv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(HPIv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].HPIV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    //EVs
	    AtkEv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(AtkEv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].atkEV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    DefEv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(DefEv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].defEV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    SpdEv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(SpdEv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].spdEV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    SpAtkEv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(SpAtkEv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].spAtkEV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    SpDefEv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(SpDefEv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].spDefEV=hurr.nextInt();
				onStatChanged();
			}
	    });
	    
	    HPEv.addTextChangedListener(new TextWatcher()
	    {
			public void afterTextChanged(Editable s)
			{/*nothin'*/}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{/*nothin'*/}
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Scanner hurr=new Scanner(HPEv.getText().toString());
				if(hurr.hasNextInt())
					chosen[currentTarget].HPEV=hurr.nextInt();
				onStatChanged();
			}
	    });
	}
	
	public void onResume()
	{
		super.onResume();
    	for(Pokemon i : chosen)
    	{
    		i.atkEV=0;
    		i.defEV=0;
    		i.spdEV=0;
    		i.spAtkEV=0;
    		i.spDefEV=0;
    		i.HPEV=0;
    		
    		i.atkIV=31;
    		i.defIV=31;
    		i.spdIV=31;
    		i.spAtkIV=31;
    		i.spDefIV=31;
    		i.HPIV=31;
    		
    		i.heldItem=Item.getFromId(((int) Math.random()*NUMBER_OF_ITEMS));
    		
    		i.nature=Nature.getFromId((int) Math.random()*NUMBER_OF_NATURES);
    	}
    	onPokemonChange();
	}
	
	public void doneButtonClicked(View v)
	{
		done.show();
	}
	
	public void onPokemonChange()
	{
    	Image.setImageBitmap(chosen[currentTarget].getFrontSprite(getAssets()));
    	Name.setText(""+chosen[currentTarget].name);
    	ID.setText("#"+chosen[currentTarget].id);

        if(chosen[currentTarget].type2==17)
        	Types.setText(""+chosen[currentTarget].typeToString(1));
        else
        	Types.setText(""+chosen[currentTarget].typeToString(1)+"/"+chosen[currentTarget].typeToString(2));
        
        //set iv/ev fields
        AtkEv.setText(""+chosen[currentTarget].atkEV);
        DefEv.setText(""+chosen[currentTarget].defEV);
        SpdEv.setText(""+chosen[currentTarget].spdEV);
        SpAtkEv.setText(""+chosen[currentTarget].spAtkEV);
        SpDefEv.setText(""+chosen[currentTarget].spDefEV);
        HPEv.setText(""+chosen[currentTarget].HPEV);
        
        AtkIv.setText(""+chosen[currentTarget].atkIV);
        DefIv.setText(""+chosen[currentTarget].defIV);
        SpdIv.setText(""+chosen[currentTarget].spdIV);
        SpAtkIv.setText(""+chosen[currentTarget].spAtkIV);
        SpDefIv.setText(""+chosen[currentTarget].spDefIV);
        HPIv.setText(""+chosen[currentTarget].HPIV);
        
    	AtkBase.setText(""+chosen[currentTarget].baseAtk);
    	DefBase.setText(""+chosen[currentTarget].baseDef);
    	SpAtkBase.setText(""+chosen[currentTarget].baseSpAtk);
    	SpDefBase.setText(""+chosen[currentTarget].baseSpDef);
    	SpdBase.setText(""+chosen[currentTarget].baseSpd);
    	HPBase.setText(""+chosen[currentTarget].baseHP);
    	
    	items.setSelection(chosen[currentTarget].heldItem.getID());
    	natureSpinner.setSelection(chosen[currentTarget].nature.getID());
    	genderSpinner.setSelection(chosen[currentTarget].gender);
    	
    	backingArray=chosen[currentTarget].Movepool;
    	ArrayList<String> attackNames=new ArrayList<String>();
    	for(Integer i:backingArray)
    		attackNames.add(SQLSource.getString(i, 1, "moves"));
    	
    	Hashtable<String, Integer> vals=new Hashtable<String, Integer>(attackNames.size());
    	for(int i=0; i<attackNames.size(); i++)
    		vals.put(attackNames.get(i), backingArray.get(i));
    	
    	Collections.sort(attackNames);
    	
    	backingArray.clear();
    	
    	for(int i=0; i<attackNames.size(); i++)
    	{
    		String name=attackNames.get(i);
    		backingArray.add(vals.get(name));
    	}
    	backingArray.add(0, 10000);
    	attackNames.add(0, "---");
    	
        ArrayAdapter<String> attackAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, attackNames);
        attackAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	backingAbilityArray=new ArrayList<Integer>();
    	backingAbilityArray.add(chosen[currentTarget].ability1);
    	if(chosen[currentTarget].ability2!=-1)
    		backingAbilityArray.add(chosen[currentTarget].ability2);
    	backingAbilityArray.add(chosen[currentTarget].DWAbility);
    	
    	ArrayList<String> abilities=new ArrayList<String>();
    	for(int i:backingAbilityArray)
    		abilities.add(SQLSource.getString(i, 1, "ability_names"));
    	
    	ArrayAdapter<String> abilityAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, abilities);
    	abilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
    	abiliSpinner.setAdapter(abilityAdapter);
    	
        attack1.setAdapter(attackAdapter);
        attack2.setAdapter(attackAdapter);
        attack3.setAdapter(attackAdapter);
        attack4.setAdapter(attackAdapter);
        
        attack1.setSelection(backingArray.indexOf(chosen[currentTarget].attack1.id));
        attack2.setSelection(backingArray.indexOf(chosen[currentTarget].attack2.id));
        attack3.setSelection(backingArray.indexOf(chosen[currentTarget].attack3.id));
        attack4.setSelection(backingArray.indexOf(chosen[currentTarget].attack4.id));
        
        abiliSpinner.setSelection(backingAbilityArray.indexOf(chosen[currentTarget].chosenAbility));
        
		onStatChanged();
	}
	
	public void onStatChanged()
	{
		chosen[currentTarget].recomputeStats();
		chosen[currentTarget].recomputeHP();
		
    	Attack.setText(""+chosen[currentTarget].atk);
    	Defense.setText(""+chosen[currentTarget].def);
    	SpAtk.setText(""+chosen[currentTarget].spAtk);
    	SpDef.setText(""+chosen[currentTarget].spDef);
    	Speed.setText(""+chosen[currentTarget].spd);

    	AtkFinal.setText(""+chosen[currentTarget].atk);
    	DefFinal.setText(""+chosen[currentTarget].def);
    	SpAtkFinal.setText(""+chosen[currentTarget].spAtk);
    	SpDefFinal.setText(""+chosen[currentTarget].spDef);
    	SpdFinal.setText(""+chosen[currentTarget].spd);		//i dont ever wanna feeeeeeeeeel like i did that day
    	HPFinal.setText(""+chosen[currentTarget].maxHP);	//take me to the place i love, take me all the waaaaaaay
    	
    	
    	finalStats=chosen[currentTarget].atk+chosen[currentTarget].def+chosen[currentTarget].spAtk+
    			+chosen[currentTarget].spDef+chosen[currentTarget].spd+chosen[currentTarget].maxHP;
    	finalIV=chosen[currentTarget].atkIV+chosen[currentTarget].defIV+chosen[currentTarget].spAtkIV+
				+chosen[currentTarget].spDefIV+chosen[currentTarget].spdIV+chosen[currentTarget].HPIV;
    	finalEV=chosen[currentTarget].atkEV+chosen[currentTarget].defEV+chosen[currentTarget].spAtkEV+
				+chosen[currentTarget].spDefEV+chosen[currentTarget].spdEV+chosen[currentTarget].HPEV;
    	finalBase=chosen[currentTarget].baseAtk+chosen[currentTarget].baseDef+chosen[currentTarget].baseSpAtk+
    			+chosen[currentTarget].baseSpDef+chosen[currentTarget].baseSpd+chosen[currentTarget].baseHP;

    	//so you think you can love me and leave me to die?
		//oooooooooooooooooh baby, can't do this to me baby
		//just gotta get out
		//just gotta get right out of here
		//*wah wah wah wah wah-wah wah, wah wah wah wawawah....*
    	
    	FinalTotal.setText(""+finalStats);
    	FinalIV.setText(""+finalIV);
    	FinalEV.setText(""+finalEV);		//whoa-oh-oh-ohhhhh, sweet child o' mine,
    	FinalBase.setText(""+finalBase);	//whoa-oh-oh-oh, sweet love o' miiiine.
    	

    	int color;
    	
    	if(finalIV>MAX_IV)
    		color=res.getColor(R.color.Red);
    	else if(finalIV>(MAX_IV*.85))
    		color=res.getColor(R.color.Yellow);
    	else
    		color=res.getColor(R.color.Default);
    	
    	FinalIV.setTextColor(color);
    	
    	if(finalEV>MAX_EV)
    		color=res.getColor(R.color.Red);
    	else if(finalEV>(MAX_EV*.85))
    		color=res.getColor(R.color.Yellow);
    	else
    		color=res.getColor(R.color.Default);

    	FinalEV.setTextColor(color);
	}
	
	public static String typeToString(int type)
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
	
	public class ImageAdapter extends BaseAdapter
	{
	    int mGalleryItemBackground;
	    private Context mContext;

	    public ImageAdapter(Context c)
	    {
	        mContext = c;
	    }

		public int getCount()
	    {
	        return PokemonCustomizer.chosen.length;
	    }

	    public Object getItem(int position)
	    {
	        return position;
	    }

	    public long getItemId(int position)
	    {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent)
	    {
	        ImageView i = new ImageView(mContext);
	        i.setImageBitmap(PokemonCustomizer.chosen[position].getFrontSprite(mContext.getAssets()));
	        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
	        i.setBackgroundResource(mGalleryItemBackground);
	        return i;
	    }
	}
}