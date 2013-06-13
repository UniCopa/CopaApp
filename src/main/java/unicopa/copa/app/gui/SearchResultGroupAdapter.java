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
package unicopa.copa.app.gui;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import unicopa.copa.app.R;
import unicopa.copa.base.event.EventGroup;

/**
 * This Adapter helps to show the List of Events.
 * 
 * @author Christiane Kuhn
 */
public class SearchResultGroupAdapter extends BaseAdapter {

    ArrayList<EventGroup> GroupList;
    Context context;

    public SearchResultGroupAdapter(Context context,
	    ArrayList<EventGroup> groupList) {
	this.context = context;
	this.GroupList = groupList;
    }

    @Override
    public int getCount() {
	return GroupList.size();
    }

    @Override
    public Object getItem(int arg0) {
	return GroupList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
	return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	if (convertView == null) {
	    LayoutInflater inflater = (LayoutInflater) this.context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater
		    .inflate(R.layout.listitem_result_group, null);
	    holder = new ViewHolder();
	    holder.name = (TextView) convertView
		    .findViewById(R.id.item_result_group_name);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.item_result_group_list);
	    holder.info = (Button) convertView
		    .findViewById(R.id.item_result_group_info);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	final EventGroup eventGroup = (EventGroup) this.getItem(position);
	holder.name.setText(eventGroup.getEventGroupName());
	Drawable draw = context.getResources().getDrawable(R.drawable.border);
	holder.colour.setBackgroundDrawable(draw);

	holder.info.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		PopUp.alert(context, eventGroup.getEventGroupName(),
			eventGroup.getEventGroupInfo());
	    }

	});
	return convertView;
    }

    static class ViewHolder {
	TextView name;
	Button info;
	LinearLayout colour;
    }

}
