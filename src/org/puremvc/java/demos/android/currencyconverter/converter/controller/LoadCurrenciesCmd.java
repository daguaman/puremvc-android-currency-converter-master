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

import java.util.Locale;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.MediatorNames;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.ProxyNames;
import org.puremvc.java.demos.android.currencyconverter.converter.model.CurrencyServiceProxy;
import org.puremvc.java.demos.android.currencyconverter.converter.view.ConverterActivityMediator;
import org.puremvc.java.demos.android.currencyconverter.converter.view.LoadingMediator;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import android.content.Context;

public class LoadCurrenciesCmd
	extends SimpleCommand
{
    public void execute( INotification notification )
    {
    	LoadingMediator loadingMediator = (LoadingMediator) getFacade().retrieveMediator( MediatorNames.LOADING );
    	loadingMediator.loading();

    	callWebService();
    }
    
    /**
     * Initialize curencies data loading by calling a webservice.
     */
    private void callWebService()
    {
    	CurrencyServiceProxy currencyProxy = (CurrencyServiceProxy) getFacade().retrieveProxy( ProxyNames.CURRENCY_SERVICE_PROXY );

    	/*
    	 * We don't need to authorize the application to call the currency web
    	 * service simultaneously multiple times. 
    	 */
    	if( currencyProxy.isLoading() )
    		return;

    	String uri = getGatewayUri();
    	currencyProxy.setUri(uri);

    	String service = getServiceName();
    	currencyProxy.setService(service);    	

    	//TODO Need to be the system default currency not "USD" (country->currency...)
    	String currency = "usd";
    	currencyProxy.setCurrency(currency);   	

    	String lang = Locale.getDefault().getLanguage();
    	currencyProxy.setLang(lang);

    	currencyProxy.load();
    }

    /**
     * Get the gateway URI from the Android application configuration file.
     * @return
     */
	private String getGatewayUri()
    {
        ConverterActivityMediator currencyConverterMediator = (ConverterActivityMediator) getFacade().retrieveMediator(MediatorNames.CONVERTER_ACTIVITY);
        Context context = currencyConverterMediator.getContext();
        return context.getString(R.string.gateway_uri);
    }

    /**
     * Get the service name to call on the gateway from the Android application
     * configuration file.
     * @return
     */
	private String getServiceName()
	{
		ConverterActivityMediator currencyConverterMediator = (ConverterActivityMediator) getFacade().retrieveMediator(MediatorNames.CONVERTER_ACTIVITY);
		Context context = currencyConverterMediator.getContext();
		return context.getString(R.string.service);
	}
}