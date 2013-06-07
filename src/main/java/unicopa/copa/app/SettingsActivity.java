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

import unicopa.copa.app.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * In this activity a user can change his settings.
 * 
 * @author Christiane Kuhn
 */
public class SettingsActivity extends Activity implements
	OnItemSelectedListener {

    Spinner language;
    TextView selectedLanguage;
    Spinner notification;
    TextView selectedNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);
	selectedLanguage = (TextView) super.findViewById(R.id.language_text);
	language = (Spinner) findViewById(R.id.languageSpinner);
	language.setOnItemSelectedListener(this);
	selectedNotification = (TextView) super.findViewById(R.id.notification);
	notification = (Spinner) findViewById(R.id.notificationSpinner);
	notification.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;
    }

    /**
     * reaction of selecting an entry in one of the spinners
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
	    long arg3) {
	// TODO Auto-generated method stub
	int languagePosition = language.getSelectedItemPosition();
	switch (languagePosition) {
	case 0:
	    selectedLanguage.setText("English");
	    break;
	case 1:
	    selectedLanguage.setText("German");
	    break;
	default:
	    break;
	}
	int notificationPosition = notification.getSelectedItemPosition();
	switch (notificationPosition) {
	case 0:
	    selectedNotification.setText("Mail");
	    break;
	case 1:
	    selectedNotification.setText("Push");
	    break;
	case 2:
	    selectedNotification.setText("None");
	default:
	    break;

	}
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub

    }

}
