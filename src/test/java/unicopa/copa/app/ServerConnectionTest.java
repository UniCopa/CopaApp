package unicopa.copa.app;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;

import unicopa.copa.base.com.exception.APIException;
import unicopa.copa.base.com.exception.InternalErrorException;
import unicopa.copa.base.com.exception.PermissionException;
import unicopa.copa.base.com.exception.RequestNotPracticableException;
import unicopa.copa.base.event.SingleEvent;

import android.content.Context;

/**
 * Test class for the ServerConnection class.
 * 
 * @author Martin Rabe
 * 
 */
public class ServerConnectionTest {

    /**
     * This method tests the login method.
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test
    @Ignore
    public void loginTest() throws ClientProtocolException, IOException {
	String userName = "me";
	String password = "me";
	// TODO not sure if this is OK
	Context context = null;
	ServerConnection scon = ServerConnection.getInstance();

	assertEquals(scon.login(userName, password, context), true);
    }

    /**
     * This Method tests the GetSingleEvent method
     * 
     * @throws ClientProtocolException
     * @throws APIException
     * @throws PermissionException
     * @throws RequestNotPracticableException
     * @throws InternalErrorException
     * @throws IOException
     */
    @Test
    @Ignore
    public void getSingleEventTest() throws ClientProtocolException,
	    APIException, PermissionException, RequestNotPracticableException,
	    InternalErrorException, IOException {
	int singleEventID = 1;
	SingleEvent singleEvent = new SingleEvent();
	ServerConnection scon = ServerConnection.getInstance();

	assertEquals(scon.getSingleEvent(singleEventID), singleEvent);
    }

}
