package com.games.pokedroid.server.game;

import java.util.ArrayList;

public class Move extends com.games.pokedroid.game.Move
{
	private static final long serialVersionUID = -8907736163398906577L;

	public Move(int id)
	{
		super(id);
	}
	
	public Move(String durr)
	{
		super(durr);
	}

	public int getInt(int index, int column, String table)
	{
		return SQLSource.getInt(index, column, table);
	}
	
	public String getString(int index, int column, String table)
	{
		return SQLSource.getString(index, column, table);
	}
	
	public ArrayList<Integer> getIntList(int id, int row, String table)
	{
		return SQLSource.getIntList(id, row, table);
	}
}