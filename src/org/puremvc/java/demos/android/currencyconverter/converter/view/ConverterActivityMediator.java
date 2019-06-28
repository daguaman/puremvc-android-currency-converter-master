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

import java.util.Observable;
import java.util.Observer;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.common.Event;
import org.puremvc.java.demos.android.currencyconverter.converter.ConverterActivity;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import android.content.Context;
import android.content.Intent;

/**
 * The <code>Mediator</code> object which is responsible for the application
 * activity.
 * 
 * It has for only role to give access to the application context without using
 * a direct reference to it, what might have broken the loose coupling rules.
 */
public class ConverterActivityMediator 
	extends Mediator
	implements Observer
{
	/**
	 * Construct the <code>Mediator</code> instance.
	 * 
	 * @param mediatorName
	 * 		Name of the <code>Mediator</code> object.
	 * 
	 * @param activity
	 * 		The activity associated with the <em>Converter</em> activity view
	 * 		component.
	 */
	public ConverterActivityMediator( String mediatorName, ConverterActivity activity )
	{
		super( mediatorName, activity );
		
		activity.addObserver(this);
	}

	protected ConverterActivity getConverterActivity()
	{
		return (ConverterActivity) viewComponent;
	}
	
	public Context getContext()
	{
		return (Context) getConverterActivity();
	}



	//▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪
	// OptionsMenu handling
	
	/**
	 * Called when the application options menu opens.
	 */
	public void optionsMenuOpenedHandler()
	{
		
	}
	
    /**
     * Called when an item of the application menu is chosen.
	 * 
	 * @param id
	 * 		The Android options menu item selected id.
	 */
	public void onOptionsMenuItemSelected( int id )
	{
		if( id == R.id.OptionsMenuRefreshItem )
			sendNotification( NotificationNames.REFRESH_CURRENCIES );
		else
		if( id == R.id.OptionsMenuAboutItem )
		{
			getConverterActivity().startAboutActivity();
		}
		else
		if( id == R.id.OptionsMenuPreferencesItem )
		{
			getConverterActivity().startPreferencesActivity();
		}
	}



	//▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪
	// Activities management
	
	/**
	 * Called when the  item of the application menu is chosen.
	 * 
	 * @param result
	 * 		The result data sent by the <em>Preferences</em> activity when it
	 * 		closes.
	 */
	public void onPreferencesActivityClosed( Intent result )
	{
		
	}
	
	

	//▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪
	// Observer implementation

	@Override
	public void update( Observable observable, Object data )
	{
		Event event = (Event) data;
		
		if( event.name.equals( ConverterActivity.OPTIONS_MENU_ITEM_SELECTED ) )
			onOptionsMenuItemSelected( getConverterActivity().lastSelectedOptionsMenuItem );
		else
		if( event.name.equals( ConverterActivity. PREFERENCES_ACTIVITY_CLOSED ) )
			onPreferencesActivityClosed( (Intent) event.data );
	}
}