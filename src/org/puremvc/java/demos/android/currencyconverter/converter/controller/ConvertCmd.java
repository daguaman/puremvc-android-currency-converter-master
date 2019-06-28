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

import java.text.DecimalFormat;
import java.util.Currency;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.MediatorNames;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.demos.android.currencyconverter.converter.view.ConverterMediator;
import org.puremvc.java.demos.android.currencyconverter.converter.view.ConverterActivityMediator;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import android.content.Context;

/**
 * Does the conversion of the value entered by the user. 
 */
public class ConvertCmd
	extends SimpleCommand
{
    public void execute( INotification notification )
    {
    	Object[] params = (Object[]) notification.getBody();
        String userInput = (String) params[0];
        CurrencyVo from = (CurrencyVo) params[1];
        CurrencyVo to = (CurrencyVo) params[2];

        double value = getValueFromUserInput( userInput );
        
        String result = convert( value, from, to );

        ConverterMediator converterMediator = (ConverterMediator) getFacade().retrieveMediator( MediatorNames.CONVERTER );
		converterMediator.setResult( result );
    }
    
    /**
     * Convert the value entered by the user.
     * 
     * @param value
     * 		The value to convert.
     * 
     * @param from
     * 		The currency from which to convert.
     * 
     * @param to
     * 		The currency into which the value is converted.
     * 
     * @return
     * 		The converted value.
     */
	private String convert( double value, CurrencyVo from, CurrencyVo to )
	{
		/*
		 * Here we use the native Java Currency object to have the result
		 * expressed in locale currency format.
		 */
		Currency javaCurrency = Currency.getInstance(to.code);
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setCurrency(javaCurrency);

		double result = value * ( to.rate/from.rate );
		return decimalFormat.format(result);
	}

	private double getValueFromUserInput( String userInput )
	{
        try
        {
        	return Double.valueOf( userInput.trim() ).doubleValue();
        }
        catch( NumberFormatException e )
        {
            ConverterActivityMediator currencyConverterMediator = (ConverterActivityMediator) getFacade().retrieveMediator( MediatorNames.CONVERTER_ACTIVITY );
            Context context = currencyConverterMediator.getContext();
            String message = context.getString( R.string.conversion_error_bad_input_value, userInput );
            String internalMessage = "NumberFormatException: " + e.getMessage();
            getFacade().sendNotification( NotificationNames.CONVERSION_ERROR, message, internalMessage );

        	return 0;
        }
	}
}