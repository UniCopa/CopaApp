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
import android.widget.TextView;

/**
 * This activity shows helpful information as the rights hierarchy of CoPA.
 * 
 * @author Christiane Kuhn
 */
public class HelpExtendedActivity extends Activity {

    /**
     * Creates HelpActivity with a text.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.help_extended);
	Intent intent = getIntent();
	int from = intent.getIntExtra("from", 0);
	TextView text = (TextView) findViewById(R.id.helpEx_text);
	switch (from) {
	case 1:
	    text.setText(R.string.help_priv);
	    break;
	case 2:
	    text.setText(R.string.help_functionality);
	    break;
	case 3:
	    break;
	default:
	    break;
	}
    }
}
