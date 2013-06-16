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
import android.widget.TextView;

/**
 * In this activity a user can update a event, if he has the rights to do it.
 * 
 * @author Christiane Kuhn
 */
public class ChangeSingleEventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.changesingleevent);
	Intent intent = getIntent();
	int sEventID = intent.getIntExtra("singleID", 0);

	Database db = Database.getInstance(ChangeSingleEventActivity.this);

	SingleEventLocal sEventLocal = db
		.getSingleEventBySingleEventID(sEventID);
	db.close();

	TextView name = (TextView) findViewById(R.id.change_eventGroupname);
	TextView location = (TextView) findViewById(R.id.change_location);
	TextView date = (TextView) findViewById(R.id.change_date);
	TextView time = (TextView) findViewById(R.id.change_time);
	TextView supervisor = (TextView) findViewById(R.id.change_supervisor);
	TextView durationtime = (TextView) findViewById(R.id.change_dura);

	name.setText(sEventLocal.getName());
	location.setText(sEventLocal.getLocation());
	date.setText(new SimpleDateFormat("dd.MM").format(sEventLocal.getDate()));
	time.setText(new SimpleDateFormat("HH:mm").format(sEventLocal.getDate()));
	supervisor.setText(sEventLocal.getSupervisor());
	durationtime.setText(String.valueOf(sEventLocal.getDurationMinutes()));

    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.all_items_menu, menu);
	return true;
    }

    /**
     * Switch to other activity, depending on which item was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(ChangeSingleEventActivity.this,
		    LoginActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(ChangeSingleEventActivity.this,
		    MainActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(ChangeSingleEventActivity.this,
		    SearchActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(ChangeSingleEventActivity.this,
		    PrivilegesActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(ChangeSingleEventActivity.this,
		    SettingsActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    ChangeSingleEventActivity.this, SubscriptionActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }

}
