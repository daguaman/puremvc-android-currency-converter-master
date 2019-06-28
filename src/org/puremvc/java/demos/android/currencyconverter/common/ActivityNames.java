package org.puremvc.java.demos.android.currencyconverter.common;

/**
 * Defines activity names to open them without having to share any
 * <code>Activity</code> concrete classes between each other.
 * 
 * Also used to give a PureMVC multiton key to each <code>Activity</code> 
 */
public class ActivityNames
{
	/**
	 * Fully qualified name for the About screen <code>Activity</code>.
	 */
	public static final String ABOUT = "org.puremvc.java.demos.android.currencyconverter.about.AboutActivity";

	/**
	 * Fully qualified name for the Converter screen <code>Activity</code>.
	 */
	public static final String CONVERTER = "org.puremvc.java.demos.android.currencyconverter.converter.ConverterActivity";
	
	/**
	 * Fully qualified name for the Preferences screen <code>Activity</code>.
	 */
	public static final String PREFERENCES = "org.puremvc.java.demos.android.currencyconverter.preferences.PreferencesActivity";
}