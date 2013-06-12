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

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
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
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.SingleEvent;

/**
 * In this activity the user sees a list of SingleEvents.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class MainActivity extends Activity {

    ArrayList<SingleEvent> sEvents = new ArrayList<SingleEvent>();
    MainAdapter sEventAdapter;

    // JUST FOR DEMO
    int i = 0;

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

	sEvents.add(new SingleEvent(1, 3, "HU 102", Calendar.getInstance()
		.getTime(), "David", 4));
	sEvents.add(new SingleEvent(3, 2, "HU 103", Calendar.getInstance()
		.getTime(), "Robin", 00));

	sEventAdapter = new MainAdapter(this, sEvents);
	singleEventListView.setAdapter((ListAdapter) sEventAdapter);

	singleEventListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1,
		    int position, long arg3) {

		Intent intent = new Intent(MainActivity.this,
			SingleEventActivity.class);
		SingleEvent clicked = (SingleEvent) singleEventListView
			.getAdapter().getItem(position);
		intent.putExtra("selectedID", clicked.getSingleEventID());

		startActivity(intent);
	    }
	});

    }

    public void onRefreshButtonClick(View view) {
	SingleEvent sEventNew = null;

	ServerConnection scon = ServerConnection.getInstance();

	if (scon.getConnected()) {

	    scon.setUrl("https://copa.prakinf.tu-ilmenau.de:443/service");

	    // JUST FOR DEMO
	    // DONE 13 works
	    // DONE ID 0 InternalErrorException
	    // DONE ID 42 RequestNotPracticableException
	    // DONE ID -1 PermissionException
	    int singleEventID = i;

	    // JUST FOR DEMO
	    if (i == 0) {
		i = 42;
	    } else {
		if (i == 42) {
		    i = -1;
		} else {
		    if (i == -1) {
			i = 13;
		    } else {
			if (i == 13) {
			    i = 0;
			}
		    }
		}
	    }

	    try {
		sEventNew = scon.getSingleEvent(singleEventID);
	    } catch (ClientProtocolException e) {
		PopUp.exceptionAlert(this, "ClientProtocolException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (APIException e) {
		PopUp.exceptionAlert(this, "APIException!", e.getMessage());
		// e.printStackTrace();
	    } catch (PermissionException e) {
		PopUp.exceptionAlert(this, "PermissionException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (RequestNotPracticableException e) {
		PopUp.exceptionAlert(this, "RequestNotPracticableException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (InternalErrorException e) {
		PopUp.exceptionAlert(this, "InternalErrorException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (IOException e) {
		PopUp.exceptionAlert(this, "IOException!", e.getMessage());
		// e.printStackTrace();
	    }

	    if (sEventNew != null) {
		sEvents.add(sEventNew);
		sEventAdapter.notifyDataSetChanged();
	    }
	} else {
	    // TODO l18n
	    PopUp.alert(this, "Login!", "You are not logged in.");
	}
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
