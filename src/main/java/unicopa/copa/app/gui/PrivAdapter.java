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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import unicopa.copa.app.R;
import unicopa.copa.base.event.Event;

/**
 * This Adapter helps to show the List of Events.
 * 
 * @author Christiane Kuhn
 */
public class PrivAdapter extends BaseAdapter {

    ArrayList<Event> EventList;
    Context context;

    public PrivAdapter(Context context, ArrayList<Event> eventList) {
	this.context = context;
	this.EventList = eventList;
    }

    @Override
    public int getCount() {
	return EventList.size();
    }

    @Override
    public Object getItem(int arg0) {
	return EventList.get(arg0);
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
	    convertView = inflater.inflate(R.layout.listitem_priv, null);
	    holder = new ViewHolder();

	    holder.eventName = (TextView) convertView.findViewById(R.id.event);
	    holder.change = (Button) convertView.findViewById(R.id.priv_change);
	    holder.other = (Button) convertView.findViewById(R.id.priv_others);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.PrivListView);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	Event event = (Event) this.getItem(position);
	holder.eventName.setText(event.getEventName());
	Drawable draw = context.getResources().getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);

	holder.change.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		Intent intentSingleEventList = new Intent(context,
			SingleEventListActivity.class);
		intentSingleEventList.putExtra("key", "value");
		context.startActivity(intentSingleEventList);

	    }

	});

	holder.other.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intentEventPriv = new Intent(context,
			EventPrivActivity.class);
		intentEventPriv.putExtra("key", "value");
		context.startActivity(intentEventPriv);

	    }

	});

	return convertView;
    }

    static class ViewHolder {
	TextView eventName;
	TextView eventGroupName;
	Button change;
	Button other;
	LinearLayout colour;
    }

}