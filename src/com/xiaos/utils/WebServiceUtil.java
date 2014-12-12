package com.xiaos.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * WebService 工具类
 * @author XIAOS
 *
 */
public class WebServiceUtil {
	private static WebServiceUtil instance;;
	
	public static WebServiceUtil getInstance(){
		return instance == null ? (instance = new WebServiceUtil()):instance;
	}
	
	private String getResult(SoapObject rpc,String endPoint,String soapAction){
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本  
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);  
  
        envelope.bodyOut = rpc;  
        // 设置是否调用的是dotNet开发的WebService  
        envelope.dotNet = true;  
        // 等价于envelope.bodyOut = rpc;  
        envelope.setOutputSoapObject(rpc);  
  
        HttpTransportSE transport = new HttpTransportSE(endPoint);  
        try {  
            // 调用WebService  
            transport.call(soapAction, envelope);  
            // 获取返回的数据  
            Object object = envelope.getResponse(); 
            
            if (object instanceof SoapPrimitive) {
            	object = (SoapPrimitive) object;
            } else {
            	object = ((SoapObject) object);
            }
            // 获取返回的结果  
            String result = object.toString();
            return result;
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "error";
	}
	
	/**
	 * 上传平板更新记录
	 * @param guid
	 * @param jsonData
	 * @return
	 */
	public String putUpdateLog(String guid, String jsonData) {  
        // 命名空间  
        String nameSpace = "http://192.168.60.113:8086";  
        // 调用的方法名称  
        String methodName = "HotelRoomEquipProduct";  
        // EndPoint  
        String endPoint = "http://192.168.60.113:8086/WebService/WSHotelRoomEquipProduct.asmx";  
        // SOAP Action  
        String soapAction = nameSpace+"/"+methodName;  
  
        // 指定WebService的命名空间和调用的方法名  
        SoapObject rpc = new SoapObject(nameSpace, methodName);  
  
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId  
        rpc.addProperty("Guid", guid);  
        rpc.addProperty("HotelInfoManageIdInfo", jsonData);
  
        return getResult(rpc, endPoint, soapAction);
    }
	
}
