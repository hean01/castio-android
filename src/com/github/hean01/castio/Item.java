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
    public String title;
    public String description;
    public String type;
    public String uri;
    Bitmap image;

    public Item(Context ctx, JSONObject object)
    {
	JSONObject md;
	try
	{
	    // get item properties
	    if (object.has("uri"))
		this.uri = object.getString("uri");

	    if (object.has("type"))
		this.type = object.getString("type");

	    // parse metadata
	    if (object.has("metadata"))
	    {
		md = object.getJSONObject("metadata");
		if (md.has("title"))
		    this.title = md.getString("title");
		if (md.has("description"))
		    this.description = md.getString("description");

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
	out.writeString(title);
	out.writeString(description);
	out.writeString(type);
	out.writeString(uri);
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
	title = in.readString();
	description = in.readString();
	type = in.readString();
	uri = in.readString();
    }
};
