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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * Build, execute and wait for a result of a call to an HTTP service.
 */
public class HttpCall
	implements Closeable
{
    /**
     * {@link StatusLine} HTTP status code when no server error has occurred.
     */
    private static final int HTTP_STATUS_OK = 200;
    
    /**
     * Log tag for this class.
     */
    private static final String TAG = "JsonService";

    /**
     * Shared buffer used by {@link #read()} method to read the result
     * of an HTTP call.
     */
    private byte[] buffer = new byte[512];
    
    /**
     * <code>URI</code> of the web service to call.
     */
    private String uri;
    
    /**
     * HTTP protocol headers collection.
     */
    private ArrayList<NameValuePair> headers;
    
    /**
     * HTTP protocol parameters collection.
     */
    private ArrayList<NameValuePair> params;
    
    private HttpClient httpClient;
    private HttpPost request;
    private InputStream inputStream;
    private HttpEntity entity;
    private ByteArrayOutputStream result;
	private boolean closed = false;
    
    /**
     * Create and initialize an <code>HttpCall</code> object.
     */
    public HttpCall()
    {
        httpClient = new DefaultHttpClient();
        request = new HttpPost();

    	headers = new ArrayList<NameValuePair>();
        params = new ArrayList<NameValuePair>();
    }

	/**
     * Pull the raw text content of the given URL. This call blocks until the
     * operation has completed, and is synchronized because it uses a shared
     * buffer {@link #buffer}.
     *
     * @throws ApiException
     * 		If any connection or server error occurs.
     * 
     * @throws URISyntaxException
     * 		If the <code>URI</code> string is invalid.
     */
    public synchronized void load()
    	throws ApiException
    {
    	if(closed)
    		return;
    	
        if( uri == null )
        	throw new ApiException("Target URI of the call must be set");

        //Set the URI
        try
        {
			request.setURI( new URI(uri) );
		}
        catch( URISyntaxException e )
        {
        	throw new ApiException("Target URI of the call is malformed");
		}
        
        //Add headers
        for( int i=0; i<headers.size(); i++ )
    	{
    		NameValuePair pair = headers.get(i);
    		request.addHeader( pair.getName(), pair.getName() );
    	}

    	try
    	{
			request.setEntity( new UrlEncodedFormEntity( params, HTTP.UTF_8 ) );
		}
    	catch( UnsupportedEncodingException e1 )
    	{
            throw new ApiException("Problem adding parameters to the call", e1);
		}

    	//Execute the call
        try
        {
            HttpResponse response = httpClient.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if( status.getStatusCode() != HTTP_STATUS_OK )
                throw new ApiException("Invalid response from server: " + status.toString());

            // Pull content stream from response
            entity = response.getEntity();
            inputStream = entity.getContent();
        }
        catch( IllegalArgumentException e2 )
        {
            throw new ApiException("Problem executing call", e2);
        }
        catch( IllegalStateException e2 )
        {
        	throw new ApiException("Problem calling server", e2);
        }
        catch( UnknownHostException e2 )
        {
        	throw new ApiException("Unknown host: " + uri, e2);
        }
        catch( IOException e2 )
        {
            throw new ApiException("Problem communicating with server", e2);
        }
        
        result = new ByteArrayOutputStream();
    }
    
    /**
     * Return the <code>Content-Length</code> returned by the HTTP server for
     * the result.
     * 
     * @return 
     * 		The <code>Content-Length</code> returned by the HTTP server for
     * 		the result. Returns -1 not any result has yet been received from
     * 		the	server.
     */
    public long contentLength()
    {
        long contentLength = -1;

        if( entity != null )
        	contentLength = entity.getContentLength();
        
        return contentLength;
    } 
    
    /**
     * Return the current length of the result returned by the web service.
     * 
     * @return 
     * 		The <code>Content-Length</code> returned by the HTTP server for
     * 		the result. Returns -1 not any result has yet been received from
     * 		the	server.
     */
    public int getResultLength()
    {
    	if( result == null )
    		return -1;

        return result.size();
    }
    
    /**
     * Return the current result returned by the call to the HTTP server.
     * 
     * @return 
     * 		The result returned by the HTTP server as a <code>String</code>.
     */
    public String getResult()
    {
    	if( result == null )
    		return null;

        return result.toString();
    }
    
    /**
     * Read the next packet from the stream obtained as a result of the call.
     * 
     * This method has to be called until it returns -1 to dispatch a progress
     * event before calling the {@link #getResult()}.
     * 
     * @return
     * 		The length of the last packet read from the result or -1 if the end
     * 		of the stream has been reached.
     * 
     * @throws ApiException
     * 		If any problem occurs when reading the resulting stream.
     */
    public int read()
		throws ApiException
    {
    	if(closed)
    		return -1;

    	int readBytes;
		try
		{
			readBytes = inputStream.read(buffer);
		}
		catch( IOException e )
		{
            throw new ApiException("Problem reading result from the webservice", e);
		}

        if( readBytes > -1 )
        	result.write( buffer, 0, readBytes );

        return readBytes;
    }
    
    /**
     * Get the <code>URI</code> of the HTTP service to call.
     * 
     * @param uri
     * 		<code>URI</code> of the HTTP service to call.
     */
    public String getUri()
    {
    	return this.uri;
    }

    /**
     * Set the <code>URI</code> of the HTTP service to call.
     * 
     * @param uri
     * 		<code>URI</code> of the HTTP service to call.
     */
    public void setUri( String uri )
    {
    	this.uri = uri;
    } 

    /**
     * Add an header to the HTTP call.
     * 
     * @param name
     * 		Added header name.
     * 
     * @param value
     *      Added header value.
     */
    public void addHeader( String name, String value )
    {
    	headers.add( new BasicNameValuePair(name, value) );
    }
    
    /**
     * Get the headers used by the HTTP call.
     * 
     * @return
     * 		The headers used by the HTTP call.
     */
    public ArrayList<NameValuePair> getHeaders()
    {
    	return headers;
    }
    
    /**
     * Add an HTTP protocol parameter to the call.
     * 
     * @param name
     * 		Added parameter name.
     * 
     * @param value
     *      Added parameter value.
     */
    public void addParam( String name, String value )
    {
        params.add( new BasicNameValuePair(name, value) );
    }
    
    /**
     * Get the HTTP protocol parameters added to the call.
     * 
     * @return
     * 		The HTTP protocol parameters added to the call.
     */
    public ArrayList<NameValuePair> getParams()
    {
    	return params;
    }

    /**
     * Thrown when there were problems contacting the remote API server, either
     * because of a network error, or the server returned a bad status code.
     */
	public class ApiException
    	extends Exception
    {
        /**
		 * Auto-generated <code>serialVersionUID</code>.
		 */
		private static final long serialVersionUID = 4796952360343843493L;

		public ApiException( String detailMessage, Throwable throwable )
        {
            super(detailMessage, throwable);
        }

        public ApiException( String detailMessage )
        {
            super(detailMessage);
        }
    }

	/**
	 * Cancel the call, closing it an cleaning any properties use for its
	 * initialization.
	 */
	public void cancel()
	{
		close();
	}

	/**
	 * Try to clean everything related to the call here.
	 */
	public void close()
	{
    	if(closed)
    		return;

		request.abort();
		httpClient.getConnectionManager().shutdown();
		
		if( inputStream != null )
		{	try
			{
				inputStream.close();
			}
			catch (IOException e)
			{
				Log.d( TAG, e.getLocalizedMessage() );
			}
		}
		
		closed  = true;
	}
}