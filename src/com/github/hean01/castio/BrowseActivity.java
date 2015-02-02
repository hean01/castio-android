package com.github.hean01.castio;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import java.util.Queue;

import android.widget.LinearLayout;

public class BrowseActivity extends MainActivity
{
    private final static String TAG = "BrowseActivity";
    private Item item;
    private ItemsListView items;
    private Provider provider;
    private LinearLayout loader;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	ActionBar ab = getActionBar();
	Bundle args = getIntent().getExtras();
	item = args.getParcelable("item");
	provider = args.getParcelable("provider");

	// Get content view and add ItemListView
	setContentView(R.layout.front_layout);
	final LinearLayout content = (LinearLayout) findViewById(R.id.container);

	// Create listeners for prefetch
	ItemsListView.Listener listener = new ItemsListView.Listener() {
		@Override
		public void onPrefetchFinished()
		{
		    // remove loader animation and show items
		    content.removeView(loader);
		    content.addView(items);
		}
	    };

	if (item != null)
	{
	    ab.setTitle(item.get("metadata.title"));
	    ab.setSubtitle(item.get("metadata.description"));
	    items = new ItemsListView(this, item.get("uri"), listener);
	}
	else if(provider != null)
	{
	    ab.setTitle(provider.get("name"));
	    ab.setSubtitle(provider.get("description"));
	    items = new ItemsListView(this, "/providers/" + provider.get("id"), listener);
	}

	// setup and add a loader animation to view
	loader = (LinearLayout) getLayoutInflater().inflate(R.layout.loading, null);
	ImageView image = (ImageView) loader.findViewById(R.id.image);
	Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
	image.startAnimation(anim);
	content.addView(loader);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Make actionbar HomeAsUp act as back button.
	if (item.getItemId() == android.R.id.home)
	{
	    onBackPressed();
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }
}
