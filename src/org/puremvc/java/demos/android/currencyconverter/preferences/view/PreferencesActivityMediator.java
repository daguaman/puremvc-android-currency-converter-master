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

package org.puremvc.java.demos.android.currencyconverter.preferences.view;

import java.util.Observable;
import java.util.Observer;

import org.puremvc.java.demos.android.currencyconverter.common.Event;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.demos.android.currencyconverter.preferences.PreferencesActivity;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import android.widget.ArrayAdapter;

/**
 * The <code>Mediator</code> object which is responsible for the preferences
 * screen.
 */
public class PreferencesActivityMediator 
	extends Mediator
	implements Observer
{
	/**
	 * The <code>ArrayAdapter</code> object needed to inject and manipulate the
	 * currencies list from within the Spinners.
	 */
	ArrayAdapter<CurrencyVo> adapter;
	
	private boolean needToRefreshCurrencies;

	/**
	 * Construct the <code>Mediator</code> instance.
	 * 
	 * @param mediatorName
	 * 		Name of the <code>Mediator</code> object.
	 * 
	 * @param activity
	 * 		The activity associated with the <em>Preferences</em> activity view
	 * 		component.
	 */
	public PreferencesActivityMediator( String mediatorName, PreferencesActivity activity )
	{
		super( mediatorName, activity );
		
		activity.addObserver(this);
	}

	/**
	 * Gives an access to the <code>Converter</code> view component.
	 * 
	 * @return
	 * 		The <code>Converter</code> view component.
	 */
	protected PreferencesActivity getPreferencesActivity()
	{
		return (PreferencesActivity) viewComponent;
	}

	@Override
	public String[] listNotificationInterests()
	{
		return new String[]
		{

		};
	}

	@Override
	public void handleNotification( INotification note )
	{

	}



	//▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪
	// Observer implementation

	@Override
	public void update( Observable observable, Object data )
	{
		Event event = (Event) data;

		if( event.name.equals( PreferencesActivity.UPDATE_FREQUENCY_CHANGED ) )
			onUpdateFrequencyChanged( getPreferencesActivity().updateFrequency );
	}

	private void onUpdateFrequencyChanged( CharSequence updateFrequency )
	{
		/*
		 * As we need to call the RefreshCurrencies command in the converter
		 * when this activity is closed we need to store it here to pass it
		 * as a result.
		 */
		//TODO Call the activity with startActivityForResult(Intent, int)
		needToRefreshCurrencies = true;
	}
}