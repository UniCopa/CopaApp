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
 * In this activity a user can see all Events, which he has subscribed.
 * 
 * @author Christiane Kuhn
 */
public class SubscriptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.subscription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.subscription_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_log:
	    Intent intentLog = new Intent(SubscriptionActivity.this,
		    LoginActivity.class);
	    SubscriptionActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SubscriptionActivity.this,
		    MainActivity.class);
	    SubscriptionActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_search:
	    Intent intentSearch = new Intent(SubscriptionActivity.this,
		    SearchActivity.class);
	    SubscriptionActivity.this.startActivity(intentSearch);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SubscriptionActivity.this,
		    PrivilegesActivity.class);
	    SubscriptionActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SubscriptionActivity.this,
		    SettingsActivity.class);
	    SubscriptionActivity.this.startActivity(intentSettings);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Is used if AlDatesButton is clicked. Switches to SingleEventListActivity.
     * 
     * @param view
     */
    public void onAllDatesButtonClick(View view) {
	Intent intentSingleEventList = new Intent(SubscriptionActivity.this,
		SingleEventListActivity.class);
	intentSingleEventList.putExtra("key", "value");
	SubscriptionActivity.this.startActivity(intentSingleEventList);
    }
}
