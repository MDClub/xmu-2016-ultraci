package com.uitraci.hotel.service;

import com.uitraci.hotel.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘凯
 *         Created on 2016/4/5 13:07.
 */
public class FileUploadService {
    public static String responseMessage;
    private static String token = "cVko8367";
    private static Utils utils = new Utils();
    private static String path = "http://" + Utils.ipaddress+ ":8081/HotelCheckInServer/FileUploadServlet.do";

    /**
     * 文件上传
     * @param random
     * @param signature
     * @param device
     * @param type
     * @return
     */
    public static boolean HeartBeat(String random , String signature , String device , String type ) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("type", type));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
