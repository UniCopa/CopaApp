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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

/**
 * This singleton class manages the connection to the server.
 * 
 * @author Martin Rabe
 */
public class ServerConnection {
    private boolean m_connected = false;
    // private String m_gcmKey = "";

    private String value = "";
    private DefaultHttpClient client = null;

    // TODO URL needs to be read from configuration file file or settings not hard coded
    private String m_url = "";

    private static ServerConnection m_instance;

    public static ServerConnection getInstance() {
	if (m_instance == null) {
	    m_instance = new ServerConnection();
	}

	return m_instance;
    }

    // private ServerConnection() {
    // }

    public void setUrl(String url) {
	m_url = url;
    }

    // public String getUrl(String url) {
    // return m_url;
    // }

    public void setConnected(boolean connected) {
	m_connected = connected;
    }

    public boolean getConnected() {
	return m_connected;
    }

    // public void setGCMKey(String gcmKey) {
    // m_gcmKey = gcmKey;
    // }

    // public String getGCMKey() {
    // return m_gcmKey;
    // }

    /**
     * This Method opens connection to the server and saves cookie.
     */
    public boolean login(String userName, String password, Context context) {
	// TODO getApplicationContext information needed
	client = new CoPAAppHttpClient(context);

	// TODO check this
	// redirect
	HttpParams params = client.getParams();
	HttpClientParams.setRedirecting(params, false);

	HttpPost loginMsg = new HttpPost(m_url);

	// login data
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.add(new BasicNameValuePair("j_username", userName));
	nameValuePairs.add(new BasicNameValuePair("j_password", password));
	try {
	    loginMsg.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	} catch (UnsupportedEncodingException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// lend login data
	HttpResponse response = null;
	try {
	    response = client.execute(loginMsg);
	} catch (ClientProtocolException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// handle response
	InputStreamReader reader = null;
	try {
	    reader = new InputStreamReader(response.getEntity().getContent());
	} catch (IllegalStateException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	BufferedReader rd = new BufferedReader(reader);
	List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore()
		.getCookies();

	if (cookies.isEmpty()) {
	    Log.w("List<cookies>:", "is empty");
	} else {
	    value = cookies.get(0).getValue().toString();
	}

	// cleaning
	try {
	    rd.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try {
	    reader.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// return
	if (cookies.isEmpty()) {
	    setConnected(false);
	    return false;
	} else {
	    setConnected(true);
	    return true;
	}
    }

    public boolean logout(String userName) {
	return true;
    }

    /**
     * This Method is just for the Communication Test filled with this content.
     */
    public String sendToServer(/* String jsonRequest */) {
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.clear();
	nameValuePairs.add(new BasicNameValuePair("req", "ISENTTOCOPATHIS"));
	nameValuePairs.add(new BasicNameValuePair("JSESSIONID", value));

	HttpPost post = new HttpPost(m_url);

	try {
	    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	} catch (UnsupportedEncodingException e3) {
	    // TODO Auto-generated catch block
	    e3.printStackTrace();
	}

	HttpResponse response = null;

	try {
	    response = client.execute(post);
	} catch (ClientProtocolException e3) {
	    // TODO Auto-generated catch block
	    e3.printStackTrace();
	} catch (IOException e3) {
	    // TODO Auto-generated catch block
	    e3.printStackTrace();
	}

	// TODO check this
	// TODO do something with test
	// check for http 302 (fail) or 200 (ok)
	String test = response.getStatusLine().toString();

	// Log.v("Site available:", test);

	// Log.v("StatusLIne", test);

	BufferedReader rd2 = null;

	try {
	    rd2 = new BufferedReader(new InputStreamReader(response.getEntity()
		    .getContent()));
	} catch (IllegalStateException e2) {
	    // TODO Auto-generated catch block
	    e2.printStackTrace();
	} catch (IOException e2) {
	    // TODO Auto-generated catch block
	    e2.printStackTrace();
	}

	String line = "";
	String temp = "";
	try {
	    while ((line = rd2.readLine()) != null) {
		temp = line;
	    }
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	try {
	    rd2.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return temp;
    }

    public void openConnection() {
    }

    public boolean connectionCheck() {
	return true;
    }

    public String sendGCMKey() {
	return "";
    }

    public void closeConnection() {
    }
}