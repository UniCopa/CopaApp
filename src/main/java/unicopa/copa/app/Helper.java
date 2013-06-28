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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

import unicopa.copa.app.exceptions.NoEventException;
import unicopa.copa.app.exceptions.NoEventGroupException;
import unicopa.copa.app.exceptions.NoSingleEventException;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.UserSettings;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.Event;
import unicopa.copa.base.event.EventGroup;
import unicopa.copa.base.event.SingleEvent;
import unicopa.copa.base.event.SingleEventUpdate;

/**
 * This is a helper class which implements some methods for repeating tasks that
 * are needed on various occasions.
 * 
 * @author Martin Rabe
 */
public class Helper {

    /**
     * This method adds an Event to the subscription list and saves all
     * necessary data in the local database.
     * 
     * @param EventID
     * @param SettingsLocal
     * @return True for success. / False for Failure.
     * @throws InternalErrorException
     * @throws RequestNotPracticableException
     * @throws PermissionException
     * @throws APIException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static boolean subscribe(int eventID, SettingsLocal settingsLocal,
	    Context context) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException {

	// TODO if (gmc-auto or (gmc-manu and !notift)) == true
	// setLastUpdate(currentDate)

	// TODO perform update if ((gmc-manu and notify) or none) == true

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	settingsLocal.addSubscription(eventID);

	UserEventSettings eventSettings = new UserEventSettings();
	eventSettings.setColorCode("000000");

	settingsLocal.putEventSettings(eventID, eventSettings);

	UserSettings userSettings = null;
	userSettings = (UserSettings) settingsLocal;

	boolean success = false;
	success = scon.setSettings(userSettings);

	if (!success) {
	    return false;
	}

	Storage storage = null;
	storage = Storage.getInstance(null);

	storage.store(settingsLocal);

	Date date = null;
	date = Calendar.getInstance().getTime();

	List<SingleEvent> sEvents = null;
	sEvents = scon.getCurrentSingleEvents(eventID, date);

	Database db = Database.getInstance(context);

	// if Event has no SingleEvents just insert the Event and EventGroup
	if (sEvents.size() == 0) {
	    Event event = null;
	    event = scon.getEvent(eventID);

	    try {
		db.insert(event, eventID);
	    } catch (NoEventGroupException e1) {
		int eventGroupID;
		eventGroupID = event.getEventGroupID();

		EventGroup eventGroup = null;
		eventGroup = scon.getEventGroup(eventGroupID);

		if (eventGroup == null) {
		    return false;
		}

		try {
		    db.insert(eventGroup, eventGroupID);
		    db.insert(event, eventID);
		} catch (NoEventGroupException e2) {
		    // This should never happen
		    e2.printStackTrace();
		} catch (NoEventException e2) {
		    // This should never happen
		    e2.printStackTrace();
		}
	    } catch (NoEventException e1) {
		// This should never happen
		e1.printStackTrace();
	    }
	}

	// if Event has SingleEvents insert all
	if (sEvents.size() != 0) {
	    for (SingleEvent sEvent : sEvents) {
		SingleEventLocal sEventLocal = null;
		sEventLocal = Helper.singleEventToSingleEventLocal(sEvent, "");

		try {
		    db.insert(sEventLocal, -1);
		} catch (NoEventGroupException e) {
		    // This should never happen
		    e.printStackTrace();
		} catch (NoEventException e) {
		    Event event = null;
		    event = scon.getEvent(eventID);

		    if (event == null) {
			return false;
		    }

		    try {
			db.insert(event, eventID);
			db.insert(sEventLocal, -1);

		    } catch (NoEventGroupException e1) {
			int eventGroupID;
			eventGroupID = event.getEventGroupID();

			EventGroup eventGroup = null;
			eventGroup = scon.getEventGroup(eventGroupID);

			if (eventGroup == null) {
			    return false;
			}

			try {
			    db.insert(eventGroup, eventGroupID);
			    db.insert(event, eventID);
			    db.insert(sEventLocal, -1);
			} catch (NoEventGroupException e2) {
			    // This should never happen
			    e2.printStackTrace();
			} catch (NoEventException e2) {
			    // This should never happen
			    e2.printStackTrace();
			}
		    } catch (NoEventException e1) {
			// This should never happen
			e1.printStackTrace();
		    }
		}
	    }
	}

	return true;
    }

    /**
     * This method removes an Event from the subscription list and erases all
     * unnecessary data from the local database.
     * 
     * @param EventID
     * @param EettingsLocal
     * @return True for success. / False for failure.
     * @throws InternalErrorException
     * @throws RequestNotPracticableException
     * @throws PermissionException
     * @throws APIException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static boolean unsubscribe(int eventID, SettingsLocal settingsLocal,
	    Context context) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	settingsLocal.removeSubscription(eventID);

	UserSettings userSettings = null;
	userSettings = (UserSettings) settingsLocal;

	boolean success = false;
	success = scon.setSettings(userSettings);

	if (!success) {
	    return false;
	}

	Storage storage = null;
	storage = Storage.getInstance(null);

	storage.store(settingsLocal);

	Database db = Database.getInstance(context);

	db.deleteEventByEventID(eventID);

	return true;
    }

    /**
     * This method implements the changing of a SingleEvent.
     * 
     * @param SingleEventLocal
     * @param Message
     * @param Context
     * @return True for success. / False for failure.
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     * @throws NoStorageException
     */
    public static boolean setUpdate(SingleEventLocal sEventLocal, String msg,
	    Context context) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException, NoStorageException {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	// TODO only perform update if ((gmc-manu and notify) or none) == true

	boolean success = false;

	// begin update

	if (true) {
	    SettingsLocal settingsLocal = null;
	    settingsLocal = scon.getSettings();

	    if (settingsLocal != null) {
		Date date = null;
		date = settingsLocal.getLastUpdate();

		success = Helper.getUpdate(date, context);
	    } else {
		return false;
	    }

	    if (!success) {
		return false;
	    }
	}

	// end update

	// TODO check for success
	scon.setSingleEventUpdate(sEventLocal, msg);

	SettingsLocal settingsLocal = null;
	settingsLocal = scon.getSettings();

	if (settingsLocal != null) {
	    Date date = null;
	    date = settingsLocal.getLastUpdate();

	    success = Helper.getUpdate(date, context);
	} else {
	    return false;
	}

	if (success) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * This method implements the removal of a SingleEvent.
     * 
     * @param sEventID
     * @param context
     * @return
     * @throws NoStorageException
     * @throws InternalErrorException
     * @throws RequestNotPracticableException
     * @throws PermissionException
     * @throws APIException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static boolean removeSingleEvent(int sEventID, String msg,
	    Context context) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException, NoStorageException {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	// TODO only perform update if ((gmc-manu and notify) or none) == true

	boolean success = false;

	// begin update

	if (true) {
	    SettingsLocal settingsLocal = null;
	    settingsLocal = scon.getSettings();

	    if (settingsLocal != null) {
		Date date = null;
		date = settingsLocal.getLastUpdate();

		success = Helper.getUpdate(date, context);
	    } else {
		return false;
	    }

	    if (!success) {
		return false;
	    }
	}

	// end update

	success = scon.removeSingleEvent(sEventID, msg);

	if (success) {
	    SettingsLocal settingsLocal = null;
	    settingsLocal = scon.getSettings();

	    if (settingsLocal != null) {
		Date date = null;
		date = settingsLocal.getLastUpdate();

		success = Helper.getUpdate(date, context);

	    } else {
		return false;
	    }
	}

	if (success) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * This method gets all updates since a given date and saves them to the
     * local database
     * 
     * @param Date
     * @return True for success. / False for failure.
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     * @throws NoStorageException
     */
    public static boolean getUpdate(Date date, Context context)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException, NoStorageException {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	List<List<SingleEventUpdate>> sEventUpdatesListList = null;
	sEventUpdatesListList = scon.getSubscribedSingleEventUpdates(date);

	// TODO check whether request succeeds

	for (List<SingleEventUpdate> sEventUpdateList : sEventUpdatesListList) {
	    int listSize = 0;
	    listSize = sEventUpdateList.size();

	    boolean cancel = false;
	    cancel = sEventUpdateList.get(listSize - 1).isCancellation();

	    int cancelID = 0;
	    if (cancel) {
		cancelID = sEventUpdateList.get(listSize - 1)
			.getOldSingleEventID();

		sEventUpdateList.remove(listSize - 1);

		listSize -= 1;
	    }

	    int oldSEventID = 0;
	    oldSEventID = sEventUpdateList.get(listSize - 1)
		    .getOldSingleEventID();

	    SingleEventLocal sEventLocal = null;
	    sEventLocal = checkChanges(sEventUpdateList);

	    Database db = Database.getInstance(context);

	    try {
		db.insert(sEventLocal, oldSEventID);
	    } catch (NoEventGroupException e) {
		// This should never happen
		e.printStackTrace();
	    } catch (NoEventException e) {
		int eventID = 0;
		eventID = sEventLocal.getEventID();

		Event event = null;
		event = scon.getEvent(eventID);

		if (event == null) {
		    return false;
		}

		try {
		    db.insert(event, eventID);
		    db.insert(sEventLocal, oldSEventID);
		} catch (NoEventGroupException e1) {
		    int eventGroupID;
		    eventGroupID = event.getEventGroupID();

		    EventGroup eventGroup = null;
		    eventGroup = scon.getEventGroup(eventGroupID);

		    if (eventGroup == null) {
			return false;
		    }

		    try {
			db.insert(eventGroup, eventGroupID);
			db.insert(event, eventID);
			db.insert(sEventLocal, oldSEventID);
		    } catch (NoEventGroupException e2) {
			// This should never happen
			e2.printStackTrace();
		    } catch (NoEventException e2) {
			// This should never happen
			e2.printStackTrace();
		    }

		    // e1.printStackTrace();
		} catch (NoEventException e1) {
		    // This should never happen
		    e1.printStackTrace();
		}
	    }

	    if (cancel) {
		db.deleteSingleEvent(cancelID);
	    }
	}

	Storage storage = null;
	storage = Storage.getInstance(null);

	SettingsLocal settingsLocal = null;
	settingsLocal = storage.load();

	date = Calendar.getInstance().getTime();

	settingsLocal.setLastUpdate(date);

	storage.store(settingsLocal);

	return true;
    }

    /**
     * This method updates the database with received permissions.
     * 
     * @return True for success. / False for failure.
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public static boolean getRights() throws ClientProtocolException,
	    IOException, APIException, PermissionException,
	    RequestNotPracticableException, InternalErrorException {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	List<List<Integer>> rights = null;
	rights = scon.getMyEvents();

	Database db = null;
	db = Database.getInstance(null);

	db.clearPermissions();

	List<Integer> rightholder = null;
	rightholder = rights.get(0);

	List<Integer> deputy = null;
	deputy = rights.get(1);

	List<Integer> owner = null;
	owner = rights.get(2);
	try {
	    db.updatePermissions(rightholder, 1);
	    db.updatePermissions(deputy, 2);
	    db.updatePermissions(owner, 3);
	} catch (NoEventGroupException e) {
	    // This should never happen
	    e.printStackTrace();
	} catch (NoEventException e) {
	    int eventID = 0;
	    eventID = e.getEventID();

	    Event event = null;
	    event = scon.getEvent(eventID);

	    if (event == null) {
		return false;
	    }

	    try {
		db.insert(event, eventID);
	    } catch (NoEventGroupException e1) {
		int eventGroupID;
		eventGroupID = event.getEventGroupID();

		EventGroup eventGroup = null;
		eventGroup = scon.getEventGroup(eventGroupID);

		if (eventGroup == null) {
		    return false;
		}
		// e1.printStackTrace();
	    } catch (NoEventException e1) {
		// This should never happen
		e1.printStackTrace();
	    }
	}

	return true;
    }

    /**
     * This method checks what changes are made in a list of SingleEventUpdates
     * and returns a SingleEventLocal.
     * 
     * @param sEventUpdateList
     * @return The newest version of the SingleEvents.
     */
    public static SingleEventLocal checkChanges(
	    List<SingleEventUpdate> sEventUpdateList) {
	int size = 0;
	size = sEventUpdateList.size();

	SingleEvent newestSEvent = null;
	newestSEvent = sEventUpdateList.get(size - 1).getUpdatedSingleEvent();

	String location = "";
	location = newestSEvent.getLocation();

	int locationCount = 0;

	Date date = null;
	date = newestSEvent.getDate();

	int dateCount = 0;

	String supervisor = "";
	supervisor = newestSEvent.getSupervisor();

	int supervisorCount = 0;

	int duration = 0;
	duration = newestSEvent.getDurationMinutes();

	int durationCount = 0;

	int singleEventID = 0;
	singleEventID = newestSEvent.getSingleEventID();

	int eventID = 0;
	eventID = newestSEvent.getEventID();

	for (SingleEventUpdate sEventUpdate : sEventUpdateList) {
	    SingleEvent sEvent = null;
	    sEvent = sEventUpdate.getUpdatedSingleEvent();

	    if (location != sEvent.getLocation()) {
		locationCount = 1;
	    }
	}

	for (SingleEventUpdate sEventUpdate : sEventUpdateList) {
	    SingleEvent sEvent = null;
	    sEvent = sEventUpdate.getUpdatedSingleEvent();

	    if (date.getTime() != sEvent.getDate().getTime()) {
		dateCount = 1;
		break;
	    }
	}

	for (SingleEventUpdate sEventUpdate : sEventUpdateList) {
	    SingleEvent sEvent = null;
	    sEvent = sEventUpdate.getUpdatedSingleEvent();

	    if (supervisor != sEvent.getSupervisor()) {
		supervisorCount = 1;
		break;
	    }
	}

	for (SingleEventUpdate sEventUpdate : sEventUpdateList) {
	    SingleEvent sEvent = null;
	    sEvent = sEventUpdate.getUpdatedSingleEvent();

	    if (duration != sEvent.getDurationMinutes()) {
		durationCount = 1;
		break;
	    }
	}

	String comment = "";
	comment = sEventUpdateList.get(size - 1).getComment();

	SingleEventLocal sEventLocal = null;
	sEventLocal = new SingleEventLocal(singleEventID, eventID, location,
		date, supervisor, duration, "000000" /* colorCode */,
		"" /* name */, locationCount, dateCount, supervisorCount,
		durationCount, 0 /* permission */, comment);

	return sEventLocal;
    }

    /**
     * This method returns to a given SinlgeEvent a SingleEventLocal with the
     * necessary default values.
     * 
     * @param SingleEvent
     * @param name
     * @return A SingleEventLocal.
     */
    public static SingleEventLocal singleEventToSingleEventLocal(
	    SingleEvent sEvent, String name) {
	int sel_singleEventID = sEvent.getSingleEventID();
	int sel_eventID = sEvent.getEventID();
	String sel_location = sEvent.getLocation();
	Date sel_date = sEvent.getDate();
	String sel_supervisor = sEvent.getSupervisor();
	int sel_durationMinutes = sEvent.getDurationMinutes();
	String sel_colorCode = "000000";
	String sel_name = name;
	int sel_locationUpdateCounter = 0;
	int sel_dateUpdateCounter = 0;
	int sel_supervisorUpdateCounter = 0;
	int sel_durationMinutesUpdateCounter = 0;
	int sel_permission = 0;
	String sel_comment = "";

	SingleEventLocal sEventLocal = null;
	sEventLocal = new SingleEventLocal(sel_singleEventID, sel_eventID,
		sel_location, sel_date, sel_supervisor, sel_durationMinutes,
		sel_colorCode, sel_name, sel_locationUpdateCounter,
		sel_dateUpdateCounter, sel_supervisorUpdateCounter,
		sel_durationMinutesUpdateCounter, sel_permission, sel_comment);

	return sEventLocal;
    }

    /**
     * This method converts a instance of UserSettings to SettingsLocal.
     * 
     * @param UserSettings
     * @return SettingsLocal
     * @throws NoStorageException
     */
    public static SettingsLocal userSettingsToSettingsLocal(
	    UserSettings settings) throws NoStorageException {
	Set<String> gcmKeys = null;
	gcmKeys = settings.getGCMKeys();

	boolean emailNotification = false;
	emailNotification = settings.isEmailNotificationEnabled();

	String language = "";
	language = settings.getLanguage();

	Map<Integer, UserEventSettings> eventSettings = null;
	eventSettings = new HashMap<Integer, UserEventSettings>();

	Set<Integer> subscriptions = null;
	subscriptions = settings.getSubscriptions();

	for (int eventID : subscriptions) {
	    UserEventSettings eSettings = settings.getEventSettings(eventID);

	    eventSettings.put(eventID, eSettings);
	}

	Storage storage = null;
	storage = Storage.getInstance(null);

	SettingsLocal oldSettingsLocal = null;
	oldSettingsLocal = storage.load();

	int notificationKind = 0;
	notificationKind = oldSettingsLocal.getNotificationKind();

	Date lastUpdate = null;
	lastUpdate = oldSettingsLocal.getLastUpdate();

	String localGcmKey = "";
	localGcmKey = oldSettingsLocal.getLocalGcmKey();

	SettingsLocal settingsLocal = null;
	settingsLocal = new SettingsLocal(gcmKeys, emailNotification, language,
		eventSettings, notificationKind, lastUpdate, localGcmKey);

	return settingsLocal;
    }

}
