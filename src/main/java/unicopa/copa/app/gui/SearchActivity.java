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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * In this activity a user can search a event.
 * 
 * @author Christiane Kuhn
 */
public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.search_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(SearchActivity.this,
		    LoginActivity.class);
	    SearchActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SearchActivity.this,
		    MainActivity.class);
	    SearchActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SearchActivity.this,
		    PrivilegesActivity.class);
	    SearchActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SearchActivity.this,
		    SettingsActivity.class);
	    SearchActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(SearchActivity.this,
		    SubscriptionActivity.class);
	    SearchActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Is used if AllDatesButton is clicked. Switches to
     * SingleEventListActivity.
     * 
     * @param view
     */
    public void onAllDatesButtonClick(View view) {
	Intent intentEventPriv = new Intent(SearchActivity.this,
		SingleEventListActivity.class);
	intentEventPriv.putExtra("key", "value");
	SearchActivity.this.startActivity(intentEventPriv);
    }
}
