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
package unicopa.copa.app.test;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import org.junit.Test;

import unicopa.copa.app.ServerConnection;
import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.SingleEvent;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Test class for the ServerConnection class.
 * 
 * @author Martin Rabe
 * 
 */
public class ServerConnectionTest extends AndroidTestCase {

    protected void setUp() throws Exception {
	super.setUp();
    }

    /**
     * This method tests the login method.
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test
    public void /*test*/Login() throws ClientProtocolException, IOException {
	String userName = "derp";
	String password = "pwd";
	Context context = getContext();
	ServerConnection scon = ServerConnection.getInstance();

	assertEquals(scon.login(userName, password, context), true);
    }

    /**
     * This method tests the GetSingleEvent method
     * 
     * @throws ClientProtocolException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     * @throws IOException
     */
    @Test
    public void /* test */GetSingleEvent() throws ClientProtocolException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException, IOException {
	int singleEventID = 1;
	SingleEvent singleEvent = new SingleEvent();
	ServerConnection scon = ServerConnection.getInstance();

	assertEquals(scon.getSingleEvent(singleEventID), singleEvent);
    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

}
