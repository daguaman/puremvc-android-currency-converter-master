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

package org.puremvc.java.demos.android.currencyconverter.converter.view;

import java.util.ArrayList;
import java.util.Comparator;

import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.NotificationNames;
import org.puremvc.java.demos.android.currencyconverter.converter.view.components.Converter;
import org.puremvc.java.demos.android.currencyconverter.converter.view.components.CurrencyListAdapter;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * The <code>Mediator</code> object which is responsible for the converter
 * screen.
 */
public class ConverterMediator 
	extends Mediator
{
	/**
	 * The <code>ArrayAdapter</code> object needed to inject and manipulate the
	 * currencies list from within the Spinners.
	 */
	ArrayAdapter<CurrencyVo> adapter;

	private EditText inputFrom;
	private EditText inputTo;
	private Spinner spinnerFrom;
	private Spinner spinnerTo;


	/**
	 * Construct the <code>Mediator</code> instance.
	 * 
	 * @param mediatorName
	 * 		Name of the <code>Mediator</code> object.
	 * 
	 * @param viewComponent
	 * 		The view component used to display the converter screen.
	 */
	public ConverterMediator( String mediatorName, View viewComponent )
	{
		super( mediatorName, viewComponent );
		
		Converter converter = getConverter();
		inputFrom = converter.inputFrom;
		inputTo = converter.inputTo;
		spinnerFrom = converter.spinnerFrom;
		spinnerTo = converter.spinnerTo;
		
		addListeners();
	}

	/**
	 * Gives an access to the <code>Converter</code> view component.
	 * 
	 * @return
	 * 	The <code>Converter</code> view component.
	 */
	protected Converter getConverter()
	{
		return (Converter) viewComponent;
	}

	@Override
	public String[] listNotificationInterests()
	{
		return new String[]
		{
			NotificationNames.CONVERSION_ERROR
		};
	}

	@Override
	public void handleNotification( INotification note )
	{
		if( note.getName().equals(NotificationNames.CONVERSION_ERROR) )
		{
			//TODO Highlight the input text field to notify of the error
		}
	}
	
	@Override
	public void onRemove()
	{
		removeListeners();
	}

	private void addListeners()
	{
		spinnerFrom.setOnItemSelectedListener(spinnerSelectedListener);
		spinnerTo.setOnItemSelectedListener(spinnerSelectedListener);
		inputFrom.addTextChangedListener(inputToTextChangedListener);
	}
	
	private void removeListeners()
	{
		spinnerFrom.setOnItemSelectedListener(null);
		spinnerTo.setOnItemSelectedListener(null);
		inputFrom.addTextChangedListener(null);
	}

	/**
	 * Add the currencies list reference to the view.
	 * 
	 * This method is invoked when an updated currencies list is received.
	 * The currencies list names are injected into the comboboxes to let the
	 * user select both needed for the conversion.
	 * 
	 * @param currenciesList
	 * 		The currencies list reference
	 */
	public void setCurrencies( ArrayList<CurrencyVo> currenciesList )
	{
		adapter = new CurrencyListAdapter( getConverter().getContext(), currenciesList );

		adapter.sort(LOCALIZED_NAME_ORDER);

       	spinnerFrom.setAdapter( adapter );
       	spinnerTo.setAdapter( adapter );
	}
	
	/**
	 * Defines the default currency selected for the <code>SpinnerFrom</code>
	 * spinner.
	 * 
	 * @param code
	 * 		The code of the currency to	select.
	 */
	public void setCurrencyFrom( String code )
	{
		selectCurrency( spinnerFrom, code );
	}
	
	/**
	 * Defines the default currency selected for the <code>SpinnerTo</code>
	 * spinner.
	 * 
	 * @param code
	 * 		The code of the currency to	select.
	 */
	public void setCurrencyTo( String code )
	{
		selectCurrency( spinnerTo, code );
	}
	
	/**
	 * Defines the default currency selected for in one of the
	 * <code>Spinner</code>s.
	 * 
	 * @param code
	 * 		The code of the currency to	select.
	 */
	private void selectCurrency( Spinner spinner, String code )
	{
       	int len = adapter.getCount();
       	for( int i=0; i<len; i++ )
       		if( adapter.getItem(i).code.equals( code ) )
       			spinner.setSelection(i);
	}

	/**
	 * Set the result of a conversion.
	 * 
	 * @param result
	 * 		The result of the conversion expressed as a string.
	 */
	public void setResult( String result )
	{
		inputTo.setText( result );
	}

	/**
	 * Gather field values and dispatch a notification asking the application
	 * to convert the value entered by the user.
	 */
	private void convert()
	{
		String value = inputFrom.getText().toString();
		CurrencyVo from = adapter.getItem( spinnerFrom.getSelectedItemPosition() );
		CurrencyVo to = adapter.getItem( spinnerTo.getSelectedItemPosition() );

    	Object[] body = new Object[]{ value, from, to };
    	getFacade().sendNotification( NotificationNames.CONVERT, body );
    }
	
	private OnItemSelectedListener spinnerSelectedListener = new OnItemSelectedListener()
	{
		/**
		 * Callback method to be invoked when an item in a spinner has been
		 * selected.
		 * 
		 * @param parent
		 * 		The AdapterView where the selection happened.
		 * 
		 * @param view
		 * 		The view within the AdapterView that was clicked.
		 * 
		 * @param position
		 * 		The position of the view in the adapter.
		 * 
		 * @param id
		 * 		The row id of the item that is selected.
		 */
	    public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
	    {
	    	convert();
	    }

	    /**
		 * Callback method to be invoked when no item in a spinner has been
		 * selected.
	     * 
	     * @param parent
	     * 		The AdapterView that now contains no selected item.  
	     */
	    public void onNothingSelected( AdapterView<?> parent )
	    {
	    	//Simply force the conversion
	    	convert();
	    }
	};

    
    /**
     * Event listener associated to text changes events of the currency input
     * value text field.
     */
    private TextWatcher inputToTextChangedListener = new TextWatcher()
    {
        /**
         * This method is called to notify you that, somewhere within s, the text
         * has been changed.
         */
    	public void afterTextChanged( Editable s )
    	{
    		convert();
    	}

    	/**
    	 * This method is called to notify you that, within s, the count characters
    	 * beginning at start are about to be replaced by new text with length
    	 * after. It is an error to attempt to make changes to s from this
    	 * callback.
    	 */
    	public void beforeTextChanged( CharSequence s, int start, int count, int after )
    	{
    		//Nothing to do here
    	}

    	/**
    	 * This method is called to notify you that, within s, the count characters
    	 * beginning at start have just replaced old text that had length before.
    	 * It is an error to attempt to make changes to s from this callback. 
    	 */
    	public void onTextChanged( CharSequence s, int start, int before, int count )
    	{
    		//Nothing to do here
    	}
    };

	/**
	 * Comparator used to order currencies by their localized name.
	 */
	private static final Comparator<CurrencyVo> LOCALIZED_NAME_ORDER = new Comparator<CurrencyVo>()
	{
		public int compare( CurrencyVo obj1, CurrencyVo obj2 )
		{
			String name1 = obj1.name;
			String name2 = obj2.name;

			return name1.compareTo(name2);
		}
	};
}