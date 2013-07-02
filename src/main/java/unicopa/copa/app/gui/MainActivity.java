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

import static unicopa.copa.app.GCMCommonUtilities.EXTRA_MESSAGE;
import static unicopa.copa.app.GCMCommonUtilities.DISPLAY_MESSAGE_ACTION;
import static unicopa.copa.app.GCMCommonUtilities.SENDER_ID;
import static unicopa.copa.app.GCMCommonUtilities.SERVER_URL;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.gcm.GCMRegistrar;
import unicopa.copa.app.GCMServerUtilities;

import unicopa.copa.app.Database;
import unicopa.copa.app.Helper;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.SingleEventLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.app.exceptions.NoStorageException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.SingleEvent;

/**
 * In this activity the user sees a list of his next SingleEvents.
 * 
 * @author Christiane Kuhn, Martin Rabe, Robin Muench
 */
public class MainActivity extends Activity {

    ArrayList<SingleEventLocal> sEvents = new ArrayList<SingleEventLocal>();
    MainAdapter sEventAdapter;
    TextView text;
    ListView singleEventListView;
    int num = 5;

    // begin GCM
    TextView mDisplay;
    AsyncTask<Void, Void, Void> mRegisterTask;

    // end GCM

    /**
     * Creates MainActivity with a list of the next SingleEvents. By clicking on
     * a SingleEvent it switches to SingleEventActivity and shows details about
     * the SingleEvent.If no SingleEvent exists that could be shown an infotext
     * appears. If the "refresh"-button is used it loads all new updates if the
     * user is logged in. If not a dialog reminds the user to log in.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	text = (TextView) findViewById(R.id.main_nothing);

	// begin GCM
	checkNotNull(SERVER_URL, "SERVER_URL");
	checkNotNull(SENDER_ID, "SENDER_ID");
	// Make sure the device has the proper dependencies.
	GCMRegistrar.checkDevice(this);
	// Make sure the manifest was properly set - comment out this line
	// while developing the app, then uncomment it when it's ready.
	GCMRegistrar.checkManifest(this);
	setContentView(R.layout.main);
	registerReceiver(mHandleMessageReceiver, new IntentFilter(
		DISPLAY_MESSAGE_ACTION));
	final String regId = GCMRegistrar.getRegistrationId(this
		.getApplicationContext());
	if (regId.equals("")) {
	    // Automatically registers application on startup.
	    GCMRegistrar.register(this.getApplicationContext(), SENDER_ID);
	} else {
	    // Device is already registered on GCM, check server.
	    if (GCMRegistrar.isRegisteredOnServer(this.getApplicationContext())) {
		// Skips registration.
		mDisplay.append(getString(R.string.already_registered) + "\n");
	    } else {
		// Try to register again, but not in the UI thread.
		// It's also necessary to cancel the thread onDestroy(),
		// hence the use of AsyncTask instead of a raw thread.
		final Context context = this.getApplicationContext();
		mRegisterTask = new AsyncTask<Void, Void, Void>() {

		    @Override
		    protected Void doInBackground(Void... params) {
			boolean registered;
			try {
			    registered = GCMServerUtilities.register(context,
				    regId);
			    // At this point all attempts to register with the
			    // app
			    // server failed, so we need to unregister the
			    // device
			    // from GCM - the app will try to register again
			    // when
			    // it is restarted. Note that GCM will send an
			    // unregistered callback upon completion, but
			    // GCMIntentService.onUnregistered() will ignore it.
			    if (!registered) {
				GCMRegistrar.unregister(context);
			    }

			} catch (NoStorageException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

			return null;
		    }

		    @Override
		    protected void onPostExecute(Void result) {
			mRegisterTask = null;
		    }

		};
		mRegisterTask.execute(null, null, null);
	    }
	}
	// end GCM

	singleEventListView = (ListView) MainActivity.this
		.findViewById(R.id.singleEventView);

	singleEventListView.setAdapter(null);

	Storage storage = null;
	storage = Storage.getInstance(this.getApplicationContext());

	SettingsLocal settingsLocal = null;

	/**
	 * check if settings exists if not create it with default values
	 */
	try {
	    settingsLocal = storage.load();
	} catch (NoStorageException e) {
	    PopUp.firstAlert(this);

	    Set<String> gcmKeys = new HashSet<String>();
	    boolean emailNotification = true;
	    String language = "german";
	    Map<Integer, UserEventSettings> map = null;
	    int notificationKind = 1; // set notification to gcm-auto
	    Date lastUpdate = new Date(0);

	    settingsLocal = new SettingsLocal(gcmKeys, emailNotification,
		    language, map, notificationKind, lastUpdate, regId);

	    storage.store(settingsLocal);
	    // e.printStackTrace();
	}

