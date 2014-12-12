package com.xiaos.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class MapsApiUtils {
	private static MapsApiUtils mapsApiUtils = new MapsApiUtils();
	
	/**
	 * 单例模式
	 * 
	 * @return
	 */
	synchronized public static MapsApiUtils getInstance() {
		return mapsApiUtils;
	}
	
	/**
	 * 根据API地址和参数获取响应对象HttpResponse
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	private HttpResponse post(Map<String, Object> params, String url) {

		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("charset", HTTP.UTF_8);
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		HttpResponse response = null;
		if (params != null && params.size() > 0) {
			List<NameValuePair> nameValuepairs = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				nameValuepairs.add(new BasicNameValuePair(key, (String) params
						.get(key)));
			}
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuepairs,
						HTTP.UTF_8));
				response = client.execute(httpPost);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		} else {
			try {
				response = client.execute(httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	/**
	 * 得到JSON值
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	private Object getValues(Map<String, Object> params, String url) {
		String token = "";
		HttpResponse response = post(params, url);
		if (response != null) {
			try {
				token = EntityUtils.toString(response.getEntity());
				response.removeHeaders("operator");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return token;
	}
	
	/**
	 * 根据google API 获取两地的路線
	 * @param origin 起點
	 * @param destination 終點
	 * @param mode 出行方式 driving駕車，  walking步行， bicycling自行車, transit公交車
	 * @param sensor 是否来自装有位置传感器的设备  true Or false
	 * @return
	 */
	public Object getRoutes(String origin, String destination) {
		String url = "http://maps.googleapis.com/maps/api/directions/json?origin="+ origin +"&" +
				"destination="+ destination +"&sensor=false&mode=driving&region=hk&language=zh-TW";
		return getValues(null, url);
	}
	
	/**
	 * 根据經緯度 获取地理位置
	 * LatLng 經緯度以逗號隔開  緯度,經度
	 * @return
	 */
	public Object getAddress(String latlng) {
		String url = "http://maps.google.com/maps/api/geocode/json?latlng="+
				latlng+"&language=zh-HK&sensor=false";
		return getValues(null, url);
	}
	
	/**
	 * 根據地址獲取經緯度
	 * @return
	 */
	public Object getLatlng(String str) {
		String url = "http://maps.google.com/maps/api/geocode/json?address="+
				str+"&language=zh-HK&sensor=false";
		return getValues(null, url);
	}
}
