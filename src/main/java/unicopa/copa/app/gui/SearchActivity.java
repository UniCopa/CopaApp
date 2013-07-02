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
import java.util.Collections;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;

import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.CategoryNode;
import unicopa.copa.base.event.CategoryNodeImpl;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;

/**
 * In this activity a user start his search after a event by chosing categories
 * and a searchterm.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class SearchActivity extends Activity {

    ArrayList<CategoryNodeImpl> categories = new ArrayList<CategoryNodeImpl>();
    SearchAdapter searchAdapter;
    int currentID = 0;
    TextView cat;
    EditText searchEdit;

    /**
     * Creates SearchActivity and shows the first level of the category-tree.
     * The user can navigate through the category-tree and add a searchterm for
     * his search. It also handles the click on the "search"-button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search);
	final ListView catListView = (ListView) SearchActivity.this
		.findViewById(R.id.search_list);
	cat = (TextView) findViewById(R.id.search_categorie);
	catListView.setAdapter(null);
	searchEdit = (EditText) findViewById(R.id.search_edit);

	ServerConnection scon = ServerConnection.getInstance();

	// check if logged in if not redirect to LoginActivity
	if (!scon.getConnected()) {
	    PopUp.loginFail(this);
	} else {
	    CategoryNode category = null;

	    try {
		category = scon.getCategory();
	    } catch (ClientProtocolException e) {
		PopUp.alert(this, "ClientProtocolException!", e.getMessage());
		// e.printStackTrace();
	    } catch (APIException e) {
		PopUp.alert(this, "APIException!", e.getMessage());
		// e.printStackTrace();
	    } catch (PermissionException e) {
		PopUp.alert(this, "PermissionException!", e.getMessage());
		// e.printStackTrace();
	    } catch (RequestNotPracticableException e) {
		PopUp.alert(this, "RequestNotPracticableException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (InternalErrorException e) {
		PopUp.alert(this, "InternalErrorException!", e.getMessage());
		// e.printStackTrace();
	    } catch (IOException e) {
		PopUp.alert(this, "IOException!", e.getMessage());
		// e.printStackTrace();
	    }

	    if (category != null) {
		CategoryNodeImpl impl = (CategoryNodeImpl) category;
		cat.setText(impl.getName());
		categories.clear();
		categories.addAll((ArrayList<CategoryNodeImpl>) impl
			.getChildren());
	    }

	}

	searchAdapter = new SearchAdapter(this, categories);
	catListView.setAdapter((ListAdapter) searchAdapter);

	catListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1,
		    int position, long arg3) {

		CategoryNodeImpl clicked = (CategoryNodeImpl) searchAdapter
			.getItem(position);
		currentID = clicked.getId();
		List<CategoryNodeImpl> list = clicked.getChildren();
		if (!list.equals(Collections.<CategoryNodeImpl> emptyList())) {
		    cat.setText(clicked.getName());
		    categories.clear();
		    categories.addAll((ArrayList<CategoryNodeImpl>) list);
		    searchAdapter.notifyDataSetChanged();
		} else {
		    String term = searchEdit.getText().toString();
		    Intent intentResult = new Intent(SearchActivity.this,
			    SearchResultGroupActivity.class);
		    intentResult.putExtra("categoryID", currentID);
		    intentResult.putExtra("searchterm", term);
		    SearchActivity.this.startActivity(intentResult);
		}

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
	case R.id.action_help:
	    Intent intentHelp = new Intent(SearchActivity.this,
		    HelpActivity.class);
	    SearchActivity.this.startActivity(intentHelp);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Handles click on "search"-button and starts the SearchResultGroupActivity
     * on which the searchrequest is send to the server.
     * 
     * @param view
     */
    public void onSearchButtonClick(View view) {
	String term = searchEdit.getText().toString();
	Intent intentResult = new Intent(SearchActivity.this,
		SearchResultGroupActivity.class);
	intentResult.putExtra("categoryID", currentID);
	intentResult.putExtra("searchterm", term);
	SearchActivity.this.startActivity(intentResult);
    }
}
