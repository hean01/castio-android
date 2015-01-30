package com.github.hean01.castio;

import android.content.Context;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item>
{
    public ItemAdapter(Context context, ArrayList<Item> items)
    {
	super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	Item item = getItem(position);

	if (convertView == null)
	{
	    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.item, parent, false);
	}

	TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
	tvTitle.setText(item.get("metadata.title"));

	TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
	tvDescription.setText(item.get("metadata.description"));

	ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
	ivImage.setImageBitmap(item.getImage());

	return convertView;
    }
}
