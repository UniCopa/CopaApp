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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import unicopa.copa.app.Database;
import unicopa.copa.app.R;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.event.Event;

/**
 * This Adapter helps to show the List of Events to which a user has special
 * rights.
 * 
 * @author Christiane Kuhn
 */
public class PrivAdapter extends BaseAdapter {

    ArrayList<Event> EventList;
    Context context;

    /**
     * Creates a PrivAdapter with a list of events that should be shown.
     * Typically these events are those for which a user has rights to change
     * depending SingleEvents.
     * 
     * @param context
     * @param eventList
     */
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

    /**
     * Creates the ListView with Events in user defined colors (black as
     * default). In the ListView the EventGroupName, the EventName and two
     * buttons are shown. It also handles the clicks on the buttons. If the
     * "change"-button is clicked the SingleEventList opens and the user can
     * chose a SingleEvent to update it. If the "others"-button is clicked the
     * EventPricActivity is started and the user can see all rightholders,
     * deputies and owners.
     * 
     * 
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	if (convertView == null) {
	    LayoutInflater inflater = (LayoutInflater) this.context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.listitem_priv, null);
	    holder = new ViewHolder();
	    holder.eventGroupName = (TextView) convertView
		    .findViewById(R.id.eventGroup);
	    holder.eventName = (TextView) convertView.findViewById(R.id.event);
	    holder.change = (Button) convertView.findViewById(R.id.priv_change);
	    holder.other = (Button) convertView.findViewById(R.id.priv_others);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.PrivListView);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	Database db = Database.getInstance(context);

	final Event event = (Event) this.getItem(position);
	String colored = "#000000";
	Storage S = null;
	S = Storage.getInstance(context);

	SettingsLocal settings = null;

	try {
	    settings = S.load();
	    UserEventSettings evsettings = settings.getEventSettings(event
		    .getEventID());
	    if (evsettings != null && evsettings.getColorCode() != null)
		colored = "#" + evsettings.getColorCode();
	} catch (NoStorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	holder.eventGroupName.setText(db.getEventGroupName(event
		.getEventGroupID()));
	holder.eventName.setText(event.getEventName());

	// Convert string color to int

	int mColor = Color.parseColor(colored);

	GradientDrawable draw = (GradientDrawable) context.getResources()
		.getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);
	draw.setStroke(5, mColor);

	holder.change.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		Intent intentSingleEventList = new Intent(context,
			SingleEventListActivity.class);
		intentSingleEventList.putExtra("selected", event.getEventID());
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

    /**
     * Helps to show the events in the PrivAdapter.
     * 
     * @author Christiane Kuhn
     * 
     */
    static class ViewHolder {
	TextView eventName;
	TextView eventGroupName;
	Button change;
	Button other;
	LinearLayout colour;
    }

}
