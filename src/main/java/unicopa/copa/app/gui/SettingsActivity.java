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
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * In this activity a user can change his settings.
 * 
 * @author Christiane Kuhn
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

	ServerConnection scon = ServerConnection.getInstance();

	// check if logged in if not redirect to LoginActivity
	if (!scon.getConnected()) {
	    // TODO redirects instantly without waiting
	    PopUp.loginFail(this);

	    Intent intentLog = new Intent(SettingsActivity.this,
		    LoginActivity.class);
	    SettingsActivity.this.startActivity(intentLog);
	}

	mail = (CheckBox) findViewById(R.id.settings_noti_mail);
	language = (RadioGroup) findViewById(R.id.settings_radio_languages);
	gcm = (RadioGroup) findViewById(R.id.settings_radio_gcm);
	english = (RadioButton) findViewById(R.id.settings_language_english);
	german = (RadioButton) findViewById(R.id.settings_language_german);
	gcmNone = (RadioButton) findViewById(R.id.settings_noti_gcm_none);
	gcmManu = (RadioButton) findViewById(R.id.settings_noti_gcm_manu);
	gcmAuto = (RadioButton) findViewById(R.id.settings_noti_gcm_auto);

	// Later depending on saved settings
	if (true) {
	    mail.setChecked(true);
	}

	switch (1) {
	case 1:
	    gcmAuto.setChecked(true);
	default:
	    gcmAuto.setChecked(true);
	}

	if (true) {
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

	// TODO read SettingsLocal from local database and set new values
	
	try {
	    scon.setSettings(settings);
	} catch (ClientProtocolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (APIException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (PermissionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (RequestNotPracticableException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InternalErrorException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	// TODO if setSettings succeeded save SettingsLocal to local database
	
	PopUp.alert(this, getString(R.string.success),
		getString(R.string.settings_saved));
    }
}
