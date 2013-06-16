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
import unicopa.copa.base.com.request.GetCategoriesRequest;
import unicopa.copa.base.com.request.GetCategoriesResponse;
import unicopa.copa.base.com.request.GetCurrentSingleEventsRequest;
import unicopa.copa.base.com.request.GetCurrentSingleEventsResponse;
import unicopa.copa.base.com.request.GetEventGroupRequest;
import unicopa.copa.base.com.request.GetEventGroupResponse;
import unicopa.copa.base.com.request.GetEventGroupsRequest;
import unicopa.copa.base.com.request.GetEventGroupsResponse;
import unicopa.copa.base.com.request.GetEventRequest;
import unicopa.copa.base.com.request.GetEventResponse;
import unicopa.copa.base.com.request.GetEventsRequest;
import unicopa.copa.base.com.request.GetEventsResponse;
import unicopa.copa.base.com.request.GetSingleEventRequest;
import unicopa.copa.base.com.request.GetSingleEventResponse;
import unicopa.copa.base.com.request.GetSingleEventUpdatesRequest;
import unicopa.copa.base.com.request.GetSingleEventUpdatesResponse;
import unicopa.copa.base.com.request.GetSubscribedSingleEventUpdatesRequest;
import unicopa.copa.base.com.request.GetSubscribedSingleEventUpdatesResponse;
import unicopa.copa.base.com.request.GetUserSettingsRequest;
import unicopa.copa.base.com.request.GetUserSettingsResponse;
import unicopa.copa.base.com.request.SetUserSettingsRequest;
import unicopa.copa.base.com.request.SetUserSettingsResponse;
import unicopa.copa.base.com.serialization.ClientSerializer;
import unicopa.copa.base.event.CategoryNode;
import unicopa.copa.base.event.Event;
import unicopa.copa.base.event.EventGroup;
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

    private static ServerConnection instance;
    private boolean connected = false;
    // private String gcmKey = "";
    private String sessionID = "";

    // TODO read urls from file
    private String loginUrl = "https://copa.prakinf.tu-ilmenau.de:443/j_security_check";
    private String requestUrl = "https://copa.prakinf.tu-ilmenau.de:443/service";

    private DefaultHttpClient client = null;

    /**
     * This method is called to get an instance of ServerConnection.
     * 
     * @return
     */
    public static ServerConnection getInstance() {
	if (instance == null) {
	    instance = new ServerConnection();
	}

	return instance;
    }

    /**
     * Private constructor of the ServerConnection class.
     */
    private ServerConnection() {
    }

    /**
     * This method sends a json String to the server and returns the answer as a
     * String.
     * 
     * @param requestType
     * @param request
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    private String sendToServer(String requestObject)
	    throws ClientProtocolException, IOException {
	List<NameValuePair> nameValuePairsMsg = new ArrayList<NameValuePair>(1);

	nameValuePairsMsg.clear();

	nameValuePairsMsg.add(new BasicNameValuePair("req", requestObject));
	nameValuePairsMsg.add(new BasicNameValuePair("JSESSIONID", sessionID));

	Log.v("REQUEST:", nameValuePairsMsg.toString());
	Log.v("URL", this.requestUrl);

	HttpPost post = new HttpPost(requestUrl);
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

    public void setConnected(boolean connected) {
	this.connected = connected;
    }

    public boolean getConnected() {
	return connected;
    }

    // login / logout

    /**
     * This method opens connection to the server and saves the session cookie.
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
	client = new CoPAAppHttpClient(context);

	// redirect
	HttpParams params = client.getParams();
	HttpClientParams.setRedirecting(params, false);

	Log.v("URL", this.loginUrl);
	HttpPost loginMsg = new HttpPost(loginUrl);

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

	if (!response.getFirstHeader("location").toString().substring(10, 46)
		.matches("https://copa.prakinf.tu-ilmenau.de/;")) {
	    setConnected(false);
	    return false;
	}

	// handle response
	InputStreamReader reader = null;
	reader = new InputStreamReader(response.getEntity().getContent());
	BufferedReader rd = new BufferedReader(reader);
	List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore()
		.getCookies();

	if (cookies.isEmpty()) {
	    Log.w("List<cookies>:", "is empty");
	} else {
	    sessionID = cookies.get(0).getValue().toString();
	}

	Log.v("SESSIONID:", sessionID);

	// cleaning
	rd.close();
	reader.close();

	setConnected(true);
	return true;
    }

    /**
     * This method erases the session cookie and tells the server to invalidate
     * the session.
     * 
     * @return success
     */
    public boolean logout() {
	// TODO get on https://copa.prakinf.tu-ilmenau.de/logout.jsp to
	// invalidate session on server side
	setConnected(false);

	sessionID = "";

	Log.v("SESSIONID", sessionID);
	
	return true;
    }

    // Server requests

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
	GetCategoriesRequest reqObj = new GetCategoriesRequest();

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetCategoriesResponse resObj = null;
	resObj = (GetCategoriesResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetCategoriesResponse) {
	    return resObj.getCategoryTree();
	} else {
	    return null;
	}
    }

    /**
     * This method returns for a given categoryID and searchTerm a list of
     * EventGroup.
     * 
     * @param categoryNodeID
     * @param searchTerm
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws InternalErrorException
     * @throws RequestNotPracticableException
     * @throws PermissionException
     * @throws APIException
     */
    public List<EventGroup> getEventGroups(int categoryNodeID, String searchTerm)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	GetEventGroupsRequest reqObj = new GetEventGroupsRequest(
		categoryNodeID, searchTerm);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetEventGroupsResponse resObj = null;
	resObj = (GetEventGroupsResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetEventGroupsResponse) {
	    return resObj.getEventGroupList();
	} else {
	    return null;
	}
    }

    /**
     * This method returns for a given eventGroupID and categoryID a list of
     * Event.
     * 
     * @param eventGroupID
     * @param categoryNodeID
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public List<Event> getEvents(int eventGroupID, int categoryNodeID)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	GetEventsRequest reqObj = new GetEventsRequest(eventGroupID,
		categoryNodeID);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetEventsResponse resObj = null;
	resObj = (GetEventsResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetEventsResponse) {
	    return resObj.getEventList();
	} else {
	    return null;
	}
    }

    /**
     * This method returns to a given eventID the current SingleEvents.
     * 
     * @param eventID
     * @param date
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public List<SingleEvent> getCurrentSingleEvents(int eventID, Date date)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	GetCurrentSingleEventsRequest reqObj = new GetCurrentSingleEventsRequest(
		eventID, date);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetCurrentSingleEventsResponse resObj = null;
	resObj = (GetCurrentSingleEventsResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetCurrentSingleEventsResponse) {
	    return resObj.getSingleEvents();
	} else {
	    return null;
	}
    }

    /**
     * This method returns for a given eventID a Event.
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
    public Event getEvent(int eventID) throws ClientProtocolException,
	    IOException, APIException, PermissionException,
	    RequestNotPracticableException, InternalErrorException {
	GetEventRequest reqObj = new GetEventRequest(eventID);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetEventResponse resObj = null;
	resObj = (GetEventResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetEventResponse) {
	    return resObj.getEvent();
	} else {
	    return null;
	}
    }

    /**
     * This method returns to a given eventGroupID a EventGroup.
     * 
     * @param eventID
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws InternalErrorException
     * @throws RequestNotPracticableException
     * @throws PermissionException
     * @throws APIException
     */
    public EventGroup getEventGroup(int eventGroupID)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	GetEventGroupRequest reqObj = new GetEventGroupRequest(eventGroupID);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetEventGroupResponse resObj = null;
	resObj = (GetEventGroupResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetEventGroupResponse) {
	    return resObj.getEventGroup();
	} else {
	    return null;
	}
    }

    // TODO do we need this?
    /**
     * This method returns to a given eventID all SingleEventUpdates since a
     * given date.
     * 
     * @param eventID
     * @param date
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public List<List<SingleEventUpdate>> getSingleEventUpdates(int eventID,
	    Date date) throws ClientProtocolException, IOException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	GetSingleEventUpdatesRequest reqObj = new GetSingleEventUpdatesRequest(
		eventID, date);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	GetSingleEventUpdatesResponse resObj = null;
	resObj = (GetSingleEventUpdatesResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetSingleEventUpdatesResponse) {
	    return resObj.getUpdateList();
	} else {
	    return null;
	}
    }

    // TODO do we need this?
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
	resStr = sendToServer(reqStr);

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
	resStr = sendToServer(reqStr);

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
     * This method sends the UserSettings to the server.
     * 
     * @param settings
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     */
    public boolean setSettings(UserSettings settings)
	    throws ClientProtocolException, IOException, APIException,
	    PermissionException, RequestNotPracticableException,
	    InternalErrorException {
	SetUserSettingsRequest reqObj = new SetUserSettingsRequest(settings);

	String reqStr = "";
	reqStr = ClientSerializer.serialize(reqObj);

	String resStr = "";
	resStr = sendToServer(reqStr);

	SetUserSettingsResponse resObj = null;
	resObj = (SetUserSettingsResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof SetUserSettingsResponse) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * This method returns the UserSettings.
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
	resStr = sendToServer(reqStr);

	GetUserSettingsResponse resObj = null;
	resObj = (GetUserSettingsResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetUserSettingsResponse) {
	    return resObj.getUserSettings();
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
	resStr = sendToServer(reqStr);

	GetAllOwnersResponse resObj = null;
	resObj = (GetAllOwnersResponse) ClientSerializer
		.deserializeResponse(resStr);

	if (resObj instanceof GetAllOwnersResponse) {
	    return resObj.getNames();
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
    // resStr = sendToServer(reqStr);
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

    /**
     * This method checks the connection status with the server.
     * 
     * @return
     */
    public boolean connectionCheck() {
	return true;
    }

}
