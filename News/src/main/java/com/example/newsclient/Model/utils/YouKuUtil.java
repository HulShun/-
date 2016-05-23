package com.example.newsclient.Model.utils;

import android.text.TextUtils;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.SysParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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

    /**
     * 请用setTimestamp()方法设置时间戳
     *
     * @return
     */
    public SysParam getParam() {
        if (param == null) {
            param = new SysParam();
            try {
                String s = null;
                s = URLEncoder.encode("youku.api.vod.get.videostream", "UTF-8");
                param.setAction(s);
                param.setClient_id(URLEncoder.encode(Configuration.YOUKU_CLIENT_ID, "UTF-8"));
                param.setVersion(URLEncoder.encode("3.0", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return param;
    }

    public String getSign() {
        return sign;
    }

    //确保params中的参数值进行了UTF-8的URLEncode。
    public String signApiRequest(Map params, String secret) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = (String[]) params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            String value = (String) params.get(key);
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }
        query.append(secret);
        // 第三步：使用MD5加密
        //URLEncoder编码
        String signStr = URLEncoder.encode(query.toString(), "UTF-8");
        //32位小写md5加密
        return getMd5Value(signStr);


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
