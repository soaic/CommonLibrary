package com.xiaos.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;


public class SystemUtil {
	private static SystemUtil systemUtils;
	
	synchronized public static SystemUtil getIntences(){
		return systemUtils = systemUtils==null?new SystemUtil():systemUtils;
	}  

	/**
	 * 获取系统所有APP应用
	 * 
	 * @param context
	 */
	public ArrayList<HashMap<String,Object>> getAllApp(Context context) {
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		// 将获取到的APP的信息按名字进行排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		ArrayList<HashMap<String,Object>> appList = new ArrayList<HashMap<String,Object>>();
		for (ResolveInfo info : apps) {
			HashMap<String,Object> appInfo = new HashMap<String,Object>();
			appInfo.put("lable",info.loadLabel(manager));
			appInfo.put("icon",info.loadIcon(manager));
			appInfo.put("packageName",info.activityInfo.packageName);
			appInfo.put("className",info.activityInfo.name);
			//System.out.println("info.activityInfo.packageName="+info.activityInfo.packageName);
			//System.out.println("info.activityInfo.name="+info.activityInfo.name);
			appList.add(appInfo);
		}

		return appList;
	}

	/**
	 * 获取系统用户安装的APP应用
	 * 
	 * @param context
	 */
	public ArrayList<HashMap<String,Object>> getUserApp(Context context) {
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		// 将获取到的APP的信息按名字进行排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		ArrayList<HashMap<String,Object>> appList = new ArrayList<HashMap<String,Object>>();
		for (ResolveInfo info : apps) {
			HashMap<String,Object> appInfo = new HashMap<String,Object>();
			ApplicationInfo ainfo = info.activityInfo.applicationInfo;
			if ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				appInfo.put("lable",info.loadLabel(manager));
				appInfo.put("icon",info.loadIcon(manager));
				appInfo.put("packageName",info.activityInfo.packageName);
				appInfo.put("className",info.activityInfo.name);
				appList.add(appInfo);
			}
		}

		return appList;
	}

	/**
	 * 根据包名和Activity启动名查询应用信息
	 * 
	 * @param cls
	 * @param pkg
	 * @return
	 */
	public HashMap<String,Object> getAppByClsPkg(Context context, String pkg, String cls) {
		HashMap<String,Object> appInfo = new HashMap<String,Object>();

		PackageManager pm = context.getPackageManager();
		Drawable icon;
		CharSequence label = "";
		ComponentName comp = new ComponentName(pkg, cls);
		try {
			ActivityInfo info = pm.getActivityInfo(comp, 0);
			icon = pm.getApplicationIcon(info.applicationInfo);
			label = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0));
		} catch (NameNotFoundException e) {
			icon = pm.getDefaultActivityIcon();
			cls = null;
		}
		appInfo.put("lable",label);
		appInfo.put("icon",icon);
		appInfo.put("packageName",pkg);
		appInfo.put("className",cls);

		return appInfo;
	}

	/**
	 * 跳转到WIFI设置
	 * 
	 * @param context
	 */
	public void intentWifiSetting(Context context) {
		if (android.os.Build.VERSION.SDK_INT > 10) {
			// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
			context.startActivity(new Intent(
					android.provider.Settings.ACTION_SETTINGS));
		} else {
			context.startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	/**
	 * WIFI网络开关
	 * 
	 */
	public void toggleWiFi(Context context, boolean enabled) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(enabled);
	}

	/**
	 * 移动网络开关
	 */
	public void toggleMobileData(Context context, boolean enabled) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * GPS开关 当前若关则打开 当前若开则关闭
	 */
	public void toggleGPS(Context context) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调节系统音量
	 * 
	 * @param context
	 */
	public void showAudioDilog(Context context) {
		AudioManager audiomanage = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// int maxVolume =
		// audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC); //获取系统最大音量
		// int currentVolume =
		// audiomanage.getStreamVolume(AudioManager.STREAM_RING); //获取当前音量
		// audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM,
		// currentVolume, AudioManager.FLAG_PLAY_SOUND);
		audiomanage.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

	}

	/**
	 * 设置亮度
	 * 
	 * @param resolver
	 * @param brightness
	 */
	public void setBrightness(Activity activity) {
		ContentResolver resolver = activity.getContentResolver();
		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");
		int nowScreenBri = getScreenBrightness(activity);
		nowScreenBri = nowScreenBri <= 225 ? nowScreenBri + 30 : 30;
		System.out.println("nowScreenBri==" + nowScreenBri);
		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				nowScreenBri);
		resolver.notifyChange(uri, null);
	}

	/**
	 * 获取屏幕的亮度
	 * 
	 * @param activity
	 * @return
	 */
	public int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/**
	 * 跳转到系统设置
	 * 
	 * @param context
	 */
	public void intentSetting(Context context) {
		String pkg = "com.android.settings";
		String cls = "com.android.settings.Settings";

		ComponentName component = new ComponentName(pkg, cls);
		Intent intent = new Intent();
		intent.setComponent(component);

		context.startActivity(intent);
	}
	
	/**
	 * 获取文件夹下所有文件
	 * @param path
	 * @return
	 */
	public ArrayList<File> getFilesArray(String path){
		File file = new File(path);
		File files[] = file.listFiles();
		ArrayList<File> listFile = new ArrayList<File>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					listFile.add(files[i]);
				}
				if (files[i].isDirectory()) {
					listFile.addAll(getFilesArray(files[i].toString()));
				}
			}
		}
		return listFile;
	}
	
	/** 
     * 获取视频的缩略图 
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。 
     * @param videoPath 视频的路径 
     * @param width 指定输出视频缩略图的宽度 
     * @param height 指定输出视频缩略图的高度度 
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
     * @return 指定大小的视频缩略图 
     */  
	public Bitmap getVideoThumbnail(String videoPath, int width, int height,  
            int kind) {  
        Bitmap bitmap = null;  
        // 获取视频的缩略图   
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
        //System.out.println("w"+bitmap.getWidth());  
        //System.out.println("h"+bitmap.getHeight());  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }
	
	/**
	 * 打开视频文件
	 * @param context
	 * @param file 视频文件
	 */
	public void intentVideo(Context context, File file){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String type = "video/*";
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, type);
		context.startActivity(intent);
	}
	
	/**
	 * 打开网址
	 * @param context
	 * @param url
	 * @param browserPackage
	 * @param browserClassName
	 */
	public void intentBrowser(Context context, String url, String browserPackage, String browserClassName){
		Uri uri = Uri.parse(url);  
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
        if(browserPackage!=null&&browserClassName!=null)
        	intent.setClassName(browserPackage,browserClassName);
        context.startActivity(intent);
	}
}
