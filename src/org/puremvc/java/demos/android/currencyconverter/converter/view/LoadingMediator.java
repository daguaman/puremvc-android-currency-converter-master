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

package org.puremvc.java.demos.android.currencyconverter.converter.view;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.converter.view.components.Loading;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import android.view.View;
import android.widget.TextView;

/**
 * The <code>Mediator</code> object which is responsible for the loading
 * screen.
 */
public class LoadingMediator 
	extends Mediator
{
	public LoadingMediator( String mediatorName, Object viewComponent )
	{
		super( mediatorName, viewComponent );
	}

	protected Loading getLoading()
	{
		return (Loading) viewComponent;
	}

	public String[] listNotificationInterests()
	{
		return new String[]
		{
			
		};
	}

	public void handleNotification( INotification note )
	{
		if(note.getName().equals(""))
		{
		}
	}

	public void loading()
	{
       	View loadingFailedScreen = getLoading().findViewById(R.id.LoadingFailedScreen);
       	loadingFailedScreen.setVisibility( View.INVISIBLE );
       	
       	View loadingProgressScreen = getLoading().findViewById(R.id.LoadingProgressScreen);
       	loadingProgressScreen.setVisibility( View.VISIBLE );
       	
		getLoading().setVisibility( View.VISIBLE );		
	}
	
	public void loaded()
	{
		getLoading().setVisibility( View.INVISIBLE );		
	}

	/**
	 * Called when an error happens while loading currencies data.
	 * 
	 * @param localizedMessage
	 * 		A message to display to the user in its language describing the
	 * 		error.
	 */
	public void loadingFailed( String localizedMessage )
	{
       	View loadingFailedScreen = getLoading().findViewById(R.id.LoadingFailedScreen);
       	loadingFailedScreen.setVisibility( View.VISIBLE );
       	
       	View loadingProgressScreen = getLoading().findViewById(R.id.LoadingProgressScreen);
       	loadingProgressScreen.setVisibility( View.INVISIBLE );
       	
       	TextView exceptionTextView = (TextView) getLoading().findViewById(R.id.LoadingFailedException);
       	exceptionTextView.setText(localizedMessage);

       	getLoading().setVisibility( View.VISIBLE );
	}
}