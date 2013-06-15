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

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.Event;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * On this activity the Events as results of a search are shown.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class SearchResultEventActivity extends Activity {

    SearchResultEventAdapter searchEventAdapter;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search_result_event);
	Intent intent = getIntent();
	name = intent.getStringExtra("groupname");
	int categoryId = intent.getIntExtra("categoryID", 0);
	int eventGroupID = intent.getIntExtra("eventGroupID", 0);

	TextView title = (TextView) findViewById(R.id.result_event_title);
	title.setText(name);

	final ListView eventListView = (ListView) SearchResultEventActivity.this
		.findViewById(R.id.result_event_list);

	ArrayList<Event> eventList = new ArrayList<Event>();

	eventListView.setAdapter(null);

	// begin getEvents

	ServerConnection scon = ServerConnection.getInstance();

	try {
	    eventList = (ArrayList<Event>) scon.getEvents(eventGroupID,
		    categoryId);
	} catch (ClientProtocolException e) {
	    PopUp.exceptionAlert(this, getString(R.string.cp_ex),
		    e.getMessage());
	    // e.printStackTrace();
	} catch (APIException e) {
	    PopUp.exceptionAlert(this, getString(R.string.api_ex),
		    e.getMessage());
	    // e.printStackTrace();
	} catch (PermissionException e) {
	    PopUp.exceptionAlert(this, getString(R.string.per_ex),
		    e.getMessage());
	    // e.printStackTrace();
	} catch (RequestNotPracticableException e) {
	    PopUp.exceptionAlert(this, getString(R.string.rnp_ex),
		    e.getMessage());
	    // e.printStackTrace();
	} catch (InternalErrorException e) {
	    PopUp.exceptionAlert(this, getString(R.string.ie_ex),
		    e.getMessage());
	    // e.printStackTrace();
	} catch (IOException e) {
	    PopUp.exceptionAlert(this, getString(R.string.io_ex),
		    e.getMessage());
	    // e.printStackTrace();
	}

	// end getEvents

	searchEventAdapter = new SearchResultEventAdapter(this, eventList);
	eventListView.setAdapter((ListAdapter) searchEventAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.search_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(SearchResultEventActivity.this,
		    LoginActivity.class);
	    SearchResultEventActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SearchResultEventActivity.this,
		    MainActivity.class);
	    SearchResultEventActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SearchResultEventActivity.this,
		    PrivilegesActivity.class);
	    SearchResultEventActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SearchResultEventActivity.this,
		    SettingsActivity.class);
	    SearchResultEventActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    SearchResultEventActivity.this, SubscriptionActivity.class);
	    SearchResultEventActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
