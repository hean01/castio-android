package com.github.hean01.castio;


import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;

import android.content.Context;
import android.os.Parcelable;
import android.os.Parcel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Item implements Parcelable
{
    /* TODO: This is unbareable, consider to store the JSONObject
       instead of each string member which also make implementation of
       parcelable a lot easier. Just write the json text instead of
       each string data.
     */
    public String title;
    public String description;
    public String type;
    public String uri;
    public String artist;
    public String listeners;
    public String on_air;
    public String year;

    Bitmap image;

    public Item(Context ctx, JSONObject object)
    {
	JSONObject md;
	try
	{
	    // get item properties
	    if (object.has("type"))
		this.type = object.getString("type");

	    if (object.has("uri"))
		this.uri = object.getString("uri");

	    // parse metadata
	    if (object.has("metadata"))
	    {
		md = object.getJSONObject("metadata");

		// Get fields for all types
		if (md.has("title"))
		    this.title = md.getString("title");
		if (md.has("description"))
		    this.description = md.getString("description");
		if (md.has("year"))
		    this.year = md.getString("year");

		// Get music metadata
		if (md.has("artist"))
		    this.artist = md.getString("artist");

		// Get radiostation metadata
		if (md.has("listeners"))
		    this.listeners = md.getString("listeners");

		if (md.has("on_air"))
		    this.on_air = md.getString("on_air");

		// Try load image from specified url
		if (md.has("image"))
		{
		    try
		    {
			Integer crop = 4;
			URL url = new URL(md.getString("image"));
			image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			image = Bitmap.createBitmap(image, crop, crop,
						    image.getWidth() - crop*2,
						    image.getHeight() - crop*2);
		    }
		    catch (MalformedURLException e)
		    {
			e.printStackTrace();
		    }
		    catch (IOException e)
		    {
			e.printStackTrace();
		    }
		}

	    }

	    if (image == null)
	    {
		image = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon);
	    }
	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	}
    }

    // Factory creating list of objects from JSONArray
    public static ArrayList<Item> fromJson(Context ctx, JSONArray objects)
    {
	ArrayList<Item> items = new ArrayList<Item>();
	for (int i = 0; i < objects.length(); i++)
	{
	    try
	    {
		items.add(new Item(ctx, objects.getJSONObject(i)));
	    }
	    catch (JSONException e)
	    {
		e.printStackTrace();
	    }
	}
	return items;
    }

    @Override
    public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	// TODO Auto-generated method stub
	out.writeString(type);
	out.writeString(uri);

	out.writeString(title);
	out.writeString(description);
	out.writeString(year);

	out.writeString(artist);

	out.writeString(listeners);
	out.writeString(on_air);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
	public Item createFromParcel(Parcel in) {
	    return new Item(in);
	}

	public Item[] newArray(int size) {
	    return new Item[size];
	}
    };

    private Item(Parcel in) {
	type = in.readString();
	uri = in.readString();

	title = in.readString();
	description = in.readString();
	year = in.readString();

	artist = in.readString();

	listeners = in.readString();
	on_air = in.readString();
    }
};
