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

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import unicopa.copa.app.R;
import unicopa.copa.base.event.Event;

/**
 * This Adapter helps to show the List of Events.
 * 
 * @author Christiane Kuhn
 */
public class SearchResultEventAdapter extends BaseAdapter {

    ArrayList<Event> EventList;
    Context context;

    public SearchResultEventAdapter(Context context, ArrayList<Event> eventList) {
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
	    convertView = inflater
		    .inflate(R.layout.listitem_result_event, null);
	    holder = new ViewHolder();
	    holder.name = (TextView) convertView
		    .findViewById(R.id.item_result_event_name);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.item_result_event_list);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	Event event = (Event) this.getItem(position);
	holder.name.setText(event.getEventName());
	Drawable draw = context.getResources().getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);

	return convertView;
    }

    static class ViewHolder {
	TextView name;
	LinearLayout colour;
    }

}
