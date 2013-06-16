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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.Database;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.SingleEventLocal;
import unicopa.copa.app.Storage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.Event;
import unicopa.copa.base.event.EventGroup;
import unicopa.copa.base.event.SingleEvent;

/**
 * In this activity the user sees a list of his next SingleEvents.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class MainActivity extends Activity {

    ArrayList<SingleEventLocal> sEvents = new ArrayList<SingleEventLocal>();
    MainAdapter sEventAdapter;

    // begin GCM

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";

    /**
     * Default lifespan (7 days) of a reservation until it is considered
     * expired.
     */
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

    /**
     * Substitute you own sender ID here. TODO
     */
    String SENDER_ID = "Your-Sender-ID";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    // GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;

    // end GCM

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

	// begin GCM

	context = getApplicationContext();
	regid = getRegistrationId(context);

	if (regid.length() == 0) {
	    registerBackground();
	}
	// gcm = GoogleCloudMessaging.getInstance(this);

	// end GCM

	final ListView singleEventListView = (ListView) MainActivity.this
		.findViewById(R.id.singleEventView);

	singleEventListView.setAdapter(null);

	// begin Just for testing

	// Settings
	Set<String> gcmKeys = new HashSet<String>();
	gcmKeys.add("ololo");
	boolean emailNotification = true;
	String language = "German";
	Map<Integer, UserEventSettings> map = new HashMap<Integer, UserEventSettings>();
	UserEventSettings farb1 = new UserEventSettings();
	UserEventSettings farb2 = new UserEventSettings();
	farb1.setColorCode("#999999");
	farb2.setColorCode("#444444");

	map.put(1, farb1);
	map.put(2, farb2);

	int notificationKind = 2;
	Date lastUpdate = new Date(4000);

	SettingsLocal setLoc = new SettingsLocal(gcmKeys, emailNotification,
		language, map, notificationKind, lastUpdate);

	Storage S = Storage.getInstance(this.getApplicationContext());
	S.store(setLoc);

	// Database
	Database db = Database.getInstance(MainActivity.this);
	db.Table_delete("SingleEventLocal");
	db.Table_delete("Event");
	db.Table_delete("EventGroup");
	db.Table_init();

