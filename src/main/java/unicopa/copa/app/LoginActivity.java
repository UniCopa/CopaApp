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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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
	ServerConnection scon = ServerConnection.getInstance();
	scon.setUrl("https://copa.prakinf.tu-ilmenau.de:443/my-webapp-auth/j_security_check");

	// TODO useName and password empty
	String userName = "me";
	String password = "me";

	// TODO read userName and password from respective textEdit

	Log.v("User Name:", userName);
	Log.v("Password:", password);

	// A Loading Screen or something similar would be nice. Otherwise when
	// you click during load the app crashes.

	if (scon.login(userName, password, getApplicationContext())) {
	    Intent intentComm = new Intent(LoginActivity.this,
		    CommunicationTestActivity.class);
	    intentComm.putExtra("key", "value");
	    LoginActivity.this.startActivity(intentComm);
	}
    }
}
