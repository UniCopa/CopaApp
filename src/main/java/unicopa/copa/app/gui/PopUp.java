package unicopa.copa.app.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PopUp {

    public static void alert(Context context, String title, String msg) {
	new AlertDialog.Builder(context)
	.setTitle(title)
	.setMessage(msg)
	.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
		// do nothing
	    }
	}).show();
    }
    
    public static void exceptionAlert(Context context, String title, String msg) {
	new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(msg)
//		.setPositiveButton("Retry",
//			new DialogInterface.OnClickListener() {
//			    public void onClick(DialogInterface dialog,
//				    int which) {
//				// TODO retry
//			    }
//			})
//		.setNegativeButton("Cancel",
//			new DialogInterface.OnClickListener() {
//			    public void onClick(DialogInterface dialog,
//				    int which) {
//				// do nothing
//			    }
//			})
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			// do nothing
		    }
		}).show();
    }

}
