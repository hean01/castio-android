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

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	Bundle args = getIntent().getExtras();
	item = args.getParcelable("item");

	// Set action bar title and description for this activity
	ActionBar ab = getActionBar();
	ab.setTitle(item.title);
	ab.setSubtitle(item.description);

	// Get content view and add ItemListView
	setContentView(R.layout.front_layout);
	LinearLayout content = (LinearLayout) findViewById(R.id.container);
	content.addView(new ItemsListView(this, item.uri));
    }
}
