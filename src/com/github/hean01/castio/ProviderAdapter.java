package com.github.hean01.castio;

import android.content.Context;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ProviderAdapter extends ArrayAdapter<Provider>
{
    public ProviderAdapter(Context context, ArrayList<Provider> items)
    {
	super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	Provider provider = getItem(position);

	if (convertView == null)
	{
	    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.provider, parent, false);
	}

	TextView tvTitle = (TextView) convertView.findViewById(R.id.name);
	tvTitle.setText(provider.get("name"));

	TextView tvDescription = (TextView) convertView.findViewById(R.id.description);
	tvDescription.setText(provider.get("description"));

	ImageView ivImage = (ImageView) convertView.findViewById(R.id.image);
	ivImage.setImageBitmap(provider.getImage());

	return convertView;
    }
}
