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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import unicopa.copa.app.R;
import unicopa.copa.base.event.CategoryNodeImpl;

/**
 * This Adapter helps to show the List of Events.
 * 
 * @author Christiane Kuhn
 */
public class SearchAdapter extends BaseAdapter {

    ArrayList<CategoryNodeImpl> CategoryList;
    Context context;

    public SearchAdapter(Context context,
	    ArrayList<CategoryNodeImpl> categoryList) {
	this.context = context;
	this.CategoryList = categoryList;
    }

    @Override
    public int getCount() {
	return CategoryList.size();
    }

    @Override
    public Object getItem(int arg0) {
	return CategoryList.get(arg0);
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
	    convertView = inflater.inflate(R.layout.listitem_category, null);
	    holder = new ViewHolder();

	    holder.catName = (TextView) convertView
		    .findViewById(R.id.item_category);
	    holder.colour = (LinearLayout) convertView
		    .findViewById(R.id.search_linear);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	CategoryNodeImpl node = (CategoryNodeImpl) this.getItem(position);
	holder.catName.setText(node.getName());
	Drawable draw = context.getResources().getDrawable(
		R.drawable.border_cat);
	holder.colour.setBackgroundDrawable(draw);

	return convertView;
    }

    static class ViewHolder {
	TextView catName;
	LinearLayout colour;
    }

}
