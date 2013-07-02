/*
 * Copyright (C) 2013 UniCoPA
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
package unicopa.copa.app.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import unicopa.copa.app.R;
import unicopa.copa.base.event.SingleEvent;

/**
 * This Adapter helps to show the List of SingleEvents which belong to a before
 * with the search found Event.
 * 
 * @author Christiane Kuhn
 */
public class SearchResultSingleEventAdapter extends BaseAdapter {

    ArrayList<SingleEvent> singleEventList;
    Context context;

    /**
     * Creates a SearchResultSingleEventAdapter with a SingleEventList that
     * should be shown.
     * 
     * @param context
     * @param eventList
     */
    public SearchResultSingleEventAdapter(Context context,
	    ArrayList<SingleEvent> eventList) {
	this.context = context;
	this.singleEventList = eventList;
    }

    @Override
    public int getCount() {
	return singleEventList.size();
    }

    @Override
    public Object getItem(int arg0) {
	return singleEventList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
	return arg0;
    }

    /**
     * Creates the ListView with SingleEvents. The time and date to every
     * SingleEvent are shown.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	if (convertView == null) {
	    LayoutInflater inflater = (LayoutInflater) this.context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(
		    R.layout.listitem_result_singleevent, null);
	    holder = new ViewHolder();

	    holder.time = (TextView) convertView
		    .findViewById(R.id.search_list_time);
	    holder.date = (TextView) convertView
		    .findViewById(R.id.search_list_date);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.SingleEventView);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	SingleEvent sEvent = (SingleEvent) this.getItem(position);

	holder.date.setText(new SimpleDateFormat("dd.MM.yyyy").format(sEvent
		.getDate()));
	holder.time.setText(new SimpleDateFormat("HH:mm").format(sEvent
		.getDate()));

	GradientDrawable draw = (GradientDrawable) context.getResources()
		.getDrawable(R.drawable.border_cat);
	holder.colour.setBackgroundDrawable(draw);

	return convertView;

    }

    /**
     * Helps to show the SingleEvents in the SearchResultSingleEventAdapter.
     * 
     * @author Christiane Kuhn
     * 
     */
    static class ViewHolder {
	TextView time;
	TextView date;

	LinearLayout colour;
    }

}
