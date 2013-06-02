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
	
	
public class PrivilegesActivity extends Activity {
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.priv);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.priv_menu, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			case R.id.action_log:
				Intent intentLog = new Intent(PrivilegesActivity.this, LoginActivity.class);
				PrivilegesActivity.this.startActivity(intentLog);
				return true;
			case R.id.action_main:
				Intent intentMain = new Intent(PrivilegesActivity.this, MainActivity.class);
				PrivilegesActivity.this.startActivity(intentMain);
				return true;
			case R.id.action_search:
				Intent intentSearch = new Intent(PrivilegesActivity.this, SearchActivity.class);
				PrivilegesActivity.this.startActivity(intentSearch);
				return true;
			case R.id.action_settings:
				Intent intentSettings = new Intent(PrivilegesActivity.this, SettingsActivity.class);
				PrivilegesActivity.this.startActivity(intentSettings);
				return true;
			case R.id.action_subscription:
				Intent intentSubscription = new Intent(PrivilegesActivity.this, SubscriptionActivity.class);
				PrivilegesActivity.this.startActivity(intentSubscription);
				return true;
			
			default: return super.onOptionsItemSelected(item);
			}
		}
		
		/**
    		 * Is used if PrivButton is clicked.
    		 * Switches to EventPrivActivity.
    		 * 
    		 * @param view
    		 */
		public void onPrivButtonClick(View view){
		    	Intent intentEventPriv = new Intent(PrivilegesActivity.this, EventPrivActivity.class);
		    	intentEventPriv.putExtra("key", "value");
			PrivilegesActivity.this.startActivity(intentEventPriv);
		}
		
		/**
    		 * Is used if PrivChangeButton is clicked.
    		 * Switches to SingleEventListActivity.
    		 * 
    		 * @param view
    		 */
		public void onPrivChangeButtonClick(View view){
		    	Intent intentSingleEventList = new Intent(PrivilegesActivity.this, SingleEventListActivity.class);
		    	intentSingleEventList.putExtra("key", "value");
			PrivilegesActivity.this.startActivity(intentSingleEventList);
		}
			
}
