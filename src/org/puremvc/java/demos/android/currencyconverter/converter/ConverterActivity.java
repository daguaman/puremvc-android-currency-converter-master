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

package org.puremvc.java.demos.android.currencyconverter.converter;

import java.util.Observable;
import java.util.Observer;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.common.ActivityNames;
import org.puremvc.java.demos.android.currencyconverter.common.Event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

/**
 * Android <code>Activity</code> base class for the Application.
 */
public class ConverterActivity
	extends Activity
{
	/**
	 * The request code used to retrieve the result of the <em>Preferences</em>
	 * execution.
	 */
	private static final int PREFERENCES_ACTIVITY_REQUEST_CODE = 0;

	/**
	 * Event name of the event dispatched when the OptionsMenu item is selected.
	 */
	public static final String OPTIONS_MENU_ITEM_SELECTED = "optionsMenuItemSelected";

	/**
	 * Event name of the event dispatched when the Preferences activity item is closed.
	 */
	public static final String PREFERENCES_ACTIVITY_CLOSED = "preferencesActivityClosed";

	private ConverterActivityFacade facade;
	private ObservableComposite observable;

	public boolean optionsMenuOpened;
	public int lastSelectedOptionsMenuItem;
	
	/**
	 * Constructor.
	 */
	public ConverterActivity()
	{
		super();

		observable = new ObservableComposite();
	}

    /**
     * Initialize our application with the Android <code>Activity</code>.
     */
	protected void startApp()
	{
		/*
		 * In some cases, the Activity instance could have been previously
		 * silently destroyed by the Android system we need to ensure that our
		 * Facade uses an existing Activity so we re-init it.
		 */
		ConverterActivityFacade.removeCore( ActivityNames.CONVERTER );
		
		facade = ConverterActivityFacade.getInst( ActivityNames.CONVERTER );
		facade.startup(this);
	}
	


    //-------------------------------------------------------------------------
    // Event listeners
    //-------------------------------------------------------------------------
	
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        
        //FIXME work on this when the application re-start
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.currency_converter);
        getWindow().setFeatureInt( Window.FEATURE_CUSTOM_TITLE, R.layout.app_title );
        
		startApp();
    }
    
	/**
     * Called (or not called) when the activity is destroyed by the Android
     * system.
     */
    @Override   
    public void onDestroy()
    {
    	super.onDestroy();
    	
    	ConverterActivityFacade.removeCore( ActivityNames.CONVERTER );
    }

    /**
	 * Called the first time the application options menu is opened.
	 * 
	 * @param menu
	 * 		The options menu in which you place your items. 
	 * 
	 * @return
	 * 		You must return true for the menu to be displayed; if you return
	 * 		false it will not be shown.
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.options_menu,menu);

		return true;
	}
	
	/**
	 * Called each time the application options menu is opened.
	 * 
	 * @param menu
	 * 		The options menu in which you place your items. 
	 * 
	 * @return
	 * 		You must return true for the menu to be displayed; if you return
	 * 		false it will not be shown.
	 */
	@Override
	public boolean onPrepareOptionsMenu( Menu menu )
	{		
		optionsMenuOpened = true;
		
		return true;
	}

	/**
	 * This hook is called whenever the options menu is being closed (either by
	 * the user canceling the menu with the back/menu button, or when an item
	 * is selected).
	 * 
	 * @param menu
	 * 		The options menu as last shown or first initialized by onCreateOptionsMenu().
	 */
	@Override
	public void onOptionsMenuClosed( Menu menu )
	{
		optionsMenuOpened = false;
	}

    /**
     * Called when an item of the application menu is chosen.
     * 
     * @param item
     * 		The menu item that was selected.
     * 
     * @return
     * 		Return false to allow normal menu processing to proceed, true to
     * 		consume it here.
     */
	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		lastSelectedOptionsMenuItem = item.getItemId();
		observable.setChanged();
		observable.notifyObservers( new Event( OPTIONS_MENU_ITEM_SELECTED ) );
		observable.clearChanged();		
		
		return false;
	}
	
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );

        if( requestCode == PREFERENCES_ACTIVITY_REQUEST_CODE )
        {
			observable.setChanged();
			observable.notifyObservers( new Event( PREFERENCES_ACTIVITY_CLOSED, data ) );
			observable.clearChanged();
        }
    }
    
    
	//▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪
	// Activity management

	/**
	 * Start the <em>About</em> activity.
	 */
	public void startAboutActivity()
	{
        Intent myIntent = new Intent();
        myIntent.setClassName( this, ActivityNames.ABOUT );

        startActivity(myIntent);
	}
	
	/**
	 * Start the <em>Preferences</em> activity.
	 */
	public void startPreferencesActivity()
	{
		Intent myIntent = new Intent();
		myIntent.setClassName( this, ActivityNames.PREFERENCES );
		
		startActivityForResult( myIntent, PREFERENCES_ACTIVITY_REQUEST_CODE  );
	}

	//▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪
	// Observable "implementation"
	
	/**
	 * An Observable implementation used as a composite for the current
	 * <code>Activity</code>.
	 */
	class ObservableComposite
		extends Observable
	{
		@Override
		public void setChanged()
		{
			super.setChanged();
		}

		@Override
		public void clearChanged()
		{
			super.clearChanged();
		}
	}
	
	/**
	 * 	Adds the specified observer to the list of observers.
	 * 
	 * @param observer
	 * 		The observer to add.
	 */
	public void addObserver( Observer observer ) 
	{
		observable.addObserver(observer);
	}
	
	/**
	 * Returns the number of observers registered to this Observable.
	 */ 
	public int countObservers()
	{
		return observable.countObservers();
	}
	 
	/**
	 * Removes the specified observer from the list of observers.
	 * 
	 * @param observer
	 * 		The observer to remove.
	 */
	public synchronized void deleteObserver( Observer observer )
	{
		observable.deleteObserver( observer );		
	}
	 
	/**
	 * Removes all observers from the list of observers. 
	 */
	public synchronized void deleteObservers()
	{
		observable.deleteObservers();
	}
}