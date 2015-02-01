package com.github.hean01.castio;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;


import java.util.Queue;

import android.widget.LinearLayout;

public class ItemDetailsActivity extends MainActivity
{
    private final static String TAG = "ItemDetailsActivity";
    private Item item;

    private View createMetadataView(String name, String value, ViewGroup parent)
    {
	TextView tv;
	LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view = inflater.inflate(R.layout.metadata, parent, false);
	tv = (TextView)view.findViewById(R.id.name);  tv.setText(name + ":");
	tv = (TextView)view.findViewById(R.id.value); tv.setText(value);
	return view;
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	TextView tv;

	// get item from extras
	Bundle args = getIntent().getExtras();
	item = args.getParcelable("item");

	// Set action bar title and description for this activity
	ActionBar ab = getActionBar();
	ab.setTitle(item.get("metadata.title"));
	ab.setSubtitle(item.get("metadata.description"));

	// Get content view and add ItemListView
	LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view = inflater.inflate(R.layout.item_details, null, false);
	setContentView(view);

	// Set data into view
	tv = (TextView)view.findViewById(R.id.title); tv.setText(item.get("metadata.title"));
	tv = (TextView)view.findViewById(R.id.description); tv.setText(item.get("metadata.description"));

	// Set image
	ImageView iv = (ImageView)view.findViewById(R.id.image); iv.setImageBitmap(item.getImage());

	// Populate dynamic item metadata
	LinearLayout md = (LinearLayout)view.findViewById(R.id.metadata);

	String value;
	value = item.get("metadata.length");
	if (value != null)
	    md.addView(createMetadataView("Length", value, md));

	value = item.get("metadata.artist");
	if (value != null)
	    md.addView(createMetadataView("Artist", value, md));

	value = item.get("metadata.year");
	if (value != null)
	    md.addView(createMetadataView("Year", value, md));

	value = item.get("metadata.listeners");
	if (value != null)
	    md.addView(createMetadataView("Listeners", value, md));

	value = item.get("metadata.episode");
	if (value != null)
	    md.addView(createMetadataView("Episode", value, md));

	value = item.get("metadata.air_date");
	if (value != null)
	    md.addView(createMetadataView("Air date", value, md));

	value = item.get("metadata.on_air");
	if (value != null)
	    md.addView(createMetadataView("On Air", value, md));

    }
}
