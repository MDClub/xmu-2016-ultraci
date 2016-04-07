package com.uitraci.hotel.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author 刘凯
 * Created on 2016/4/4 23:47.
 */
public class Utils {
    public static String ipaddress = "103.37.165.152";

    /**
     * random和token将字典顺序排序
     * @param random
     * @param token
     * @return
     */
    public String DictOrder(String random, String token){
        String[] array = new String[] { token, random };
        Arrays.sort(array);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * SHA1加密
     * @param decript
     * @return
     */
    public String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 随机生成数
     * @return
     */
    public String RandomGen(){
        return String.valueOf((int)(Math.random()*1000));
    }

    /**
     * 初始化HttpClient，并设置超时
     * @return
     */
    public static HttpClient getHttpClient()
    {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);//请求超时设为5s
        HttpConnectionParams.setSoTimeout(httpParams, 10000);//等待数据超时设为10s
        HttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }
}
