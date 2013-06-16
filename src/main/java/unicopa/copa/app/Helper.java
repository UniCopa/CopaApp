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
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

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
 * This is a helper class which implements some methods.
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
    public static boolean subscribe(int eventID, SettingsLocal settings)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	ServerConnection scon = ServerConnection.getInstance();
	UserSettings userSettings = (UserSettings) settings;

	Date date = new Date();
	boolean success = false;
	List<SingleEvent> sEvents = null;
	Event event = null;
	EventGroup eventGroup = null;

	userSettings.addSubscription(eventID);

	success = scon.setSettings(userSettings);

	if (!success) {
	    return false;
	}

	sEvents = scon.getCurrentSingleEvents(eventID, date); // TODO read
							      // current date

	if (sEvents == null) {
	    return false; // TODO is it possible to subscribe an event with no
			  // SingleEvents?
	}

	event = scon.getEvent(eventID);

	if (event == null) {
	    return false;
	}

	int eventGroupID;
	eventGroupID = event.getEventGroupID();

	eventGroup = scon.getEventGroup(eventGroupID);

	if (eventGroup == null) {
	    return false;
	}

	// TODO save all in local database

	return true;
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
     */
    public static boolean update(Date date) throws ClientProtocolException,
	    IOException, APIException, PermissionException,
	    RequestNotPracticableException, InternalErrorException {
	ServerConnection scon = ServerConnection.getInstance();
	List<List<SingleEventUpdate>> sEventUpdatesListList = null;

	sEventUpdatesListList = scon.getSubscribedSingleEventUpdates(date);

	for (List<SingleEventUpdate> sEventUpdateList : sEventUpdatesListList) {
	    SingleEventLocal sEventLocal = null;
	    int listSize = 0;
	    int oldSEventID = 0;

	    listSize = sEventUpdateList.size();
	    oldSEventID = sEventUpdateList.get(listSize - 1)
		    .getOldSingleEventID();
	    sEventLocal = checkChanges(sEventUpdateList);

	    // TODO save sEventLocal in local database

	    // TODO inconsistency (see white board) could be solved by saving
	    // every sEvent one after another not just the last one, but this is
	    // a lot of unnecessary work
	}

	return true;
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

	String location = newestSEvent.getLocation();
	int locationCount = 0;
	Date date = null;
	int dateCount = 0;
	String supervisor = "";
	int supervisorCount = 0;
	int duration = 0;
	int durationCount = 0;
	int singleEventID = 0;
	int eventID = 0;

	date = newestSEvent.getDate();
	supervisor = newestSEvent.getSupervisor();
	duration = newestSEvent.getDurationMinutes();
	singleEventID = newestSEvent.getSingleEventID();
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

	    if (date != sEvent.getDate()) {
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
		date, supervisor, duration, "" /* colorCode */, "" /* name */,
		locationCount, dateCount, supervisorCount, durationCount, -1 /* permission */);

	return sEventLocal;
    }

}