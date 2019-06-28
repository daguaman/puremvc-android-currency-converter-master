/*
 * PPureMVC Java Currency Converter for Android
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

import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.ConvertCmd;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.CurrenciesLoadedCmd;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.CurrenciesLoadingFailedCmd;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.CurrenciesRefreshedCmd;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.LoadCurrenciesCmd;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.RefreshCurrenciesCmd;
import org.puremvc.java.demos.android.currencyconverter.converter.controller.StartupCmd;
import org.puremvc.java.multicore.patterns.facade.Facade;

public class ConverterActivityFacade
	extends Facade
{
	/**
	 * Construct the <code>Facade</code> multiton instance for this
	 * <code>Activity</code>.
	 * 
	 * @param multitonKey
	 * 		Multiton key for this <code>Facade</code> multiton instance.
	 */
	public ConverterActivityFacade( String multitonKey )
	{
		super( multitonKey );
	}

	/**
	 * <code>ApplicationFacade</code> singleton fabrication method
	 * implementation.
	 *
	 * @param multitonKey
	 * 		Multiton key of the facade instance to get.
	 */
	public static ConverterActivityFacade getInst( String multitonKey )
	{
		if( instanceMap.get(multitonKey) == null )
			instanceMap.put( multitonKey, new ConverterActivityFacade( multitonKey ) );

		return (ConverterActivityFacade) instanceMap.get(multitonKey);
	}

	@Override
	protected void initializeController()
	{
		super.initializeController();

		registerCommand( NotificationNames.STARTUP, new StartupCmd() );
		
		registerCommand( NotificationNames.LOAD_CURRENCIES, new LoadCurrenciesCmd() );
		registerCommand( NotificationNames.CURRENCIES_LOADED, new CurrenciesLoadedCmd() );
		registerCommand( NotificationNames.CURRENCIES_LOADING_FAILED, new CurrenciesLoadingFailedCmd() );

		registerCommand( NotificationNames.REFRESH_CURRENCIES, new RefreshCurrenciesCmd() );
		registerCommand( NotificationNames.CURRENCIES_REFRESHED, new CurrenciesRefreshedCmd() );
		
		registerCommand( NotificationNames.CONVERT, new ConvertCmd() );
	}

	/**
	 * Start the <code>Activity</code> initialization sequence.
	 * 
	 * @param activity
	 * 		A reference to the <code>Activity</code> to initialize.
	 */
	public void startup( ConverterActivity activity )
	{
		sendNotification(  NotificationNames.STARTUP, activity  );
	}
}