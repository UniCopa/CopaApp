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
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.Database;
import unicopa.copa.app.Helper;
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
 * In this activity a user can manage his privileges.
 * 
 * @author Christiane Kuhn
 */
public class PrivilegesActivity extends Activity {

    /**
     * Creates PrivilegesActivity with a list of the Events to which the user
     * has special rights. If there is no such Event an infotext appears.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.priv);

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	// check if logged in if not redirect to LoginActivity
	if (!scon.getConnected()) {
	    PopUp.loginFail(this);
	} else {
	    // update list of privileges in database
	    try {
		Helper.getRights();
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

	    TextView text = (TextView) findViewById(R.id.priv_nothing);
	    final ListView eventListView = (ListView) PrivilegesActivity.this
		    .findViewById(R.id.privListView);

	    eventListView.setAdapter(null);

	    Database db = Database.getInstance(PrivilegesActivity.this);

	    ArrayList<Event> events = new ArrayList<Event>();

	    List<Event> list = db.getEventsWithPermission();

	    if (list == null) {
		text.setText(getString(R.string.nothing));
	    } else {

		for (Event item : list) {
		    events.add(item);
		}

		PrivAdapter privAdapter = new PrivAdapter(this, events);
		eventListView.setAdapter((ListAdapter) privAdapter);
	    }
	}
    }

    /**
     * Shows the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.priv_menu, menu);
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
	case R.id.action_help:
	    Intent intentHelp = new Intent(PrivilegesActivity.this,
		    HelpActivity.class);
	    PrivilegesActivity.this.startActivity(intentHelp);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
