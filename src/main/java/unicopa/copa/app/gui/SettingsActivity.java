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

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.UserSettings;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * In this activity a user can change his settings.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class SettingsActivity extends Activity {

    CheckBox mail;
    RadioGroup language;
    RadioGroup gcm;
    RadioButton english;
    RadioButton german;
    RadioButton gcmNone;
    RadioButton gcmManu;
    RadioButton gcmAuto;

    /**
     * Creates the SettingsActivity and shows the saved settings as picked.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	// check if logged in if not redirect to LoginActivity
	if (!scon.getConnected()) {
	    PopUp.loginFail(this);
	}

	mail = (CheckBox) findViewById(R.id.settings_noti_mail);
	language = (RadioGroup) findViewById(R.id.settings_radio_languages);
	gcm = (RadioGroup) findViewById(R.id.settings_radio_gcm);
	english = (RadioButton) findViewById(R.id.settings_language_english);
	german = (RadioButton) findViewById(R.id.settings_language_german);
	gcmNone = (RadioButton) findViewById(R.id.settings_noti_gcm_none);
	gcmManu = (RadioButton) findViewById(R.id.settings_noti_gcm_manu);
	gcmAuto = (RadioButton) findViewById(R.id.settings_noti_gcm_auto);

	// TODO would it be better to read settings from server?
	Storage S = null;
	S = Storage.getInstance(this.getApplicationContext());

	SettingsLocal settings = null;

	try {
	    settings = S.load();
	} catch (NoStorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (settings.isEmailNotificationEnabled()) {
	    mail.setChecked(true);
	} else {
	    mail.setChecked(false);
	}

	switch (settings.getNotificationKind()) {
	case 0:
	    gcmManu.setChecked(true);
	    break;
	case 1:
	    gcmAuto.setChecked(true);
	    break;
	case 2:
	    gcmNone.setChecked(true);
	    break;
	default:
	    gcmAuto.setChecked(true);
	    break;
	}

	if (settings.getLanguage().equalsIgnoreCase("german")) {
	    german.setChecked(true);
	} else {
	    english.setChecked(true);
	}

    }

    /**
     * Shows the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;
    }

    /**
     * Handles click on "apply"-button and sends the modified UserSettings to
     * server and saves GCMsettings on device. If it succeeds the user is
     * informed with a dialog.
     */
    public void onApplyButtonClick(View view) {
	boolean email = mail.isChecked();
	int selectedLanguage = language.getCheckedRadioButtonId();
	int selectedGCM = gcm.getCheckedRadioButtonId();

	SettingsLocal settingsLocal = null;
	settingsLocal = new SettingsLocal();

	Storage S = Storage.getInstance(this.getApplicationContext());

	try {
	    settingsLocal = S.load();
	} catch (NoStorageException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	if (email) {
	    settingsLocal.enableEmailNotification();
	} else {
	    settingsLocal.disableEmailNotification();
	}

	if (english.isChecked()) {
	    settingsLocal.setLanguage("english");
	}

	if (german.isChecked()) {
	    settingsLocal.setLanguage("german");
	}

	if (gcmNone.isChecked()) {
	    settingsLocal.setNotificationKind(2);
	}

	if (gcmAuto.isChecked()) {
	    settingsLocal.setNotificationKind(1);
	}

	if (gcmManu.isChecked()) {
	    settingsLocal.setNotificationKind(0);
	}

	String gcmKey = "";
	gcmKey = settingsLocal.getLocalGcmKey();

	if (settingsLocal.getNotificationKind() == 2) {
	    settingsLocal.removeGCMKey(gcmKey);
	} else {
	    settingsLocal.addGCMKey(gcmKey);
	}

	ServerConnection scon = null;
	scon = ServerConnection.getInstance();

	boolean success = false;

	try {
	    success = scon.setSettings(settingsLocal);
	} catch (ClientProtocolException e) {
	    PopUp.exceptionAlert(this, "ClientProtocolException!",
		    e.getMessage());
	    // e.printStackTrace();
	} catch (APIException e) {
	    PopUp.exceptionAlert(this, "APIException!", e.getMessage());
	    // e.printStackTrace();
	} catch (PermissionException e) {
	    PopUp.exceptionAlert(this, "PermissionException!", e.getMessage());
	    // e.printStackTrace();
	} catch (RequestNotPracticableException e) {
	    PopUp.exceptionAlert(this, "RequestNotPracticableException!",
		    e.getMessage());
	    // e.printStackTrace();
	} catch (InternalErrorException e) {
	    PopUp.exceptionAlert(this, "InternalErrorException!",
		    e.getMessage());
	    // e.printStackTrace();
	} catch (IOException e) {
	    PopUp.exceptionAlert(this, "IOException!", e.getMessage());
	    // e.printStackTrace();
	}

	Storage storage = null;
	storage = Storage.getInstance(SettingsActivity.this);

	if (success) {
	    storage.store(settingsLocal);

	    PopUp.alert(this, getString(R.string.success),
		    getString(R.string.settings_saved));
	}
    }

    /**
     * Handles clicks on a menu-item and switches to other activity, depending
     * on which item was clicked.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_about:
	    Intent intentLog = new Intent(SettingsActivity.this,
		    AboutActivity.class);
	    SettingsActivity.this.startActivity(intentLog);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
