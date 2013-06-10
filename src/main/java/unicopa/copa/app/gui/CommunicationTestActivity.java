package unicopa.copa.app.gui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import unicopa.copa.app.R;
import unicopa.copa.app.ServerConnection;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class CommunicationTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.comm);
	Intent intent = getIntent();
	String key = intent.getStringExtra("key");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.no_item_menu, menu);
	return true;
    }

    public void onTestButtonClick(View view) {
	// TODO check instance exist
	ServerConnection scon = ServerConnection.getInstance();

	String answer = "empty";

	scon.setUrl("https://copa.prakinf.tu-ilmenau.de:443/my-webapp-auth/hello/Hello");

	try {
	    answer = scon.sendToServerTest();
	} catch (ClientProtocolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	TextView textViewAnswer = (TextView) super.findViewById(R.id.answer);
	textViewAnswer.setText(answer);
    }
}
