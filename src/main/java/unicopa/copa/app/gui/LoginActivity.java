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

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.NoStorageException;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * In this activity a user can enter his name and password to login or click a
 * button to logout.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class LoginActivity extends Activity {
    ServerConnection scon = ServerConnection.getInstance();
    int fail;

    /**
     * Shows Layout and depending on whether the user is logged in or not the
     * logout- oder login-button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);
	Intent intent = getIntent();
	fail = intent.getIntExtra("failed", 0);

	TextView title = (TextView) findViewById(R.id.login_title);
	TextView user = (TextView) findViewById(R.id.login_username);
	TextView password = (TextView) findViewById(R.id.login_pw);
	EditText name = (EditText) findViewById(R.id.login_usernameField);
	EditText pw = (EditText) findViewById(R.id.login_passwordField);
	Button loginButton = (Button) findViewById(R.id.login_login_button);
	Button logoutButton = (Button) findViewById(R.id.login_logout_button);
	if (scon.getConnected()) {
	    loginButton.setVisibility(View.GONE);
	    user.setVisibility(View.GONE);
	    password.setVisibility(View.GONE);
	    name.setVisibility(View.GONE);
	    pw.setVisibility(View.GONE);
	    logoutButton.setVisibility(View.VISIBLE);
	    title.setText(getString(R.string.title_logout));
	} else {
	    logoutButton.setVisibility(View.GONE);
	    loginButton.setVisibility(View.VISIBLE);
	    title.setText(getString(R.string.title_login));
	}

    }

    /**
     * Lead to MainActivity if PopUp.loginFail happened before.
     */
    @Override
    public void onBackPressed() {
	if (fail == 1) {
	    Intent intentMain = new Intent(LoginActivity.this,
		    MainActivity.class);
	    LoginActivity.this.startActivity(intentMain);
	} else {
	    finish();
	}
    }

    /**
     * Shows the Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;

    }

    /**
     * Is used if LoginButton is clicked. Calls the ServerConnection.login
     * method if it was successful switches back to MainActivity.
     * 
     * @param view
     */
    public void onLoginButtonClick(View view) {

	// A Loading Screen or something similar would be nice. Otherwise when
	// you click during load the app crashes.
	Toast toast = Toast.makeText(LoginActivity.this, "Please wait.",
		Toast.LENGTH_SHORT);
	toast.show();

	// TODO check if already logged in
	if (!scon.getConnected()) {

	    String userName = "";
	    String password = "";

	    // read userName and password from respective textEdit
	    EditText name = (EditText) findViewById(R.id.login_usernameField);
	    userName = name.getText().toString();
	    EditText pw = (EditText) findViewById(R.id.login_passwordField);
	    password = pw.getText().toString();

	    boolean success = false;

	    try {
		success = scon.login(userName, password,
			getApplicationContext());
	    } catch (ClientProtocolException e) {
		PopUp.exceptionAlert(this, getString(R.string.cp_ex),
			e.getMessage());
		// e.printStackTrace();
	    } catch (IOException e) {
		PopUp.exceptionAlert(this, getString(R.string.io_ex),
			e.getMessage());
		// e.printStackTrace();
	    }

	    if (success) {
		SettingsLocal settingsLocal = null;

		try {
		    settingsLocal = scon.getSettings();
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
		    PopUp.exceptionAlert(this,
			    "RequestNotPracticableException!", e.getMessage());
		    // e.printStackTrace();
		} catch (InternalErrorException e) {
		    PopUp.exceptionAlert(this, "InternalErrorException!",
			    e.getMessage());
		    // e.printStackTrace();
		} catch (IOException e) {
		    PopUp.exceptionAlert(this, "IOException!", e.getMessage());
		    // e.printStackTrace();
		} catch (NoStorageException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		if (settingsLocal != null) {
		    Storage storage = null;
		    storage = Storage.getInstance(null);

		    storage.store(settingsLocal);
		}

		Intent intentMain = new Intent(LoginActivity.this,
			MainActivity.class);
		LoginActivity.this.startActivity(intentMain);
	    } else {
		PopUp.exceptionAlert(this, getString(R.string.login_error), "" /* TODO */);
	    }
	}
    }

    /**
     * Is used if LogoutButton is clicked. Calls the ServerConnection.logout
     * method if it was successful switches back to MainActivity.
     * 
     * @param view
     */
    public void onLogoutButtonClick(View view) {

	scon.logout();

	Toast toast = Toast.makeText(LoginActivity.this, "Logged out.",
		Toast.LENGTH_SHORT);
	toast.show();

	Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
	LoginActivity.this.startActivity(intentMain);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_about:
	    Intent intentLog = new Intent(LoginActivity.this,
		    AboutActivity.class);
	    LoginActivity.this.startActivity(intentLog);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }
}
