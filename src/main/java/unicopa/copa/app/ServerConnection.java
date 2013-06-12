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
import java.util.ArrayList;
import java.util.Date;
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

import unicopa.copa.base.UserSettings;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.com.request.GetAllOwnersRequest;
import unicopa.copa.base.com.request.GetAllOwnersResponse;
import unicopa.copa.base.com.request.GetCategoryRequest;
import unicopa.copa.base.com.request.GetCategoryResponse;
import unicopa.copa.base.com.request.GetSingleEventRequest;
import unicopa.copa.base.com.request.GetSingleEventResponse;
import unicopa.copa.base.com.request.GetSubscribedSingleEventUpdatesRequest;
import unicopa.copa.base.com.request.GetSubscribedSingleEventUpdatesResponse;
import unicopa.copa.base.com.request.GetUserSettingsRequest;
import unicopa.copa.base.com.request.GetUserSettingsResponse;
import unicopa.copa.base.com.serialization.ClientSerializer;
import unicopa.copa.base.event.CategoryNode;
import unicopa.copa.base.event.SingleEvent;
import unicopa.copa.base.event.SingleEventUpdate;

import android.content.Context;
import android.util.Log;

/**
 * This singleton class manages the connection with the server.
 * 
 * @author Martin Rabe
 */
public class ServerConnection {

    private static ServerConnection m_instance;
    private boolean m_connected = false;
    // private String m_gcmKey = "";
    private String m_sessionID = "";
    private String m_url = "";
    private DefaultHttpClient client = null;

    /**
     * This method is called to get an instance of ServerConnection.
     * 
     * @return
     */
    public static ServerConnection getInstance() {
	if (m_instance == null) {
	    m_instance = new ServerConnection();
	}

	return m_instance;
    }

    /**
     * Private constructor of the ServerConnection class.
     */
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
	List<NameValuePair> nameValuePairsMsg = new ArrayList<NameValuePair>(1);

	nameValuePairsMsg.clear();

	nameValuePairsMsg.add(new BasicNameValuePair("req", requestObject));
	nameValuePairsMsg
		.add(new BasicNameValuePair("JSESSIONID", m_sessionID));

	Log.v("REQUEST:", nameValuePairsMsg.toString());
	Log.v("URL", this.getUrl());

	HttpPost post = new HttpPost(m_url);
	post.setEntity(new UrlEncodedFormEntity(nameValuePairsMsg));

	HttpResponse response = null;
	response = client.execute(post);

	// Log.v("SITE:", response.toString());
	// Log.v("SITE AVAILABLE:", response.getStatusLine().toString());

	BufferedReader rd = null;

	rd = new BufferedReader(new InputStreamReader(response.getEntity()
		.getContent()));

	String line = "";
	String temp = "";

	while ((line = rd.readLine()) != null) {
	    temp = line;
	}

	rd.close();

	Log.v("RESPONSE:", temp);

	return temp;
    }

    // TODO URL needs to be read from configuration file or settings
    public void setUrl(String url) {
	m_url = url;
    }

    public String getUrl() {
	return m_url;
    }

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
	// TODO move this to constructor
	client = new CoPAAppHttpClient(context);

	// TODO check this
	// redirect
	HttpParams params = client.getParams();
	HttpClientParams.setRedirecting(params, false);

	Log.v("URL", this.getUrl());
	HttpPost loginMsg = new HttpPost(m_url);

	// login data
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.add(new BasicNameValuePair("j_username", userName));
	nameValuePairs.add(new BasicNameValuePair("j_password", password));
	loginMsg.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	Log.v("LOGINMSG", nameValuePairs.toString());

	// lend login data
	HttpResponse response = null;
	response = client.execute(loginMsg);

	// Log.v("SITE", response.toString());
	// Log.v("SITE AVAILABLE", response.getStatusLine().toString());

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

	Log.v("SESSIONID:", m_sessionID);

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
     * This method erases the session cookie and tells the server to invalidate
     * the session.
     * 
     * @return success
     */
    // TODO remove this
    public boolean logout() {
	// TODO get on https://copa.prakinf.tu-ilmenau.de/logout.jsp
	// TODO erase the session cookie and sessionID
	if (true) {
	    this.setConnected(false);

	    return true;
	} else {
	    return false;
	}
    }

    /**
     * This method returns to a given singleEventID a SingleEvent.
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
    public SingleEvent getSingleEvent(int singleEventID) throws APIException,
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

	if (resObj instanceof GetSingleEventResponse) {
	    return resObj.getSingleEvent();
	} else {
	    return null;
	}
    }

    /**
     * This method returns the category tree.
     * 
     * @return categoryNode
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public CategoryNode getCategory() throws ClientProtocolException,
	    IOException, APIException, PermissionException,
	    RequestNotPracticableException, InternalErrorException {
	GetCategoryRequest reqObj = new GetCategoryRequest();

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer("GetCategoryRequest", reqStr);

	GetCategoryResponse resObj = null;
	resObj = (GetCategoryResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetCategoryResponse) {
	    return resObj.getCategoryTree();
	} else {
	    return null;
	}
    }

    /**
     * This method returns to a given eventID all owners.
     * 
     * @param eventID
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public List<String> getOwners(int eventID) throws ClientProtocolException,
	    IOException, APIException, PermissionException,
	    RequestNotPracticableException, InternalErrorException {
	GetAllOwnersRequest reqObj = new GetAllOwnersRequest(eventID);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer("GetAllOwnersRequest", reqStr);

	GetAllOwnersResponse resObj = null;
	resObj = (GetAllOwnersResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetAllOwnersResponse) {
	    return resObj.getNames();
	} else {
	    return null;
	}
    }

    /**
     * This method returns all updates since a given date for subscribed
     * SingeEvents.
     * 
     * @param date
     * @return List<List<SingleEventUpdate>>
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public List<List<SingleEventUpdate>> getSubscribedSingleEventUpdates(
	    Date date) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	GetSubscribedSingleEventUpdatesRequest reqObj = new GetSubscribedSingleEventUpdatesRequest(
		date);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer("GetSubscribedSingleEventUpdatesRequest", reqStr);

	GetSubscribedSingleEventUpdatesResponse resObj = null;
	resObj = (GetSubscribedSingleEventUpdatesResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetSubscribedSingleEventUpdatesResponse) {
	    return resObj.getUpdates();
	} else {
	    return null;
	}
    }

    /**
     * This method returns the Settings.
     * 
     * @return UserSettings
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public UserSettings getSettings() throws ClientProtocolException,
	    IOException, APIException, PermissionException,
	    RequestNotPracticableException, InternalErrorException {
	GetUserSettingsRequest reqObj = new GetUserSettingsRequest();

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer("GetUserSettingsRequest", reqStr);

	GetUserSettingsResponse resObj = null;
	resObj = (GetUserSettingsResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetUserSettingsResponse) {
	    return resObj.getUserSettings();
	} else {
	    return null;
	}
    }

    // stub for requests, replace X with the appropriate names
    // public X get() {
    // GetXRequest reqObj = new GetXRequest();
    //
    // String reqStr = "";
    // reqStr = ClientSerializer.serialize(reqObj);
    //
    // String resStr = "";
    // resStr = sendToServer("GetXRequest", reqStr);
    //
    // GetXResponse resObj = null;
    // resObj = (GetXResponse) ClientSerializer.deserializeResponse(resStr);
    //
    // if (resObj instanceof GetXResponse) {
    // return resObj.getX;
    // } else {
    // return null;
    // }
    // }

    public boolean connectionCheck() {
	return true;
    }

    public String sendGCMKey() {
	return "";
    }

}
