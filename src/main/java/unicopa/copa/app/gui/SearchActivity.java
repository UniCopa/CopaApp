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
import unicopa.copa.base.event.CategoryNode;
import unicopa.copa.base.event.CategoryNodeImpl;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;

/**
 * In this activity a user can search a event.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class SearchActivity extends Activity {

    ArrayList<CategoryNodeImpl> categories = new ArrayList<CategoryNodeImpl>();
    SearchAdapter searchAdapter;
    int currentID = 0;
    TextView cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search);

	ServerConnection scon = ServerConnection.getInstance();

	if (scon.getConnected()) {
	    CategoryNode category = null;

	    try {
		category = scon.getCategory();
	    } catch (ClientProtocolException e) {
		PopUp.exceptionAlert(this, "ClientProtocolException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (APIException e) {
		PopUp.exceptionAlert(this, "APIException!", e.getMessage());
		// e.printStackTrace();
	    } catch (PermissionException e) {
		PopUp.exceptionAlert(this, "PermissionException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (RequestNotPracticableException e) {
		PopUp.exceptionAlert(this, "RequestNotPracticableException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (InternalErrorException e) {
		PopUp.exceptionAlert(this, "InternalErrorException!",
			e.getMessage());
		// e.printStackTrace();
	    } catch (IOException e) {
		PopUp.exceptionAlert(this, "IOException!", e.getMessage());
		// e.printStackTrace();
	    }

	    if (category != null) {
		// TODO display categories
	    }

	} else {
	    // TODO l18n
	    PopUp.alert(this, "Login!", "You are not logged in.");
	}

	final ListView catListView = (ListView) SearchActivity.this
		.findViewById(R.id.search_list);
	cat = (TextView) findViewById(R.id.search_categorie);
	catListView.setAdapter(null);

	CategoryNodeImpl cattest = new CategoryNodeImpl(1, "Category5");
	cattest.addChildNode(new CategoryNodeImpl(4, "Category4"));
	categories.add(cattest);
	categories.add(new CategoryNodeImpl(2, "Category2"));
	categories.add(new CategoryNodeImpl(3, "Category3"));

	searchAdapter = new SearchAdapter(this, categories);
	catListView.setAdapter((ListAdapter) searchAdapter);

	catListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1,
		    int position, long arg3) {

		CategoryNodeImpl clicked = (CategoryNodeImpl) searchAdapter
			.getItem(position);
		currentID = clicked.getId();
		cat.setText(clicked.getName());
		categories.clear();
		categories.addAll((ArrayList<CategoryNodeImpl>) clicked
			.getChildren());
		searchAdapter.notifyDataSetChanged();
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

    public void onSearchButtonClick(View view) {
	Intent intentResult = new Intent(SearchActivity.this,
		SearchResultGroupActivity.class);
	intentResult.putExtra("categoryID", currentID);
	SearchActivity.this.startActivity(intentResult);
    }
}
