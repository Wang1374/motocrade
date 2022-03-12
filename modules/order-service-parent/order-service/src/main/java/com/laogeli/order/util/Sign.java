package com.laogeli.order.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Sign {
    public static String connectJava(String method, String appId, String appKey, String url, HashMap<String, String> map) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        Map<String, String> _map = new HashMap<>();
        _map.put("appId", appId);
        _map.put("method", method);
        _map.put("timestamp", timestamp);
        _map.put("data", JSON.toJSONString(map));
        TreeMap<String, String> tmap = new TreeMap();
        tmap.putAll(map);
        String param = "";
        for (Map.Entry<String, String> _m : tmap.entrySet()) {
            param = param.concat(_m.getKey()).concat("=").concat(_m.getValue()).concat(",");
        }
        param = param.substring(0, param.length() - 1);
        String md5 = appKey.concat(appId).concat(param).concat(method).concat(timestamp).concat(appKey);
        param = getMD5(md5);
        _map.put("sign", param);
        String result = HttpClientUtils.doPost(url, JSONObject.toJSONString(_map));
        return result;
    }

    public static String getMD5(String data) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));

            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return md5StrBuff.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(getMD5("654H3BC2BEDDB3F49618AFCYHA4BEUYY2855companyId=1323,truckerMobile=18637905266,truckerName=雷军account.applyCard1565747299654H3BC2BEDDB3F49618AFCYHA4BEUYY"));
    }
}
