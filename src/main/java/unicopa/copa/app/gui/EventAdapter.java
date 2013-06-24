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

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Dialog;
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
import unicopa.copa.app.Helper;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.Event;

/**
 * This Adapter helps to show the List of subscribed Events.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class EventAdapter extends BaseAdapter {

    ArrayList<Event> EventList;
    Context context;

    /**
     * Creates a EventAdapter with a EventList that should be shown.
     * 
     * @param context
     * @param eventList
     */
    public EventAdapter(Context context, ArrayList<Event> eventList) {
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
     * default). In the ListView the EventGroupName, the EventName and three
     * buttons are shown. It also handles the clicks on the buttons. If the
     * "color"-button is clicked a colorDialog opens. If the
     * "unsubscribe"-button is clicked a message to the server is send that
     * unsubscribes the user.The user will also be informed by a dialog. If the
     * "details"-button is clicked the SingleEventListActivity with all
     * SingleEvents, that belong to the currently watched event, is shown.
     * 
     * 
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	if (convertView == null) {
	    LayoutInflater inflater = (LayoutInflater) this.context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.listitem_event, null);
	    holder = new ViewHolder();

	    holder.eventGroupName = (TextView) convertView
		    .findViewById(R.id.eventGroup);
	    holder.eventName = (TextView) convertView.findViewById(R.id.event);
	    holder.colorButton = (Button) convertView
		    .findViewById(R.id.color_change);
	    holder.unsubscribe = (Button) convertView
		    .findViewById(R.id.unsubscribe_event);
	    holder.details = (Button) convertView.findViewById(R.id.details);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.subscrListView);
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

	holder.details.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intentSingleEvent = new Intent(context,
			SingleEventListActivity.class);
		intentSingleEvent.putExtra("selected", event.getEventID());
		context.startActivity(intentSingleEvent);

	    }

	});

	holder.colorButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		ServerConnection scon = null;
		scon = ServerConnection.getInstance();

		// check if logged in if not redirect to LoginActivity
		if (!scon.getConnected()) {
		    PopUp.loginFail(context);
		} else {
		    ColorDialog color = new ColorDialog();
		    Dialog diag = color.onCreateDialog(null, context,
			    event.getEventID());
		    diag.show();
		}

	    }

	});

	holder.unsubscribe.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		int eventID = event.getEventID();

		Storage storage = null;
		storage = Storage.getInstance(null);

		ServerConnection scon = null;
		scon = ServerConnection.getInstance();

		// check if logged in if not redirect to LoginActivity
		if (!scon.getConnected()) {

		    PopUp.loginFail(context);

		} else {

		    SettingsLocal settingsLocal = null;
		    try {
			settingsLocal = (SettingsLocal) storage.load();
		    } catch (NoStorageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }

		    try {
			Helper.unsubscribe(eventID, settingsLocal, context);
		    } catch (ClientProtocolException e) {
			PopUp.exceptionAlert(context,
				context.getString(R.string.cp_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (APIException e) {
			PopUp.exceptionAlert(context,
				context.getString(R.string.api_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (PermissionException e) {
			PopUp.exceptionAlert(context,
				context.getString(R.string.per_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (RequestNotPracticableException e) {
			PopUp.exceptionAlert(context,
				context.getString(R.string.rnp_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (InternalErrorException e) {
			PopUp.exceptionAlert(context,
				context.getString(R.string.ie_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (IOException e) {
			PopUp.exceptionAlert(context,
				context.getString(R.string.io_ex),
				e.getMessage());
			// e.printStackTrace();
		    }

		    PopUp.unsubscribed(context);

		}
	    }

	});

	return convertView;
    }

    /**
     * Helps to show the events in the EventAdapter.
     * 
     * @author Christiane Kuhn
     * 
     */
    static class ViewHolder {
	TextView eventName;
	TextView eventGroupName;
	Button colorButton;
	Button unsubscribe;
	Button details;
	LinearLayout colour;
    }

}
