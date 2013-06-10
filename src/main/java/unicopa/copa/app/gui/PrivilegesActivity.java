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
import java.util.Calendar;

import unicopa.copa.app.R;
import unicopa.copa.base.event.Event;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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

	ArrayList<Event> sEvents = new ArrayList<Event>();
	sEvents.add(new Event(1, 3, "Event1", new ArrayList<Integer>()));
	sEvents.add(new Event(2, 2, "Event2", new ArrayList<Integer>()));
	sEvents.add(new Event(3, 4, "Event3", new ArrayList<Integer>()));

	PrivAdapter privAdapter = new PrivAdapter(this, sEvents);
	eventListView.setAdapter((ListAdapter) privAdapter);

	eventListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		Intent intent = new Intent(PrivilegesActivity.this,
			SingleEventActivity.class);
		intent.putExtra("selected",
			eventListView.getAdapter().getItem(arg2).toString());
		startActivity(intent);
	    }
	});
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
