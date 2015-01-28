package com.github.hean01.castio;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Item
{
    public String title;
    public String description;
    public String type;
    public String uri;

    public Item()
    {
	// Dummy
    }

    public Item(JSONObject object)
    {
	try
	{
	    this.title = object.getString("metadata.title");
	    this.description = object.getString("metadata.description");
	    this.uri = object.getString("uri");
	    this.type = object.getString("type");

	    // TODO: load icon if specified or fallback to type specific icon

	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	}
    }

    // Factory creating list of objects from JSONArray
    public static ArrayList<Item> fromJson(JSONArray objects)
    {
	ArrayList<Item> items = new ArrayList<Item>();
	for (int i = 0; i < objects.length(); i++)
	{
	    try
	    {
		items.add(new Item(objects.getJSONObject(i)));
	    }
	    catch (JSONException e)
	    {
		e.printStackTrace();
	    }
	}
	return items;
    }

};
