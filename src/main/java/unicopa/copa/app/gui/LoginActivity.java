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
import android.util.Log;
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
     * Is used if LoginButton is clicked. Switches back to MainActivity.
     * 
     * @param view
     */
    public void onLoginButtonClick(View view) {
	Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
	LoginActivity.this.startActivity(intentMain);
    }

    public void onCommTestButtonClick(View view) {

	// A Loading Screen or something similar would be nice. Otherwise when
	// you click during load the app crashes.
	Toast toast = Toast.makeText(LoginActivity.this, "Please wait.",
		Toast.LENGTH_SHORT);
	toast.show();

	ServerConnection scon = ServerConnection.getInstance();

	// TODO check if already logged in
	if (!scon.getConnected()) {
	    scon.setUrl("https://copa.prakinf.tu-ilmenau.de:443/my-webapp-auth/j_security_check");

	    // read userName and password from respective textEdit
	    String userName = "";
	    String password = "";

	    EditText name = (EditText) findViewById(R.id.usernameField);
	    userName = name.getText().toString();

	    EditText pw = (EditText) findViewById(R.id.passwordField);
	    password = pw.getText().toString();

	    Log.v("User Name:", userName);
	    Log.v("Password:", password);

	    try {
		if (!scon.login(userName, password, getApplicationContext())) {
		// TODO feedback if login fails
		}
	    } catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	Intent intentComm = new Intent(LoginActivity.this,
		CommunicationTestActivity.class);
	intentComm.putExtra("key", "value");
	LoginActivity.this.startActivity(intentComm);

    }
}
