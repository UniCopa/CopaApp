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
 * This class manages subscriptions.
 * 
 * @author Martin Rabe
 */
public class Subscription {
    private int[] m_subList; //[eventID, color]
    private int[] m_perList; //[eventID, permissionCode]
    
    public boolean add(int eventID) {
	//TODO return value
	return true;
    }
    
    public void change(int eventID, String userName, int permissionCode) {
	
    }
    
    public boolean remove(int eventID) {
	//TODO return value
	return true;
    }
    
    public int sendSubscribe(int eventID) {
	//TODO return value
	return 1;
    }
    
    public void sendPermission(int eventID) {
	
    }
    
    public int sendPermissionChange(int eventID, String email) {
	//TODO return value
	return 1;
    }
    
    public int sendUnsubscribe(int eventID) {
	//TODO return value
	return 1;
    }
    
    public int getColor(int eventID) {
	//TODO return value
	return 1;
    }
    
    public void setColor(int color, int eventID) {
	
    }
    
//TODO need to specify which date is to be used
/*    public int getSingleEventsUpdate(date timeStamp) {
	
    }*/
}
