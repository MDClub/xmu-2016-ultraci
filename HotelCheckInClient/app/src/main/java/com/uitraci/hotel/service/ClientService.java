package com.uitraci.hotel.service;

import com.uitraci.hotel.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 刘凯
 *         Created on 2016/4/4 22:00.
 */
public class ClientService {

    public static String responseMessage;
    private static String token = "cVko8367";
    private static Utils utils = new Utils();
    private static String path = "http://" + Utils.ipaddress+ ":8081/HotelCheckInServer/ClientServlet.do";

    /**
     * 心跳
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public static String HeartBeat(String random , String signature , String action , String device) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public static String Init(String random , String signature , String action , String device) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *员工登录
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public static String Login(String random , String signature , String action , String device, String cardid) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("cardid", cardid));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 登出
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public static String Logout(String random , String signature , String action , String device, String cardid) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("cardid", cardid));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *查询会员
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public static String Member(String random , String signature , String action , String device, String cardid) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("cardid", cardid));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询房型
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public static String Type(String random , String signature , String action , String device) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询楼层
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public static String Floor(String random , String signature , String action , String device) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询房态
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param floor
     * @param type
     * @return
     */
    public static String Status(String random , String signature , String action , String device , String floor , String type) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("floor", floor));
            parameters.add(new BasicNameValuePair("type", type));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *查询房间
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public static String Room(String random , String signature , String action , String device, String cardid) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("cardid", cardid));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询其它
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param type
     * @return
     */
    public static String Info(String random , String signature , String action , String device, String type) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("type", type));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上报散客
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param mobile
     * @param idcard
     * @return
     */
    public static String Guest(String random , String signature , String action , String device, String mobile , String idcard) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("mobile", mobile));
            parameters.add(new BasicNameValuePair("idcard", idcard));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选房
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param customer
     * @param room
     * @param time
     * @return
     */
    public static String Checkin(String random , String signature , String action , String device, String customer , String room , String time) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("customer", customer));
            parameters.add(new BasicNameValuePair("room", room));
            parameters.add(new BasicNameValuePair("time", time));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *退房
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public static String Checkout(String random , String signature , String action , String device, String cardid) {
        boolean request = false;
        try {
            HttpClient client = utils.getHttpClient();
            HttpPost httpPost = new HttpPost(path);
            // 指定要提交的数据实体
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("random", random));
            parameters.add(new BasicNameValuePair("signature", signature));
            parameters.add(new BasicNameValuePair("action", action));
            parameters.add(new BasicNameValuePair("device", device));
            parameters.add(new BasicNameValuePair("cardid", cardid));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 执行
            HttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                request = true;
                responseMessage = EntityUtils.toString(response.getEntity());
                return responseMessage;
            } else {
                request = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
