package com.xiaos.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class PagerViewAdapter extends PagerAdapter{
	private ArrayList<View> arrays;
	public PagerViewAdapter(ArrayList<View> arrays){
		this.arrays = arrays;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(arrays.get(position),0);
		return arrays.get(position);
	}

	@Override
	public int getCount() {
		return arrays.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
     
    @Override  
    public int getItemPosition(Object object) {  
	   return POSITION_NONE;  
    }  
}
