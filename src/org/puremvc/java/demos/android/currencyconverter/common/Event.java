package org.puremvc.java.demos.android.currencyconverter.common;

/**
 * Our <code>Event</code> object implementation used to transport event data
 * instead of solely notifications names.
 */
public class Event
{
	/**
	 * Identifier of the event.
	 */
	public String name;

	/**
	 * Custom data shared by the event.
	 */
	public Object data;

	/**
	 * Build an event 
	 * 
	 * @param name
	 * 		Identifier of the event.
	 */
	public Event( String name )
	{
		this.name = name;
	}

	/**
	 * Build an <code>Event</code> object setting both its <code>name</code>
	 * and <code>data</code> properties.
	 * 
	 * @param name
	 * 		Identifier of the event.
	 * 
	 * @param data
	 * 		Custom data shared by the event.
	 */
	public Event( String name, Object data )
	{
		this.name = name;
		this.data = data;
	}
}