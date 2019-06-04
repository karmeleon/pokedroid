package com.games.pokedroid.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.games.pokedroid.R;
import com.games.pokedroid.R.id;
import com.games.pokedroid.R.layout;
import com.games.pokedroid.game.Pokemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MPMenu extends Activity
{
	TextView tv;
	Socket socket;
	
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.multiplayermenu);
		
		tv=(TextView) findViewById(R.id.consoleoutput);
	}
	
	public void onResume()
	{
		super.onResume();
	}
	
	public void onConnectPressed(View v)
	{
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.servertextentry, null);
        final EditText edit=(EditText) view.findViewById(R.id.ip_edit);
        
        AlertDialog.Builder d=new AlertDialog.Builder(this);
        d.setTitle("Target IP")
        .setView(view)
        .setPositiveButton("Connect", new OnClickListener()
        {
			public void onClick(DialogInterface arg0, int arg1)
			{
				String host=edit.getText().toString();
				new ClientTask().execute(host);
			}
        })
        .setNegativeButton("Nevermind.", new OnClickListener()
        {
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
			}
        });
        d.show();
	}
	
	public class ClientTask extends AsyncTask<String, String, Void>
	{
		protected Void doInBackground(String... arg0)
		{
			String ip=arg0[0];
			
	        socket = null;
	        PrintWriter out = null;
	        BufferedReader in = null;
	        int PORT=1070;
	        
	        try
	        {
	        	publishProgress("Trying to connect to server at "+ip+"...");
	            socket = new Socket(""+ip, PORT);
	            out = new PrintWriter(socket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            publishProgress("Connected to server at IP " + socket.getInetAddress().getHostAddress()+".");
	        }
	        catch (UnknownHostException e)
	        {
	            Log.e("pokedroid","Unknown host: "+ip);
	            return null;
	        }
	        catch (IOException e)
	        {
	            Log.e("pokedroid","Couldn't connect to server at "+ip);
	            return null;
	        }
	        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	        String fromServer;
	        
		    
	        try
	        {
	        	
				while (true)
				{
					fromServer=in.readLine();
					
				    if (fromServer.equals("Bye."))
				    {
				    	publishProgress("You've been booted by the server. D'aww.");
				        break;
				    }
				    else if(fromServer.equals("pro [pkm]"))
				    {
				    	out.write(new Pokemon(50).writeToString());
				    	publishProgress("Server requested a Pokemon; sent #50.");
				    }
				    else if(fromServer.equals("[png]"))
				    {
				    	out.write("[pon]");
				    }
				    else
				    {
				    	publishProgress(fromServer);
				    	//TODO: others
				    }
				}
				
			}
	        catch (Exception e1)
			{
				Log.e("Pokedroid", e1.toString());
			}
	        
	        out.write(new Pokemon(50).writeToString());
	        
	        try
	        {
		        out.close();
		        in.close();
		        stdIn.close();
		        socket.close();
	        }
	        catch (Exception e)
	        {
	        	Log.e("Pokedroid", e.toString());
	        }
			return null;
		}
		
		public void onProgressUpdate(String...strings)
		{
			tv.append(strings[0]+"\n");
		}
	}
	
	public void onStop()
	{
		super.onStop();
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("[bye]");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO: disconnections
	}
}