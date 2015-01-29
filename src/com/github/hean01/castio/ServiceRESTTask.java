package com.github.hean01.castio;

import android.net.Uri;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;

public class ServiceRESTTask extends RESTTask
{
    public ServiceRESTTask(RESTTask.Listener listener)
    {
	super(listener);
    }

    // Builds service arguments to pass to execute()
    public static Bundle arguments(Context context,
				   String object, Bundle query)
    {
	SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);

	Uri uri = Uri.parse("http://"
			    + sp.getString("service_address", "localhost")
			    + ":" + sp.getString("service_port", "1457")
			    + object);

	Bundle args = new Bundle();
	args.putParcelable(RESTTask.ARGS_URI, uri);
	args.putString(RESTTask.ARGS_AUTH_USER,
		       sp.getString("service_username_key", "admin"));
	args.putString(RESTTask.ARGS_AUTH_PASSWORD,
		       sp.getString("service_password_key", "password"));
	args.putParcelable(RESTTask.ARGS_QUERY, query);

	return args;
    }
}
