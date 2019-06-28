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

package org.puremvc.java.demos.android.currencyconverter.application;

import org.puremvc.java.demos.android.currencyconverter.R;

import android.app.Application;
import android.preference.PreferenceManager;

/**
 * Android <code>Application</code> base class for the Application.
 */
public class CurrencyConverterApplication
	extends Application
{
	/**
	 * Multiton key for the <code>Facade</code> instance of the application.
	 */
	private String CURRENCY_CONVERTER_APPLICATION_MULTITON_KEY = "curencyConverterApplicationMultitonKey";

	private CurrencyConverterApplicationFacade facade;
	
    /**
     * Initialize our application with the Android <code>Activity</code>.
     */
	protected void startApp()
	{
		facade = CurrencyConverterApplicationFacade.getInst( CURRENCY_CONVERTER_APPLICATION_MULTITON_KEY );
		facade.startup(this);
	}

    @Override
    public void onCreate()
    {
        super.onCreate();
        
        PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
		startApp();		
    }
    
    @Override
    public void onTerminate()
    {
    	
    }
}