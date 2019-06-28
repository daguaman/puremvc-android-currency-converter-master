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

import java.util.ArrayList;

import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.MediatorNames;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.ProxyNames;
import org.puremvc.java.demos.android.currencyconverter.converter.model.CurrencyProxy;
import org.puremvc.java.demos.android.currencyconverter.converter.view.LoadingMediator;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

/**
 * Performs operations following the receipt of the currency webservice result.
 */
public class CurrenciesLoadedCmd
	extends SimpleCommand
{
	@SuppressWarnings("unchecked")
	public void execute( INotification notification )
    {
		ArrayList<CurrencyVo> currencies = (ArrayList<CurrencyVo>) notification.getBody();
    	
       	LoadingMediator loadingMediator = (LoadingMediator) getFacade().retrieveMediator( MediatorNames.LOADING );
    	loadingMediator.loaded();
    	
       	CurrencyProxy currencyProxy = (CurrencyProxy) getFacade().retrieveProxy( ProxyNames.CURRENCY_PROXY );
       	currencyProxy.setCurrencies(currencies);

       	sendNotification( NotificationNames.CURRENCIES_REFRESHED );
    }
}