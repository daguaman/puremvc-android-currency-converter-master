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

package org.puremvc.java.demos.android.currencyconverter.about;

import org.puremvc.java.demos.android.currencyconverter.about.abc.NotificationNames;
import org.puremvc.java.demos.android.currencyconverter.about.controller.StartupCmd;
import org.puremvc.java.multicore.patterns.facade.Facade;

public class AboutActivityFacade
	extends Facade
{
	/**
	 * Construct the <code>Facade</code> multiton instance for this
	 * <code>Activity</code>.
	 * 
	 * @param multitonKey
	 * 		Multiton key for this <code>Facade</code> multiton instance.
	 */
	public AboutActivityFacade( String multitonKey )
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
	public static AboutActivityFacade getInst( String multitonKey )
	{
		if( instanceMap.get(multitonKey) == null )
			instanceMap.put( multitonKey, new AboutActivityFacade( multitonKey ) );

		return (AboutActivityFacade) instanceMap.get(multitonKey);
	}

	@Override
	protected void initializeController()
	{
		super.initializeController();

		registerCommand( NotificationNames.STARTUP, new StartupCmd() );
	}

	/**
	 * Start the <code>Activity</code> initialization sequence.
	 * 
	 * @param activity
	 * 		A reference to the <code>Activity</code> to initialize.
	 */
	public void startup( AboutActivity activity )
	{
		sendNotification(  NotificationNames.STARTUP, activity  );
	}

}