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

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * This class implements the Android SharedPreferences to store Settings.
 * 
 * @author Robin Muench, Martin Rabe
 */

public class Storage {

    private static Storage instance;

    private Context context;

    public static Storage getInstance(Context context) {
	if (instance == null) {
	    instance = new Storage(context);
	}

	return instance;
    }

    private Storage(Context context) {
	this.context = context;
    }

    public void store(SettingsLocal sLoc) {
	    Gson gson = new Gson();
	    SharedPreferences appSharedPrefs = context.getSharedPreferences(
		    "Settings", 0);
	    Editor prefsEditor = appSharedPrefs.edit();
	    String json = gson.toJson(sLoc);
	    prefsEditor.putString("SettingsLocalObject", json);
	    prefsEditor.commit();
    }

    public SettingsLocal load() {
	SharedPreferences appSharedPrefs = context.getSharedPreferences(
		"Settings", 0);
	Gson gson = new Gson();
	String json = appSharedPrefs.getString("SettingsLocalObject", "empty");
	Log.w("JSON String",json);
	if(json == "empty") return null;
	else
	return gson.fromJson(json,SettingsLocal.class);
    }

    public void deleteSettings() {
	context.getSharedPreferences("Settings", 0).edit().clear().commit();
    }
}
