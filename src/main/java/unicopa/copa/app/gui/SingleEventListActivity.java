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
import java.util.List;

import unicopa.copa.app.Database;
import unicopa.copa.app.R;
import unicopa.copa.app.SingleEventLocal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * In this activity a user sees all SingleEvents to an Event.
 * 
 * @author Christiane Kuhn
 */
public class SingleEventListActivity extends Activity {

    /**
     * Creates SingleEventListActivity with a list of all SingleEvents that
     * belong to an Event which has been chosen before.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.singleeventlist);
	Intent intent = getIntent();
	int event = intent.getIntExtra("selected", 0);
	TextView text = (TextView) findViewById(R.id.singlelist_nothing);

	final ListView singleEventListView = (ListView) SingleEventListActivity.this
		.findViewById(R.id.singleEventListView);

	singleEventListView.setAdapter(null);
	ArrayList<SingleEventLocal> sEvents = new ArrayList<SingleEventLocal>();
	Database db = Database.getInstance(SingleEventListActivity.this);

	List<SingleEventLocal> sEventsloc = db.getSingleEventsByEventID(event);
	db.close();
	if (sEventsloc == null) {
	    text.setText("no dates");
	} else {

	    for (SingleEventLocal item : sEventsloc) {
		sEvents.add(item);
	    }
	    SingleEventListAdapter sEventAdapter = new SingleEventListAdapter(
		    this, sEvents);
	    singleEventListView.setAdapter((ListAdapter) sEventAdapter);
	}

    }

    /**
     * Shows the menu.
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
	    Intent intentLog = new Intent(SingleEventListActivity.this,
		    LoginActivity.class);
	    SingleEventListActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SingleEventListActivity.this,
		    MainActivity.class);
	    SingleEventListActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(SingleEventListActivity.this,
		    SearchActivity.class);
	    SingleEventListActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SingleEventListActivity.this,
		    PrivilegesActivity.class);
	    SingleEventListActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SingleEventListActivity.this,
		    SettingsActivity.class);
	    SingleEventListActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    SingleEventListActivity.this, SubscriptionActivity.class);
	    SingleEventListActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
