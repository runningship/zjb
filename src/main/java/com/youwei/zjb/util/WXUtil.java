package com.youwei.zjb.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.bc.sdak.utils.LogUtil;

public class WXUtil {

	private static String token = "";
	private static String js_ticket = "";
	private static Map<String , Long> activeDate = new HashMap<String , Long>();
	 /**
     * 获取接口访问凭证
     * 
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static String getAccess_token(String appid, String appsecret) {
    	if(isValid("token")){
    		return token;
    	}
        //凭证获取(GET)
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appsecret;
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
                String access_token = null;
        if (null != jsonObject) {
            try {
                access_token = jsonObject.getString("access_token");
            } catch (JSONException e) {
                // 获取token失败
                LogUtil.info("获取token失败:"+jsonObject);
            }
        }
        token = access_token;
        activeDate.put("token", System.currentTimeMillis());
        return access_token;
    }
    
    private static boolean isValid(String key){
    	if(!activeDate.containsKey(key)){
    		return false;
    	}
    	Long time = activeDate.get(key);
    	//7200秒过期
    	if(System.currentTimeMillis()-time>=7000*1000){
    		return false;
    	}
    	return true;
    }
    
    private static JSONObject httpsRequest(String requestUrl, String string, Object object) {
		try {
			URL url = new URL(requestUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			return JSONObject.fromObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
     * 调用微信JS接口的临时票据
     * 
     * @param access_token 接口访问凭证
     * @return
     */
    public static String getJsApiTicket(String access_token) {
    	if(isValid("js_ticket")){
    		return js_ticket;
    	}
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = url.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        String ticket = null;
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
            	LogUtil.info("获取jsapiticket失败:"+jsonObject);
            }
        }
        js_ticket = ticket;
        activeDate.put("js_ticket", System.currentTimeMillis());
        return ticket;
    }
    
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String str;
        String signature = "";
 
        //注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
 
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
 
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
 
        return ret;
    }
    
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
 
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
 
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
