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

/**
 * This class manages the connection to the server.
 * 
 * @author Martin Rabe
 */
public class ServerConnection {
    private boolean m_connected = false;
    private String m_gcmKey = "";

    public void setConnected(boolean connected) {
	m_connected = connected;
    }

    public boolean getConnected() {
	return m_connected;
    }
    
    public void setGMCKey(String gmcKey) {
	m_gcmKey = gmcKey;
    }

    public String getGMCKey() {
	return m_gcmKey;
    }

    public boolean login(String userName, String password) {
	//TODO change return value
	return true;
	
    }
    
    public boolean logout(String userName) {
	//TODO change return value
	return true;
    }

    public String sendToServer(String jsonRequest) {
	//TODO change return value
	return "";
    }
    
    public void openConnection() {
    }

    public boolean connectionCheck() {
	//TODO change return value
	return true;
    }

    public String sendGMCKey() {
	//TODO change return value
	return "";
    }
    
    public void closeConnection() {
    }
}
