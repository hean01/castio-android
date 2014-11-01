package com.github.hean01.castio;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.util.Log;

import android.util.TypedValue;
import android.util.DisplayMetrics;

import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.text.TextUtils;

import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;

public class MainActivity extends Activity {
    private LinearLayout content;
    private final static String TAG = "MainActivity";

    private View createItem(String name) {
	RelativeLayout rl = new RelativeLayout(this);
	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
									 RelativeLayout.LayoutParams.FILL_PARENT);
	rl.setLayoutParams(lp);
	rl.setBackgroundColor(0xff202020);

	/* add background image */
	ImageView iv = new ImageView(this);
	iv.setImageResource(R.drawable.logo);
	iv.setLayoutParams(lp);
	iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	rl.addView(iv);

	/* add text overlay */
	lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
					     RelativeLayout.LayoutParams.WRAP_CONTENT);
	lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
	lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

	TextView tv = new TextView(this);
	tv.setSingleLine(true);
	tv.setEllipsize(TextUtils.TruncateAt.END);
	tv.setText(name);
	//tv.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8.0f);
	tv.setTextColor(0xffffffff);
	tv.setBackgroundColor(0xb0000000);
	tv.setLayoutParams(lp);
	tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	rl.addView(tv);

	return rl;

	/*
	  <RelativeLayout>
	    <ImageView />
	    <FrameLayout>
	      <TextView />
	    </FrameLayout>
	  </RelativeLayout>
	*/
    }

    private void createSection(String name) {
	LinearLayout ll = new LinearLayout(this);
	ll.setOrientation(LinearLayout.VERTICAL);

	/* create the label */
	TextView tv = new TextView(this);
	tv.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12.0f);
	tv.setText(name);
	tv.setTextColor(0xfffbe220);
	ll.addView(tv);

	/* add a ruler */
	View hr = new View(this);
	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1);
	hr.setLayoutParams(lp);
	hr.setBackgroundColor(0xfffbe220);
	ll.addView(hr);

	/* creat horizontal item placeholder */
	HorizontalScrollView sv = new HorizontalScrollView(this);
	sv.setVerticalScrollBarEnabled(false);
	sv.setHorizontalScrollBarEnabled(false);
	sv.setPadding(0, 10, 0, 0);

	lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
					   LinearLayout.LayoutParams.WRAP_CONTENT);

	LinearLayout ll2 = new LinearLayout(this);
	ll2.setLayoutParams(lp);
	sv.addView(ll2);
	ll.addView(sv);

	/* add dummy sample items */
	DisplayMetrics dm = this.getResources().getDisplayMetrics();
	int width = dm.widthPixels / 5;

	lp = new LinearLayout.LayoutParams(width, (int)(width * 1.3));
	lp.setMargins(4,0,4,0);

	for (int i=0; i < 20; i++) {
	    ll2.addView(createItem("Radio seven a blast"), lp);
	}
	ll.setPadding(0, 20, 0, 0);
	content.addView(ll);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActionBar ab = getActionBar();
	ab.setTitle("Front Page");
	ab.setSubtitle("The source for your media...");
	ab.setDisplayHomeAsUpEnabled(false);

	setContentView(R.layout.front_layout);
	content = (LinearLayout) findViewById(R.id.container);

	/* add some sample sections */
	createSection("Latest additions");
	createSection("Top rated");
	createSection("Play queue");
	createSection("Favorites");
	createSection("Providers");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.main_actions, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch(item.getItemId())
	{
	case R.id.action_prefs:
	    startActivity(new Intent(this, PreferenceActivity.class));
	    return true;

	case R.id.action_search:
	    // TODO: start search activity
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
