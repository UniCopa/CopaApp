/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package unicopa.copa.app;

import static unicopa.copa.app.GCMCommonUtilities.SENDER_ID;
import static unicopa.copa.app.GCMCommonUtilities.displayMessage;
import static unicopa.copa.app.gcm.GCMConstants.EXTRA_SPECIAL_MESSAGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListAdapter;

import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.app.gcm.GCMBaseIntentService;
import unicopa.copa.app.gcm.GCMRegistrar;
import unicopa.copa.app.gui.MainActivity;
import unicopa.copa.app.gui.MainAdapter;
import unicopa.copa.app.gui.PopUp;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
	super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId)
	    throws NoStorageException {
	Log.i(TAG, "Device registered: regId = " + registrationId);
	displayMessage(context, getString(R.string.gcm_registered));
	GCMServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
	    throws NoStorageException {
	Log.i(TAG, "Device unregistered");
	displayMessage(context, getString(R.string.gcm_unregistered));
	if (GCMRegistrar.isRegisteredOnServer(context)) {
	    GCMServerUtilities.unregister(context, registrationId);
	} else {
	    // This callback results from the call to unregister made on
	    // ServerUtilities when the registration to the server failed.
	    Log.i(TAG, "Ignoring unregister callback");
	}
    }

    @Override
    protected void onMessage(Context arg0, Intent intent) {

	Log.d(TAG, "MESSAGE RECEIVED : " + intent.getExtras().toString());
	String action = intent.getStringExtra("msg");
	String gcmNotification = "Something got updated";

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	Storage storage = null;
	storage = Storage.getInstance(null);

	SettingsLocal settingsLocal = null;
	try {
	    settingsLocal = storage.load();
	} catch (NoStorageException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// is exception handling OK in this class, or should it be removed?

	if (action.equals("USER_SETTINGS_CHANGED")) {
	    gcmNotification = getString(R.string.gcm_USER_SETTINGS_CHANGED);

	    if (scon.getConnected() && settingsLocal.getNotificationKind() == 1) {
		try {
		    settingsLocal = scon.getSettings();
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

		storage.store(settingsLocal);
	    } else {
		// TODO set settingsUpdateAvailable == true
	    }
	}
	if (action.equals("SERVER_STATUS_NOTE")) {
	    gcmNotification = getString(R.string.gcm_SERVER_STATUS_NOTE);
	    // TODO handle change of UserSettings
	}
	if (action.equals("USER_EVENT_PERMISSIONS_CHANGED")) {
	    gcmNotification = getString(R.string.gcm_USER_EVENT_PERMISSIONS_CHANGED);

	    if (scon.getConnected() && settingsLocal.getNotificationKind() == 1) {
		try {
		    Helper.getRights();
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
		}
	    } else {
		// TODO set rightsUpdateAvailable == true
	    }

	}
	if (action.equals("SINGLE_EVENT_UPDATE")) {
	    gcmNotification = getString(R.string.gcm_SINGLE_EVENT_UPDATE);

	    if (scon.getConnected() && settingsLocal.getNotificationKind() == 1) {
		try {
		    settingsLocal = scon.getSettings();
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

		if (settingsLocal != null) {
		    Date date = null;
		    date = settingsLocal.getLastUpdate();

		    boolean success = false;

		    try {
			success = Helper.getUpdate(date, this); // TODO is this
								// context
								// right?
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
		    if (!success) {
			// TODO set sEventUpdateAvailable == true
		    }
		}
	    } else {
		// TODO set sEventUpdateAvailable == true
	    }
	}

	generateNotification(getApplicationContext(), gcmNotification);
    }

    /*
     * @Override protected void onMessage(Context context, Intent intent) {
     * Log.i(TAG, "Received message"); String message =
     * getString(R.string.gcm_message); displayMessage(context, message); //
     * notifies user generateNotification(context, message); }
     */

    @Override
    protected void onDeletedMessages(Context context, int total) {
	Log.i(TAG, "Received deleted messages notification");
	String message = getString(R.string.gcm_deleted, total);
	// displayMessage(context, message);
	// notifies user
	generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
	Log.i(TAG, "Received error: " + errorId);
	// displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
	// log message
	Log.i(TAG, "Received recoverable error: " + errorId);
	// displayMessage(context, getString(R.string.gcm_recoverable_error,
	// errorId));
	return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
	int icon = R.drawable.logo_copa;
	long when = System.currentTimeMillis();
	NotificationManager notificationManager = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);
	Notification notification = new Notification(icon, message, when);
	String title = context.getString(R.string.app_name);
	Intent notificationIntent = new Intent(context, MainActivity.class);
	// set intent so it does not start a new activity
	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		| Intent.FLAG_ACTIVITY_SINGLE_TOP);
	PendingIntent intent = PendingIntent.getActivity(context, 0,
		notificationIntent, 0);
	notification.setLatestEventInfo(context, title, message, intent);
	notification.flags |= Notification.FLAG_AUTO_CANCEL;
	notificationManager.notify(0, notification);
    }

}
