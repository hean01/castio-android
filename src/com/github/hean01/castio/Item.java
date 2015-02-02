package com.github.hean01.castio;


import java.io.IOException;
import java.util.ArrayList;
import java.net.URISyntaxException;

import android.content.Context;
import android.os.Parcelable;
import android.os.Parcel;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Item implements Parcelable
{
    private static final String TAG = "Item";
    private JSONObject object;
    private Bitmap image;

    private JSONObject getObject(String parts[])
    {
	JSONObject obj = object;
	for (int i = 0; i < parts.length - 1; i++)
	{
	    obj = obj.optJSONObject(parts[i]);

	    if (obj == null)
		break;
	}

	return obj;
    }

    public String get(String attribute)
    {
	String [] parts = attribute.split("\\.");

	// Check if accessing non recursive attribute
	if (parts.length == 0)
	    return object.optString(attribute);

	// Retreive recursive object
	JSONObject obj = getObject(parts);
	if (obj == null)
	    return null;

	return obj.optString(parts[parts.length - 1], null);
    }

    public Bitmap getImage()
    {
	return image;
    }

    public Item(Context ctx, JSONObject object)
    {
	this.object = object;

	// If image is specified, try load it
	String image_uri = get("metadata.image");
	if (image_uri != null)
	{

	    try
	    {
		Integer crop = 4;
		Service.Response resp = Service.getCache(ctx, image_uri);
		if (resp.statusLine.getStatusCode() == 200)
		{
		    image = BitmapFactory.decodeByteArray(resp.data, 0, resp.data.length);
		    image = Bitmap.createBitmap(image, crop, crop,
						image.getWidth() - crop*2,
						image.getHeight() - crop*2);

		    Float ratio = image.getWidth() / (float)image.getHeight();
		    image = Bitmap.createScaledBitmap(image, 256, (int)Math.round(256/ratio), true);
		}
	    }
	    catch (URISyntaxException e)
	    {
		e.printStackTrace();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}

	// If not image was loaded, get one from resources
	// corresponding to item type
	if (image == null)
	{
	    int res;
	    String type = this.get("type");
	    if (type.equals("folder")) res = R.drawable.folder;
	    else if (type.equals("radiostation")) res = R.drawable.radiostation;
	    else res = R.drawable.placeholder;
	    image = BitmapFactory.decodeResource(ctx.getResources(), res);
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
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	// Write JSONObject to parcel
	out.writeString(object.toString());

	// Write image to parcel
	image.writeToParcel(out, 0);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
	public Item createFromParcel(Parcel in) {
	    return new Item(in);
	}

	public Item[] newArray(int size) {
	    return new Item[size];
	}
    };

    // Construct Item from parcel
    private Item(Parcel in)
    {
	// Get JSONObject from parcel
	try
	{
	    this.object = new JSONObject(in.readString());
	}
	catch(JSONException e)
	{
	    e.printStackTrace();
	}

	// Get image from parcel
	this.image = Bitmap.CREATOR.createFromParcel(in);
    }
};
