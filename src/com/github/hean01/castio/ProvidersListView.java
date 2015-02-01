package com.github.hean01.castio;

import java.util.ArrayList;;

import android.net.Uri;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;

import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.AttributeSet;

import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import android.os.Bundle;

public class ProvidersListView extends ListView implements RESTTask.Listener
{
    private final String TAG = "ProvidersListView";

    private boolean have_all_items;
    private String uri;
    private Object task;
    private ArrayAdapter<Provider> adapter;

    public ProvidersListView(Context ctx)
    {
	super(ctx);
	have_all_items = false;
	ListView.LayoutParams lp;

	this.uri = "/providers";

	// create and set provider adapter
	ArrayList<Provider> providers = new ArrayList<Provider>();
	adapter = new ProviderAdapter(ctx, providers);
	this.setAdapter(adapter);

	// Setup layout
	lp = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,
				       ListView.LayoutParams.MATCH_PARENT);
	this.setLayoutParams(lp);

	// Add endless scroll listener
	this.setOnScrollListener(new InfiniteScrollListener() {
		@Override
		public void onLoadMore(short offset, short limit)
		{
		    if (task != null) return;
		    onLoadMoreProviders(offset, limit);
		}
	    });

	// Add provider click listener
	this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int pos, long id)
		{
		    onProviderPress(pos, id);
		}
	    });

	// Add provider long click listener
	this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
		{
		    return onProviderLongPress(pos, id);
		}
	    });

	// pre fetch providers the the list
	onLoadMoreProviders((short)0, (short)20);
    }

    public void onProviderPress(int pos, long id)
    {
	// launch view intent of uri
	ArrayAdapter adapter = (ArrayAdapter) this.getAdapter();
	Provider provider = (Provider) adapter.getItem(pos);
	if (provider == null)
	    return;

	Intent intent = new Intent((Activity)this.getContext(), BrowseActivity.class);
	Bundle args = new Bundle();
	args.putParcelable("provider", provider);
	intent.putExtras(args);
	this.getContext().startActivity(intent);
    }

    public boolean onProviderLongPress(int pos, long id)
    {
	// launch provider detail activity
	ArrayAdapter adapter = (ArrayAdapter) this.getAdapter();
	Provider provider = (Provider) adapter.getItem(pos);
	if (provider == null)
	    return false;

	Intent intent = new Intent((Activity)this.getContext(), ProviderDetailsActivity.class);
	Bundle args = new Bundle();
	args.putParcelable("provider", provider);
	intent.putExtras(args);
	this.getContext().startActivity(intent);
	return true;
    }

    public void onLoadMoreProviders(short offset, short limit)
    {
	if (have_all_items)
	    return;


	Bundle query = new Bundle();
	query.putShort("offset", offset);
	query.putShort("limit", limit);

	Bundle args = ServiceRESTTask.arguments(this.getContext(), uri, query);

	task = new ServiceRESTTask(this).execute(args);
    }

    // RESTTask.Listener impl

    // This is called on task thread
    public void onResponse(RESTTask.Response response)
    {
	JSONArray result;
	ArrayList<Provider> providers;

	if (response.code != 200)
	    return;

	try
	{
	    result = new JSONArray(response.data);
	    providers = Provider.fromJson(this.getContext(), result);

	    if (providers.isEmpty())
	    {
		have_all_items = true;
		return;
	    }

	    // Prevent notify on change when adding providers
	    adapter.setNotifyOnChange(false);
	    adapter.addAll(providers);
	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	}
    }

    // This is called on main thread
    public void onComplete()
    {
	task = null;

	// Notify data change on main ui thread
	adapter.notifyDataSetChanged();
    }

}
