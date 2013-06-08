package unicopa.copa.app;

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

	answer = scon.sendToServer();

	TextView textViewAnswer = (TextView) super.findViewById(R.id.answer);
	textViewAnswer.setText(answer);
    }
}
