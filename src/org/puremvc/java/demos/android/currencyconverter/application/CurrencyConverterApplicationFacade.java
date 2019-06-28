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

package org.puremvc.java.demos.android.currencyconverter.application;

import org.puremvc.java.multicore.patterns.facade.Facade;

public class CurrencyConverterApplicationFacade
	extends Facade
{
	/**
	 * Construct the <code>ApplicationFacade</code> singleton needed by the
	 * application.
	 * 
	 * @param multitonKey
	 * 		Multiton key for this facade instance.
	 */
	public CurrencyConverterApplicationFacade( String multitonKey )
	{
		super(multitonKey);
	}

	/**
	 * <code>ApplicationFacade</code> singleton fabrication method
	 * implementation.
	 * 
	 * @param multitonKey
	 * 		Multiton key of the facade instance to get.
	 */
	public static CurrencyConverterApplicationFacade getInst( String multitonKey )
	{
		if( instanceMap.get(multitonKey) == null )
			instanceMap.put( multitonKey, new CurrencyConverterApplicationFacade( multitonKey ) );

		return (CurrencyConverterApplicationFacade) instanceMap.get(multitonKey);
	}

	protected void initializeController()
	{
		super.initializeController();
	}

	public void startup( CurrencyConverterApplication application )
	{
	}
}