/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.puremvc.java.demos.android.currencyconverter.converter.view.components;

import java.util.ArrayList;

import org.puremvc.java.demos.android.currencyconverter.R;
import org.puremvc.java.demos.android.currencyconverter.converter.abc.CurrencyVo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Represents each currency list item displayed in the two Currencies spinner
 * from the converter. 
 */
public class CurrencyListAdapter
    extends ArrayAdapter<CurrencyVo>
	implements SpinnerAdapter
{
    private LayoutInflater mInflater;
    private Bitmap mIcon1;
    private Bitmap mIcon2;
	private ArrayList<CurrencyVo> data;

    public CurrencyListAdapter( Context context, ArrayList<CurrencyVo> data )
    {
    	super( context, 0, data );

        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        
        this.data = data;

        // Icons bound to the rows.
        //mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_1);
        //mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public CurrencyVo getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.currency_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.currency_item_name);
            //holder.icon = (ImageView) convertView.findViewById(R.id.currency_item_icon);
            holder.symbol = (TextView) convertView.findViewById(R.id.currency_item_symbol);

            convertView.setTag(holder);
        }
        else
        {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        
        CurrencyVo currency = data.get(position);

        // Bind the data efficiently with the holder.
        holder.text.setText(currency.name);
        //holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);
        holder.symbol.setText(currency.symbol);

        return convertView;
    }
    
    @Override
    public View getDropDownView( int position, View convertView, ViewGroup parent )
    {
    	return getView(position, convertView, parent);
    }

    static class ViewHolder
    {
        TextView text;
        ImageView icon;
        TextView symbol;
    }
}