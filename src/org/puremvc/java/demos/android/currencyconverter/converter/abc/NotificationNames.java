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

/**
 * Define the list of <code>Notification</code>s names used by the
 * <code>ConverterActivity</code>.
 */
public class NotificationNames
{
	public static final String STARTUP = "startup";

	public static final String LOAD_CURRENCIES = "loadCurrencies";
	public static final String CURRENCIES_LOADED = "currenciesLoaded";
	public static final String CURRENCIES_LOADING_FAILED = "currenciesLoadingFailed";

	public static final String REFRESH_CURRENCIES = "refreshCurrencies";
	public static final String CURRENCIES_REFRESHED = "currenciesRefreshed";
	
	public static final String CONVERT = "convert";
	public static final String CONVERSION_ERROR = "conversionError";
}