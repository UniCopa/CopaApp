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

import unicopa.copa.app.gui.MainActivity;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Handling of GCM messages.
 * 
 * @author Martin Rabe
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {

    static final String TAG = "GCMDemo";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    // NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
	// GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
	ctx = context;
	// String messageType = gcm.getMessageType(intent);
	// if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
	// {
	// sendNotification("Send error: " + intent.getExtras().toString());
	// } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
	// .equals(messageType)) {
	// sendNotification("Deleted messages on server: "
	// + intent.getExtras().toString());
	// } else {
	// sendNotification("Received: " + intent.getExtras().toString());
	// }
	setResultCode(Activity.RESULT_OK);
    }

    // Put the GCM message into a notification and post it.
    private void sendNotification(String msg) {
	mNotificationManager = (NotificationManager) ctx
		.getSystemService(Context.NOTIFICATION_SERVICE);

	PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
		new Intent(ctx, MainActivity.class), 0);

	// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
	// ctx).setSmallIcon(R.drawable.ic_stat_gcm)
	// .setContentTitle("GCM Notification")
	// .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
	// .setContentText(msg);

	// mBuilder.setContentIntent(contentIntent);
	// mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
