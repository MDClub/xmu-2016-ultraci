package com.uitraci.hotel.service;

import com.uitraci.hotel.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘凯
 * Created on 2016/4/5 13:06.
 */
public class AdminService {
    public static String responseMessage;
    private static String token = "cVko8367";
    private static Utils utils = new Utils();
    private static String path = "http://" + Utils.ipaddress+ ":8081/HotelCheckInServer/AdminServlet.do";

    /**
     * Test
     * @return
     */
    public static boolean Admin() {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
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
