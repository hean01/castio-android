package com.github.hean01.castio;

import android.os.Bundle;
import android.app.ActionBar;

public class PreferenceActivity extends android.preference.PreferenceActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	ActionBar ab = getActionBar();
	ab.setTitle("Preferences");
	ab.setSubtitle("Setup service connection...");

        super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
    }
}
