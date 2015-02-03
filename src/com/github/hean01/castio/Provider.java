package com.github.hean01.castio;

import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
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

public class Provider implements Parcelable
{
    private static final String TAG = "Provider";
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

    public String getVersion()
    {
	String value = "undefined";
	JSONArray arr = object.optJSONArray("version");
	if (arr == null)
	    return value;

	try
        {
	    value = arr.getInt(0) + "." + arr.getInt(1) + "." + arr.getInt(2);
	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	}

	return value;
    }

    public Bitmap getImage()
    {
	return image;
    }

    public Provider(Context ctx, JSONObject object)
    {
	this.object = object;

	// If provider image is specified, try load it
	String image_uri = get("icon");
	if (image_uri != null)
	{
	    try
	    {
		Service.Response resp = Service.getCache(ctx, image_uri);
		if (resp.statusLine.getStatusCode() == 200)
		{
		    image = BitmapFactory.decodeByteArray(resp.data, 0, resp.data.length);
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

	if (image == null)
	{
	    image = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon);
	}
    }

    // Factory creating list of objects from JSONArray
    public static ArrayList<Provider> fromJson(Context ctx, JSONArray objects)
    {
	ArrayList<Provider> items = new ArrayList<Provider>();
	for (int i = 0; i < objects.length(); i++)
	{
	    try
	    {
		items.add(new Provider(ctx, objects.getJSONObject(i)));
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

    public static final Parcelable.Creator<Provider> CREATOR = new Parcelable.Creator<Provider>() {
	public Provider createFromParcel(Parcel in) {
	    return new Provider(in);
	}

	public Provider[] newArray(int size) {
	    return new Provider[size];
	}
    };

    // Construct Provider from parcel
    private Provider(Parcel in)
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
