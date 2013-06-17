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
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import unicopa.copa.app.R;
import unicopa.copa.app.SingleEventLocal;

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
	    holder.date = (TextView) convertView
		    .findViewById(R.id.list_main_date);
	    holder.dura = (TextView) convertView.findViewById(R.id.duration);
	    holder.eventName = (TextView) convertView
		    .findViewById(R.id.eventName);
	    holder.supervisor = (TextView) convertView
		    .findViewById(R.id.supervisor);
	    holder.time = (TextView) convertView.findViewById(R.id.time);
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
	holder.date.setText(new SimpleDateFormat("dd.MM").format(sEvent
		.getDate()));
	holder.dura.setText(String.valueOf(sEvent.getDurationMinutes()));
	holder.time.setText(new SimpleDateFormat("HH:mm").format(sEvent
		.getDate()));

	int locationUpdateCounter = sEvent.getLoactionUpdateCounter();
	int dateUpdateCounter = sEvent.getDateUpdateCounter();
	int supervisorUpdateCounter = sEvent.getSupervisorUpdateCounter();
	int durationMinutesUpdateCounter = sEvent
		.getDurationMinutesUpdateCounter();

	if (locationUpdateCounter > 0) {
	    holder.location.setTextColor(context.getResources().getColor(
		    R.color.changed));
	}
	if (dateUpdateCounter > 0) {
	    holder.time.setTextColor(context.getResources().getColor(
		    R.color.changed));
	}
	if (supervisorUpdateCounter > 0) {
	    holder.supervisor.setTextColor(context.getResources().getColor(
		    R.color.changed));
	}
	if (durationMinutesUpdateCounter > 0) {
	    holder.dura.setTextColor(context.getResources().getColor(
		    R.color.changed));
	}

	// Convert string color to int
	String colored = "#" + sEvent.getColorCode();
	int mColor = Color.parseColor(colored);

	GradientDrawable draw = (GradientDrawable) context.getResources()
		.getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);
	draw.setStroke(5, mColor);

	return convertView;
    }

    static class ViewHolder {
	TextView date;
	TextView dura;
	TextView supervisor;
	TextView location;
	TextView eventName;
	TextView time;
	LinearLayout colour;
    }

}
