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

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * In this activity a user can enter his name and password.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;
    }

    /**
     * Is used if LoginButton is clicked. Calls the CerverConnection.login
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

	ServerConnection scon = ServerConnection.getInstance();

	// TODO check if already logged in
	if (!scon.getConnected()) {
	    scon.setUrl("https://copa.prakinf.tu-ilmenau.de:443/j_security_check");

	    // read userName and password from respective textEdit
	    String userName = "";
	    String password = "";

	    EditText name = (EditText) findViewById(R.id.usernameField);
	    userName = name.getText().toString();

	    EditText pw = (EditText) findViewById(R.id.passwordField);
	    password = pw.getText().toString();

	    // TODO should only switch activity when login successful, but does it anyway
	    try {
		if (scon.login(userName, password, getApplicationContext())) {
		    Intent intentMain = new Intent(LoginActivity.this,
			    MainActivity.class);
		    LoginActivity.this.startActivity(intentMain);
		} else {
		    Toast toast2 = Toast.makeText(LoginActivity.this,
			    "Login Error!", Toast.LENGTH_LONG);
		    toast2.show();
		}
	    } catch (ClientProtocolException e) {
		Toast toast2 = Toast.makeText(LoginActivity.this,
			"ClientProtocolException!\r\n" + e.getMessage(),
			Toast.LENGTH_LONG);
		toast2.show();
		// e.printStackTrace();
	    } catch (IOException e) {
		Toast toast2 = Toast.makeText(LoginActivity.this,
			"IOException!\r\n" + e.getMessage(), Toast.LENGTH_LONG);
		toast2.show();
		// e.printStackTrace();
	    }
	}
    }

    public void onCommTestButtonClick(View view) {
	Toast toast = Toast.makeText(LoginActivity.this, "Please wait.",
		Toast.LENGTH_SHORT);
	toast.show();

	ServerConnection scon = ServerConnection.getInstance();

	if (!scon.getConnected()) {
	    scon.setUrl("https://copa.prakinf.tu-ilmenau.de:443/my-webapp-auth/j_security_check");

	    String userName = "";
	    String password = "";

	    EditText name = (EditText) findViewById(R.id.usernameField);
	    userName = name.getText().toString();

	    EditText pw = (EditText) findViewById(R.id.passwordField);
	    password = pw.getText().toString();

	    try {
		if (!scon.login(userName, password, getApplicationContext())) {
		}
	    } catch (ClientProtocolException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	Intent intentComm = new Intent(LoginActivity.this,
		CommunicationTestActivity.class);
	intentComm.putExtra("key", "value");
	LoginActivity.this.startActivity(intentComm);
    }

}
