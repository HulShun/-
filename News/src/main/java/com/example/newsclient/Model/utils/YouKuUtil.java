package com.example.newsclient.Model.utils;

import android.text.TextUtils;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.SysParam;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016-05-11.
 */
public class YouKuUtil {
    private static YouKuUtil util;
    private String sign;
    private SysParam param;

    public static YouKuUtil getInstance() {
        if (util == null)
            synchronized (AppUtil.class) {
                if (util == null) {
                    util = new YouKuUtil();
                }
            }
        return util;
    }

    public SysParam getParam() {
        if (param == null) {
            try {
                getSign();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return param;
    }

    public String getSign() throws IOException {
        if (sign == null) {
            param = new SysParam();
            String s = URLEncoder.encode("youku.api.vod.get.videostream", "UTF-8");
            param.setAction(s);
            param.setClient_id(URLEncoder.encode(Configuration.YOUKU_KEYWORD, "UTF-8"));
            long time = (System.currentTimeMillis() / 1000);
            param.setTimestamp(URLEncoder.encode(String.valueOf(time), "UTF-8"));
            param.setVersion(URLEncoder.encode("3.0", "UTF-8"));

            Map<String, String> map = new HashMap<>();
            map.put("action", param.getAction());
            map.put("client_id", param.getClient_id());
            map.put("access_token", param.getAccess_token());
            map.put("format", param.getFormat());
            map.put("timestamp", param.getTimestamp());
            map.put("version", param.getVersion());

            sign = signApiRequest(map, Configuration.YOUKU_SECRET, "md5");
            param.setSign(sign);
        }

        return sign;
    }

    //确保params中的参数值进行了UTF-8的URLEncode。参数值为空的参数，也要加入到签名字符串中。
    public String signApiRequest(Map params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = (String[]) params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            String value = (String) params.get(key);
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                query.append(key)
                        .append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if ("HmacSHA256".equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
            // 第四步：把二进制转化为大写的十六进制
            return byte2hex(bytes);
        } else {
            query.append(secret);
            //32位小写md5加密
            return getMd5Value(query.toString());
        }

    }

    private byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    private String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }

    private String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
