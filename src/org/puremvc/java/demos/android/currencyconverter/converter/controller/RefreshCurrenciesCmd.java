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

package org.puremvc.java.demos.android.currencyconverter.converter.controller;

import java.util.Date;

import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.ProxyNames;
import org.puremvc.java.demos.android.currencyconverter.converter.model.CurrencyProxy;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

/**
 * Determine if currencies needs an update, if so performs an update by calling
 * the currency webservice, if not it simply refresh or initialize currencies
 * data used by the application from data stored in the local database. 
 */
public class RefreshCurrenciesCmd
	extends SimpleCommand
{
	/**
	 * Minimal delay (expressed in seconds) separating each call to the
	 * web service instead of using the last updated stored in the
	 * database. 
	 */
	private static final int UPDATE_FREQUENCY = 3600;
	
	public void execute( INotification notification )
    {
		//TODO Use a smart date grid that excludes update on w-e and night GMT. 
		
       	if( upToDate() )
       		sendNotification(NotificationNames.CURRENCIES_REFRESHED);
       	else
       		sendNotification(NotificationNames.LOAD_CURRENCIES);
    }
	
	/**
	 * Determine if currencies data are up to date.
	 * 
	 * @return
	 * 		Currencies data are up to date and don't need an update.
	 */
	private Boolean upToDate()
	{
    	CurrencyProxy currencyProxy = (CurrencyProxy) getFacade().retrieveProxy( ProxyNames.CURRENCY_PROXY );
    	Date lastCurrenciesUpdate = currencyProxy.lastUpdated();
 
    	return new Date().getTime() - lastCurrenciesUpdate.getTime() < UPDATE_FREQUENCY*1000;
	}
}