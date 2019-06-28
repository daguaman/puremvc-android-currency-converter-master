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

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.converter.ConverterActivity;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.MediatorNames;
import org.puremvc.java.demos.android.currencyconverter.converter.view.ConverterMediator;
import org.puremvc.java.demos.android.currencyconverter.converter.view.ConverterActivityMediator;
import org.puremvc.java.demos.android.currencyconverter.converter.view.LoadingMediator;
import org.puremvc.java.demos.android.currencyconverter.converter.view.components.Converter;
import org.puremvc.java.demos.android.currencyconverter.converter.view.components.Loading;
import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

/**
 * Initialize <code>Mediator</code>s with the view they are responsible for.
 */
public class PrepViewCmd
	extends SimpleCommand
	implements ICommand
{
    public void execute(INotification note )
    {
    	ConverterActivity activity = (ConverterActivity) note.getBody();

       	Loading loading = (Loading) activity.findViewById(R.id.LoadingLayout);
       	Converter converter = (Converter) activity.findViewById(R.id.ConverterLayout);
       	//Preferences preferences = (Preferences) activity.findViewById(R.id.PreferencesLayout);

       	ConverterActivityMediator converterActivityMediator = new ConverterActivityMediator( MediatorNames.CONVERTER_ACTIVITY, activity );

       	LoadingMediator loadingMediator = new LoadingMediator( MediatorNames.LOADING, loading );
       	ConverterMediator converterMediator = new ConverterMediator( MediatorNames.CONVERTER, converter );
        //PreferencesMediator preferencesMediator = new PreferencesMediator( MediatorNames.PREFERENCES, preferences );

        getFacade().registerMediator( converterActivityMediator );
        getFacade().registerMediator( loadingMediator );
        getFacade().registerMediator( converterMediator );
        //facade.registerMediator( preferencesMediator );

    }
}