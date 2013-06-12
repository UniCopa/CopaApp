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

import java.util.ArrayList;
import unicopa.copa.base.event.Event;
import unicopa.copa.base.event.EventGroup;

/**
 * This class implements the search function.
 * 
 * @author Martin Rabe
 */
public class Search {
    private String m_searchItem = "";
    private int m_categoryID = 0;

    public void setSearchItem(String searchItem) {
	m_searchItem = searchItem;
    }

    public String getSearchItem() {
	return m_searchItem;
    }

    public void setCategoryID(int categoryID) {
	m_categoryID = categoryID;
    }

    public int getCategoryID() {
	return m_categoryID;
    }

    public ArrayList<Event> getEventSearch(int categoryID, String searchItem,
	    int eventGroupID) {
	return null;
    }

    public ArrayList<EventGroup> getEventGroupSearch(int categoryID,
	    String searchItem) {
	return null;
    }
    
}
