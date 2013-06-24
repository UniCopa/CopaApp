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
import unicopa.copa.app.SettingsLocal;
import unicopa.copa.app.Storage;
import unicopa.copa.app.exceptions.NoStorageException;
import unicopa.copa.base.UserEventSettings;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * This is a helper class for the colordialog. With it a user will pick the
 * color for an event.
 * 
 * @author Christiane Kuhn, Martin Rabe
 */
public class ColorDialog extends DialogFragment {

    /**
     * This method creates a colorDialog. The dailog offers different colors and
     * it saves the picked color in SettingsLocal and sends the new userSettings
     * to the server. After saving a color the updated SubscriptionActivity is
     * shown.
     * 
     * @param savedInstanceState
     * @param context
     * @param eventId
     * @return
     */
    public Dialog onCreateDialog(Bundle savedInstanceState,
	    final Context context, final int eventId) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle(R.string.pick_color).setItems(R.array.colors_array,
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			String color;
			switch (which) {
			case 0:// blue
			    color = "6495ED";
			    break;
			case 1:// red
			    color = "B22222";
			    break;
			case 2:// yellow
			    color = "FFA500";
			    break;
			case 3:// pink
			    color = "FF1493";
			    break;
			case 4:// green
			    color = "9ACD32";
			    break;
			case 5:// orange
			    color = "FF8C00";
			    break;
			case 6:// purple
			    color = "5C1B72";
			    break;
			default:// black
			    color = "000000";
			    break;
			}
			Storage S = null;
			S = Storage.getInstance(context);

			SettingsLocal settings = null;

			try {
			    settings = S.load();
			} catch (NoStorageException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

			UserEventSettings evSettings = settings
				.getEventSettings(eventId);

			if (evSettings != null) {
			    evSettings.setColorCode(color);
			} else {
			    settings.putEventSettings(eventId,
				    new UserEventSettings(color));
			}

			ServerConnection scon = null;
			scon = ServerConnection.getInstance();

			// TODO context is missing in popups
			boolean success = false;
			try {
			    success = scon.setSettings(settings);
			} catch (ClientProtocolException e) {
			    PopUp.exceptionAlert(context,
				    getString(R.string.cp_ex), e.getMessage());
			    // e.printStackTrace();
			} catch (APIException e) {
			    PopUp.exceptionAlert(context,
				    getString(R.string.api_ex), e.getMessage());
			    // e.printStackTrace();
			} catch (PermissionException e) {
			    PopUp.exceptionAlert(context,
				    getString(R.string.per_ex), e.getMessage());
			    // e.printStackTrace();
			} catch (RequestNotPracticableException e) {
			    PopUp.exceptionAlert(context,
				    getString(R.string.rnp_ex), e.getMessage());
			    // e.printStackTrace();
			} catch (InternalErrorException e) {
			    PopUp.exceptionAlert(context,
				    getString(R.string.ie_ex), e.getMessage());
			    // e.printStackTrace();
			} catch (IOException e) {
			    PopUp.exceptionAlert(context,
				    getString(R.string.io_ex), e.getMessage());
			    // e.printStackTrace();
			}

			if (success) {
			    S.store(settings);
			} else {
			    // TODO error popup
			}

			Intent intentSubscription = new Intent(context,
				SubscriptionActivity.class);
			context.startActivity(intentSubscription);
		    }
		});
	return builder.create();
    }
}
