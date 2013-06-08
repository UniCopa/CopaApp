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
package unicopa.copa.app;

import java.util.ArrayList;
import java.util.Calendar;

import unicopa.copa.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import unicopa.copa.base.event.SingleEvent;

/**
 * In this activity the user sees a list of SingleEvents.
 * 
 * @author Christiane Kuhn
 */
public class MainActivity extends Activity {

    /**
     * creates Activity with a list of SingleEvents. By clicking on a
     * SingleEvent it switches to SingleEventActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	final ListView singleEventListView = (ListView) MainActivity.this
		.findViewById(R.id.singleEventView);

	singleEventListView.setAdapter(null);

	ArrayList<SingleEvent> sEvents = new ArrayList<SingleEvent>();
	sEvents.add(new SingleEvent(1, 3, "HU 102", Calendar.getInstance()
		.getTime(), "David", 4));
	sEvents.add(new SingleEvent(2, 2, "HU 103", Calendar.getInstance()
		.getTime(), "Robin", 00));
	sEvents.add(new SingleEvent(3, 4, "HU 104", Calendar.getInstance()
		.getTime(), "Philip", 0));
	sEvents.add(new SingleEvent(4, 3, "HU 105", Calendar.getInstance()
		.getTime(), "Felix", 7));
	sEvents.add(new SingleEvent(5, 4, "HU 106", Calendar.getInstance()
		.getTime(), "Philipp", 66));
	sEvents.add(new SingleEvent(6, 5, "HU 102", Calendar.getInstance()
		.getTime(), "David", 80));
	sEvents.add(new SingleEvent(7, 6, "HU 103", Calendar.getInstance()
		.getTime(), "Tuki", 55));
	sEvents.add(new SingleEvent(8, 7, "HU 104", Calendar.getInstance()
		.getTime(), "Christiane", 45));
	sEvents.add(new SingleEvent(9, 8, "HU 105", Calendar.getInstance()
		.getTime(), "Jule", 4));
	sEvents.add(new SingleEvent(10, 9, "HU 106", Calendar.getInstance()
		.getTime(), "Max", 45));
	sEvents.add(new SingleEvent(11, 1, "HU 102", Calendar.getInstance()
		.getTime(), "Moritz", 33));
	sEvents.add(new SingleEvent(12, 2, "HU 103", Calendar.getInstance()
		.getTime(), "Anne", 123));
	sEvents.add(new SingleEvent(13, 3, "HU 104", Calendar.getInstance()
		.getTime(), "Marie", 34));
	sEvents.add(new SingleEvent(14, 4, "HU 105", Calendar.getInstance()
		.getTime(), "Silke", 76));
	sEvents.add(new SingleEvent(15, 5, "HU 106", Calendar.getInstance()
		.getTime(), "Jan", 90));
	SingleEventAdapter sEventAdapter = new SingleEventAdapter(this, sEvents);
	singleEventListView.setAdapter((ListAdapter) sEventAdapter);

	singleEventListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		Intent intent = new Intent(MainActivity.this,
			SingleEventActivity.class);
		intent.putExtra("selected", singleEventListView.getAdapter()
			.getItem(arg2).toString());
		startActivity(intent);
	    }
	});

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(MainActivity.this,
		    LoginActivity.class);
	    MainActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(MainActivity.this,
		    SearchActivity.class);
	    MainActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(MainActivity.this,
		    PrivilegesActivity.class);
	    MainActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(MainActivity.this,
		    SettingsActivity.class);
	    MainActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(MainActivity.this,
		    SubscriptionActivity.class);
	    MainActivity.this.startActivity(intentSubscription);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_main_menu, menu);
	return true;
    }

}
