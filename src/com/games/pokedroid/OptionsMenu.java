package com.games.pokedroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class OptionsMenu extends PreferenceActivity
{
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		addPreferencesFromResource(R.xml.settings);
	}
}