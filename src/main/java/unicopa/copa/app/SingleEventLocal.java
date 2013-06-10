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

import unicopa.copa.base.event.SingleEvent;

/**
 * This class manages the updatestatus of SingleEvents.
 * 
 * @author Martin Rabe
 */
public class SingleEventLocal extends SingleEvent {
    private ArrayList<Integer> m_changeUpdateStatus;

    private String m_colorCode;

    public void setChangeUpdateStatus(ArrayList<Integer> changeUpdateStatus) {
	m_changeUpdateStatus = changeUpdateStatus;
    }
    
    public void setColorCode(String colorCode) {
	m_colorCode = colorCode;
    }
    
    public String setColorCode() {
	return m_colorCode;
    }

    public ArrayList<Integer> getChangeUpdateStatus() {
	return null;
    }
}