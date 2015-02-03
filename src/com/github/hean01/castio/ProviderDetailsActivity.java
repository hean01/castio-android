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

public class ProviderDetailsActivity extends MainActivity
{
    private final static String TAG = "ProviderDetailsActivity";
    private Provider provider;

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
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// get item from extras
	Bundle args = getIntent().getExtras();
	provider = args.getParcelable("provider");

	// Set action bar title and description for this activity
	ActionBar ab = getActionBar();
	ab.setTitle(provider.get("name"));
	ab.setSubtitle(provider.get("description"));

	// Get content view and add ItemListView
	LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view = inflater.inflate(R.layout.provider_details, null, false);
	setContentView(view);

	// Set data into view
	TextView tv;
	tv = (TextView)view.findViewById(R.id.name); tv.setText(provider.get("name"));
	tv = (TextView)view.findViewById(R.id.description); tv.setText(provider.get("description"));

	// Set image
	ImageView iv = (ImageView)view.findViewById(R.id.image); iv.setImageBitmap(provider.getImage());

	// Populate provider
	LinearLayout md = (LinearLayout)view.findViewById(R.id.metadata);

	String value;
	value = provider.getVersion();
	if (value != null)
	    md.addView(createMetadataView("Version", value, md));

	value = provider.get("copyright");
	if (value != null)
	    md.addView(createMetadataView("Copyright", value, md));

	value = provider.get("homepage");
	if (value != null)
	    md.addView(createMetadataView("Homepage", value, md));

    }
}
