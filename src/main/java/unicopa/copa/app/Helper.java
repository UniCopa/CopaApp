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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

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
     * @param eventID
     * @param settings
     * @return
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

	Storage storage = Storage.getInstance(null);

	storage.store(settingsLocal);

	Date date = null;
	date = Calendar.getInstance().getTime();

	List<SingleEvent> sEvents = null;
	sEvents = scon.getCurrentSingleEvents(eventID, date);

	Event event = null;
	event = scon.getEvent(eventID);

	if (event == null) {
	    return false;
	}

	int eventGroupID;
	eventGroupID = event.getEventGroupID();

	EventGroup eventGroup = null;
	eventGroup = scon.getEventGroup(eventGroupID);

	if (eventGroup == null) {
	    return false;
	}

	Database db = Database.getInstance(context);

	if (sEvents.size() != 0) {
	    for (SingleEvent sEvent : sEvents) {
		String name = "";
		name = eventGroup.getEventGroupName() + event.getEventName();

		SingleEventLocal sEventLocal = null;
		sEventLocal = Helper
			.singleEventToSingleEventLocal(sEvent, name);

		db.insert(sEventLocal, -1);
	    }
	}

	db.insert(event, -1);
	db.insert(eventGroup, -1);

	return true;
    }

    /**
     * This method removes an Event from the subscription list and erases all
     * unnecessary data from the local database.
     * 
     * @param eventID
     * @param settingsLocal
     * @return
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
     * This method what to do with a SingleEvent change.
     * 
     * @param sEventLocal
     * @param msg
     * @param context
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public static boolean setUpdate(SingleEventLocal sEventLocal, String msg,
	    Context context) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException {

	// TODO i guess we should force an update before and after the change

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	int sEventID = 0;
	sEventID = sEventLocal.getSingleEventID();

	int newEventID = 0;
	boolean success = false;

	switch (sEventID) {
	case 0: // TODO not sure what the ID needs to be for remove
	    success = scon.removeSingleEvent(sEventID, msg);
	    break;
	default:
	    newEventID = scon.setSingleEventUpdate(sEventLocal, msg);
	    break;
	}

	if (newEventID != -1) {
	    Database db = null;
	    db = Database.getInstance(context);

	    SingleEvent newSEvent = null;
	    newSEvent = scon.getSingleEvent(newEventID);

	    SingleEventLocal newSEventLocal = null;
	    newSEventLocal = Helper
		    .singleEventToSingleEventLocal(newSEvent, "");

	    db.insert(newSEventLocal, sEventID);

	    return true;
	}

	if (success) {
	    Database db = null;
	    db = Database.getInstance(context);

	    // db. TODO remove SinlgeEvent from localDatabase

	    return true;
	}

	return false;
    }

    /**
     * This method gets all updates since a given date and saves them to the
     * local database;
     * 
     * @param date
     * @return
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

	    int oldSEventID = 0;
	    oldSEventID = sEventUpdateList.get(listSize - 1)
		    .getOldSingleEventID();

	    SingleEventLocal sEventLocal = null;
	    sEventLocal = checkChanges(sEventUpdateList);

	    Database db = Database.getInstance(context);

	    db.insert(sEventLocal, oldSEventID);

	    // TODO inconsistency (see white board) could be solved by saving
	    // every sEvent one after another not just the last one, but this is
	    // a lot of unnecessary work
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
     * 
     * 
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public static boolean getRights(Context context)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	List<List<Integer>> rights = null;
	rights = scon.getMyEvents();

	if (rights != null) {
	    Database db = null;
	    db = Database.getInstance(context);

	    db.clearPermissions();

	    int permissionCode = 1;
	    for (List<Integer> right : rights) {
		db.updatePermissions(right, permissionCode);
		permissionCode++;
	    }

	    return true;
	} else {
	    return false;
	}
    }

    /**
     * This method checks what changes are made in a list of SingleEventUpdates
     * and returns a SingleEventLocal.
     * 
     * @param sEventUpdateList
     * @return
     */
    public static SingleEventLocal checkChanges(
	    List<SingleEventUpdate> sEventUpdateList) {

	SingleEvent newestSEvent = null;
	newestSEvent = sEventUpdateList.get(0).getUpdatedSingleEvent();

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

	SingleEventLocal sEventLocal = null;
	sEventLocal = new SingleEventLocal(singleEventID, eventID, location,
		date, supervisor, duration, "000000" /* colorCode */,
		"" /* name */, locationCount, dateCount, supervisorCount,
		durationCount, 0 /* permission */);

	return sEventLocal;
    }

    /**
     * This method returns to a given SinlgeEvent a SingleEventLocal with the
     * necessary default values.
     * 
     * @param sEvent
     * @param name
     * @return
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

	SingleEventLocal sEventLocal = null;
	sEventLocal = new SingleEventLocal(sel_singleEventID, sel_eventID,
		sel_location, sel_date, sel_supervisor, sel_durationMinutes,
		sel_colorCode, sel_name, sel_locationUpdateCounter,
		sel_dateUpdateCounter, sel_supervisorUpdateCounter,
		sel_durationMinutesUpdateCounter, sel_permission);

	return sEventLocal;
    }

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
