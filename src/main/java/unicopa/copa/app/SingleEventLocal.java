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

import java.util.Date;

import unicopa.copa.base.event.SingleEvent;

/**
 * This class extends the BaseClass SingleEvent with the update status and color
 * code.
 * 
 * @author Martin Rabe
 */
public class SingleEventLocal extends SingleEvent {

    private String colorCode;
    private String name;
    private int locationUpdateCounter;
    private int dateUpdateCounter;
    private int supervisorUpdateCounter;
    private int durationMinutesUpdateCounter;
    private int colorCodeUpdateCounter;
    private int permission;

    public SingleEventLocal(int singleEventID, int eventID, String location,
	    Date date, String supervisor, int durationMinutes,
	    String colorCode, String name, int locationUpdateCounter,
	    int dateUpdateCounter, int supervisorUpdateCounter,
	    int durationMinutesUpdateCounter, int colorCodeUpdateCounter, int permission) {
	super(singleEventID, eventID, location, date, supervisor,
		durationMinutes);
	this.colorCode = colorCode;
	this.name = name;
	this.locationUpdateCounter = locationUpdateCounter;
	this.dateUpdateCounter = dateUpdateCounter;
	this.supervisorUpdateCounter = supervisorUpdateCounter;
	this.durationMinutesUpdateCounter = durationMinutesUpdateCounter;
	this.colorCodeUpdateCounter = colorCodeUpdateCounter;
	this.permission = permission;
    }

    public void setColorCode(String colorCode) {
	this.colorCode = colorCode;
    }

    public String getColorCode() {
	return colorCode;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public int getLoactionUpdateCounter() {
	return locationUpdateCounter;
    }

    public int getDateUpdateCounter() {
	return dateUpdateCounter;
    }

    public int getSupervisorUpdateCounter() {
	return supervisorUpdateCounter;
    }

    public int getDurationMinutesUpdateCounter() {
	return durationMinutesUpdateCounter;
    }

    public int getColorCodeUpdateCounter() {
	return colorCodeUpdateCounter;
    }
    
    public int getPermission() {
	return permission;
    }

}
