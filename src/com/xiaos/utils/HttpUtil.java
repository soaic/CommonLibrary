package com.xiaos.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * 访问API工具类
 * 
 * @author SoAi
 * 
 */
public class HttpUtil {
	private String host = "http://192.168.1.166:8080/Servers/";
	private static HttpUtil httpUtils;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	synchronized public static HttpUtil getInstance() {
		return httpUtils = httpUtils==null?new HttpUtil():httpUtils;
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
	 * 登录DEMO
	 * 
	 * @param loginName
	 * @param passWord
	 * @return
	 */
	public Object login(String loginName, String passWord) {
		String url = host + "taxilogin/taxilogin-taxiLogin.action";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("logiName", loginName);
		params.put("passWord", passWord);
		return getValues(params, url);
	}
}
