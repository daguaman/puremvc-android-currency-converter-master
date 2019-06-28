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

package org.puremvc.java.demos.android.currencyconverter.preferences;

import java.util.Observable;
import java.util.Observer;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.about.AboutActivityFacade;
import org.puremvc.java.demos.android.currencyconverter.common.ActivityNames;
import org.puremvc.java.demos.android.currencyconverter.common.Event;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

/**
 * The preferences screen used to display settings and allow changes to it.
 */
public class PreferencesActivity
	extends PreferenceActivity
{
	/**
	 * Frequency update changed notification name.
	 */
	public static final String UPDATE_FREQUENCY_CHANGED = "updateFrequencyChanged";

	/**
	 * Value of the current selected update frequency preference.
	 */
	public CharSequence updateFrequency;

	private PreferencesActivityFacade facade;

	private ObservableComposite observable;

	/**
	 * Constructor.
	 */
	public PreferencesActivity()
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
		PreferencesActivityFacade.removeCore( ActivityNames.PREFERENCES );

		facade = PreferencesActivityFacade.getInst( ActivityNames.PREFERENCES );
		
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
        
        addPreferencesFromResource(R.layout.preferences);
     
        addListeners();
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
    	
    	PreferencesActivityFacade.removeCore( ActivityNames.PREFERENCES );   	
    }
    
    private void addListeners()
	{
    	/*
    	 * Update Frequency
    	 */
    	ListPreference preference = (ListPreference) findPreference( getString( R.string.preferences_update_frequency_key ) );
    	updateFrequency = preference.getEntry();
    	preference.setSummary( updateFrequency );
    	preference.setOnPreferenceChangeListener
    	( 
    			new OnPreferenceChangeListener()
				{
					@Override
					public boolean onPreferenceChange( Preference preference, Object newValue )
					{
						CharSequence[] values = ((ListPreference) preference).getEntryValues();
						CharSequence[] entries = ((ListPreference) preference).getEntries();
						for( int i=0; i<values.length; i++)
						{
							if( values[i].equals(newValue) )
							{
								preference.setSummary( entries[i] );
								break;
							}
						}

						updateFrequency = (CharSequence) newValue;
						observable.setChanged();
						observable.notifyObservers( new Event( UPDATE_FREQUENCY_CHANGED ) );
						observable.clearChanged();		

						return true;
					}
				}
		);
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