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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * This is a helper class for dialogs and alerts.
 * 
 * @author Martin Rabe
 */
public class PopUp {

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
     * Creates an alert dialog with two buttons labeled 'Retry' and 'Cancel'.
     * 
     * @param context
     * @param title
     * @param msg
     */
    public static void exceptionAlert(Context context, String title, String msg) {
	new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(msg)
		.setPositiveButton("Retry",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int which) {
				// TODO retry
			    }
			})
		.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int which) {
				// do nothing
			    }
			}).show();
    }

}
