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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * This is a helper class for the colordialog.
 * 
 * @author Christiane Kuhn
 */
public class ColorDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState, Context context) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle(R.string.pick_color).setItems(R.array.colors_array,
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			// The 'which' argument contains the index position
			// of the selected item
		    }
		});
	return builder.create();
    }
}
