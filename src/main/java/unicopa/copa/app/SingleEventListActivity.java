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

/**
 * In this activity a user sees all SingleEvents to a Event.
 * 
 * @author Christiane Kuhn
 */
public class SingleEventListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.singleeventlist);
	Intent intent = getIntent();
	String event = intent.getStringExtra("key");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.all_items_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(SingleEventListActivity.this,
		    LoginActivity.class);
	    SingleEventListActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SingleEventListActivity.this,
		    MainActivity.class);
	    SingleEventListActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(SingleEventListActivity.this,
		    SearchActivity.class);
	    SingleEventListActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SingleEventListActivity.this,
		    PrivilegesActivity.class);
	    SingleEventListActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SingleEventListActivity.this,
		    SettingsActivity.class);
	    SingleEventListActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    SingleEventListActivity.this, SubscriptionActivity.class);
	    SingleEventListActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Is used if SingleEventListButton is clicked. Switches to
     * SingleEventActivity.
     * 
     * @param view
     */
    public void onSingleEventListButtonClick(View view) {
	Intent intentSingleEvent = new Intent(SingleEventListActivity.this,
		SingleEventActivity.class);
	SingleEventListActivity.this.startActivity(intentSingleEvent);
    }

    /**
     * Is used if ChangeDateButton is clicked. Switches to
     * ChangeSingleEventActivity.
     * 
     * @param view
     */
    public void onChangeDateButtonClick(View view) {
	Intent intentSingleEvent = new Intent(SingleEventListActivity.this,
		ChangeSingleEventActivity.class);
	SingleEventListActivity.this.startActivity(intentSingleEvent);
    }

}
