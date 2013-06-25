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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import unicopa.copa.base.event.SingleEvent;
import unicopa.copa.base.event.SingleEventUpdate;

/**
 * This is the TestClass of the class Helper.
 * 
 * @author Martin Rabe
 */
public class HelperTest {

    /**
     * This method tests the checkChanges method.
     */
    @Test
    public void checkChangesTest() {
	Date date1 = null;
	Date date2 = null;
	Date date3 = null;
	Date date4 = null;
	Date date5 = null;
	Date date6 = null;

	SingleEvent sEvent1 = null;
	SingleEvent sEvent2 = null;
	SingleEvent sEvent3 = null;

	SingleEventUpdate sEventUpdate1 = null;
	SingleEventUpdate sEventUpdate2 = null;
	SingleEventUpdate sEventUpdate3 = null;

	List<SingleEventUpdate> sEventUpdates = new ArrayList<SingleEventUpdate>();

	SingleEventLocal sEventLocal = null;

	date1 = new Date(113, 4, 13);
	date2 = new Date(113, 4, 13);
	date3 = new Date(113, 4, 13);
	date4 = new Date(113, 4, 14);
	date5 = new Date(113, 4, 15);
	date6 = new Date(113, 4, 16);

	sEvent1 = new SingleEvent(1234, 123, "HU-HS", date1, "Robin", 90);
	sEvent2 = new SingleEvent(1235, 123, "HS-2", date2, "Robin", 90);
	sEvent3 = new SingleEvent(1236, 123, "HS-2", date3, "David", 90);

	sEventUpdate1 = new SingleEventUpdate(sEvent1, 0, date4, "Martin", "just cause1");
	sEventUpdate2 = new SingleEventUpdate(sEvent2, 1234, date5,
		"Christiane", "just cause2");
	sEventUpdate3 = new SingleEventUpdate(sEvent3, 1235, date6, "Tuki", "just cause3");

	sEventUpdates.add(sEventUpdate3);
	sEventUpdates.add(sEventUpdate2);
	sEventUpdates.add(sEventUpdate1);

	sEventLocal = new SingleEventLocal(1236, 123, "HS-2", date1, "David",
		90, "000000", "", 1, 0, 1, 0, 0, "just cause3");

	SingleEventLocal returnevent = Helper.checkChanges(sEventUpdates);

	returnevent = Helper.checkChanges(sEventUpdates);

	assertEquals(sEventLocal.getSingleEventID(),
		returnevent.getSingleEventID());
	assertEquals(sEventLocal.getEventID(), returnevent.getEventID());
	assertEquals(sEventLocal.getLocation(), returnevent.getLocation());
	assertEquals(sEventLocal.getDate(), returnevent.getDate());
	assertEquals(sEventLocal.getSupervisor(), returnevent.getSupervisor());
	assertEquals(sEventLocal.getDurationMinutes(),
		returnevent.getDurationMinutes());
	assertEquals(sEventLocal.getColorCode(), returnevent.getColorCode());
	assertEquals(sEventLocal.getName(), returnevent.getName());
	assertEquals(sEventLocal.getLoactionUpdateCounter(),
		returnevent.getLoactionUpdateCounter());
	assertEquals(sEventLocal.getDateUpdateCounter(),
		returnevent.getDateUpdateCounter());
	assertEquals(sEventLocal.getSupervisorUpdateCounter(),
		returnevent.getSupervisorUpdateCounter());
	assertEquals(sEventLocal.getDurationMinutesUpdateCounter(),
		returnevent.getDurationMinutesUpdateCounter());
	assertEquals(sEventLocal.getPermission(), returnevent.getPermission());
    }

    /**
     * This method tests the singleEventToSingleEventLocal method.
     */
    @Test
    public void singleEventToSingleEventLocalTest() {
	int singleEventID = 1234;
	int eventID = 123;
	String location = "HS-Hu";
	Date date = new Date(113, 1, 1);
	String supervisor = "Someone";
	int durationMinutes = 42;

	SingleEvent sEvent = null;
	sEvent = new SingleEvent(singleEventID, eventID, location, date,
		supervisor, durationMinutes);

	SingleEventLocal singleEventLocal = null;
	singleEventLocal = Helper.singleEventToSingleEventLocal(sEvent, "");

	String colorCode = "000000";
	String name = "";
	int locationUpdateCounter = 0;
	int dateUpdateCounter = 0;
	int supervisorUpdateCounter = 0;
	int durationMinutesUpdateCounter = 0;
	int permission = 0;
	String comment = "";

	SingleEventLocal expectedSingleEventLocal = null;
	expectedSingleEventLocal = new SingleEventLocal(singleEventID, eventID,
		location, date, supervisor, durationMinutes, colorCode, name,
		locationUpdateCounter, dateUpdateCounter,
		supervisorUpdateCounter, durationMinutesUpdateCounter,
		permission, comment);

	assertEquals(expectedSingleEventLocal.getSingleEventID(),
		singleEventLocal.getSingleEventID());
	assertEquals(expectedSingleEventLocal.getEventID(),
		singleEventLocal.getEventID());
	assertEquals(expectedSingleEventLocal.getLocation(),
		singleEventLocal.getLocation());
	assertEquals(expectedSingleEventLocal.getDate(),
		singleEventLocal.getDate());
	assertEquals(expectedSingleEventLocal.getSupervisor(),
		singleEventLocal.getSupervisor());
	assertEquals(expectedSingleEventLocal.getDurationMinutes(),
		singleEventLocal.getDurationMinutes());
	assertEquals(expectedSingleEventLocal.getColorCode(),
		singleEventLocal.getColorCode());
	assertEquals(expectedSingleEventLocal.getName(),
		singleEventLocal.getName());
	assertEquals(expectedSingleEventLocal.getLoactionUpdateCounter(),
		singleEventLocal.getLoactionUpdateCounter());
	assertEquals(expectedSingleEventLocal.getDateUpdateCounter(),
		singleEventLocal.getDateUpdateCounter());
	assertEquals(expectedSingleEventLocal.getSupervisorUpdateCounter(),
		singleEventLocal.getSupervisorUpdateCounter());
	assertEquals(
		expectedSingleEventLocal.getDurationMinutesUpdateCounter(),
		singleEventLocal.getDurationMinutesUpdateCounter());
	assertEquals(expectedSingleEventLocal.getPermission(),
		singleEventLocal.getPermission());

    }

}
