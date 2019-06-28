/*
 * PureMVC Java Currency Converter for Android
 * Copyright (C) 2010  Frederic Saunier - www.tekool.net
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.puremvc.java.demos.android.currencyconverter.converter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.multicore.patterns.proxy.Proxy;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

/**
 * <code>Proxy</code> object used to control access to currencies data.
 */
public class CurrencyServiceProxy
	extends Proxy
{
    private static final String TAG = "CurrencyServiceProxy";

    /**
	 * The <code>AsyncTask</code> class used to execute the call to the web
	 * service in a different thread than from the UI thread.
	 */
	private AsyncHttpCall asyncHttpCall;

	/**
	 * The currencies list managed by the <code>Proxy</code> object. 
	 */
	protected ArrayList<CurrencyVo> currenciesList;

	private String gateway;
	private String lang;
	private String currency;
	private String service;
	private Context context;

	private boolean loading;
	
	/**
	 * Construct a <code>NetworkCurrencyProxy</code> object.
	 * 
	 * @param name
	 * 		Name of the <code>Proxy</code> for PureMVC framework.
	 * 
     * @param context
     * 		A reference to an Android <code>Context</code> instance needed to
     * 		build the user-agent property.
	 */
	//TODO Pass user-agent as a string in params...
    public CurrencyServiceProxy( String name, Context context )
    {
        super( name, new ArrayList<CurrencyVo>() );
        
        this.context = context;
    }
    
    /**
     * Get the <code>HttpCall</code> object used to initialize the current
     * <code>Proxy</proxy> object.
     *  
     * @return
     * 		The <code>HttpCall</code> object used to initialize the current
     * 		<code>Proxy</proxy> object.
     */
    public HttpCall getHttpCall()
    {
    	return (HttpCall) getData();
    }
   
    /**
     * Sets the <code>gateway</code> of the webservice that has to be called to
     * retrieve currencies list.
     * 
     * @param gateway
     * 		Webservice <code>gateway</code> uri.
	 */
	public void setUri( String gateway )
	{
		this.gateway = gateway;	
	}

    /**
     * Sets the <code>service</code> param needed to indicate which webservice
     * to call on the gateway.
     * 
     * @param service
     * 		The <code>service</code> name.
	 */
	public void setService( String service )
	{
		this.service = service;
	}
	
	/**
     * Sets the <code>lang</code> param needed to retrieve currencies list with
     * currencies names expressed in the right language.
     * 
     * @param lang
     * 		Code (3 letters) of the language in which currencies names need to
     * 		be expressed.
	 */
	public void setLang( String lang )
	{
		this.lang = lang;
	}

    /**
     * Sets the <code>currency</code> param needed to retrieve currencies list
     * with currencies values expressed in the right currency.
     * 
     * @param currency
     * 		Code (3 letters) of the reference currency in which are expressed
     * 		currencies values.
   	 */
	public void setCurrency( String currency )
	{
		this.currency = currency;
	}
    
    /**
     * Call a Json webservice at the given gateway <code>uri</code>.
     * 
     * Parameters need to be set be fore calling this method.
     */
	public void load()
    {
        Params params = new Params();
 
        if( gateway == null || gateway.equals("") )
        	Log.e( TAG, "Missing `gateway` parameter" );
        else
        	params.uri = gateway;
        
        if( service == null || service.equals("") )
        	Log.e( TAG, "Missing `service` parameter" );
        else
        	params.service = service;

        if( currency == null || currency.equals("") )
        	Log.e( TAG, "Missing `currency` parameter" );
        else
        	params.currency = currency;

        if( lang == null || lang.equals("") )
        	Log.e( TAG, "Missing `lang` parameter" );
        else
        	params.lang = lang;

        params.userAgent = prepareUserAgent();
 
        asyncHttpCall = new AsyncHttpCall();
        asyncHttpCall.execute( params );
        loading = true;
    }
	
	public Boolean isLoading()
	{
		return loading;
	}

	/**
	 * Called when the answer from is received from the web service.
	 * 
	 * @param content
	 * 		The answer received from the web service. The answer can describe
	 * 		a fault or a successful call
	 */
	protected void loaded( String content )
	{
        loading = false;

		try
		{
			JSONObject response = new JSONObject(content);
			
			if( response.has("fault") )
			{
				String message = response.getString("fault");
				sendNotification( NotificationNames.CURRENCIES_LOADING_FAILED, "Error while retrieving webservice response", message );
				return;
			}

			JSONObject result = response.getJSONObject("result");
	        
			currenciesList = new ArrayList<CurrencyVo>();

			Iterator<?> codes = (Iterator<?>) result.keys();
			while( codes.hasNext() )
            {
				String code = (String) codes.next(); 
				JSONArray arr = result.getJSONArray(code);
            	
				/*
				 * Translate the webservice result list in our CurrencyVos list. 
				 */
            	CurrencyVo currencyVo = new CurrencyVo();
            	currencyVo.code = code.toUpperCase();
            	currencyVo.rate = arr.getDouble(0);
            	currencyVo.name = arr.getString(1);
            	currencyVo.symbol = arr.getString(2);
            	currencyVo.date = new Date(arr.getLong(3)*1000);
            	currenciesList.add( currencyVo );
            }
            
            sendNotification( NotificationNames.CURRENCIES_LOADED, currenciesList );
        }
        catch( JSONException e )
        {
            sendNotification( NotificationNames.CURRENCIES_LOADING_FAILED, "Problem parsing webservice response", e.getLocalizedMessage() );
        }
	}
	
	/**
	 * Called when an internal error happens when loading data from the web
	 * service.
	 * 
	 * @param exception
	 * 		The error thrown by the web service.
	 */
	protected void loadingFailed( Exception exception )
	{
        sendNotification( NotificationNames.CURRENCIES_LOADING_FAILED, "Couldn't contact webservice - " + exception.getMessage(), exception.getLocalizedMessage() );
	}
    
    /**
     * Prepare the internal User-Agent string to use when calling the web
     * service.
     */
	protected String prepareUserAgent()
    {

        String userAgent = "";
        try
        {
            //Read package name and version number from manifest
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            userAgent = String.format( context.getString( R.string.user_agent_template), info.packageName, info.versionName );
        }
        catch( NameNotFoundException e )
        {
            Log.e( TAG, "Couldn't find User-Agent information from PackageManager", e );
        }
        
        return userAgent;
    }
    
    /**
     * Parameters needed by the <code>InitTask</code> class to execute the
     * webservice call.
     */
	protected final class Params
	{
		public String uri;
		public String service;
		public String currency;
		public String lang;
		public String userAgent;
	}
    
    /**
     * Sub-class of Android <code>AsyncTask</code> class used to execute the
     * call to the web service in a different thread than from the UI thread.
     * 
     * {@link http://labs.makemachine.net/2010/05/android-asynctask-example/}
     */
    protected class AsyncHttpCall
    	extends AsyncTask< Params, Integer, String >
    {
		private HttpCall httpCall;
		private Exception exception;
  	
    	/**
    	 * Execute the call to the web service.
    	 * 
    	 * @param params
    	 * 		Parameters {@link #Params} used to initialize the call to the
    	 * 		web service.
	     */
		@Override
		protected String doInBackground( Params... parameters ) 
		{
			Params params = parameters[0];
			
			httpCall = new HttpCall();
			httpCall.addHeader( "User-Agent", params.userAgent );
			httpCall.addParam( "service", params.service );
			httpCall.addParam( "currency", params.currency );
			httpCall.addParam( "lang", params.lang );
			httpCall.setUri( params.uri );

	        try
	        {
	        	httpCall.load();

	        	int progress = 0;

	        	//TODO Actually the content length returned by the server is always -1, I will try to configure the server to change this…
	        	float contentLength = (float) httpCall.contentLength();
	        	
	        	int read = -1;
	        	int totalRead = 0;
	        	while( ( read = httpCall.read()) > -1 )
	        	{
	        		totalRead += read;
	        		progress = Math.round( 100/(contentLength/totalRead) );
	        		publishProgress( progress );
	        	}

	        	return httpCall.getResult();
	        }
	        catch( Exception e )
	        {
	        	Log.e
	        	(
        			CurrencyServiceProxy.TAG,
        			"error while retrieving currencies from server : " + "\n"
	        			+ "{ " + "\n"
	        			+ "	uri: \"" + params.uri + "\"," + "\n"
	        			+ "	service:\"" + params.service + "\"," + "\n"
	        			+ "	currency:\"" + params.currency + "\"," + "\n"
	        			+ "	lang:\"" + params.lang + "\"" + "\n"
	        			+ "}",
        			e
	        	);
	        	exception = e;
	            cancel(true);
	            return "";
	        }
		}
		
		/**
		 * Called if the <code>AsyncTask</code> is canceled.
		 */
		@Override
		protected void onCancelled()
		{
			super.onCancelled();
			Log.i( "AsyncHttpCall", "onCancelled()" );
            httpCall.cancel();
            httpCall.close();
        }
		
		/**
		 * Called from the publish progress.
		 * 
		 * Notice that the datatype of the second param gets passed to this
		 * method.
		 */
		@Override
		protected void onProgressUpdate( Integer... values ) 
		{
			super.onProgressUpdate(values);
			Log.i( "AsyncHttpCall", "onProgressUpdate(): " +  String.valueOf( values[0] ) );
		}

		/**
		 * Called as soon as doInBackground method completes.
		 *
		 * Notice that the third param gets passed to this method.
		 * 
		 * @param result
		 * 		The string result obtained from the webservice.
		 */
		@Override
		protected void onPostExecute( String result ) 
		{
			super.onPostExecute(result);
			Log.i( "AsyncHttpCall", "onPostExecute(): " + result );
			
			/*
			 * An exception happened during the call.
			 */
			if( exception != null )
				loadingFailed(exception);
			else
				loaded( result );
		}
    }
}