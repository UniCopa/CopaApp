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
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.Database;
import unicopa.copa.app.Helper;
import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.SingleEventLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * In this activity a user can enter his name and password to login or click a
 * button to logout.
 * 
 * @author Christiane Kuhn, Martin Rabe, Robin Muench
 */
public class LoginActivity extends Activity {
    ServerConnection scon = ServerConnection.getInstance();
    int fail;
    TextView title;
    TextView user;
    TextView passwordtext;
    EditText name;
    EditText pw;
    Button loginButton;
    Button logoutButton;
    String userName;
    TextView currentUserName;
    boolean firstTime;

    /**
     * Shows Layout and depending on whether the user is logged in or not the
     * logout- oder login-button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);
	Intent intent = getIntent();
	firstTime = false;
	fail = intent.getIntExtra("failed", 0);
	title = (TextView) findViewById(R.id.login_title);
	user = (TextView) findViewById(R.id.login_username);
	passwordtext = (TextView) findViewById(R.id.login_pw);
	currentUserName = (TextView) findViewById(R.id.login_current_username);
	name = (EditText) findViewById(R.id.login_usernameField);
	pw = (EditText) findViewById(R.id.login_passwordField);
	loginButton = (Button) findViewById(R.id.login_login_button);
	logoutButton = (Button) findViewById(R.id.login_logout_button);
	Button changeUser = (Button) findViewById(R.id.login_changeUser_button);
	if (scon.getConnected()) {
	    loginButton.setVisibility(View.GONE);
	    user.setVisibility(View.GONE);
	    passwordtext.setVisibility(View.GONE);
	    name.setVisibility(View.GONE);
	    currentUserName.setVisibility(View.GONE);
	    pw.setVisibility(View.GONE);
	    changeUser.setVisibility(View.GONE);
	    logoutButton.setVisibility(View.VISIBLE);
	    title.setText(getString(R.string.title_logout));
	} else {
	    logoutButton.setVisibility(View.GONE);
	    loginButton.setVisibility(View.VISIBLE);
	    title.setText(getString(R.string.title_login));
	}

	Storage S = null;
	S = Storage.getInstance(this.getApplicationContext());

	SettingsLocal settings = null;

	try {
	    settings = S.load();
	} catch (NoStorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	String actualUser = settings.getUserName();
	if (actualUser == "empty") {
	    changeUser.setVisibility(View.GONE);
	    firstTime = true;
	} else {
	    currentUserName.setText(actualUser);
	    name.setVisibility(View.GONE);
	}

    }

    /**
     * Lead to MainActivity if PopUp.loginFail happened before.
     */
    @Override
    public void onBackPressed() {
	if (fail == 1) {
	    finish();
	    Intent intentMain = new Intent(LoginActivity.this,
		    MainActivity.class);
	    LoginActivity.this.startActivity(intentMain);
	} else {
	    finish();
	}
    }

