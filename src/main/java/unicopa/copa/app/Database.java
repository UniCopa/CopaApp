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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import unicopa.copa.app.exceptions.NoEventException;
import unicopa.copa.app.exceptions.NoEventGroupException;
import unicopa.copa.app.exceptions.NoSingleEventException;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.event.Event;
import unicopa.copa.base.event.EventGroup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class implements the Android Database as a singleton.
 * 
 * @author Robin Muench, Martin Rabe
 */
public class Database extends SQLiteOpenHelper {

    private static Database instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "COPA_DB";
    private static final String[] SingleEventLocal_scheme = { "singleEventID",
	    "eventID", "name", "date", "dateUpdateCounter", "supervisor",
	    "supervisorUpdateCounter", "location", "locationUpdateCounter",
	    "durationMinutes", "durationMinutesUpdateCounter", "colorCode",
	    "permission", "comment" };
    private static final String[] Event_scheme = { "eventID", "eventGroupID",
	    "eventName" };
    private static final String[] EventGroup_scheme = { "eventGroupID",
	    "eventGroupName", "eventGroupInfo" };

    SQLiteDatabase data;

    /**
     * This method is called to get an instance of Database.
     * 
     * @return Database
     */
    public static Database getInstance(Context context) {
	if (instance == null) {
	    instance = new Database(context);
	}

	return instance;
    }

