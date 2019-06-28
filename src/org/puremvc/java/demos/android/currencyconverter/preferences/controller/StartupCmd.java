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

package org.puremvc.java.demos.android.currencyconverter.preferences.controller;

import org.puremvc.java.multicore.patterns.command.MacroCommand;

/**
 * Performs operations needed to initialize the <code>Activity</code>.
 */
public class StartupCmd
	extends MacroCommand
{
	protected void initializeMacroCommand()
	{
		addSubCommand( new PrepModelCmd() );
		addSubCommand( new PrepViewCmd() );
	}
}