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
import unicopa.copa.base.event.Event;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * In this activity a user can manage his privileges.
 * 
 * @author Christiane Kuhn
 */
public class PrivilegesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.priv);

	final ListView eventListView = (ListView) PrivilegesActivity.this
		.findViewById(R.id.privListView);

	eventListView.setAdapter(null);

	Database db = Database.getInstance(PrivilegesActivity.this);

	ArrayList<Event> events = new ArrayList<Event>();

	List<Event> list = db.getEventsWithPermission();

	for (Event item : list) {
	    events.add(item);
	}

	PrivAdapter privAdapter = new PrivAdapter(this, events);
	eventListView.setAdapter((ListAdapter) privAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.priv_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(PrivilegesActivity.this,
		    LoginActivity.class);
	    PrivilegesActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(PrivilegesActivity.this,
		    MainActivity.class);
	    PrivilegesActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(PrivilegesActivity.this,
		    SearchActivity.class);
	    PrivilegesActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(PrivilegesActivity.this,
		    SettingsActivity.class);
	    PrivilegesActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(PrivilegesActivity.this,
		    SubscriptionActivity.class);
	    PrivilegesActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
