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

import unicopa.copa.app.Database;
import unicopa.copa.app.R;
import unicopa.copa.app.SingleEventLocal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * In this activity a user can see all details of a SingleEvent.
 * 
 * @author Christiane Kuhn
 */
public class SingleEventActivity extends Activity {
    SingleEventLocal sEventLocal;

    /**
     * Shows all information of a SingleEvent which are in the database.
     * Depending on the user role of the corresponding event the change-button
     * is shown.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.singleevent);
	Intent intent = getIntent();
	int singleEventID = intent.getIntExtra("selectedID", 0);
	Database db = Database.getInstance(SingleEventActivity.this);

	sEventLocal = db.getSingleEventBySingleEventID(singleEventID);
	db.close();

	TextView location = (TextView) super.findViewById(R.id.location_sEv);
	TextView date = (TextView) super.findViewById(R.id.date_sEv);
	TextView time = (TextView) super.findViewById(R.id.time_sEv);
	TextView supervisor = (TextView) super
		.findViewById(R.id.supervisor_sEv);
	TextView durationtime = (TextView) super
		.findViewById(R.id.durationtime_sEv);
	TextView comment = (TextView) super.findViewById(R.id.comment_sEv);
	Button change = (Button) findViewById(R.id.sEv_change);

	location.setText(sEventLocal.getLocation());
	date.setText(new SimpleDateFormat("dd.MM.yyyy").format(sEventLocal
		.getDate()));
	time.setText(new SimpleDateFormat("HH:mm").format(sEventLocal.getDate()));
	supervisor.setText(sEventLocal.getSupervisor());
	durationtime.setText(String.valueOf(sEventLocal.getDurationMinutes()));
	comment.setText(sEventLocal.getComment());

	int locationUpdateCounter = sEventLocal.getLoactionUpdateCounter();
	int dateUpdateCounter = sEventLocal.getDateUpdateCounter();
	int supervisorUpdateCounter = sEventLocal.getSupervisorUpdateCounter();
	int durationMinutesUpdateCounter = sEventLocal
		.getDurationMinutesUpdateCounter();
	if (locationUpdateCounter > 0) {
	    location.setTextColor(SingleEventActivity.this.getResources()
		    .getColor(R.color.changed));
	}
	if (dateUpdateCounter > 0) {
	    date.setTextColor(SingleEventActivity.this.getResources().getColor(
		    R.color.changed));
	    time.setTextColor(SingleEventActivity.this.getResources().getColor(
		    R.color.changed));
	}
	if (supervisorUpdateCounter > 0) {
	    supervisor.setTextColor(SingleEventActivity.this.getResources()
		    .getColor(R.color.changed));
	}

	if (sEventLocal.getDurationMinutes() == 0) {
	    durationtime.setText(R.string.cancellation);
	}

	if (durationMinutesUpdateCounter > 0) {
	    durationtime.setTextColor(SingleEventActivity.this.getResources()
		    .getColor(R.color.changed));
	}
	change.setVisibility(View.GONE);

	if (sEventLocal.getPermission() > 0) {
	    change.setVisibility(View.VISIBLE);
	}

    }

    /**
     * Shows the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.all_items_menu, menu);
	return true;
    }

    /**
     * Handles clicks on a menu-item and switches to other activity, depending
     * on which item was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(SingleEventActivity.this,
		    LoginActivity.class);
	    SingleEventActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SingleEventActivity.this,
		    MainActivity.class);
	    SingleEventActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(SingleEventActivity.this,
		    SearchActivity.class);
	    SingleEventActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SingleEventActivity.this,
		    PrivilegesActivity.class);
	    SingleEventActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SingleEventActivity.this,
		    SettingsActivity.class);
	    SingleEventActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(SingleEventActivity.this,
		    SubscriptionActivity.class);
	    SingleEventActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Is used if ChangeDateButton is clicked. Switches to
     * ChangeSingleEventActivity.
     * 
     * @param view
     */
    public void onChangeDateButtonClick(View view) {
	Intent intentChangeSingleEvent = new Intent(SingleEventActivity.this,
		ChangeSingleEventActivity.class);
	intentChangeSingleEvent.putExtra("singleID",
		sEventLocal.getSingleEventID());
	SingleEventActivity.this.startActivity(intentChangeSingleEvent);
    }

}
