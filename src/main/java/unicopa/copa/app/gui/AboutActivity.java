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
 * This activity shows information about CoPA.
 * 
 * @author Christiane Kuhn
 */
public class AboutActivity extends Activity {

    /**
     * Creates AboutActivity with a text and a button. The text contains
     * information about CoPA.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.about);
    }

    /**
     * Handles click on "OK"-Button with a switch to the MainActivity.
     * 
     */
    public void onOkButtonClick(View view) {
	Intent intentMain = new Intent(AboutActivity.this, MainActivity.class);
	AboutActivity.this.startActivity(intentMain);
    }
}
