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
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import unicopa.copa.app.R;
import unicopa.copa.app.SingleEventLocal;
import unicopa.copa.base.event.SingleEvent;

/**
 * This Adapter helps to show the List of SingleEvents on the MainActivity.
 * 
 * @author Christiane Kuhn
 */
public class MainAdapter extends BaseAdapter {

    ArrayList<SingleEventLocal> singleEventList;
    Context context;

    public MainAdapter(Context context, ArrayList<SingleEventLocal> eventList) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	if (convertView == null) {
	    LayoutInflater inflater = (LayoutInflater) this.context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.listitem_main, null);
	    holder = new ViewHolder();
	    holder.location = (TextView) convertView
		    .findViewById(R.id.location);
	    holder.eventName = (TextView) convertView
		    .findViewById(R.id.eventName);
	    holder.supervisor = (TextView) convertView
		    .findViewById(R.id.supervisor);
	    holder.date = (TextView) convertView.findViewById(R.id.time);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.SingleEventView);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	SingleEventLocal sEvent = (SingleEventLocal) this.getItem(position);

	holder.location.setText(sEvent.getLocation());
	holder.eventName.setText(sEvent.getName());
	holder.supervisor.setText(sEvent.getSupervisor());
	holder.date.setText(new SimpleDateFormat("HH:mm").format(sEvent
		.getDate()));

	int mColor = 0x99224488;
	GradientDrawable draw = (GradientDrawable) context.getResources()
		.getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);
	draw.setStroke(5, mColor);

	return convertView;
    }

    static class ViewHolder {
	TextView supervisor;
	TextView location;
	TextView eventName;
	TextView date;
	LinearLayout colour;
    }

}
