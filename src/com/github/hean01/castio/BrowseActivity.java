package com.github.hean01.castio;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;


import java.util.Queue;

import android.widget.LinearLayout;

public class BrowseActivity extends MainActivity {
    private final static String TAG = "BrowseActivity";
    private Item item;
    private Provider provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActionBar ab = getActionBar();
	Bundle args = getIntent().getExtras();
	item = args.getParcelable("item");
	provider = args.getParcelable("provider");

	// Get content view and add ItemListView
	setContentView(R.layout.front_layout);
	LinearLayout content = (LinearLayout) findViewById(R.id.container);

	if (item != null)
	{
	    ab.setTitle(item.get("metadata.title"));
	    ab.setSubtitle(item.get("metadata.description"));
	    content.addView(new ItemsListView(this, item.get("uri")));
	}
	else if(provider != null)
	{
	    ab.setTitle(provider.get("name"));
	    ab.setSubtitle(provider.get("description"));
	    content.addView(new ItemsListView(this, "/providers/" + provider.get("id")));
	}
    }
}
