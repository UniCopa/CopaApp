package unicopa.copa.app;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import unicopa.copa.base.event.SingleEvent;
import unicopa.copa.base.event.SingleEventUpdate;

public class HelperTest {

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

	sEventUpdate1 = new SingleEventUpdate(sEvent1, 0, date4, "Martin", "");
	sEventUpdate2 = new SingleEventUpdate(sEvent2, 1234, date5,
		"Christiane", "");
	sEventUpdate3 = new SingleEventUpdate(sEvent3, 1235, date6, "Tuki", "");

	sEventUpdates.add(sEventUpdate3);
	sEventUpdates.add(sEventUpdate2);
	sEventUpdates.add(sEventUpdate1);

	sEventLocal = new SingleEventLocal(1236, 123, "HS-2", date1, "David",
		90, "", "", 1, 0, 1, 0, 0);

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

}