    private Database(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * This is a helper-method to create the SQLDatabase-schemeString for
     * SingleEventLocal.
     * 
     * @param primaryKey
     * @return
     */
    private String SingleEventLocal_sqlScheme(String primaryKey) {
	String sqlString = "(";
	for (int i = 0; i < SingleEventLocal_scheme.length; i++)
	    sqlString = sqlString + SingleEventLocal_scheme[i] + ",";
	if (primaryKey != null)
	    sqlString = sqlString + "PRIMARY KEY (" + primaryKey + "))";
	else
	    sqlString = delLast(sqlString) + ")";
	return sqlString;
    }

    /**
     * This is a helper-method to create the SQLDatabase-schemeString for Event.
     * 
     * @param primaryKey
     * @return
     */
    private String Event_sqlScheme(String primaryKey) {
	String sqlString = "(";
	for (int i = 0; i < Event_scheme.length; i++)
	    sqlString = sqlString + Event_scheme[i] + ",";
	if (primaryKey != null)
	    sqlString = sqlString + "PRIMARY KEY (" + primaryKey + "))";
	else
	    sqlString = delLast(sqlString) + ")";
	return sqlString;
    }
    
    /**
     * This is a debug-method to print local Tables in LogCat
     * @param table
     */
    public void printTable(String table){
	data = this.getReadableDatabase();
	String columns[] = null;
	String selection = null;
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = null;
	Cursor c = data.query(table, columns, selection,
		selectionArgs, groupBy, having, orderBy);
	
	if(c.getCount()>0){
	    c.moveToFirst();
	    for(int i = 0; i<c.getCount(); i++){
		Log.i("Table: "+table,"#####Dataset "+String.valueOf(i+1)+"#####");
		for(int j = 0; j<c.getColumnCount();j++){
		    Log.i(c.getColumnName(j),c.getString(j));
		}
		c.moveToNext();
	    }
	}
	   c.close();
	   data.close();
    }

    /**
     * This is a helper-method to create the SQLDatabase-schemeString for
     * EventGroup.
     * 
     * @param primaryKey
     * @return
     */
    private String EventGroup_sqlScheme(String primaryKey) {
	String sqlString = "(";
	for (int i = 0; i < EventGroup_scheme.length; i++)
	    sqlString = sqlString + EventGroup_scheme[i] + ",";
	if (primaryKey != null)
	    sqlString = sqlString + "PRIMARY KEY (" + primaryKey + "))";
	else
	    sqlString = delLast(sqlString) + ")";
	return sqlString;
    }

    /**
     * This is a helper-method to create a bracketed String of values, separated
     * with commas.
     * 
     * @param values
     * @return
     */
    private String sqlValues(String[] values) {
	String valueString = "(";
	for (int i = 0; i < values.length; i++)
	    valueString = valueString + "'" + values[i] + "',";
	valueString = delLast(valueString) + ")";
	return valueString;
    }

    /**
     * This is a helper-method to delete the last char of a String
     * 
     * @param s
     * @return
     */
    private String delLast(String s) {
	return s.substring(0, s.length() - 1);
    }

    /**
     * This method initializes the Tables:
     * 
     * EventGroup Event SingleEventLocal
     * 
     * that are required in the Application
     * 
     * @throws SQLiteException
     */
    public void Table_init() throws SQLiteException {
	data = this.getWritableDatabase();
	String Table_Creation_String;

	// Create SingleEventLocal
	Table_Creation_String = "CREATE TABLE IF NOT EXISTS SingleEventLocal"
		+ SingleEventLocal_sqlScheme("singleEventID");
	Log.w("try", Table_Creation_String);
	data.execSQL(Table_Creation_String);

	// Create Event
	Table_Creation_String = "CREATE TABLE IF NOT EXISTS Event"
		+ Event_sqlScheme("eventID");
	Log.w("try", Table_Creation_String);
	data.execSQL(Table_Creation_String);

	// Create EventGroup
	Table_Creation_String = "CREATE TABLE IF NOT EXISTS EventGroup"
		+ EventGroup_sqlScheme("eventGroupID");
	Log.w("try", Table_Creation_String);
	data.execSQL(Table_Creation_String);

	data.close();
    }

    /**
     * This method deletes an existing Database-table by name.
     * 
     * @param name
     */
    public void Table_delete(String name) {
	data = this.getWritableDatabase();
	Log.w("try", "DROP TABLE IF EXISTS " + name);
	data.execSQL("DROP TABLE IF EXISTS " + name);
	data.close();
    }

    /**
     * This method inserts an Object into the Database. Allowed Objects are:
     * 
     * EventGroup Event SingleEventLocal
     * 
     * The parameter ID_old is used to update SingleEventLocals and should
     * contain the ID of the SingleEvent that should be updated. If ID_old is
     * not found in the Database or -1, the Object will be inserted.
     * 
     * @param obj
     * @param ID_old
     * @throws NoEventGroupException
     * @throws NoEventException
     */
    public void insert(Object obj, int ID_old) throws NoEventGroupException,
	    NoEventException{
	data = this.getWritableDatabase();
	String TableName = obj.getClass().getSimpleName();
	boolean newEntry = false;

	// Fill SingleEventLocal
	if (obj instanceof SingleEventLocal) {
	    SingleEventLocal sev = (SingleEventLocal) obj;

	    if (ID_old != -1)
		newEntry = false; // Update
	    if (!newEntry) {

		// Test whether SingleEvent is canceled
		if (sev.getSingleEventID() == 0 && sev.getEventID() == 0) {
		    	//test ID_old
			String columns[] = {"singleEventID"};
			String selection = "singleEventID='" + String.valueOf(ID_old)
				+ "'";
			String selectionArgs[] = null;
			String groupBy = null;
			String having = null;
			String orderBy = null;
			Cursor c = data.query(TableName, columns, selection,
				selectionArgs, groupBy, having, orderBy);
			if(c.getCount()>=1){
		    c.close();
		    String cancelString = "UPDATE SingleEventLocal SET durationMinutes = '0' WHERE singleEventID='"
			    + String.valueOf(ID_old) + "'";
		    Log.w("try", cancelString);
		    data.execSQL(cancelString);
		    return;
		    }
			else {
			    c.close();
			    Log.e("SingleEvent canceled","No SingleEvent with ID_old "+String.valueOf(ID_old)+" found!");
			    return;
			}
		}

		String columns[] = null;
		String selection = "singleEventID='" + String.valueOf(ID_old)
			+ "'";
		String selectionArgs[] = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		Cursor c = data.query(TableName, columns, selection,
			selectionArgs, groupBy, having, orderBy);
		if (c != null && c.getCount() > 0) {
		    c.moveToFirst();

		    String UpdateColumns = "UPDATE " + TableName
			    + " SET singleEventID='" + sev.getSingleEventID()
			    + "',";

		    if (sev.getDateUpdateCounter() == 1) {
			UpdateColumns = UpdateColumns + "date='"
				+ sev.getDate() + "',dateUpdateCounter='"
				+ String.valueOf(c.getInt(4) + 1) + "',";
		    }
		    if (sev.getSupervisorUpdateCounter() == 1) {
			UpdateColumns = UpdateColumns + "supervisor='"
				+ sev.getSupervisor()
				+ "',supervisorUpdateCounter='"
				+ String.valueOf(c.getInt(6) + 1) + "',";
		    }
		    if (sev.getLoactionUpdateCounter() == 1) {
			UpdateColumns = UpdateColumns + "location='"
				+ sev.getLocation()
				+ "',locationUpdateCounter='"
				+ String.valueOf(c.getInt(8) + 1) + "',";
		    }
		    if (sev.getDurationMinutesUpdateCounter() == 1) {
			UpdateColumns = UpdateColumns + "durationMinutes='"
				+ sev.getDurationMinutes()
				+ "',durationMinutesUpdateCounter='"
				+ String.valueOf(c.getInt(10) + 1) + "',";
		    }
		    UpdateColumns = delLast(UpdateColumns)
			    + " WHERE singleEventID='" + ID_old + "'";

		    // check whether SingleEvent has an existing Event
		    String Tcolumns[] = { "eventID" };
		    String Tselection = "eventID='" + c.getString(1) + "'";
		    String TselectionArgs[] = null;
		    String TgroupBy = null;
		    String Thaving = null;
		    String TorderBy = "";

		    Cursor Tc = data.query("Event", Tcolumns, Tselection,
			    TselectionArgs, TgroupBy, Thaving, TorderBy);

		    if (Tc.getCount() < 1) {
			Tc.close();
			c.close();
			throw new NoEventException(
				"No matching Event for SingleEvent "
					+ c.getString(0) + " found!");
		    }
		    Tc.close();

		    c.close();

		    Log.w("try", UpdateColumns);
		    data.execSQL(UpdateColumns);
		} else {
		    newEntry = true;
		    c.close();
		}
	    }
	    if (newEntry) {

		String name = "";

		// check whether SingleEvent has an existing Event
		String ev_columns[] = { "eventID", "eventGroupID", "eventName" };
		String ev_selection = "eventID='"
			+ String.valueOf(sev.getEventID()) + "'";
		String ev_selectionArgs[] = null;
		String ev_groupBy = null;
		String ev_having = null;
		String ev_orderBy = "";

		Cursor ev_c = data.query("Event", ev_columns, ev_selection,
			ev_selectionArgs, ev_groupBy, ev_having, ev_orderBy);

		if (ev_c.getCount() < 1) {
		    ev_c.close();
		    throw new NoEventException(
			    "No matching Event for SingleEvent "
				    + String.valueOf(sev.getSingleEventID())
				    + " found!");
		} else {

		    ev_c.moveToFirst();
		    Log.i("Database Viability",
			    "singleEventID="
				    + String.valueOf(sev.getSingleEventID())
				    + " Found matching eventID="
				    + ev_c.getString(0));
		    name = name + ev_c.getString(2);

		    // check whether Event has an existing EventGroup
		    String evg_columns[] = { "eventGroupID", "eventGroupName" };
		    String evg_selection = "eventGroupID='" + ev_c.getString(1)
			    + "'";
		    String evg_selectionArgs[] = null;
		    String evg_groupBy = null;
		    String evg_having = null;
		    String evg_orderBy = "";

		    Cursor evg_c = data.query("EventGroup", evg_columns,
			    evg_selection, evg_selectionArgs, evg_groupBy,
			    evg_having, evg_orderBy);

		    if (evg_c.getCount() < 1) {
			evg_c.close();
			ev_c.close();
			throw new NoEventGroupException(
				"No matching Event for SingleEvent "
					+ String.valueOf(sev.getSingleEventID())
					+ " found!");
		    }
		    evg_c.moveToFirst();
		    Log.i("Database Viability",
			    "eventID=" + ev_c.getString(0)
				    + " Found matching eventGroupID="
				    + evg_c.getString(0));
		    name = name + " " + evg_c.getString(1);
		    Log.i("Database Viability", "Creating singleEventName: "
			    + name);
		    evg_c.close();
		}

		ev_c.close();

		String InsertString = "INSERT INTO "
			+ sev.getClass().getSimpleName() + " "
			+ SingleEventLocal_sqlScheme(null) + " VALUES ";
		String[] values = { String.valueOf(sev.getSingleEventID()),
			String.valueOf(sev.getEventID()), name,
			String.valueOf(sev.getDate().getTime()),
			String.valueOf(sev.getDateUpdateCounter()),
			sev.getSupervisor(),
			String.valueOf(sev.getSupervisorUpdateCounter()),
			sev.getLocation(),
			String.valueOf(sev.getLoactionUpdateCounter()),
			String.valueOf(sev.getDurationMinutes()),
			String.valueOf(sev.getDurationMinutesUpdateCounter()),
			sev.getColorCode(),
			String.valueOf(sev.getPermission()), sev.getComment() };
		InsertString = InsertString + sqlValues(values);
		Log.w("try", InsertString);
		try {
		    data.execSQL(InsertString);
		} catch (SQLiteConstraintException ex) {
		    Log.e("error", "singleEventID is not unique");
		}
	    }

	}

	if (obj instanceof Event) {
	    newEntry = true;
	    Event ev = (Event) obj;
	    if (newEntry) {
		String[] values = { String.valueOf(ev.getEventID()),
			String.valueOf(ev.getEventGroupID()), ev.getEventName() };

		String InsertString = "INSERT INTO "
			+ ev.getClass().getSimpleName() + " "
			+ Event_sqlScheme(null) + " VALUES "
			+ sqlValues(values);
		Log.w("try", InsertString);
		try {
		    data.execSQL(InsertString);
		} catch (SQLiteConstraintException ex) {
		    Log.e("error", "eventID is not unique");
		}
		// check whether Event has an existing EventGroup
		String columns[] = { "eventGroupID" };
		String selection = "eventGroupID='"
			+ String.valueOf(ev.getEventGroupID()) + "'";
		String selectionArgs[] = null;
		String groupBy = null;
		String having = null;
		String orderBy = "";

		Cursor c = data.query("Event", columns, selection,
			selectionArgs, groupBy, having, orderBy);

		if (c.getCount() < 1){
		    c.close();
		    throw new NoEventGroupException(
			    "No matching EventGroup for Event "
				    + String.valueOf(ev.getEventGroupID())
				    + " found!");
		}
		c.close();
	    }
	}

	if (obj instanceof EventGroup) {
	    newEntry = true;
	    EventGroup evg = (EventGroup) obj;
	    if (newEntry) {
		String[] values = { String.valueOf(evg.getEventGroupID()),
			evg.getEventGroupName(), evg.getEventGroupInfo() };

		String InsertString = "INSERT INTO "
			+ evg.getClass().getSimpleName() + " "
			+ EventGroup_sqlScheme(null) + " VALUES "
			+ sqlValues(values);
		Log.w("try", InsertString);
		try {
		    data.execSQL(InsertString);
		} catch (SQLiteConstraintException ex) {
		    Log.e("error", "eventGroupID is not unique");
		}
	    }

	}

	data.close();
    }

    /**
     * This method returns a List of all stored Events
     * 
     * @return
     */
    public List<Event> getAllEvents() {
	data = this.getReadableDatabase();
	List<Event> EventList = new ArrayList<Event>();
	int elements = 0;

	String columns[] = null;
	String selection = "";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "";

	Cursor c = data.query("Event", columns, selection, selectionArgs,
		groupBy, having, orderBy);

	if (c.getCount() > 0) {
	    c.moveToFirst();
	    elements = c.getCount();
	}

	while (elements > 0) {
	    Event ev = new Event(c.getInt(0), c.getInt(1), c.getString(2), null);
	    EventList.add(ev);
	    c.moveToNext();
	    elements--;
	}
	c.close();
	data.close();
	return EventList;
    }

    /**
     * This method returns the Name of the EventGroup with the given
     * eventGroupID
     * 
     * @param eventGroupID
     * @return
     */
    public String getEventGroupName(int eventGroupID) {
	data = this.getReadableDatabase();
	String name = "keine eventgroup gefunden";
	String columns[] = { "eventGroupName" };
	String selection = "eventGroupID='" + String.valueOf(eventGroupID)
		+ "'";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "";

	Cursor c = data.query("EventGroup", columns, selection, selectionArgs,
		groupBy, having, orderBy);

	if (c.getCount() > 0) {
	    c.moveToFirst();
	    name = c.getString(0);
	}
	c.close();
	data.close();
	return name;
    }

    /**
     * This method returns all SingleEvents with the given eventID
     * 
     * @param eventID
     * @return
     */
    public List<SingleEventLocal> getSingleEventsByEventID(int eventID) {
	data = this.getReadableDatabase();
	List<SingleEventLocal> SingleEventLocalList = new ArrayList<SingleEventLocal>();
	int elements = 0;

	String columns[] = null;
	String selection = "eventID='" + String.valueOf(eventID) + "'";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "date ASC";

	Cursor c = data.query("SingleEventLocal", columns, selection,
		selectionArgs, groupBy, having, orderBy);

	if (c.getCount() > 0) {
	    c.moveToFirst();
	    elements = c.getCount();
	} else
	    SingleEventLocalList = null;

	while (elements > 0) {
	    Date date = new Date(c.getLong(3));
	    SingleEventLocal sev = new SingleEventLocal(c.getInt(0), // singleEventID
		    c.getInt(1), // eventID
		    c.getString(7),// Location
		    date, // Date
		    c.getString(5),// supervisor
		    c.getInt(9), // durationMinutes
		    c.getString(11),// colorCode
		    c.getString(2),// name
		    c.getInt(8), // locationUpdateCounter
		    c.getInt(4), // dateUpdateCounter
		    c.getInt(6), // supervisorUpdateCounter
		    c.getInt(10), // durationMinutesUpdateCounter
		    c.getInt(12), c.getString(13)// Permissions

	    );
	    SingleEventLocalList.add(sev);
	    c.moveToNext();
	    elements--;
	}
	c.close();
	data.close();
	return SingleEventLocalList;

    }

    /**
     * This method returns a List of the next upcoming SingleEvents
     * 
     * @param num
     * @return
     */
    public List<SingleEventLocal> getNearestSingleEvents(int num) {
	data = this.getReadableDatabase();

	List<SingleEventLocal> SingleEventLocalList = new ArrayList<SingleEventLocal>();

	int elements = 0;

	String columns[] = null;
	String selection = "";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "date ASC";

	Cursor c = data.query("SingleEventLocal", columns, selection,
		selectionArgs, groupBy, having, orderBy);
	if (c.getCount() > 0) {
	    c.moveToFirst();
	    elements = c.getCount();
	}
	while (elements > 0 && num > 0) {
	    Date date = new Date(c.getLong(3));
	    SingleEventLocal sev = new SingleEventLocal(c.getInt(0), // singleEventID
		    c.getInt(1), // eventID
		    c.getString(7),// Location
		    date, // Date
		    c.getString(5),// supervisor
		    c.getInt(9), // durationMinutes
		    c.getString(11),// colorCode
		    c.getString(2),// name
		    c.getInt(8), // locationUpdateCounter
		    c.getInt(4), // dateUpdateCounter
		    c.getInt(6), // supervisorUpdateCounter
		    c.getInt(10), // durationMinutesUpdateCounter
		    c.getInt(12), c.getString(13)// Permissions

	    );
	    SingleEventLocalList.add(sev);
	    c.moveToNext();
	    num--;
	    elements--;

	}
	c.close();
	data.close();
	return SingleEventLocalList;
    }

    /**
     * This method returns a SingleEventLocal-Object with the given
     * singleEventID
     * 
     * @param singleEventID
     * @return
     */
    public SingleEventLocal getSingleEventBySingleEventID(int singleEventID) {
	data = this.getReadableDatabase();
	String columns[] = null;
	String selection = "singleEventID='" + String.valueOf(singleEventID)
		+ "'";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "";

	Cursor c = data.query("SingleEventLocal", columns, selection,
		selectionArgs, groupBy, having, orderBy);

	if (c.getCount() > 0) {
	    c.moveToFirst();
	    SingleEventLocal sev = new SingleEventLocal(c.getInt(0), // singleEventID
		    c.getInt(1), // eventID
		    c.getString(7),// Location
		    new Date(c.getLong(3)), // Date
		    c.getString(5),// supervisor
		    c.getInt(9), // durationMinutes
		    c.getString(11),// colorCode
		    c.getString(2),// name
		    c.getInt(8), // locationUpdateCounter
		    c.getInt(4), // dateUpdateCounter
		    c.getInt(6), // supervisorUpdateCounter
		    c.getInt(10), // durationMinutesUpdateCounter
		    c.getInt(12), c.getString(13)// Permissions
	    );
	    c.close();
	    data.close();
	    return sev;
	} else {
	    c.close();
	    data.close();
	    return null;
	}
    }

    /**
     * This method returns a List of all Events with a PermissionCode > 0
     * 
     * @return
     */
    public List<Event> getEventsWithPermission() {
	data = this.getReadableDatabase();
	List<Event> eventList = new ArrayList<Event>();

	int elements = 0;

	boolean distinct = true;
	String columns[] = { "eventID" };
	String selection = "permission > '0'";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "";
	String limit = null;

	Cursor c = data.query(distinct, "SingleEventLocal", columns, selection,
		selectionArgs, groupBy, having, orderBy, limit);

	if (c.getCount() > 0) {
	    c.moveToFirst();
	    elements = c.getCount();
	    while (elements > 0) {
		String ev_columns[] = null;
		String ev_selection = "eventID = '" + c.getString(0) + "'";
		String ev_selectionArgs[] = null;
		String ev_groupBy = null;
		String ev_having = null;
		String ev_orderBy = "";

		Cursor ev_c = data.query("Event", ev_columns, ev_selection,
			ev_selectionArgs, ev_groupBy, ev_having, ev_orderBy);

		if (ev_c.getCount() > 0) {
		    ev_c.moveToFirst();
		    Event ev = new Event(ev_c.getInt(0), ev_c.getInt(1),
			    ev_c.getString(2), null);
		    eventList.add(ev);
		} else {
		    Log.w("error", "no Event with ID " + c.getString(0)
			    + " found");
		  //  ev_c.close();
		}
		ev_c.close();
		c.moveToNext();
		elements--;
	    }
	    c.close();
	} else {
	    c.close();
	    data.close();
	    Log.w("error", "no SingleEventsFound");
	    return null;
	}
	data.close();
	return eventList;
    }

    /**
     * This method sets the PermissionCode of all SingleEvents to 0
     */
    public void clearPermissions() {
	data = this.getWritableDatabase();
	String updateString = "UPDATE SingleEventLocal SET permission = '0'";
	Log.w("try", updateString);
	data.execSQL(updateString);
	data.close();
    }

    /**
     * This Method is for updating
     * 
     * @param eventList
     * @param permissionCode
     * @throws NoEventException
     * @throws NoEventGroupException
     */
    public void updatePermissions(List<Integer> eventList, int permissionCode)
	    throws NoEventException, NoEventGroupException {
	data = this.getWritableDatabase();
	Iterator<Integer> iter = eventList.iterator();
	int eventID;

	Log.w("vor der", "schleife");
	while (iter.hasNext()) {
	    eventID = iter.next();
	    Log.w("Database", "eventID=" + String.valueOf(eventID) + " perm="
		    + String.valueOf(permissionCode));
/*	TODO test whether event got eventGroup
	    // Test Event
	    String ev_columns[] = { "eventID","eventGroupID" };
	    String ev_selection = "eventID = '" + String.valueOf(eventID) + "'";
	    String ev_selectionArgs[] = null;
	    String ev_groupBy = null;
	    String ev_having = null;
	    String ev_orderBy = "";

	    Cursor ev_c = data.query("Event", ev_columns, ev_selection,
		    ev_selectionArgs, ev_groupBy, ev_having, ev_orderBy);

	    if (ev_c.getCount() < 1) {
		ev_c.close();
		throw new NoEventException(eventID);
	    }

	    else {
		ev_c.moveToFirst();
		String evg_columns[] = { "eventGroupID" };
		String evg_selection = "eventGroupID = '" + ev_c.getString(1)
			+ "'";
		String evg_selectionArgs[] = null;
		String evg_groupBy = null;
		String evg_having = null;
		String evg_orderBy = "";

		Cursor evg_c = data.query("EventGroup", evg_columns,
			evg_selection, evg_selectionArgs, evg_groupBy,
			evg_having, evg_orderBy);

		if (evg_c.getCount() < 1) {
		    evg_c.close();
		    throw new NoEventGroupException("No matching EventGroup="
			    + ev_c.getString(1) + " for Event "
			    + String.valueOf(eventID) + " found!");
		}
		evg_c.close();
	    }
	    ev_c.close();*/

	    String updateString = "UPDATE SingleEventLocal SET permission = '"
		    + String.valueOf(permissionCode) + "' WHERE eventID = '"
		    + String.valueOf(eventID) + "'";

	    Log.w("try", updateString);
	    data.execSQL(updateString);
	}
	data.close();
    }

    /**
     * This Method gets colors from a SettingsLocal-Object and updates the
     * Database.
     * 
     * @param setLoc
     */
    public void updateColors(SettingsLocal setLoc) {
	data = this.getWritableDatabase();
	int eventID;
	String color;
	String updateString;
	Set<Integer> events = setLoc.getSubscriptions();
	Iterator<Integer> iter = events.iterator();

	while (iter.hasNext()) {
	    eventID = (Integer) iter.next();
	    UserEventSettings uSett = setLoc.getEventSettings(eventID);
	    color = uSett.getColorCode();
	    updateString = "UPDATE SingleEventLocal SET colorCode = '" + color
		    + "' WHERE eventID = '" + String.valueOf(eventID) + "'";
	    Log.w("try", updateString);
	    data.execSQL(updateString);
	}
	data.close();
    }

    /**
     * This Method deletes an Event and all connected SingleEvents. If the Event
     * is the last one in the related EventGroup, it will be deleted as well.
     * 
     * @param eventID
     * @throws PermissionException
     */
    public void deleteEventByEventID(int eventID) throws PermissionException {
	data = this.getWritableDatabase();
	boolean lastEvent = false;
	// Test whether User is Owner
	String sEvent_columns[] = { "permission" };
	String sEvent_selection = "eventID = '" + String.valueOf(eventID)
		+ "' AND permission > '2'";
	String sEvent_selectionArgs[] = null;
	String sEvent_groupBy = null;
	String sEvent_having = null;
	String sEvent_orderBy = "";

	Cursor c = data.query("SingleEventLocal", sEvent_columns,
		sEvent_selection, sEvent_selectionArgs, sEvent_groupBy,
		sEvent_having, sEvent_orderBy);

	if (c.getCount() > 0) {
	    c.close();
	    data.close();
	    throw new PermissionException("You are Owner of this Event");
	} else {
	    c.close();

	    String Event_columns[] = { "eventID","eventGroupID" };
	    String Event_selection = "eventID = '" + String.valueOf(eventID)
		    + "'";
	    String Event_selectionArgs[] = null;
	    String Event_groupBy = null;
	    String Event_having = null;
	    String Event_orderBy = "";

	    Cursor ev_c = data.query("Event", Event_columns, Event_selection,
		    Event_selectionArgs, Event_groupBy, Event_having,
		    Event_orderBy);
	    
	    
	    if (ev_c.getCount() < 1){
		Log.e("deleting_error","No Event with ID "+String.valueOf(eventID)+" found in local Database!");
		ev_c.close();
	    }
	    else{
	  
	    
	    ev_c.moveToFirst();
	    String eventGroupID = ev_c.getString(1);	    
	    ev_c.close();

	    //check whether Event is last in EventGroup
	    String Event1_columns[] = { "eventID","eventGroupID" };
	    String Event1_selection = "eventGroupID = '" + eventGroupID
		    + "'";
	    String Event1_selectionArgs[] = null;
	    String Event1_groupBy = null;
	    String Event1_having = null;
	    String Event1_orderBy = "";

	    Cursor ev1_c = data.query("Event", Event1_columns, Event1_selection,
		    Event1_selectionArgs, Event1_groupBy, Event1_having,
		    Event1_orderBy);
	    
	    if(ev1_c.getCount()<2){
		lastEvent=true;
		Log.w("Database","this is the last Event with ID "+eventGroupID);
	    }
	    
	    // delete Event
	    String deleteEventString = "DELETE FROM Event WHERE eventID = '"
		    + String.valueOf(eventID) + "'";
	    String deleteSingleEventString = "DELETE FROM SingleEventLocal WHERE eventID = '"
		    + String.valueOf(eventID) + "'";
	    Log.w("try", deleteEventString);
	    data.execSQL(deleteEventString);
	    Log.w("try", deleteSingleEventString);
	    data.execSQL(deleteSingleEventString);

	    if (lastEvent) {
		String deleteEventGroupString = "DELETE FROM EventGroup WHERE eventGroupID = '"
			+ eventGroupID + "'";
		Log.w("try", deleteEventGroupString);
		data.execSQL(deleteEventGroupString);
	    }

	}
	}
	data.close();
    }

    /**
     * This Method deletes all expired SingleEvents from the Database
     */
    public void deleteExpiredSingleEvents() {
	data = this.getWritableDatabase();
	Date d = new Date();
	int elements = 0;

	String columns[] = { "singleEventID", "date" };
	String selection = "";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "";

	Cursor c = data.query("SingleEventLocal", columns, selection,
		selectionArgs, groupBy, having, orderBy);

	if (c.getCount() > 0) {
	    c.moveToFirst();
	    elements = c.getCount();

	    while (elements > 0) {
		if (new Date(c.getLong(1)).compareTo(d) < 0) {
		    String deleteString = "DELETE FROM SingleEventLocal WHERE singleEventID = '"
			    + c.getString(0) + "'";
		    Log.w("try", deleteString);
		    data.execSQL(deleteString);
		}
		c.moveToNext();
		elements--;
	    }
	} else
	    Log.i("deleteExpiredSingleEvents", "No SingleEvents found");
	c.close();
	data.close();
    }

/**    
 * This Method deletes a SingleEvent from the Database
 * 
 * @param singleEventID
 */
    public void deleteSingleEvent(int singleEventID) {
	data = this.getWritableDatabase();
	String deleteString = "DELETE FROM SingleEventLocal WHERE singleEventID='"
		+ String.valueOf(singleEventID) + "'";
	Log.w("try", deleteString);
	data.execSQL(deleteString);
	data.close();
    }

}
