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
import unicopa.copa.app.R.id;
import unicopa.copa.app.R.layout;
import unicopa.copa.app.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_log:
			Intent intentLog = new Intent(MainActivity.this, LoginActivity.class);
			MainActivity.this.startActivity(intentLog);
			return true;
		case R.id.action_search:
			Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
			MainActivity.this.startActivity(intentSearch);
			return true;
		case R.id.action_priv:
			Intent intentPriv = new Intent(MainActivity.this, PrivilegesActivity.class);
			MainActivity.this.startActivity(intentPriv);
			return true;
		case R.id.action_settings:
			Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
			MainActivity.this.startActivity(intentSettings);
			return true;
		case R.id.action_subscription:
			Intent intentSubscription = new Intent(MainActivity.this, SubscriptionActivity.class);
			MainActivity.this.startActivity(intentSubscription);
			return true;
		default: return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

}
