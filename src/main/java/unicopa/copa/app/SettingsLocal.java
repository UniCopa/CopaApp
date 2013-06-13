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

import unicopa.copa.base.UserSettings;

/**
 * This class extends the BaseClass Settings with notification kind and last update.
 * 
 * @author Martin Rabe
 */
public class SettingsLocal extends UserSettings {

    private int notificationKind;
    private Date lastUpdate;

    public void setNotificationKind(int notificationKind) {
	this.notificationKind = notificationKind;
    }

    public int getNotificationKind() {
	return notificationKind;
    }

    public void setLastUpdate(Date lastUpdate) {
	this.lastUpdate = lastUpdate;
    }

    public Date getLastUpdate() {
	return lastUpdate;
    }

}
