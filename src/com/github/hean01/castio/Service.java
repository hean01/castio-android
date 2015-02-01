package com.github.hean01.castio;


import android.net.Uri;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/* placeholder for performing requests for castio service */
public class Service
{
    private static final String TAG = "Service";

    public static class Response
    {
	public String mime;
	public StatusLine statusLine;
	public byte []data;
	public Response(String mime, StatusLine statusLine, HttpEntity entity) throws IOException
	{
	    this.mime = mime;
	    this.statusLine = statusLine;

	    if (entity != null)
		this.data = EntityUtils.toByteArray(entity);
	}
    }

    private static Uri serviceUri(Context context)
    {
	SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
	Uri uri = Uri.parse("http://"
			    + sp.getString("service_address", "localhost")
			    + ":" + sp.getString("service_port", "1457"));
	return uri;
    }

    private static Response get(Context context, Uri uri) throws IOException, URISyntaxException
    {
	/* build and perform the uri request */
	SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
	HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
	DefaultHttpClient client = new DefaultHttpClient();
	client.getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
						       new UsernamePasswordCredentials(sp.getString("service_username", "admin"),
										       sp.getString("service_password", "password")));

	Log.d(TAG, "Executing request: " + uri.toString());
	HttpGet request = new HttpGet(new URI(uri.toString()));
	HttpResponse response = client.execute(request);

	return new Response("???", response.getStatusLine(), response.getEntity());
    }

    public static Response getCache(Context context, String resource) throws IOException, URISyntaxException
    {
	Uri uri = serviceUri(context);
	Uri.Builder ub = uri.buildUpon();

	ub.appendPath("cache");
	ub.appendQueryParameter("resource", resource);
	uri = ub.build();

	return get(context, uri);
    }
}