	// begin Just for testing

	// Settings
	// Set<String> gcmKeys = new HashSet<String>();
	// gcmKeys.add("ololo");
	// boolean emailNotification = true;
	// String language = "german";
	// Map<Integer, UserEventSettings> map = new HashMap<Integer,
	// UserEventSettings>();
	// UserEventSettings farb1 = new UserEventSettings();
	// UserEventSettings farb2 = new UserEventSettings();
	// farb1.setColorCode("999999");
	// farb2.setColorCode("444444");
	//
	// map.put(1, farb1);
	// map.put(2, farb2);
	//
	// int notificationKind = 2;
	// Date lastUpdate = new Date(4000);
	//
	// SettingsLocal setLoc = new SettingsLocal(gcmKeys, emailNotification,
	// language, map, notificationKind, lastUpdate);
	//
	// Storage S = Storage.getInstance(this.getApplicationContext());
	// S.store(setLoc);

	// Database
	// Database db = Database.getInstance(MainActivity.this);
	// db.Table_delete("SingleEventLocal");
	// db.Table_delete("Event");
	// db.Table_delete("EventGroup");
	// db.Table_init();

	// EventGroup g1 = new EventGroup(3, "Telematik", "info", null);
	// EventGroup g2 = new EventGroup(2, "Mathe", "blabla", null);
	// EventGroup g3 = new EventGroup(4, "Linux", "info2", null);
	//
	// Event ev1 = new Event(1, 3, "Übung1", new ArrayList<Integer>());
	// Event ev2 = new Event(2, 2, "Vorlesung", new ArrayList<Integer>());
	// Event ev3 = new Event(3, 4, "Übung2", new ArrayList<Integer>());
	//
	// SingleEventLocal test = new SingleEventLocal(1, 3, "HU 102", Calendar
	// .getInstance().getTime(), "Martin", 4, "#77DD22",
	// "Linux Übung2", 2, 2, 2, 2, 2);
	// SingleEventLocal test2 = new SingleEventLocal(5, 2, "HU 104",
	// Calendar
	// .getInstance().getTime(), "Robin", 5, "#770000",
	// "Mathe Vorlesung", 0, 0, 0, 0, 0);
	// SingleEventLocal test3 = new SingleEventLocal(6, 1, "HU 103",
	// Calendar
	// .getInstance().getTime(), "Philip", 90, "#005577",
	// "Telematik Übung1", 0, 0, 0, 0, 0);
	//
	// db.insert(test, -1);
	// db.insert(test2, -1);
	// db.insert(test3, -1);
	// db.insert(ev1, -1);
	// db.insert(ev2, -1);
	// db.insert(ev3, -1);
	// db.insert(g1, -1);
	// db.insert(g2, -1);
	// db.insert(g3, -1);
	// end Just for testing

	Database db = null;
	db = Database.getInstance(MainActivity.this);
	db.Table_init();

	List<SingleEventLocal> sEventsLocal = null;
	sEventsLocal = db.getNearestSingleEvents(num);

	for (SingleEventLocal item : sEventsLocal) {
	    sEvents.add(item);
	}

	if (sEvents.equals(new ArrayList<SingleEventLocal>())) {
	    text.setText(getString(R.string.nothing));
	}

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

