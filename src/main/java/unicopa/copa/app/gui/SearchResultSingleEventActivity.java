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
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.SingleEvent;

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
 * @author Christiane Kuhn, Martin Rabe
 */
public class SearchResultSingleEventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.singleeventlist);
	Intent intent = getIntent();
	int eventID = intent.getIntExtra("selected", 0);
	String groupname = intent.getStringExtra("groupname");
	String evname = intent.getStringExtra("eventname");
	String name = groupname + " " + evname;

	TextView title = (TextView) findViewById(R.id.singlelist_text);
	title.setText(name);

	final ListView singleEventListView = (ListView) SearchResultSingleEventActivity.this
		.findViewById(R.id.singleEventListView);

	ArrayList<SingleEvent> sEvents = new ArrayList<SingleEvent>();

	singleEventListView.setAdapter(null);

	// begin getCurrentSingleEvents

	ServerConnection scon = ServerConnection.getInstance();

	Date date = null;
	date = Calendar.getInstance().getTime();

	try {
	    sEvents = (ArrayList<SingleEvent>) scon.getCurrentSingleEvents(
		    eventID, date);
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

	// end getCurrentSingleEvents

	SearchResultSingleEventAdapter sEventAdapter = new SearchResultSingleEventAdapter(
		this, sEvents);
	singleEventListView.setAdapter((ListAdapter) sEventAdapter);
	singleEventListView.setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.all_items_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(SearchResultSingleEventActivity.this,
		    LoginActivity.class);
	    SearchResultSingleEventActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(
		    SearchResultSingleEventActivity.this, MainActivity.class);
	    SearchResultSingleEventActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(
		    SearchResultSingleEventActivity.this, SearchActivity.class);
	    SearchResultSingleEventActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(
		    SearchResultSingleEventActivity.this,
		    PrivilegesActivity.class);
	    SearchResultSingleEventActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(
		    SearchResultSingleEventActivity.this,
		    SettingsActivity.class);
	    SearchResultSingleEventActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    SearchResultSingleEventActivity.this,
		    SubscriptionActivity.class);
	    SearchResultSingleEventActivity.this
		    .startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
