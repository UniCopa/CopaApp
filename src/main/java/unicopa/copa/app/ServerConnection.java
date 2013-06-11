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

import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.com.request.GetSingleEventRequest;
import unicopa.copa.base.com.request.GetSingleEventResponse;
import unicopa.copa.base.com.serialization.ClientSerializer;
import unicopa.copa.base.event.SingleEvent;

import android.content.Context;
import android.util.Log;

/**
 * This singleton class manages the connection to the server.
 * 
 * @author Martin Rabe
 */
public class ServerConnection {
    private static ServerConnection m_instance;

    private boolean m_connected = false;
    // private String m_gcmKey = "";
    private String m_sessionID = "";
    private DefaultHttpClient client = null;
    private String m_url = "";

    public static ServerConnection getInstance() {
	if (m_instance == null) {
	    m_instance = new ServerConnection();
	}

	return m_instance;
    }

    private ServerConnection() {
    }

    /**
     * This Method sends a json String to the server and returns the answer as a
     * String.
     * 
     * @param requestType
     * @param request
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    private String sendToServer(String requestType, String requestObject)
	    throws ClientProtocolException, IOException {
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.clear();
	nameValuePairs.add(new BasicNameValuePair("type", requestType));
	nameValuePairs.add(new BasicNameValuePair("req", requestObject));
	nameValuePairs.add(new BasicNameValuePair("JSESSIONID", m_sessionID));

	HttpPost post = new HttpPost(m_url);

	post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	HttpResponse response = null;

	response = client.execute(post);

	// TODO check this
	// TODO do something with test
	// check for http 302 (fail) or 200 (ok)
	String test = response.getStatusLine().toString();

	// Log.v("Site available:", test);
	// Log.v("StatusLIne", test);

	BufferedReader rd = null;

	rd = new BufferedReader(new InputStreamReader(response.getEntity()
		.getContent()));

	String line = "";
	String temp = "";
	while ((line = rd.readLine()) != null) {
	    temp = line;
	}
	rd.close();

	return temp;
    }

    // TODO URL needs to be read from configuration file or settings
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
     * This Method opens connection to the server and saves the session cookie.
     * 
     * @param userName
     * @param password
     * @param context
     * @return success
     * @throws IOException
     * @throws ClientProtocolException
     */
    public boolean login(String userName, String password, Context context)
	    throws ClientProtocolException, IOException {
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
	loginMsg.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	// lend login data
	HttpResponse response = null;
	response = client.execute(loginMsg);

	// handle response
	InputStreamReader reader = null;
	reader = new InputStreamReader(response.getEntity().getContent());
	BufferedReader rd = new BufferedReader(reader);
	List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore()
		.getCookies();

	if (cookies.isEmpty()) {
	    Log.w("List<cookies>:", "is empty");
	} else {
	    m_sessionID = cookies.get(0).getValue().toString();
	}

	// cleaning
	rd.close();
	reader.close();

	// return
	if (cookies.isEmpty()) {
	    setConnected(false);
	    return false;
	} else {
	    setConnected(true);
	    return true;
	}
    }
/**
 * This method erases the session cookie and tells the server to invalidate the session.
 * 
 * @return success
 */
    public boolean logout() {
	// TODO get on https://copa.prakinf.tu-ilmenau.de/logout.jsp
	// TODO erase the session cookie and sessionID
	// TODO set m_connected to false
	
	return true;
    }

    /**
     * This Method return to a given singleEventID a SingleEvent.
     * 
     * @param eventID
     * @return SingleEvent
     * @throws InternalErrorException
     * @throws RequestNotPracticableException
     * @throws PermissionException
     * @throws APIException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public SingleEvent GetSingleEvent(int singleEventID) throws APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException, ClientProtocolException, IOException {
	GetSingleEventRequest reqObj = new GetSingleEventRequest(singleEventID);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer("GetSingleEventRequest", reqStr);

	GetSingleEventResponse resObj = null;
	resObj = (GetSingleEventResponse) ClientSerializer
		.deserializeResponse(resStr);

	return resObj.getSingleEvent();
    }

    /**
     * This Method is just for the Communication Test only.
     * 
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String sendToServerTest() throws ClientProtocolException,
	    IOException {
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.clear();
	nameValuePairs.add(new BasicNameValuePair("req", "ISENTTOCOPATHIS"));
	nameValuePairs.add(new BasicNameValuePair("JSESSIONID", m_sessionID));

	HttpPost post = new HttpPost(m_url);

	post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	HttpResponse response = null;

	response = client.execute(post);

	// TODO check this
	// TODO do something with test
	// check for http 302 (fail) or 200 (ok)
	String test = response.getStatusLine().toString();

	// Log.v("Site available:", test);

	// Log.v("StatusLIne", test);

	BufferedReader rd2 = null;

	rd2 = new BufferedReader(new InputStreamReader(response.getEntity()
		.getContent()));

	String line = "";
	String temp = "";
	while ((line = rd2.readLine()) != null) {
	    temp = line;
	}
	rd2.close();

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