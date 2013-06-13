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
 * This Adapter helps to show the List of SingleEvents.
 * 
 * @author Christiane Kuhn
 */
public class SingleEventAdapter extends BaseAdapter {

    ArrayList<SingleEvent> singleEventList;
    Context context;

    public SingleEventAdapter(Context context, ArrayList<SingleEvent> eventList) {
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
	    convertView = inflater.inflate(R.layout.listitem_singleevent, null);
	    holder = new ViewHolder();
	    holder.change = (Button) convertView
		    .findViewById(R.id.singleEventList_change);
	    holder.details = (Button) convertView
		    .findViewById(R.id.singleEventList_details);
	    holder.time = (TextView) convertView.findViewById(R.id.list_time);
	    holder.date = (TextView) convertView.findViewById(R.id.date);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.SingleEventView);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	SingleEvent sEvent = (SingleEvent) this.getItem(position);

	holder.date.setText(new SimpleDateFormat("dd.MM").format(sEvent
		.getDate()));
	holder.time.setText(new SimpleDateFormat("HH:mm").format(sEvent
		.getDate()));

	Drawable draw = context.getResources().getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);

	holder.change.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		Intent intentChangeSingleEvent = new Intent(context,
			ChangeSingleEventActivity.class);
		intentChangeSingleEvent.putExtra("key", "value");
		context.startActivity(intentChangeSingleEvent);

	    }

	});

	holder.details.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intentEventPriv = new Intent(context,
			SingleEventActivity.class);
		intentEventPriv.putExtra("key", "value");
		context.startActivity(intentEventPriv);

	    }

	});
	return convertView;

    }

    static class ViewHolder {
	TextView time;
	TextView date;
	Button details;
	Button change;
	LinearLayout colour;
    }

}
