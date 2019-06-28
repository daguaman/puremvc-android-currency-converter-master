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

package org.puremvc.java.demos.android.currencyconverter.about.view;

import org.puremvc.java.demos.android.currencyconverter.about.AboutActivity;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

/**
 * The <code>Mediator</code> object which is responsible for the
 * About <code>Activity</code> screen.
 */
public class AboutActivityMediator 
	extends Mediator
{
	/**
	 * Construct the <code>Mediator</code> instance.
	 * 
	 * @param mediatorName
	 * 		Name of the <code>Mediator</code> object.
	 * 
	 * @param activity
	 * 		The <code>PreferencesActivity</code> used to display the <em>about</em> screen.
	 */
	public AboutActivityMediator( String mediatorName, AboutActivity activity )
	{
		super( mediatorName, activity );
	
	}

	/**
	 * A reference to the <code>AboutActivity</code> for which the
	 * <code>Mediator</code> is responsible for.
	 * 
	 * @return
	 * 		The <code>AboutActivity</code> view component.
	 */
	protected AboutActivity getAboutActivity()
	{
		return (AboutActivity) viewComponent;
	}

	@Override
	public String[] listNotificationInterests()
	{
		return new String[]
		{

		};
	}

	@Override
	public void handleNotification( INotification note )
	{

	}
}