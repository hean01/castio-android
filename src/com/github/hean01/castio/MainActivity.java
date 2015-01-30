package com.github.hean01.castio;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;

import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private LinearLayout content;
    private final static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Set action bar title and description for this activity
	ActionBar ab = getActionBar();
	ab.setTitle("CAST.IO");
	ab.setSubtitle("The source for your media...");

	// Get content view and add ItemListView
	setContentView(R.layout.front_layout);
	content = (LinearLayout) findViewById(R.id.container);

	content.addView(new ItemsListView(this, "/providers/kwed"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.main_actions, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch(item.getItemId())
	{
	case R.id.action_prefs:
	    startActivity(new Intent(this, PreferenceActivity.class));
	    return true;

	case R.id.action_search:
	    // TODO: start search activity
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
