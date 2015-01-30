package com.github.hean01.castio;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;


import java.util.Queue;

import android.widget.LinearLayout;

public class ItemDetailsActivity extends MainActivity {
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
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	TextView tv;

	// get item from extras
	Bundle args = getIntent().getExtras();
	item = args.getParcelable("item");

	// Set action bar title and description for this activity
	ActionBar ab = getActionBar();
	ab.setTitle(item.title);
	ab.setSubtitle(item.description);

	// Get content view and add ItemListView
	LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view = inflater.inflate(R.layout.item_details, null, false);
	setContentView(view);

	// Set data into view
	tv = (TextView)view.findViewById(R.id.title); tv.setText(item.title);
	tv = (TextView)view.findViewById(R.id.description); tv.setText(item.description);

	// FIXME: parcelable item doesn't have a bitmap
	// view.findViewById(R.id.ivImage)setImageBitmap(item.image);

	// Populate dynamic item metadata
	LinearLayout md = (LinearLayout)view.findViewById(R.id.metadata);
	if (item.artist != null)
	    md.addView(createMetadataView("Artist", item.artist, md));
	if (item.year != null)
	    md.addView(createMetadataView("Year", item.year, md));
	if (item.on_air != null)
	    md.addView(createMetadataView("Listeners", item.listeners, md));

	if (item.on_air != null)
	    md.addView(createMetadataView("On Air", item.on_air, md));


    }
}