//	EventGroup g1 = new EventGroup(3, "Telematik", "info", null);
//	EventGroup g2 = new EventGroup(2, "Mathe", "blabla", null);
//	EventGroup g3 = new EventGroup(4, "Linux", "info2", null);
//
//	Event ev1 = new Event(1, 3, "Übung1", new ArrayList<Integer>());
//	Event ev2 = new Event(2, 2, "Vorlesung", new ArrayList<Integer>());
//	Event ev3 = new Event(3, 4, "Übung2", new ArrayList<Integer>());
//
//	SingleEventLocal test = new SingleEventLocal(1, 3, "HU 102", Calendar
//		.getInstance().getTime(), "Martin", 4, "#77DD22",
//		"Linux Übung2", 2, 2, 2, 2, 2);
//	SingleEventLocal test2 = new SingleEventLocal(5, 2, "HU 104", Calendar
//		.getInstance().getTime(), "Robin", 5, "#770000",
//		"Mathe Vorlesung", 0, 0, 0, 0, 0);
//	SingleEventLocal test3 = new SingleEventLocal(6, 1, "HU 103", Calendar
//		.getInstance().getTime(), "Philip", 90, "#005577",
//		"Telematik Übung1", 0, 0, 0, 0, 0);
//
//	db.insert(test, -1);
//	db.insert(test2, -1);
//	db.insert(test3, -1);
//	db.insert(ev1, -1);
//	db.insert(ev2, -1);
//	db.insert(ev3, -1);
//	db.insert(g1, -1);
//	db.insert(g2, -1);
//	db.insert(g3, -1);
	// end Just for testing

	List<SingleEventLocal> sEventsloc = db.getNearestSingleEvents(3);
	for (SingleEventLocal item : sEventsloc) {
	    sEvents.add(item);
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

    public void onRefreshButtonClick(View view) {
	SingleEvent sEventNew = null;

	ServerConnection scon = ServerConnection.getInstance();

	if (scon.getConnected()) {

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

	    if (sEventNew != null) {
		// sEvents.add(sEventNew);
		// sEventAdapter.notifyDataSetChanged();
	    }
	} else {
	    // TODO l18n
	    PopUp.loginFail(this);
	}
    }

    /**
     * Handles clicks on a menu-item
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
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    // begin GCM

    /**
     * Gets the current registration id for application on GCM service.
     * <p>
     * If result is empty, the registration has failed.
     * 
     * @return registration id, or empty string if the registration is not
     *         complete.
     */
    private String getRegistrationId(Context context) {
	final SharedPreferences prefs = getGCMPreferences(context);
	String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	if (registrationId.length() == 0) {
	    Log.v(TAG, "Registration not found.");
	    return "";
	}
	// check if app was updated; if so, it must clear registration id to
	// avoid a race condition if GCM sends a message
	int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
		Integer.MIN_VALUE);
	int currentVersion = getAppVersion(context);
	if (registeredVersion != currentVersion || isRegistrationExpired()) {
	    Log.v(TAG, "App version changed or registration expired.");
	    return "";
	}
	return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
	return getSharedPreferences(MainActivity.class.getSimpleName(),
		Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
	try {
	    PackageInfo packageInfo = context.getPackageManager()
		    .getPackageInfo(context.getPackageName(), 0);
	    return packageInfo.versionCode;
	} catch (NameNotFoundException e) {
	    // should never happen
	    throw new RuntimeException("Could not get package name: " + e);
	}
    }

    /**
     * Checks if the registration has expired.
     * 
     * <p>
     * To avoid the scenario where the device sends the registration to the
     * server but the server loses it, the app developer may choose to
     * re-register after REGISTRATION_EXPIRY_TIME_MS.
     * 
     * @return true if the registration has expired.
     */
    private boolean isRegistrationExpired() {
	final SharedPreferences prefs = getGCMPreferences(context);
	// checks if the information is not stale
	long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME,
		-1);
	return System.currentTimeMillis() > expirationTime;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration id, app versionCode, and expiration time in the
     * application's shared preferences.
     */
    private void registerBackground() {
	// new AsyncTask() {
	// @Override
	// protected String doInBackground(Object... arg0) {
	// String msg = "";
	// try {
	// if (gcm == null) {
	// gcm = GoogleCloudMessaging.getInstance(context);
	// }
	// regid = gcm.register(SENDER_ID);
	// msg = "Device registered, registration id=" + regid;
	//
	// // You should send the registration ID to your server over HTTP,
	// // so it can use GCM/HTTP or CCS to send messages to your app.
	//
	// // For this demo: we don't need to send it because the device
	// // will send upstream messages to a server that echo back the message
	// // using the 'from' address in the message.
	//
	// // Save the regid - no need to register again.
	// setRegistrationId(context, regid);
	// } catch (IOException ex) {
	// msg = "Error :" + ex.getMessage();
	// }
	// return msg;
	// }
	//
	// protected void onPostExecute() {
	// }
	// }.execute(null, null, null);
    }

    /**
     * Stores the registration id, app versionCode, and expiration time in the
     * application's {@code SharedPreferences}.
     * 
     * @param context
     *            application's context.
     * @param regId
     *            registration id
     */
    private void setRegistrationId(Context context, String regId) {
	final SharedPreferences prefs = getGCMPreferences(context);
	int appVersion = getAppVersion(context);
	Log.v(TAG, "Saving regId on app version " + appVersion);
	SharedPreferences.Editor editor = prefs.edit();
	editor.putString(PROPERTY_REG_ID, regId);
	editor.putInt(PROPERTY_APP_VERSION, appVersion);
	long expirationTime = System.currentTimeMillis()
		+ REGISTRATION_EXPIRY_TIME_MS;

	Log.v(TAG, "Setting registration expiry time to "
		+ new Timestamp(expirationTime));
	editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
	editor.commit();
    }

    // end GCM

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_main_menu, menu);
	return true;
    }

}
