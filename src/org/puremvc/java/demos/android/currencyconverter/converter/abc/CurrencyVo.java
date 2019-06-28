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

package org.puremvc.java.demos.android.currencyconverter.converter.abc;

import java.util.Date;

public class CurrencyVo
{	
	/**
	 * The three letters ISO 4127 alphabetic currency code.
	 *
	 * @see http://www.iso.org/iso/support/faqs/faqs_widely_used_standards/widely_used_standards_other/currency_codes/currency_codes_list-1.htm
	 *
	 * @serial
	 */
	public String code;
	
	/**
	 * The date the currency value was last updated.
	 *
	 * @serial
	 */
	public Date date;
	
	/**
	 * The currency rate value expressed as a 5 decimals float. 
	 *
	 * @serial
	 */	
	public double rate;

	/**
	 * The localized currency name.
	 *
	 * @serial
	 */		
	public String name;

	/**
	 * The string representation of the currency symbol.
	 *
	 * @serial
	 */		
	public String symbol;
	
	/**
	 * Will be used by the view to display a text to identify each CurrencyVo
	 * item.
	 */
	@Override
	public String toString()
	{
		return name;
	}	
}