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

public class ItemsListView extends ListView implements RESTTask.Listener
{
    private final String TAG = "ItemsListView";
    private boolean prefetch;
    private boolean have_all_items;
    private String uri;
    private Object task;
    private ArrayAdapter<Item> adapter;

    private Listener listener;

    public interface Listener
    {
	void onPrefetchFinished();
    }

    public ItemsListView(Context ctx, String uri, Listener listener)
    {
	super(ctx);
	ListView.LayoutParams lp;
	have_all_items = false;

	this.uri = uri;
	this.listener = listener;

	// create and set item adapter
	ArrayList<Item> items = new ArrayList<Item>();
	adapter = new ItemAdapter(ctx, items);
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
		    onLoadMoreItems(offset, limit);
		}
	    });

	// Add item click listener
	this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int pos, long id)
		{
		    onItemPress(pos, id);
		}
	    });

	// Add item long click listener
	this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
		{
		    return onItemLongPress(pos, id);
		}
	    });

	prefetch = true;
	onLoadMoreItems((short)0, (short)20);
    }

    public void onItemPress(int pos, long id)
    {
	// launch view intent of item uri
	ArrayAdapter adapter = (ArrayAdapter) this.getAdapter();
	Item item = (Item) adapter.getItem(pos);
	if (item == null)
	    return;

	String value;
	value = item.get("type");
	if (value != null && value.equals("folder"))
	{
	    Intent intent = new Intent((Activity)this.getContext(), BrowseActivity.class);
	    Bundle args = new Bundle();
	    args.putParcelable("item", item);
	    intent.putExtras(args);
	    this.getContext().startActivity(intent);
	}
	else
	{
	    value = item.get("uri");
	    if (value != null)
	    {
		Uri uri = Uri.parse(value);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setData(uri);
		this.getContext().startActivity(intent);
	    }
	    else
	    {
		// TODO: report using toast i think...
	    }
	}
    }

    public boolean onItemLongPress(int pos, long id)
    {
	// launch view intent of item uri
	ArrayAdapter adapter = (ArrayAdapter) this.getAdapter();
	Item item = (Item) adapter.getItem(pos);
	if (item == null)
	    return false;

	// launch item detail activity
	Intent intent = new Intent((Activity)this.getContext(), ItemDetailsActivity.class);
	Bundle args = new Bundle();
	args.putParcelable("item", item);
	intent.putExtras(args);
	this.getContext().startActivity(intent);
	return true;
    }

    public void onLoadMoreItems(short offset, short limit)
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
	ArrayList<Item> items;

	if (response.code != 200)
	    return;

	try
	{
	    result = new JSONArray(response.data);
	    items = Item.fromJson(this.getContext(), result);

	    if (items.isEmpty())
	    {
		have_all_items = true;
		return;
	    }

	    // Prevent notify on change when adding items
	    adapter.setNotifyOnChange(false);
	    adapter.addAll(items);
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

	if (prefetch)
	{
	    prefetch = false;
	    if (listener != null)
		listener.onPrefetchFinished();
	}

	// Notify data change on main ui thread
	adapter.notifyDataSetChanged();
    }

}
