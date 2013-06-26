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
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;

/**
 * In this activity a user can see all rightholders, deputies and owners of an
 * event.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class EventPrivActivity extends Activity {

    /**
     * Creates the EventPrivActivity with a list of all rightholders, deputies
     * and owners that belong to an event, that the user picked before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.eventpriv);
	Intent intent = getIntent();
	int event = intent.getIntExtra("eventID", 0);

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	List<String> rightholders = null;
	List<String> deputies = null;
	List<String> owners = null;

	int eventID = 0;
	eventID = 2; // TODO get eventID from previous activity
	
	try {
	    rightholders = scon.getRightholders(eventID);
	    deputies = scon.getDeputies(eventID);
	    owners = scon.getOwners(eventID);
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

	if (rightholders != null) {
	    Log.v("RIGHTHOLDERS: ", rightholders.toString());
	    // TODO display rightholders
	}

	if (deputies != null) {
	    Log.v("DEPUTIES: ", deputies.toString());
	    // TODO display deputies
	}

	if (owners != null) {
	    Log.v("OWNERS: ", owners.toString());
	    // TODO display owners
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
	    Intent intentLog = new Intent(EventPrivActivity.this,
		    LoginActivity.class);
	    EventPrivActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(EventPrivActivity.this,
		    MainActivity.class);
	    EventPrivActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(EventPrivActivity.this,
		    SearchActivity.class);
	    EventPrivActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(EventPrivActivity.this,
		    PrivilegesActivity.class);
	    EventPrivActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(EventPrivActivity.this,
		    SettingsActivity.class);
	    EventPrivActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(EventPrivActivity.this,
		    SubscriptionActivity.class);
	    EventPrivActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }

}
