/*
 * The pokemon.sqlite database was made primarily by Hercules Papatheodorou a.k.a Arty2 under a
 * Creative Commons License. Shawn Walton, Matt Jordan and Josh DeMent turned it into a SQLite database,
 * added to it, interpreted it through code, and wrote this .java file. He did an excellent
 * job and deserves a round of applause.
 * 
 * The exact rights:
 * You are free:
to copy, distribute, display, and perform the work
to make derivative works
Under the following conditions:
Attribution. You must give the original author credit.
Noncommercial. You may not use this work for commercial purposes.
Share Alike. If you alter, transform, or build upon this work, you may distribute the resulting work only under a license identical to this one.
For any reuse or distribution, you must make clear to others the license terms of this work. Any of these conditions can be waived if you get permission from the copyright holder.

Modification of this Program's credits is disallowed.
 */

package com.games.pokedroid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class SQLSource
{
	public static Context ctext;
	static SQLiteDatabase db;
	static String DATABASE_NAME = "pokemon.sqlite";
	static boolean hasBeenLoaded=false;
	
	public static void loadDb(Context context) throws IOException,SQLiteException
	{
		if(hasBeenLoaded)
			return;
		//Close any old db handle
		if (db != null && db.isOpen())
			db.close();
		db=null;
		BufferedInputStream buf = new BufferedInputStream(context.getAssets().open("pokemon.sqlite"));
		FileOutputStream fos=context.openFileOutput(DATABASE_NAME, Context.MODE_PRIVATE);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = buf.read(buffer)) > 0)
			fos.write(buffer, 0, length);
		fos.flush();
		fos.getFD().sync();
		fos.close();
		buf.close();
    	
		File file=context.getFileStreamPath(DATABASE_NAME);
    	
		db=SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		hasBeenLoaded=true;
	}
	
	public static void setContext(Context c)
	{
		ctext = c;
	}
	
	public static ArrayList<Integer> getIntList(int id, int row, String table) {
		ArrayList<Integer> results = new ArrayList<Integer>();
		try {
			Cursor r = db.rawQuery("SELECT * FROM " + table + " WHERE _id="+id, null);
			
			if(r.moveToFirst()){
			
				results.add(r.getInt(row));
			
			while(r.moveToNext()) {
				results.add(r.getInt(row));
			}
			}
			r.close();
		} catch (Exception e) {
			Log.e("pokedroid", e.toString());
		}
		return results;
	}
	
	public static int getInt(int index, int column, String table)	//used for getting data from DB in int form (stats)
	{
		int results = -1;
		try
		{
			Cursor r = db.rawQuery("SELECT * FROM "+table+" WHERE _id="+index, null);
			
			r.moveToFirst();
			
			results=r.getInt(column);
			r.close();
		}
		catch (Exception e)
		{
			Log.e("pokedroid", e.toString());
		}

		return results;
	}
	
	public static String getString(int index, int column, String table)	//used for getting data from DB in string form (names, probably)
	{
		String results="ERROR";
		try
		{
			Cursor r = db.rawQuery("SELECT * FROM "+table+" WHERE _id="+index, null);

			r.moveToFirst();
			
			results=r.getString(column);
			r.close();
		}
		catch (Exception e)
		{
			Log.e("pokedroid", e.toString());
		}

		return results;
	}
}