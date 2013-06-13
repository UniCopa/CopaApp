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

import java.util.ArrayList;

import unicopa.copa.app.R;

import unicopa.copa.base.event.EventGroup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * On this activity the EventGroups are shown.
 * 
 * @author Christiane Kuhn
 */
public class SearchResultGroupActivity extends Activity {

    SearchResultGroupAdapter searchGroupAdapter;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search_result_group);
	Intent intent = getIntent();
	categoryId = intent.getIntExtra("categoryID", 0);

	final ListView groupListView = (ListView) SearchResultGroupActivity.this
		.findViewById(R.id.result_group_list);

	ArrayList<EventGroup> eventGroupList = new ArrayList<EventGroup>();

	groupListView.setAdapter(null);

	eventGroupList.add(new EventGroup(1, "Telematik1", "Info", null));
	eventGroupList.add(new EventGroup(2, "Betriebssysteme", "Info2", null));
	eventGroupList.add(new EventGroup(3, "Mathe", "Info3", null));

	searchGroupAdapter = new SearchResultGroupAdapter(this, eventGroupList);
	groupListView.setAdapter((ListAdapter) searchGroupAdapter);
	groupListView.setItemsCanFocus(false);

	groupListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1,
		    int position, long arg3) {

		Intent intent = new Intent(SearchResultGroupActivity.this,
			SingleEventActivity.class);

		EventGroup clicked = (EventGroup) searchGroupAdapter
			.getItem(position);

		intent.putExtra("eventGroupID", clicked.getEventGroupID());
		intent.putExtra("categoryID", categoryId);

		startActivity(intent);

	    }
	});
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
	    Intent intentLog = new Intent(SearchResultGroupActivity.this,
		    LoginActivity.class);
	    SearchResultGroupActivity.this.startActivity(intentLog);
	    return true;
	case R.id.action_main:
	    Intent intentMain = new Intent(SearchResultGroupActivity.this,
		    MainActivity.class);
	    SearchResultGroupActivity.this.startActivity(intentMain);
	    return true;
	case R.id.action_priv:
	    Intent intentPriv = new Intent(SearchResultGroupActivity.this,
		    PrivilegesActivity.class);
	    SearchResultGroupActivity.this.startActivity(intentPriv);
	    return true;
	case R.id.action_settings:
	    Intent intentSettings = new Intent(SearchResultGroupActivity.this,
		    SettingsActivity.class);
	    SearchResultGroupActivity.this.startActivity(intentSettings);
	    return true;
	case R.id.action_subscription:
	    Intent intentSubscription = new Intent(
		    SearchResultGroupActivity.this, SubscriptionActivity.class);
	    SearchResultGroupActivity.this.startActivity(intentSubscription);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
