package com.xiaos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class ExpandableAdapter extends  BaseExpandableListAdapter{
	private String[][] childrenData;
	private String[] groupData;
	private Context context;
	
	public ExpandableAdapter(String[][] childrenData,String[] groupData,Context context){
		this.groupData = groupData; 
		this.childrenData = childrenData;
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childrenData[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		 TextView text = null;  
         if (convertView != null) {  
             text = (TextView)convertView;  
         } else {  
             text = createChildrenView();  
         }  
         text.setText(childrenData[groupPosition][childPosition]); 
         
         return text;  
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childrenData[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupData[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return groupData.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView text = null;  
        if (convertView != null) {  
            text = (TextView)convertView;  
        } else {  
            text = createGroupView();  
        }  
        text.setText(groupData[groupPosition]);  
        return text;  
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private TextView createChildrenView() {
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 50);
		TextView text = new TextView(context);
		text.setLayoutParams(layoutParams);
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		text.setPadding(50, 0, 0, 0);
		//text.setBackgroundResource(R.drawable.group_bar_style);
		text.setTextColor(context.getResources().getColor(Color.BLACK));
		text.setTextSize(16);
		return text;
	}
	
	private TextView createGroupView() {
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 50);
		TextView text = new TextView(context);
		text.setLayoutParams(layoutParams);
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		text.setPadding(40, 0, 0, 0);
		//text.setBackgroundResource(R.drawable.group_bar_style);
		text.setTextColor(context.getResources().getColor(Color.BLACK));
		text.setTextSize(18);
		return text;
	}
}
