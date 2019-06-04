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

package com.games.pokedroid.server.game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SQLSource
{
	public static Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:pokemon.sqlite", "", "");
		} catch (Exception e) {
			System.err.println("Oh shit. Errors");
			return null;
		}
	}
	
	public static ArrayList<Integer> getIntList(int id, int row, String table)
	{
		ArrayList<Integer> results = new ArrayList<Integer>();
		try
		{
			Connection c= getConnection();
			PreparedStatement p = c.prepareStatement("SELECT * FROM "+table+" WHERE _id="+id);
			ResultSet rows = p.executeQuery();
			while(rows.next()) {
				results.add(rows.getInt(row));
			}
			rows.close();
			p.close();
			c.close();
			
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return results;
	}
	
	public static int getInt(int index, int column, String table)
	{
		int result=-1;
		try
		{
			Connection c=getConnection();
			PreparedStatement p=c.prepareStatement("SELECT * FROM "+table+" WHERE _id="+index);
			ResultSet rows=p.executeQuery();
			while(rows.next())
				result = rows.getInt(column);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
		}
		return result;
	}
	
	public static String getString(int index, int column, String table)
	{
		String result="ERROR";
		try
		{
			Connection c=getConnection();
			PreparedStatement p=c.prepareStatement("SELECT * FROM "+table+" WHERE _id="+index);
			ResultSet rows=p.executeQuery();
			while(rows.next())
				result=rows.getString(column);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
		}
		return result;
	}
}