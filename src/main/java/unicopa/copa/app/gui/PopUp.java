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

import unicopa.copa.app.R;
import unicopa.copa.app.Storage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * This is a helper class for dialogs and alerts.
 * 
 * @author Martin Rabe, Christiane Kuhn
 */
public class PopUp {

    /**
     * Creates a dialog that tells the user that he is not logged in and starts
     * the LoginActivity.
     * 
     * @param context
     */
    public static void loginFail(final Context context) {
	new AlertDialog.Builder(context).setTitle(R.string.login_ex)
		.setMessage(R.string.not_login)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {

			((Activity) context).finish();

			Intent intentLog = new Intent(context,
				LoginActivity.class);
			intentLog.putExtra("failed", 1);
			context.startActivity(intentLog);

		    }
		}).show();

    }

    /**
     * Creates a dialog that tells the user that he has unsubscribed an event.
     * 
     * @param context
     */
    public static void unsubscribed(final Context context) {
	new AlertDialog.Builder(context).setTitle(R.string.success)
		.setMessage(R.string.unsubscribed)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {

			((Activity) context).finish();

			Intent intentSubscription = new Intent(context,
				SubscriptionActivity.class);
			context.startActivity(intentSubscription);

		    }
		}).show();
    }

    /**
     * Creates an alert dialog with one button labeled 'OK'.
     * 
     * @param context
     * @param title
     * @param msg
     */
    public static void alert(Context context, String title, String msg) {
	new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			// do nothing
		    }
		}).show();
    }

    /**
     * Creates an dialog to inform the user about data protection/usage with two
     * buttons labeled 'I accept.' and 'No!'.
     * 
     * @param context
     */
    public static void firstAlert(final Context context) {
	new AlertDialog.Builder(context)
		.setTitle(R.string.accept_title)
		.setMessage(R.string.accept_msg)
		.setPositiveButton(R.string.accept_no,
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int which) {
				Storage storage = null;
				storage = Storage.getInstance(context);
				storage.deleteSettings();

				((Activity) context).finish();
				System.exit(0);
			    }
			})
		.setNegativeButton(R.string.accept,
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int which) {
				// do nothing
			    }
			}).show();
    }
}
