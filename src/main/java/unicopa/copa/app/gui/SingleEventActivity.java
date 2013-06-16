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

	TextView sEventID = (TextView) super
		.findViewById(R.id.singleEventID_sEV);
	TextView eventID = (TextView) super.findViewById(R.id.eventID_sEv);
	TextView location = (TextView) super.findViewById(R.id.location_sEv);
	TextView date = (TextView) super.findViewById(R.id.date_sEv);
	TextView time = (TextView) super.findViewById(R.id.time_sEv);
	TextView supervisor = (TextView) super
		.findViewById(R.id.supervisor_sEv);
	TextView durationtime = (TextView) super
		.findViewById(R.id.durationtime_sEv);
	Button change = (Button) findViewById(R.id.sEv_change);

	sEventID.setText(String.valueOf(singleEventID));
	eventID.setText(String.valueOf(sEventLocal.getEventID()));
	location.setText(sEventLocal.getLocation());
	date.setText(new SimpleDateFormat("dd.MM").format(sEventLocal.getDate()));
	time.setText(new SimpleDateFormat("HH:mm").format(sEventLocal.getDate()));
	supervisor.setText(sEventLocal.getSupervisor());
	durationtime.setText(String.valueOf(sEventLocal.getDurationMinutes()));
	change.setVisibility(View.GONE);

	// TODO check if role-number is chosen correct
	if (sEventLocal.getPermission() > 0) {
	    change.setVisibility(View.VISIBLE);
	}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.all_items_menu, menu);
	return true;
    }

    /**
     * Handles clicks on a menu-item
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
