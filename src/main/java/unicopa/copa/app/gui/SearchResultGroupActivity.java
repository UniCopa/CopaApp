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

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
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
 * On this activity the EventGroups as results of a search are shown.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class SearchResultGroupActivity extends Activity {

    SearchResultGroupAdapter searchGroupAdapter;
    int categoryId;

    /**
     * Creates SearchResultGroupActivity with a list of EventGroups which are
     * the result of a searchrequest are shown. By clicking on one of them all
     * depending Events, which fit to the before chosen category, are shown in
     * the SearchResultEventActivity that is started then.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search_result_group);
	Intent intent = getIntent();
	categoryId = intent.getIntExtra("categoryID", 0);
	String searchTerm = intent.getStringExtra("searchterm");

	final ListView groupListView = (ListView) SearchResultGroupActivity.this
		.findViewById(R.id.result_group_list);

	ArrayList<EventGroup> eventGroupList = new ArrayList<EventGroup>();

	groupListView.setAdapter(null);

	// begin getEventGroups

	ServerConnection scon = ServerConnection.getInstance();

	try {
	    eventGroupList = (ArrayList<EventGroup>) scon.getEventGroups(
		    categoryId, searchTerm);
	} catch (ClientProtocolException e) {
	    PopUp.alert(this, getString(R.string.cp_ex), e.getMessage());
	    // e.printStackTrace();
	} catch (APIException e) {
	    PopUp.alert(this, getString(R.string.api_ex), e.getMessage());
	    // e.printStackTrace();
	} catch (PermissionException e) {
	    PopUp.alert(this, getString(R.string.per_ex), e.getMessage());
	    // e.printStackTrace();
	} catch (RequestNotPracticableException e) {
	    PopUp.alert(this, getString(R.string.rnp_ex), e.getMessage());
	    // e.printStackTrace();
	} catch (InternalErrorException e) {
	    PopUp.alert(this, getString(R.string.ie_ex), e.getMessage());
	    // e.printStackTrace();
	} catch (IOException e) {
	    PopUp.alert(this, getString(R.string.io_ex), e.getMessage());
	    // e.printStackTrace();
	}

	// end getEventGroups

	searchGroupAdapter = new SearchResultGroupAdapter(this, eventGroupList);
	groupListView.setAdapter((ListAdapter) searchGroupAdapter);
	groupListView.setItemsCanFocus(false);

	groupListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1,
		    int position, long arg3) {

		Intent intent = new Intent(SearchResultGroupActivity.this,
			SearchResultEventActivity.class);

		EventGroup clicked = (EventGroup) searchGroupAdapter
			.getItem(position);

		intent.putExtra("eventGroupID", clicked.getEventGroupID());
		intent.putExtra("categoryID", categoryId);
		intent.putExtra("groupname", clicked.getEventGroupName());

		startActivity(intent);

	    }
	});
    }

    /**
     * Shows the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.search_menu, menu);
	return true;
    }

    /**
     * Handles clicks on a menu-item and switches to other activity, depending
     * on which item was clicked.
     */
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
	case R.id.action_help:
	    Intent intentHelp = new Intent(SearchResultGroupActivity.this,
		    HelpActivity.class);
	    SearchResultGroupActivity.this.startActivity(intentHelp);
	    return true;

	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
