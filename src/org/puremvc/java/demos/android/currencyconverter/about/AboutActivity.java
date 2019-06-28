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

package org.puremvc.java.demos.android.currencyconverter.about;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.common.ActivityNames;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

/**
 * The about screen used to display general informations about the application
 * and its author.
 */
public class AboutActivity
	extends Activity
{
	private AboutActivityFacade facade;

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
		AboutActivityFacade.removeCore( ActivityNames.ABOUT );

		facade = AboutActivityFacade.getInst( ActivityNames.ABOUT );		
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

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.about);
        getWindow().setFeatureInt( Window.FEATURE_CUSTOM_TITLE, R.layout.app_title );
        
        startApp();
        initDisplay();
    }
    
	/**
     * Called (or not called) when the activity is destroyed by the Android
     * system.
     */
    @Override   
    public void onDestroy()
    {
    	super.onDestroy();
    	
		AboutActivityFacade.removeCore( ActivityNames.ABOUT );   	
    }
    
	private void initDisplay()
	{
		/*
		 * This is the only way to display HTML text in an Android TextView.
		 */
		TextView aboutText = (TextView) findViewById(R.id.AboutText);
		String htmlText = getString(R.string.about_text);
		aboutText.setText(Html.fromHtml(htmlText));
	}
}