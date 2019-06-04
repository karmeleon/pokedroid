package com.games.pokedroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.util.ByteArrayBuffer;

import com.games.pokedroid.game.Pokemon;
import com.games.pokedroid.multiplayer.MPMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class PokeDroid extends Activity
{    
    public Context PokeDroid=this;
    
    private AlertDialog start, noNewUpdate, noConnection;
    
    public static boolean nixonMode=false;
    
    public static AssetManager am;
    
    private ProgressDialog p;
    
    private static final int VERSION=11;
    private static final String CHANGELOG="-Team builder no longer needs to take a few seconds to load/n-Renamed the multiplayer menu option to hopefully clear up some confusion--it isn't actually functional yet; that was just a placeholder.\n-General optimization";
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        am=this.getAssets();
        
        try 
        {
			SQLSource.loadDb(this);
		}
        catch (Exception e)
        {
			Log.e("pokedroid",e.toString());
		}
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Send any comments/questions/concerns/death threats to notverycreative1 [at] gmail [you know the rest]. " +
        		"\n\nNEW IN THIS VERSION!\n\n"+CHANGELOG)
        .setPositiveButton("Awesomesauce.", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	dialog.dismiss();
            }
        });
       start=builder.create();
       
       AlertDialog.Builder b=new AlertDialog.Builder(this);
       b.setMessage("There is no new version of PokeDroid. Sorry!")
       .setPositiveButton("Aww :(", new OnClickListener()
       {
		public void onClick(DialogInterface arg0, int arg1)
		{
			arg0.dismiss();
		}
    	   
       });
       noNewUpdate=b.create();
       
       AlertDialog.Builder bui=new AlertDialog.Builder(this);
       bui.setMessage("You don't have a connection, silly! Get you one before ya try again!")
       .setPositiveButton("Oops! Silly me!", new OnClickListener()
       {
    	   public void onClick(DialogInterface dialog, int arg0)
    	   {
    		   dialog.dismiss();
    	   }
       });
       noConnection=bui.create();
    }
    
    public void onResume()
    {
        super.onResume();
        
    	setContentView(R.layout.mainmenu);
    	
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        nixonMode=prefs.getBoolean("nixonmode", false);
		
		if(prefs.getBoolean("firstrun"+VERSION, true))
		{
			start.show();
	        SharedPreferences.Editor editor=prefs.edit();
	        editor.putBoolean("firstrun"+VERSION, false);
	        editor.commit();
		}
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menuabout:
            about(null);
            return true;
        case R.id.menusettings:
            onSettingsSelect(null);
            return true;
        //case R.id.menubugreport:
        	//bugReport(null);
        	//return true;
        case R.id.menuupdate:
        	update(null);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onSendMove(View v)
    {
    	try {
			sendMove();
		} catch (IOException e) {
			Log.e("pokedroid",e.toString());
		}
    }
    
    private void sendMove() throws IOException
    {
        Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int PORT=1070;
        
        String i="192.168.1.99";
        
        try
        {
        	Log.e("pokedroid","trying");
            kkSocket = new Socket(""+i, PORT);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        }
        catch (UnknownHostException e)
        {
            Log.e("pokedroid","Unknown host: "+i);
            return;
        }
        catch (IOException e)
        {
            Log.e("pokedroid","Couldn't connect to server at "+i);
            return;
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        
        out.println(new Pokemon(69).writeToString());
        
        /*
        while ((fromServer = in.readLine()) != null)
        {
            Log.e("pokedroid","Server: " + fromServer);
            if (fromServer.equals("Bye."))
                break;
		  
            fromUser = new Move(69).writeToString();
		    if (fromUser != null)
		    {
		    	Log.e("pokedroid","Client: " + fromUser);
		    	out.println(fromUser);
		    }
        }*/

        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
    }
    
    public void update(View v)
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(this);
    	p=new ProgressDialog(this);
    	p.setMessage("Downloading update...");
    	p.setIndeterminate(false);
    	
    	b.setMessage("This will update the app to the newest nightly. It may be buggier than the official ones on the SourceForge page, " +
    			"but bugs may be fixed and new features may be added. Update anyway?")
    	.setPositiveButton("Sure.", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				new DownloadAndInstall().execute("http://pokedroid.cvs.sourceforge.net/viewvc/pokedroid/PokeDroid/bin/PokeDroid.apk", "PokeDroid.apk");
				p.show();
			}
    	})
    	.setNegativeButton("Nah.", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
    	})
    	.show();
    }
    
    public void onQBSelect(View v)
    {
    	PokeDroid.this.startActivity(new Intent(PokeDroid.this, SPMenu.class));
    }
    
    public void onMPSelect(View v)
    {
    	PokeDroid.this.startActivity(new Intent(PokeDroid.this, MPMenu.class));
    }
    
    public void onSettingsSelect(View v)
    {
    	PokeDroid.this.startActivity(new Intent(PokeDroid.this, OptionsMenu.class));
    }
    
    public void about(View v)
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(this);
    	b.setMessage("This app was programmed by Shawn Walton, Matt Jordan, and Josh DeMent as an AP Computer Science final project. It's done entirely in our free time, and is totally open-source." +
    			"You can grab the sources at pokedroid.cvs.souceforge.net. Donate to us at the button below if you think it's worth it; it won't go unappreciated! Send us bug reports to make us happy :)");
    	b.setPositiveButton("Donate!", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
		    	Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://sourceforge.net/donate/index.php?group_id=513156"));
		    	startActivity(browserIntent);
			}
    	})
    	.setNegativeButton("Close", new OnClickListener()
    	{
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
    	})
    	.show();
    }
    
    public void bugReport(View v)
    {
    	Process mLogcatProc = null;
    	BufferedReader reader = null;
    	String text="";
    	try
    	{
    		mLogcatProc = Runtime.getRuntime().exec("logcat -d pokedroid:I AndroidRuntime:E *:S");

    		reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

    	 	String line;
    	 	final StringBuilder log = new StringBuilder();

    		while ((line = reader.readLine()) != null)
    	    {
    			log.append(line);
    			log.append("\n");
    	    }
    		text=log.toString();
    	}
    	catch (IOException e)
    	{
    		Log.e("pokedroid", e.toString());
    	}
    	finally
    	{
    		if (reader != null)
    	    try
    	    {
    	    	reader.close();
    	    }
    	    catch (IOException e)
    	    {
    	    	Log.e("pokedroid", e.toString());
    	    }
    	} 
    	String prompt="Describe the problem.\n\nWhat were you doing when it happened?\n\nWhat should have happened vs. what happened?\n\nWhat version of the app are you running? If you updated to the newest nightly, when did you do so?\n\nThank you, your feedback is greatly appreciated!\n";
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	emailIntent.setType("plain/text");
    	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"notverycreative1@gmail.com"});
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PokeDroid Bug Report");
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, prompt+"DO NOT MODIFY TEXT BELOW HERE\n\n"+text);
    	this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    
    public void onBuilderSelect(View v)
    {
    	PokeDroid.this.startActivity(new Intent(PokeDroid.this, TeamManager.class));
    }
    
    private class DownloadAndInstall extends AsyncTask<String, Integer, Boolean>
    {
        protected Boolean doInBackground(String... derp)
        {
        	String ur=derp[0];
        	String fileName=derp[1];
        	ByteArrayBuffer baf = new ByteArrayBuffer(50);
        	
            try
            {
                URL url = new URL(ur);
                URLConnection ucon = null;
                ucon = url.openConnection();

                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                int current = 0;
                int updateCount=0;
                while ((current = bis.read()) != -1)
                {
                	if(updateCount==512)
                	{
                		publishProgress(baf.length());
                		updateCount=0;
                	}
                    baf.append((byte) current);
                    updateCount++;
                }

                FileOutputStream fos = PokeDroid.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                fos.write(baf.toByteArray());
                fos.close();

            } catch (Exception e) {
                Log.e("pokedroid", e.toString());
            }
        	
            MessageDigest digest = null;
			try {
				digest = java.security.MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				Log.e("pokedroid", e.toString());
			}
            digest.update(baf.toByteArray());
            byte[] h = digest.digest();
            
            if(baf.length()==0)
            	return null;
            String[] fileList=fileList();
            boolean exists=false;
            for(String i:fileList)
            	if(i.equals("updatehash.md5"))
            		exists=true;
            
            String newHash=new String(h);
            Log.e("pokedroid", "new="+newHash);
            
            if(exists)
            {
            	try
            	{
					String oldHash=loadObject("updatehash.md5");
					Log.e("pokedroid", "old="+oldHash);
					if(oldHash.equals(newHash))
						return false;
					else
						saveObject(newHash, "updatehash.md5");
				}
            	catch (Exception e)
            	{
					Log.e("pokedroid",e.toString());
				}
            }
            else
            {
            	try {
					saveObject(newHash, "updatehash.md5");
				} catch (IOException e) {
					Log.e("pokedroid",e.toString());
				}
            }
			return true;
        }

        protected void onProgressUpdate(Integer...integers)
        {
        	p.setMessage("Downloading update...\n"+integers[0]/1000+"kb downloaded so far.");
        }
        
        protected void onPostExecute(Boolean b)
        {
        	if(b==null)
        	{
        		noConnection.show();
        		deleteFile("PokeDroid.apk");
        		p.dismiss();
        		return;
        	}
        	if(!b)
        		noNewUpdate.show();
        	else
        	{
            	Intent intent=new Intent();
            	intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file:///data/data/com.games.pokedroid/files/PokeDroid.apk"), "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);
        	}
        	p.dismiss();
        }
        
        public void saveObject(String obj, String filename) throws IOException
        {
        	FileOutputStream fos = openFileOutput(filename,Context.MODE_PRIVATE);
        	ObjectOutputStream out = new ObjectOutputStream(fos);
        	out.writeObject(obj);
        	out.close();
        	fos.close();
        }
        
        public String loadObject(String filename) throws StreamCorruptedException, IOException, ClassNotFoundException
        {
        	FileInputStream fis=openFileInput(filename);
        	ObjectInputStream in=new ObjectInputStream(fis);
        	String out=(String) in.readObject();
        	in.close();
        	fis.close();
        	return out;
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent result)
    {
    	deleteFile("PokeDroid.apk");
    }
}