    /**
     * Shows the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;

    }

    /**
     * Is used if LoginButton is clicked. Calls the ServerConnection.login
     * method and logs in the user.If it was successful switches back to
     * MainActivity and changes layout of Login before, so that its correct if
     * the backbutton is pressed.
     * 
     * @param view
     */
    public void onLoginButtonClick(View view) {
	Storage storage = null;
	storage = Storage.getInstance(null);
	SettingsLocal settings = null;
	try {
	    settings = storage.load();
	} catch (NoStorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (firstTime) {
	    userName = name.getText().toString();

	    settings.setUserName(userName);
	    storage.store(settings);
	}

	// A Loading Screen or something similar would be nice. Otherwise when
	// you click during load the app crashes.
	Toast toast = Toast.makeText(LoginActivity.this,
		getString(R.string.wait), Toast.LENGTH_SHORT);
	toast.show();

	// TODO check if already logged in
	if (!scon.getConnected()) {

	    String password = "";

	    // read userName and password from respective textEdit
	    EditText name = (EditText) findViewById(R.id.login_usernameField);
	    EditText pw = (EditText) findViewById(R.id.login_passwordField);
	    password = pw.getText().toString();
	    userName = settings.getUserName();

	    boolean success = false;

	    try {
		success = scon.login(userName, password,
			getApplicationContext());
	    } catch (ClientProtocolException e) {
		PopUp.alert(this, getString(R.string.cp_ex), e.getMessage());
		// e.printStackTrace();
	    } catch (IOException e) {
		PopUp.alert(this, getString(R.string.io_ex), e.getMessage());
		// e.printStackTrace();
	    }

	    if (success) {

		// TODO only if notificationKind 'none' or 'gcm-manu'/'gmc-auto'
		// and a gcm message with settings update notification or first
		// time
		SettingsLocal settingsLocal = null;

		try {
		    settingsLocal = scon.getSettings();
		} catch (ClientProtocolException e) {
		    PopUp.alert(this, getString(R.string.cp_ex), e.getMessage());
		    // e.printStackTrace();
		} catch (APIException e) {
		    PopUp.alert(this, getString(R.string.api_ex),
			    e.getMessage());
		    // e.printStackTrace();
		} catch (PermissionException e) {
		    PopUp.alert(this, getString(R.string.per_ex),
			    e.getMessage());
		    // e.printStackTrace();
		} catch (RequestNotPracticableException e) {
		    PopUp.alert(this, getString(R.string.rnp_ex),
			    e.getMessage());
		    // e.printStackTrace();
		} catch (InternalErrorException e) {
		    PopUp.alert(this, getString(R.string.ie_ex), e.getMessage());
		    // e.printStackTrace();
		} catch (IOException e) {
		    PopUp.alert(this, getString(R.string.io_ex), e.getMessage());
		    // e.printStackTrace();
		} catch (NoStorageException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		if (settingsLocal != null) {
		    Storage storage1 = null;
		    storage1 = Storage.getInstance(null);

		    settingsLocal.setUserName(userName);
		    storage1.store(settingsLocal);

		    Date date = null;
		    date = settingsLocal.getLastUpdate();

		    try {
			success = Helper.getUpdate(date, LoginActivity.this);
		    } catch (ClientProtocolException e) {
			PopUp.alert(this, getString(R.string.cp_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (APIException e) {
			PopUp.alert(this, getString(R.string.api_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (PermissionException e) {
			PopUp.alert(this, getString(R.string.per_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (RequestNotPracticableException e) {
			PopUp.alert(this, getString(R.string.rnp_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (InternalErrorException e) {
			PopUp.alert(this, getString(R.string.ie_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (IOException e) {
			PopUp.alert(this, getString(R.string.io_ex),
				e.getMessage());
			// e.printStackTrace();
		    } catch (NoStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}

		finish();

		Intent intentMain = new Intent(LoginActivity.this,
			MainActivity.class);
		LoginActivity.this.startActivity(intentMain);

	    } else {
		PopUp.alert(this, getString(R.string.login_error), "" /* TODO */);
	    }
	}
    }

    /**
     * Is used if LogoutButton is clicked. Calls the ServerConnection.logout
     * method and logs out the user. If it was successful switches back to
     * MainActivity and changes layout of Login before, so that its correct if
     * the backbutton is pressed.
     * 
     * @param view
     */
    public void onLogoutButtonClick(View view) {

	boolean success;
	success = scon.logout();

	if (success) {
	    Toast toast = Toast.makeText(LoginActivity.this,
		    getString(R.string.logged_out), Toast.LENGTH_SHORT);
	    toast.show();

	    finish();

	    Intent intentMain = new Intent(LoginActivity.this,
		    MainActivity.class);
	    LoginActivity.this.startActivity(intentMain);

	} else {
	    PopUp.alert(this.getApplicationContext(),
		    getString(R.string.sorry), getString(R.string.wrong));
	}

    }

    /**
     * Handles clicks on a menu-item and switches to other activity, depending
     * on which item was clicked.
     */
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

    /**
     * Is used if ChangeUserButton is clicked. Does the necessary steps to
     * change the user if its possible. Informs the user about the other
     * necessary steps.
     * 
     * @param view
     */
    public void onChangeUserButtonClick(View view) {

	AlertDialog.Builder alert = new AlertDialog.Builder(this);

	alert.setTitle(getString(R.string.attention));
	alert.setMessage(getString(R.string.userchanged));

	// Set an EditText view to get user input
	final EditText input = new EditText(this);
	alert.setView(input);

	alert.setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.

		    }
		});

	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		SettingsLocal settings = null;
		Storage storage = null;
		storage = Storage.getInstance(null);

		try {
		    settings = storage.load();
		} catch (NoStorageException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		userName = input.getText().toString();
		// TODO store in SettingsLocal
		storage.store(settings);

		currentUserName.setText(userName);
		name.setVisibility(View.GONE);

	    }
	});

	alert.show();

    }
}
