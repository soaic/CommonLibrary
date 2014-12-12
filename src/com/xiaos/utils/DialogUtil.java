package com.xiaos.utils;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

public class DialogUtil {
	private static DialogUtil dialogUtils = null;
	private PopupWindow mPop;
	
	synchronized public static DialogUtil getInstance(){
		return dialogUtils = dialogUtils==null?new DialogUtil():dialogUtils;
	}
	
	/**
	 * 多选对话框
	 * @param context
	 * @param strArray 多选字符串数组
	 * @param isCheckeds 多选值数据
	 * @param textview 
	 * @param title
	 * @param posButton
	 * @param negButton
	 */
	public void showCheckDialog(Context context,final String[] strArray,final boolean[] isCheckeds,
			final TextView textview, CharSequence title, CharSequence posButton, CharSequence negButton) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMultiChoiceItems(strArray,isCheckeds,new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dia, int which, boolean isChecked) {
				isCheckeds[which] = isChecked;
			}
		});
		builder.setNegativeButton(posButton,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StringBuffer cont = new StringBuffer();
						for(int i=0;i<isCheckeds.length;i++){
							if(isCheckeds[i]){
								cont.append(strArray[i]+",");
								isCheckeds[i] = false;
							}
						}
						if(cont.length()>0){
							textview.setText(cont.substring(0, cont.length()-1));
						}else{
							textview.setText("");
						}
					}
				});
		builder.setNeutralButton(negButton,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	
	/**
	 * 日期选择对话框
	 * 
	 * @param view
	 * @return Dialog
	 */
	public Dialog onCreateDialog(final TextView view, Context context) {
		Calendar calendar = Calendar.getInstance();
		Dialog dialog = null;
		DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month,
					int dayOfMonth) {
				DecimalFormat format = new DecimalFormat("00");
				String mh = format.format(month + 1);
				String dm = format.format(dayOfMonth);
				String date_string = year + "-" + mh + "-" + dm;
				view.setText(date_string);
			}
		};
		dialog = new DatePickerDialog(context, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		return dialog;
	}
	
	/**
	 * 初始化popWindow
	 * @param layout
	 * @param activity
	 * @return
	 */
	public PopupWindow getPopWindow(View layout,Activity activity) {
		if (mPop == null) {
			mPop = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			// 使其聚焦
			mPop.setFocusable(true);
			// 设置允许在外点击消失
			mPop.setOutsideTouchable(false);
			// 刷新状态
			mPop.update();
			// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
			mPop.setBackgroundDrawable(new BitmapDrawable(activity.getResources()));
		}
		if (mPop.isShowing()) {
			mPop.dismiss();
		}
		return mPop;
	}
}
