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

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;

/**
 * This class implements the Bouncy Castle KeyStore which includes the SSL
 * Certificate of the unicopa server.
 * 
 * @author Martin Rabe
 */
public class CoPAAppHttpClient extends DefaultHttpClient {

    final Context context;

    /**
     * Class constructor.
     * 
     * @param context
     */
    public CoPAAppHttpClient(Context context) {
	this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
	SchemeRegistry registry = new SchemeRegistry();
	registry.register(new Scheme("http", PlainSocketFactory
		.getSocketFactory(), 80));

	registry.register(new Scheme("https", newSSLSocketFactory(), 443));

	return new SingleClientConnManager(getParams(), registry);
    }

    /**
     * This Method creates a SSLSocketFactory with the custom SSL certificate.
     * 
     * @return SSLSocketFactory
     */
    private SSLSocketFactory newSSLSocketFactory() {
	try {
	    // get instance of Bouncy Castle KeyStore format
	    KeyStore trusted = KeyStore.getInstance("BKS");

	    // get raw resource, which contains keystore with certificate
	    InputStream in = context.getResources().openRawResource(
		    R.raw.unicopa_keystore);

	    try {
		// initialize keystore with password
		trusted.load(in, "unicopa".toCharArray());
	    } finally {
		in.close();
	    }

	    // pass keystore to SSLSocketFactory
	    SSLSocketFactory sf = new SSLSocketFactory(trusted);
	    // hostname verification from certificate
	    sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

	    return sf;
	} catch (Exception e) {
	    throw new AssertionError(e);
	}
    }
    
}
