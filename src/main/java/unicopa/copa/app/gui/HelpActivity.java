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

import unicopa.copa.app.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * This activity shows the categories of help.
 * 
 * @author Christiane Kuhn
 */
public class HelpActivity extends Activity {

    /**
     * Creates HelpActivity with a buttons with which the user can choose is
     * topic of interest.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.help);
    }

    public void onPrivilegesButtonClick(View view) {
	Intent intent = new Intent(HelpActivity.this,
		HelpExtendedActivity.class);
	intent.putExtra("from", 1);
	HelpActivity.this.startActivity(intent);
    }

    public void onFunctionalityButtonClick(View view) {
	Intent intent = new Intent(HelpActivity.this,
		HelpExtendedActivity.class);
	intent.putExtra("from", 2);
	HelpActivity.this.startActivity(intent);
    }

    public void onSettingsButtonClick(View view) {
	Intent intent = new Intent(HelpActivity.this,
		HelpExtendedActivity.class);
	intent.putExtra("from", 3);
	HelpActivity.this.startActivity(intent);
    }

    public void onExceptionButtonClick(View view) {
	Intent intent = new Intent(HelpActivity.this,
		HelpExtendedActivity.class);
	intent.putExtra("from", 4);
	HelpActivity.this.startActivity(intent);
    }

}
