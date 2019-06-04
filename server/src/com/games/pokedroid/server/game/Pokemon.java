/*
 * This is just a dummy class to reflect the Pokemon object from the actual game. Make sure it's up to date and exists in your classpath.
 */

package com.games.pokedroid.server.game;

import java.util.ArrayList;

public class Pokemon extends com.games.pokedroid.game.Pokemon
{	
	private static final long serialVersionUID = -1292342208478362583L;

	public Pokemon(int id)
	{
		super(id);
	}
	
	public Pokemon(Pokemon user, Pokemon target)
	{
		super(user, target);
	}

	public Pokemon(String s)
	{
		super(s);
	}

	public int getInt(int index, int column, String table)
	{
		return SQLSource.getInt(index, column, table);
	}
	
	public String getString(int index, int column, String table)
	{
		return SQLSource.getString(index, column, table);
	}
	
	public ArrayList<Integer> getIntList(int index, int column, String table)
	{
		return SQLSource.getIntList(index, column, table);
	}
}
