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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);
	Storage S = Storage.getInstance(this.getApplicationContext());
	SettingsLocal settings = S.load();

	ServerConnection scon = ServerConnection.getInstance();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;
    }

    /**
     * Send UserSettings to server and save GCMsettings on device.
     */
    public void onApplyButtonClick(View view) {

	ServerConnection scon = ServerConnection.getInstance();
	SettingsLocal settings = new SettingsLocal();

	boolean email = mail.isChecked();
	int selectedLanguage = language.getCheckedRadioButtonId();
	int selectedGCM = gcm.getCheckedRadioButtonId();

	// TODO find out why settings were not saved
	Storage S = Storage.getInstance(this.getApplicationContext());
	settings = S.load();
	if (email) {
	    settings.enableEmailNotification();
	} else {
	    settings.disableEmailNotification();
	}

	switch (selectedLanguage) {
	case 0:
	    settings.setLanguage("english");
	    break;
	case 1:
	    settings.setLanguage("german");
	    break;
	default:
	    settings.setLanguage("english");
	    break;
	}

	settings.setNotificationKind(selectedGCM);

	try {
	    scon.setSettings(settings);
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

	// TODO if setSettings succeeded save SettingsLocal to local database

	PopUp.alert(this, getString(R.string.success),
		getString(R.string.settings_saved));
    }

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
