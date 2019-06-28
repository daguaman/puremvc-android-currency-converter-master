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

package org.puremvc.java.demos.android.currencyconverter.converter.view.components;

import org.puremvc.java.demos.android.currencyconverter.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

/**
 * The converter screen displaying view components need to convert from one
 * currency to another and display the result.
 */
public class Converter
	extends RelativeLayout
{
	public EditText inputFrom;
	public EditText inputTo;
	public Spinner spinnerFrom;
	public Spinner spinnerTo;	
	
	/**
	 * Construct the <code>Converter</code> screen.
	 * 
	 * @param context
	 * 		A reference to a valid Android <code>Context</code> object.
	 */
	public Converter( Context context )
	{
        super(context);
        
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.converter,this);

        initDisplay();
    }
	
    /**
     * Construct object, initializing with any attributes we understand from a
     * layout file. These attributes are defined in
     * SDK/assets/res/any/classes.xml.
     * 
     * @param context
     * 		
     * @param attrs
     * 
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet)
     */
    public Converter( Context context, AttributeSet attrs )
    {
        super( context, attrs );
        
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate( R.layout.converter, this );
       
		initDisplay();
		
		/*
        TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.MainScreen );

        CharSequence s = a.getString(R.string.LabelView_title);
        if (s != null) {
            setTitle(s.toString());
        }
        
        a.recycle();
        */
    }

	private void initDisplay()
	{
		spinnerFrom = (Spinner) findViewById(R.id.ConverterSpinnerFrom);
		spinnerTo = (Spinner) findViewById(R.id.ConverterSpinnerTo);
		
		inputFrom = (EditText) findViewById(R.id.ConverterInputFrom);
		inputTo = (EditText) findViewById(R.id.ConverterInputTo);		
	}
}