package com.github.hean01.castio;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RESTTask extends AsyncTask<Bundle, Void, RESTTask.Response>
{
    private final String TAG = "RESTTask";
    public static final String ARGS_URI =           "com.github.hean01.castio.RESTTask.URI";
    public static final String ARGS_QUERY =        "com.github.hean01.castio.RESTTask.QUERY";
    public static final String ARGS_AUTH_USER =     "com.github.hean01.castio.RESTTask.AUTH_USER";
    public static final String ARGS_AUTH_PASSWORD = "com.github.hean01.castio.RESTTask.AUTH_PASSWORD";

    private Listener listener;

    public class Response {
	public String data;
	public int code;

	public Response(int code, String data)
	{
	    this.code = code;
	    this.data = data;
	}
    }

    public interface Listener
    {
	public void onResponse(RESTTask.Response response);
	public void onComplete();
    }

    public RESTTask(RESTTask.Listener listener)
    {
	super();
	this.listener = listener;
    }

    @Override
    protected RESTTask.Response doInBackground(Bundle... args)
    {
	Uri uri = args[0].getParcelable(ARGS_URI);
	Bundle params = args[0].getParcelable(ARGS_QUERY);

	// Build uri with query parameters
	Uri.Builder ub = uri.buildUpon();
	for (BasicNameValuePair param : paramsToList(params))
	    ub.appendQueryParameter(param.getName(), param.getValue());
	uri = ub.build();

	// Client carrying out the request
	try
	{
	    HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
	    DefaultHttpClient client = new DefaultHttpClient();
	    client.getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
							   new UsernamePasswordCredentials(args[0].getString(ARGS_AUTH_USER),
											   args[0].getString(ARGS_AUTH_PASSWORD)));

	    Log.d(TAG, "Executing request: " + uri.toString());
	    HttpGet request = new HttpGet(new URI(uri.toString()));
	    HttpResponse response = client.execute(request);
	    HttpEntity entity = response.getEntity();

	    if (entity == null)
		return new Response(response.getStatusLine().getStatusCode(), "");

	    return new Response(response.getStatusLine().getStatusCode(),  EntityUtils.toString(entity));
	}
	catch (URISyntaxException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}

	return null;
    }

    @Override
    protected void onPostExecute(Response response)
    {
	if (response != null)
	    listener.onResponse(response);

	listener.onComplete();
    }

    private static List<BasicNameValuePair> paramsToList(Bundle query)
    {
	ArrayList<BasicNameValuePair> formList;
	formList = new ArrayList<BasicNameValuePair>(params.size());
	for (String key : params.keySet())
	{
	    Object value = params.get(key);

	    if (value == null)
		continue;
	    formList.add(new BasicNameValuePair(key, value.toString()));
	}
	return formList;
    }

    private String streamToString(InputStream is) throws IOException
    {
	BufferReader br = null;
	StringBuilder sb = new StringBuilder();
	try
	{
	    String line;
	    br = new BufferedReader(new InputStreamReader(is));
	    while ((line = br.readLine()) != null)
		sb.append(line);

	}
	finally
	{
	    if (br != null)
		br.close();
	}

	return sb.toString();
    }

}