    // for GCM
    private void checkNotNull(Object reference, String name) {
	if (reference == null) {
	    throw new NullPointerException(getString(R.string.error_config,
		    name));
	}
    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
	    String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	    Log.e("new message", newMessage);
	    mDisplay.append(newMessage + "\n");
	}
    };

    @Override
    protected void onDestroy() {
	if (mRegisterTask != null) {
	    mRegisterTask.cancel(true);
	}
	unregisterReceiver(mHandleMessageReceiver);
	GCMRegistrar.onDestroy(this.getApplicationContext());
	super.onDestroy();
    }

    // end GCM

    /**
     * Loads all updates if the user is logged in. If not a dialog reminds the
     * user to log in.
     * 
     * @param view
     */

    public void onRefreshButtonClick(View view) {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	if (scon.getConnected()) {
	    SettingsLocal settingsLocal = null;

	    try {
		settingsLocal = scon.getSettings();
	    } catch (ClientProtocolException e) {
		PopUp.alert(this, getString(R.string.cp_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (APIException e) {
		PopUp.alert(this, getString(R.string.api_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (PermissionException e) {
		PopUp.alert(this, getString(R.string.per_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (RequestNotPracticableException e) {
		PopUp.alert(this, getString(R.string.rnp_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (InternalErrorException e) {
		PopUp.alert(this, getString(R.string.ie_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (IOException e) {
		PopUp.alert(this, getString(R.string.io_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (NoStorageException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    if (settingsLocal != null) {
		Date date = null;
		date = settingsLocal.getLastUpdate();

		boolean success = false;

		try {
		    success = Helper.checkSubscriptions();
		} catch (ClientProtocolException e) {
		    PopUp.alert(this, getString(R.string.cp_ex), e.getMessage());
		    // e.printStackTrace();
		} catch (APIException e) {
		    PopUp.alert(this, getString(R.string.api_ex),
			    e.getMessage());
		    // e.printStackTrace();
		} catch (PermissionException e) {
		    PopUp.alert(this, getString(R.string.per_ex),
			    e.getMessage());
		    // e.printStackTrace();
		} catch (RequestNotPracticableException e) {
		    PopUp.alert(this, getString(R.string.rnp_ex),
			    e.getMessage());
		    // e.printStackTrace();
		} catch (InternalErrorException e) {
		    PopUp.alert(this, getString(R.string.ie_ex), e.getMessage());
		    // e.printStackTrace();
		} catch (IOException e) {
		    PopUp.alert(this, getString(R.string.io_ex), e.getMessage());
		    // e.printStackTrace();
		} catch (NoStorageException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		if (success) {

		    try {
			success = Helper.getUpdate(date, MainActivity.this);
		    } catch (ClientProtocolException e) {
			PopUp.alert(this, getString(R.string.cp_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (APIException e) {
			PopUp.alert(this, getString(R.string.api_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (PermissionException e) {
			PopUp.alert(this, getString(R.string.per_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (RequestNotPracticableException e) {
			PopUp.alert(this, getString(R.string.rnp_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (InternalErrorException e) {
			PopUp.alert(this, getString(R.string.ie_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (IOException e) {
			PopUp.alert(this, getString(R.string.io_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (NoStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    if (success) {
			Database db = null;
			db = Database.getInstance(MainActivity.this);

			List<SingleEventLocal> sEventsLocal = null;
			sEventsLocal = db.getNearestSingleEvents(num);

			sEvents.clear();
			text.setText("");

			for (SingleEventLocal item : sEventsLocal) {
			    sEvents.add(item);
			}

			if (sEvents.equals(new ArrayList<SingleEventLocal>())) {
			    text.setText(getString(R.string.nothing));
			}

			sEventAdapter = new MainAdapter(this, sEvents);

			singleEventListView
				.setAdapter((ListAdapter) sEventAdapter);
		    }
		}
	    }
	} else {
	    PopUp.loginFail(this);
	}
    }

    /**
     * Handles clicks on a menu-item and switches to other activity, depending
     * on which item was clicked.
     */
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
	case R.id.action_help:
	    Intent intentHelp = new Intent(MainActivity.this,
		    HelpActivity.class);
	    MainActivity.this.startActivity(intentHelp);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Shows the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_main_menu, menu);
	return true;
    }

    /**
     * Shows only the changes SingleEvents if "Only Updates" is clicked.
     */
    public void onOnlyUpdatesButtonClick(View view) {
	Database db = null;
	db = Database.getInstance(MainActivity.this);
	Button updates = (Button) findViewById(R.id.main_only_updates);
	String label = (String) updates.getText();
	List<SingleEventLocal> sEventsLocal = null;

	if (label.equals(getString(R.string.only_updates))) {
	    sEventsLocal = db.getUpdatedSingleEvents(num);
	    updates.setText(getString(R.string.all_dates));
	} else {
	    sEventsLocal = db.getNearestSingleEvents(num);
	    updates.setText(getString(R.string.only_updates));
	}

	sEvents.clear();
	text.setText("");

	for (SingleEventLocal item : sEventsLocal) {
	    sEvents.add(item);
	}

	if (sEvents.equals(new ArrayList<SingleEventLocal>())) {
	    text.setText(getString(R.string.nothing));
	}

	sEventAdapter = new MainAdapter(this, sEvents);

	singleEventListView.setAdapter((ListAdapter) sEventAdapter);

    }

    /**
     * Shows more SingleEvents if "Only Updates" is clicked.
     */
    public void onShowMoreButtonClick(View view) {
	num = num + 10;
	Button updates = (Button) findViewById(R.id.main_only_updates);
	String label = (String) updates.getText();

	if (label.equals(getString(R.string.only_updates))) {
	    updates.setText(getString(R.string.all_dates));
	} else {
	    updates.setText(getString(R.string.only_updates));
	}
	onOnlyUpdatesButtonClick(view);

    }
}
