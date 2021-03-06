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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.Database;
import unicopa.copa.app.Helper;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SingleEventLocal;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * In this activity a user can update a event, if he has the rights to do it.
 * 
 * @author Christiane Kuhn, Martin Rabe, Robin Muench
 */
public class ChangeSingleEventActivity extends Activity {

    EditText location;
    DatePicker datePicker;
    TimePicker timePicker;
    EditText supervisor;
    EditText durationtime;
    EditText comment;
    SingleEventLocal sEventLocal = null;

    /**
     * Creates ChangeSingleEventActivity with editText for an update input and
     * shows the current values of this SingleEvent.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.changesingleevent);
	Intent intent = getIntent();
	int sEventID = intent.getIntExtra("singleID", 0);

	ServerConnection scon = ServerConnection.getInstance();

	// check if logged in if not redirect to LoginActivity
	if (!scon.getConnected()) {
	    PopUp.loginFail(this);
	}

	Database db = Database.getInstance(ChangeSingleEventActivity.this);

	sEventLocal = db.getSingleEventBySingleEventID(sEventID);
	db.close();

	location = (EditText) findViewById(R.id.change_edit_location);
	datePicker = (DatePicker) findViewById(R.id.change_edit_date);
	timePicker = (TimePicker) findViewById(R.id.timePicker1);
	supervisor = (EditText) findViewById(R.id.change_edit_supervisor);
	durationtime = (EditText) findViewById(R.id.change_edit_dura);
	comment = (EditText) findViewById(R.id.change_comment);

	TextView name = (TextView) findViewById(R.id.change_eventGroupname);
	TextView locationText = (TextView) findViewById(R.id.change_location);
	TextView date = (TextView) findViewById(R.id.change_date);
	TextView time = (TextView) findViewById(R.id.change_time);
	TextView supervisorText = (TextView) findViewById(R.id.change_supervisor);
	TextView durationtimeText = (TextView) findViewById(R.id.change_dura);

	name.setText(sEventLocal.getName());
	locationText.setText(sEventLocal.getLocation());
	date.setText(new SimpleDateFormat("dd.MM").format(sEventLocal.getDate()));
	time.setText(new SimpleDateFormat("HH:mm").format(sEventLocal.getDate()));
	supervisorText.setText(sEventLocal.getSupervisor());
	durationtimeText.setText(String.valueOf(sEventLocal
		.getDurationMinutes()));
	location.setText(sEventLocal.getLocation());
	datePicker.setTag(sEventLocal.getDate());
	timePicker.setTag(sEventLocal.getDate());
	supervisor.setText(sEventLocal.getSupervisor());
	durationtime.setText(String.valueOf(sEventLocal.getDurationMinutes()));

	// just to convert date to calendar
	Date actualDate = sEventLocal.getDate();
	Calendar cal = new GregorianCalendar();
	cal.setTime(actualDate);

	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int hour = cal.get(Calendar.HOUR_OF_DAY);
	int min = cal.get(Calendar.MINUTE);

	datePicker.updateDate(year, month, day);

	timePicker.setCurrentHour(hour);
	timePicker.setCurrentMinute(min);

    }

    /**
     * Inflate the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.all_items_menu, menu);
	return true;
    }

    /**
     * Switch to other activity, depending on which menu item was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(ChangeSingleEventActivity.this,
		    LoginActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(ChangeSingleEventActivity.this,
		    MainActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(ChangeSingleEventActivity.this,
		    SearchActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(ChangeSingleEventActivity.this,
		    PrivilegesActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(ChangeSingleEventActivity.this,
		    SettingsActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    ChangeSingleEventActivity.this, SubscriptionActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentSubscription);
	    return true;
	case R.id.action_help:
	    Intent intentHelp = new Intent(ChangeSingleEventActivity.this,
		    HelpActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentHelp);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Handles click on "Apply"-Button and sends the currently written inputs of
     * the editTexts as an update to the server.
     * 
     */
    public void onApplyButtonClick(View view) {

	CheckBox remove = (CheckBox) findViewById(R.id.change_remove);

	boolean removeIt = remove.isChecked();

	int day = datePicker.getDayOfMonth();
	int month = datePicker.getMonth();
	// January=0
	int year = datePicker.getYear();
	int hour = timePicker.getCurrentHour();
	int minutes = timePicker.getCurrentMinute();

	Calendar cal = Calendar.getInstance();
	cal.set(year, month, day, hour, minutes);
	Date newDate = cal.getTime();

	String newSupervisor = supervisor.getText().toString();
	String newLocation = location.getText().toString();
	int newDura = Integer.parseInt(durationtime.getText().toString());
	int eventID = sEventLocal.getEventID();
	int oldSingleEventID = sEventLocal.getSingleEventID();
	String msg = comment.getText().toString();

	boolean success = false;

	if (removeIt) {
	    try {
		Helper.removeSingleEvent(oldSingleEventID, msg,
			getApplicationContext());
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
	} else {
	    SingleEventLocal newSEventLocal = null;
	    newSEventLocal = new SingleEventLocal(oldSingleEventID, eventID,
		    newLocation, newDate, newSupervisor, newDura,
		    "000000" /* colorCode */, "" /* name */,
		    0 /* locationUpdateCounter */, 0 /* dateUpdateCounter */,
		    0 /* supervisorUpdateCounter */,
		    0 /* durationMinutesUpdateCounter */, 0 /* permission */,
		    "" /* comment */);

	    try {
		success = Helper.setUpdate(newSEventLocal, msg,
			getApplicationContext());
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
	}

	if (success) {
	    PopUp.alert(this, getString(R.string.success),
		    getString(R.string.changed));

	    Intent intentMain = new Intent(ChangeSingleEventActivity.this,
		    MainActivity.class);
	    ChangeSingleEventActivity.this.startActivity(intentMain);
	}
    }
}
