package com.games.pokedroid;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.games.pokedroid.game.Pokemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BuilderActivity extends Activity
{
	private Builder build;
	
	public static final int NUMBER_OF_POKEMON=649;
	
	public static ArrayList<Integer> choose=new ArrayList<Integer>();
	
	private ListView ChooserList;
	private ImageView DexImage;
	private TextView Attack;
	private TextView Defense;
	private TextView SpAtk;
	private TextView SpDef;
	private TextView Speed;
	private TextView Name;
	private TextView ID;
	private TextView Types;
	
	AlertDialog tooMany, zero;
	
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.pokemonchooser);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your party's too big! Gonna have to kick some partygoers out to fit that 6 member limit, dawg.")
        .setCancelable(false)
        .setTitle("Uh-oh!")
        .setPositiveButton("I'll get right on that :)", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	dialog.dismiss();
            }
        });
        tooMany=builder.create();
        
        builder.setMessage("Your party's empty! How boring! Add some members!")
        .setCancelable(false)
        .setTitle("Uh-oh!")
        .setPositiveButton("I'll send out the invitations! :D", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	dialog.dismiss();
            }
        });
        zero=builder.create();
        
    	ChooserList=(ListView) findViewById(android.R.id.list);
    	DexImage=(ImageView) findViewById(R.id.imageView1);
    	Attack=(TextView) findViewById(R.id.attack);
    	Defense=(TextView) findViewById(R.id.defense);
    	SpAtk=(TextView) findViewById(R.id.spatk);
    	SpDef=(TextView) findViewById(R.id.spdef);
    	Name=(TextView) findViewById(R.id.buildername);
    	ID=(TextView) findViewById(R.id.idno);
    	Types=(TextView) findViewById(R.id.buildertype);
    	Speed=(TextView) findViewById(R.id.speed);
    	
    	ChooserList.setItemsCanFocus(false);
    	
    	//new LoaderTask().execute();
    	//derp=new ProgressDialog(this);
    	//derp.setMessage("Loading...");
    	//derp.show();
    	choose=new ArrayList<Integer>();
    	
    	setUpAdapter();
	}
    
	public void onButtonClicked(View v)
	{
		int checked=countChecks();
		if(checked>6)
			tooMany.show();
		else if(checked==0)
			zero.show();
		else
		{
			ArrayList<Pokemon> pokemans=new ArrayList<Pokemon>();
			int[] checkedIDs=whichAreChecked();
			for(int i=0; i<checked; i++)
				pokemans.add(new Pokemon(checkedIDs[i]+1));
			Bundle b=new Bundle();
			b.putSerializable("team", pokemans);
			Intent intent=new Intent(BuilderActivity.this, PokemonCustomizer.class).putExtras(b);
			BuilderActivity.this.startActivity(intent);
		}
	}
	
	public int[] whichAreChecked()
	{
		ArrayList<Integer> ints=choose;
		int[] result=new int[ints.size()];
		for(int i=0; i<result.length; i++)
			result[i]=ints.get(i);
		return result;
	}
	
	public void setUpAdapter()
	{
    	if(build==null)
    		build=new Builder(this);
    	build.gimmeAssetManager(getAssets());
    	
    	ChooserList.setAdapter(build);
    	
        ChooserList.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> a, View v, int i, long l)
            {
            	i++;
            	DexImage.setImageBitmap(getFrontSprite(getAssets(), i));
            	Attack.setText(""+SQLSource.getInt(i, Pokemon.ATTACK, "Pokemon"));
            	Defense.setText(""+SQLSource.getInt(i, Pokemon.DEFENSE, "Pokemon"));
            	SpAtk.setText(""+SQLSource.getInt(i, Pokemon.SP_ATK, "Pokemon"));
            	SpDef.setText(""+SQLSource.getInt(i, Pokemon.SP_DEF, "Pokemon"));
            	Name.setText(SQLSource.getString(i, Pokemon.NAME, "Pokemon"));
            	ID.setText("#"+(i+1));
            	Speed.setText(""+SQLSource.getInt(i, Pokemon.SPEED, "Pokemon"));
                if(SQLSource.getInt(i, Pokemon.TYPE2, "Pokemon")==17)
                	Types.setText(typeToString(SQLSource.getInt(i, Pokemon.TYPE1, "Pokemon")));
                else
                	Types.setText(typeToString(SQLSource.getInt(i, Pokemon.TYPE1, "Pokemon"))+"/"+typeToString(SQLSource.getInt(i, Pokemon.TYPE2, "Pokemon")));
            }
            
            private Bitmap getFrontSprite(AssetManager am, int id)	 
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
            
            private String typeToString(int typenum)
        	{
       			switch (typenum)
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
        	
        });
	}
	
	public int countChecks()
	{
		return choose.size();
	}
	
    public static class Builder extends BaseAdapter
    {
    	private static LayoutInflater inflater=null;;
    	private AssetManager am;
    	
    	public Builder(Activity a)
    	{
    		inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}

    	public void gimmeAssetManager(AssetManager please)
    	{
    		am=please;
    	}
    	
    	@Override
    	public int getCount()
    	{
    		return NUMBER_OF_POKEMON;
    	}

    	@Override
    	public Object getItem(int pos)
    	{
    		return pos;
    	}

    	@Override
    	public long getItemId(int pos)
    	{
    		return pos;
    	}
    	
        public static class ViewHolder
        {
            public TextView name;
            public TextView id;
            public TextView type;
            public CheckBox check;
            public ImageView image;
        }

        public boolean areAllItemsEnabled()
        {
        	return true;
        }
        
        public boolean isEnabled(int position)
        {
        	return true;
        }
        
    	public View getView(final int pos, View view, ViewGroup par)
    	{
    		View vi=view;
    		ViewHolder hold;
            
            vi = inflater.inflate(R.layout.listitem, null);
            hold=new ViewHolder();
            hold.name=(TextView) vi.findViewById(R.id.name);
            hold.image=(ImageView) vi.findViewById(R.id.imageView1);
            hold.type=(TextView) vi.findViewById(R.id.type);
            hold.id=(TextView) vi.findViewById(R.id.id);
            hold.check=(CheckBox) vi.findViewById(R.id.check);
            
            if(choose.contains(pos))
            	hold.check.setChecked(true);
            
            hold.check.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
    			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    			{
    				if(isChecked && !choose.contains(pos))
    					BuilderActivity.choose.add(pos);
    				else if(!isChecked && choose.contains(pos))
    					BuilderActivity.choose.remove(new Integer(pos));
    			}
            	
            });
            vi.setTag(hold);
            
            Bitmap frontImg=null;
            
        	try
        	{
        		BufferedInputStream buf = new BufferedInputStream(am.open("front/"+(pos+1)+".png"));
        		frontImg = BitmapFactory.decodeStream(buf);
        		buf.close();
        	}   
        	catch (IOException e)
        	{
        	   Log.e("pokedroid",e.toString());
        	}
        	
            hold.id.setText("#"+(pos+1));
            hold.name.setText(SQLSource.getString(pos+1, Pokemon.NAME, "Pokemon"));
            if(SQLSource.getInt(pos+1, Pokemon.TYPE2, "Pokemon")==17)
            	hold.type.setText(typeToString(SQLSource.getInt(pos+1, Pokemon.TYPE1, "Pokemon")));
            else
            	hold.type.setText(typeToString(SQLSource.getInt(pos+1, Pokemon.TYPE1, "Pokemon"))+"/"+typeToString(SQLSource.getInt(pos+1, Pokemon.TYPE2, "Pokemon")));
            if(frontImg==null)
            	hold.image.setImageResource(R.drawable.nixon);
            else
            	hold.image.setImageBitmap(frontImg);
            return vi;
    	}
    	
        private String typeToString(int typenum)
    	{
   			switch (typenum)
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
    }
}