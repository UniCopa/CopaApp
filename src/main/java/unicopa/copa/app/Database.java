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
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	    "permissions" };
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

    private String SingleEventLocal_sqlScheme() {
	String sqlString = "(";
	for (int i = 0; i < SingleEventLocal_scheme.length; i++)
	    sqlString = sqlString + SingleEventLocal_scheme[i] + ",";
	sqlString = delLast(sqlString) + ")";
	return sqlString;
    }

    private String Event_sqlScheme() {
	String sqlString = "(";
	for (int i = 0; i < Event_scheme.length; i++)
	    sqlString = sqlString + Event_scheme[i] + ",";
	sqlString = delLast(sqlString) + ")";
	return sqlString;
    }

    private String EventGroup_sqlScheme() {
	String sqlString = "(";
	for (int i = 0; i < EventGroup_scheme.length; i++)
	    sqlString = sqlString + EventGroup_scheme[i] + ",";
	sqlString = delLast(sqlString) + ")";
	return sqlString;
    }

    private String sqlValues(String[] values) {
	String valueString = "(";
	for (int i = 0; i < values.length; i++)
	    valueString = valueString + "'" + values[i] + "',";
	valueString = delLast(valueString) + ")";
	return valueString;
    }

    private String delLast(String s) {
	return s.substring(0, s.length() - 1);
    }

    public void Table_init() {
	data = this.getWritableDatabase();
	String Table_Creation_String;

	// Create SingleEventLocal
	Table_Creation_String = "CREATE TABLE IF NOT EXISTS SingleEventLocal"
		+ SingleEventLocal_sqlScheme();
	Log.w("try", Table_Creation_String);
	data.execSQL(Table_Creation_String);
	Log.w("exec_succ", Table_Creation_String);

	// Create Event
	Table_Creation_String = "CREATE TABLE IF NOT EXISTS Event"
		+ Event_sqlScheme();
	Log.w("try", Table_Creation_String);
	data.execSQL(Table_Creation_String);
	Log.w("exec_succ", Table_Creation_String);

	// Create EventGroup
	Table_Creation_String = "CREATE TABLE IF NOT EXISTS EventGroup"
		+ EventGroup_sqlScheme();
	Log.w("try", Table_Creation_String);
	data.execSQL(Table_Creation_String);
	Log.w("succ", Table_Creation_String);

	data.close();
    }

    public void Table_delete(String name) {
	data = this.getWritableDatabase();
	Log.w("try", "DROP TABLE IF EXISTS " + name);
	data.execSQL("DROP TABLE IF EXISTS " + name);
	Log.w("succ", "DROP TABLE IF EXISTS " + name);
	data.close();
    }

    public void insert(Object obj, int ID_old) {
	data = this.getWritableDatabase();
	String TableName = obj.getClass().getSimpleName();
	boolean insertType = false;

	// Fill SingleEventLocal
	if (obj instanceof SingleEventLocal) {
	    SingleEventLocal sev = (SingleEventLocal) obj;

	    if (ID_old != -1)
		insertType = false; // Update
	    if (!insertType) {
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
			UpdateColumns = UpdateColumns + "duration='"
				+ sev.getDurationMinutes()
				+ "',durationMinutesUpdateCounter='"
				+ String.valueOf(c.getInt(10) + 1) + "',";
		    }
		    UpdateColumns = delLast(UpdateColumns)
			    + " WHERE singleEventID='" + ID_old + "'";

		    Log.w("try", UpdateColumns);
		    data.execSQL(UpdateColumns);
		    Log.w("succ", UpdateColumns);
		} else
		    insertType = true;
	    }
	    if (insertType) {
		String InsertString = "INSERT INTO "
			+ sev.getClass().getSimpleName() + " "
			+ SingleEventLocal_sqlScheme() + " VALUES ";
		String[] values = { String.valueOf(sev.getSingleEventID()),
			String.valueOf(sev.getEventID()), sev.getName(),
			String.valueOf(sev.getDate().getTime()),
			String.valueOf(sev.getDateUpdateCounter()),
			sev.getSupervisor(),
			String.valueOf(sev.getSupervisorUpdateCounter()),
			sev.getLocation(),
			String.valueOf(sev.getLoactionUpdateCounter()),
			String.valueOf(sev.getDurationMinutes()),
			String.valueOf(sev.getDurationMinutesUpdateCounter()),
			sev.getColorCode(), String.valueOf(sev.getPermission()) };
		InsertString = InsertString + sqlValues(values);
		Log.w("try", InsertString);
		data.execSQL(InsertString);
		Log.w("succ", InsertString);
	    }

	}
	data.close();
    }

    public List<SingleEventLocal> getNearestSingleEvents(int num) {
	data = this.getReadableDatabase();

	List<SingleEventLocal> SingleEventLocalList = new ArrayList<SingleEventLocal>();

	int elements=0;
	
	String columns[] = null;
	String selection = "";
	String selectionArgs[] = null;
	String groupBy = null;
	String having = null;
	String orderBy = "date ASC";

	Cursor c = data.query("SingleEventLocal", columns, selection,
		selectionArgs, groupBy, having, orderBy);
	if(c.getCount()>0){
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
		    c.getInt(12) // Permissions
		    
	    );
	    SingleEventLocalList.add(sev);
	    c.moveToNext();
	    num--;
	    elements--;

	}
	data.close();
	return SingleEventLocalList;
    }

}
