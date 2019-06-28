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

package org.puremvc.java.demos.android.currencyconverter.converter.model;

import java.util.ArrayList;
import java.util.Date;

import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;
import org.puremvc.java.multicore.patterns.proxy.Proxy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.provider.BaseColumns;
import android.util.Log;

/**
 * The <code>Proxy</code> object used to control an Android SQLite database to
 * store currencies update between each session.
 */
public class CurrencyProxy
	extends Proxy
{
    private static final String TAG = "LocalCurrencyProxy";

    private static final String DATABASE_NAME = "currency_converter.db";
    private static final int DATABASE_VERSION = 6;
    private static final String TABLE_NAME = "currencies";
    
    private SQLiteDatabase database;
	private Context context;

    /**
     * Construct the <code>Proxy</code> object.
     * 
     * An <code>SQLiteDatabase</code> object constructor isn't visible so we
     * have to rely on the <code>Proxy</code> to create it internally. Plus
     * the helper class used to create the database needs a reference to an
     * existing <code>Context</code> so the last one replace any preferred
     * database argument.
     * 
     * We currently did not cache currencies data before reading or writing it
     * in the database because our application did not need this, but it would
     * a a good idea to do it so.
     *
     * @param name
     * 		The <code>Proxy</code> name used for PureMVC framework.
     * 
     * @param context
     * 		A reference to an Android <code>Context</code> instance needed to
     * 		access the database.
     */
    public CurrencyProxy( String name, Context context )
    {
        super( name, new ArrayList<CurrencyVo>() );

        this.context = context;
        createOrOpenDataBase();
    }

    /**
     * The database need to be created after Views complete registration to be
     * sure to have access to 
     */
    private void createOrOpenDataBase()
    {
    	if( context == null )
    	{
            Log.e( TAG, "Database need to be initialized with an Android context object" );
            return;
    	}

		DatabaseHelper databaseHelper = new DatabaseHelper(context);  
		try
		{
			database = databaseHelper.getWritableDatabase();
		}
		catch( SQLiteException e )
		{
			Log.e( TAG, e.getMessage(), e );
			return;
		}
		
		database.setLockingEnabled(false);
	}

	/**
     * Get the list of <code>CurrencyVO</code>s object stored in the database.
     * 
	 * @return
	 * 		The list of <code>CurrencyVO</code>s object stored in the database.
     */
    public ArrayList<CurrencyVo> getCurrencies()
    {
    	ArrayList<CurrencyVo> currencies = new ArrayList<CurrencyVo>();

    	if( context == null )
    	{
            Log.e( TAG, "Database need to be initialized with an Android context object" );
            return currencies;
    	}
    	
    	if( !database.isOpen() )
    	{
            Log.e( TAG, "Database need to be created and opened" );
            return currencies;
    	}

        String[] columns = new String[]
  		{ 
  			Currency.CODE,
  			Currency.RATE,
  			Currency.NAME,
  			Currency.SYMBOL,
  			Currency.DATE
  		};

    	database.beginTransaction();
		Cursor cursor = database.query
        (
        	TABLE_NAME,
        	columns ,
    		"1",							//where
    		null,							//selectionArgs
    		null,							//groupBy
    		null,							//having
    		Currency.CODE,					//orderBy
    		null							//limit
        );
    	database.endTransaction();
      
        // Make sure we are at the one and only row in the cursor.
        if( cursor.moveToFirst() )
        {
			do
			{
				CurrencyVo currency = new CurrencyVo();
				currency.code = cursor.getString(0);
				currency.rate = cursor.getDouble(1);
				currency.name = cursor.getString(2);
				currency.symbol = cursor.getString(3);
				currency.date = new Date(cursor.getInt(4)*1000);
				currencies.add(currency);
			}
			while( cursor.moveToNext() );
        }
        
    	cursor.close();
    
        return currencies;
    }

	/**
     * Set the list of <code>CurrencyVO</code>s object to store in the database.
     * 
     * @param currency
     * 		A list of <code>CurrencyVO</code>s object to store in the database.
     */
    public void setCurrencies( ArrayList<CurrencyVo> currencies )
    {
    	if( context == null )
    	{
            Log.e( TAG, "Database need to be initialized" );
            return;
    	}
    	
    	if( !database.isOpen() )
    	{
            Log.e( TAG, "Database need to be created and opened" );
            return;
    	}
    	
    	int len = currencies.size();
    	if( len == 0 )
    		return;
    	
    	clear();

    	database.beginTransaction();
    	for( int i=0; i<len; i++ )
    	{
    		CurrencyVo currency = currencies.get(i);
			ContentValues contentValues = new ContentValues();
	        contentValues.put( Currency.CODE, currency.code );
	        contentValues.put( Currency.RATE, currency.rate );
	        contentValues.put( Currency.NAME, currency.name );
	        contentValues.put( Currency.SYMBOL, currency.symbol );
	        contentValues.put( Currency.DATE, currency.date.getTime()/1000 );
	        
			try
			{
				database.insertOrThrow( TABLE_NAME, null, contentValues );
			}
			catch( SQLException e )
			{
				Log.d( TAG, e.getMessage(), e );
			}
    	}
    	
		database.setTransactionSuccessful();
		database.endTransaction();
    }

    /**
     * Returns the <code>CurrencyVo</code> item corresponding to the given
     * code.
     * 
     * @param code
     * 		The code of the currency we want to be returned.
     * 
     * @return
     * 		The <code>CurrencyVo</code> item corresponding to the code or null
     * 		of the <code>CurrencyVo</code> object could not be found.
     */
    public CurrencyVo getCurrency( String code )
    {
    	if( context == null )
    	{
            Log.e( TAG, "Database need to be initialized with an Android context object" );
            return null;
    	}
    	
    	if( !database.isOpen() )
    	{
            Log.e( TAG, "Database need to be opened" );
            return null;
    	}

        String[] columns = new String[]
		{ 
			Currency.CODE,
			Currency.RATE,
			Currency.NAME,
			Currency.SYMBOL,
			Currency.DATE
		};
	
	  	database.beginTransaction();
		Cursor cursor = database.query
		(
			TABLE_NAME,
			columns ,
			Currency.CODE + "=\"" + code + "\"",	//where
			null,						//selectionArgs
			null,						//groupBy
			null,						//having
			null,						//orderBy
			"1"							//limit
		);
	  	database.endTransaction();
	    
		if( !cursor.moveToFirst() )
			return null;

	  	CurrencyVo currency = new CurrencyVo();
		currency.code = cursor.getString(0);
		currency.rate = cursor.getDouble(1);
		currency.name = cursor.getString(2);
		currency.symbol = cursor.getString(3);
		currency.date = new Date(cursor.getLong(4)*1000);
		
    	cursor.close();
		
		return currency;
    }

    /**
     * Clear all currencies columns from the currencies table. 
     */
    private void clear()
    {
    	database.beginTransaction();
		try
		{
			database.execSQL("DELETE FROM " + TABLE_NAME );
			database.setTransactionSuccessful();
		}
		catch( SQLException e )
		{
			Log.e( TAG, e.getMessage(), e );
			return;
		}
		finally
		{
			database.endTransaction();			
		}
    }

    /**
     * Get the last update date made to Currencies to stored in the database.
     * 
     * @return date
     * 		The date object corresponding to the most recent currency updated
     * 		in the Currency database or zeroed date if the database was never
     * 		written.
     */
    public Date lastUpdated()
    {
    	Date date = new Date(0);
    	
    	String[] columns = new String[]
		{ 
    		Currency.DATE
		};
    	
    	database.beginTransaction();
    	Cursor cursor = database.query
    	(
			TABLE_NAME,
			columns ,
			"1",						//where
			null,						//selectionArgs
			null,						//groupBy
			null,						//having
			Currency.DATE + " DESC",	//orderBy
			"1"							//limit
    	);
    	database.endTransaction();

    	// Make sure we are at the one and only row in the cursor.
    	if( cursor.moveToFirst() )
    		date = new Date(cursor.getLong(0)*1000);
        
    	cursor.close();
    	
    	return date;
    }  
   
    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper
    	extends	SQLiteOpenHelper
    {
        DatabaseHelper( Context context )
        {
            super( context, DATABASE_NAME, null, DATABASE_VERSION );
        }

        @Override
        public void onCreate(SQLiteDatabase database)
        {
        	database.beginTransaction();
        	try
        	{
	            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
	            database.execSQL
	            (
	            		"CREATE TABLE " + TABLE_NAME + " "
	            		+ "("
	                    + Currency.CODE + " TEXT PRIMARY KEY,"
	                    + Currency.RATE + " DECIMAL(12,5),"
	                    + Currency.NAME + " TEXT,"
	                    + Currency.SYMBOL + " TEXT,"
	                    + Currency.DATE + " DATE"
	                    + ");"
	            );
	            database.setTransactionSuccessful();
        	}
        	catch( SQLException e )
        	{
        		Log.e( TAG, "Error creating table " + TABLE_NAME + ": " + e.toString(), e );
        	}
        	finally
        	{
        		database.endTransaction();
        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
        {
            Log.w( TAG, "Upgrading database " + DATABASE_NAME + " from version " + oldVersion + " to "  + newVersion + ", which will destroy all old data" );
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        	database.beginTransaction();
        	try
        	{
	            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
	            database.setTransactionSuccessful();
        	}
        	catch( SQLException e )
        	{
        		Log.e( TAG, "Error upgrading table " + TABLE_NAME + ": " + e.toString(), e );
        	}
        	finally
        	{
        		database.endTransaction();
        	}
        	
        	onCreate(database);
        }
    }
    
    /**
     * Represent a row of the Currency table.
     */
   private static final class Currency
    	implements BaseColumns
    {
		// This class cannot be instantiated
        private Currency() {}

        public static final String CODE = "code";
        public static final String RATE = "rate";
        public static final String NAME = "name";
        public static final String SYMBOL = "symbol";
        public static final String DATE = "date";
    }
}