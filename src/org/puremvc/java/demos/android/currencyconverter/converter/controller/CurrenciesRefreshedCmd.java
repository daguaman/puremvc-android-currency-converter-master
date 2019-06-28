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
import java.util.Currency;
import java.util.Locale;

import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.MediatorNames;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.ProxyNames;
import org.puremvc.java.demos.android.currencyconverter.converter.model.CurrencyProxy;
import org.puremvc.java.demos.android.currencyconverter.converter.view.ConverterMediator;
import org.puremvc.java.demos.android.currencyconverter.converter.view.LoadingMediator;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

/**
 * Performs operations following a success writing of currencies data to the
 * application local database.
 */
public class CurrenciesRefreshedCmd
	extends SimpleCommand
{
	public void execute( INotification notification )
    {
       	CurrencyProxy currencyProxy = (CurrencyProxy) getFacade().retrieveProxy( ProxyNames.CURRENCY_PROXY );
		ArrayList<CurrencyVo> currencies = currencyProxy.getCurrencies();
    	
       	LoadingMediator loadingMediator = (LoadingMediator) getFacade().retrieveMediator( MediatorNames.LOADING );
    	loadingMediator.loaded();
    	
    	ConverterMediator converterMediator = (ConverterMediator) getFacade().retrieveMediator( MediatorNames.CONVERTER );
       	converterMediator.setCurrencies( currencies );
     
       	setDefaultCurrencies();
    }

	/**
	 * Define default currencies displayed in the <code>Converter</code> form.
	 */
	private void setDefaultCurrencies()
	{
		/*
		 * Here we use the native Java Currency object to determine which is
		 * the locale currency code used by the OS.
		 */
       	Currency localeCurrency = Currency.getInstance(Locale.getDefault());
       	String localeCode = localeCurrency.getCurrencyCode();
       	
       	CurrencyProxy currencyProxy = (CurrencyProxy) getFacade().retrieveProxy( ProxyNames.CURRENCY_PROXY );
       	CurrencyVo currency = currencyProxy.getCurrency(localeCode);

       	//If the current default locale currency is not supported
       	if( currency == null )
       		return;

       	ConverterMediator converterMediator = (ConverterMediator) getFacade().retrieveMediator( MediatorNames.CONVERTER );
       	converterMediator.setCurrencyFrom( currency.code );
       	converterMediator.setCurrencyTo( currency.code );
	}
}