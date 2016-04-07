package com.uitraci.hotel.parse;

import com.uitraci.hotel.service.ClientService;

import com.uitraci.hotel.dto.CheckinDTO;
import com.uitraci.hotel.dto.CheckoutDTO;
import com.uitraci.hotel.dto.FileuploadDTO;
import com.uitraci.hotel.dto.FloorDTO;
import com.uitraci.hotel.dto.GuestDTO;
import com.uitraci.hotel.dto.HeartbeatDTO;
import com.uitraci.hotel.dto.InfoDTO;
import com.uitraci.hotel.dto.InitDTO;
import com.uitraci.hotel.dto.LoginDTO;
import com.uitraci.hotel.dto.LogoutDTO;
import com.uitraci.hotel.dto.MemberDTO;
import com.uitraci.hotel.dto.PayDTO;
import com.uitraci.hotel.dto.RoomDTO;
import com.uitraci.hotel.dto.StatusDTO;
import com.uitraci.hotel.dto.TypeDTO;

/**
 * @author 刘凯
 * Created on 2016/4/5 20:26.
 */
public class ParseClient {
    private ClientService clientService = new ClientService();
    private String response;

    /**
     * Parse 登录
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public LoginDTO ParseLogin(String random , String signature , String action , String device, String cardid){
        response = clientService.Login(random,signature,action,device,cardid);
        Parse parse = new Parse();
        LoginDTO loginDTO = parse.parseResponse(LoginDTO.class, response);
        return loginDTO;
    }

    /**
     * Parse 选房
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param customer
     * @param room
     * @param time
     * @return
     */
    public CheckinDTO ParseCheckin(String random , String signature , String action , String device, String customer , String room , String time){
        response = clientService.Checkin(random, signature, action, device, customer, room, time);
        Parse parse = new Parse();
        CheckinDTO checkinDTO = parse.parseResponse(CheckinDTO.class, response);
        return checkinDTO;
    }

    /**
     * Parse 退房
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public CheckoutDTO ParseCheckout(String random , String signature , String action , String device, String cardid){
        response = clientService.Checkout(random,signature,action,device,cardid);
        Parse parse = new Parse();
        CheckoutDTO checkoutDTO = parse.parseResponse(CheckoutDTO.class,response);
        return checkoutDTO;
    }

    /**
     *
     * @return
     */
    public FileuploadDTO ParseFileupload(){
        return  null;
    }

    /**
     * Parse 查询楼层
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public FloorDTO ParseFloor(String random , String signature , String action , String device){
        response = clientService.Floor(random, signature, action, device);
        Parse parse = new Parse();
        FloorDTO floorDTO = parse.parseResponse(FloorDTO.class,response);
        return floorDTO;
    }

    /**
     * Parse 上报散客
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param mobile
     * @param idcard
     * @return
     */
    public GuestDTO ParseGuest(String random , String signature , String action , String device, String mobile , String idcard){
        response = clientService.Guest(random, signature, action, device, mobile, idcard);
        Parse parse = new Parse();
        GuestDTO guestDTO = parse.parseResponse(GuestDTO.class,response);
        return guestDTO;
    }

    /**
     * Parse 心跳
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public HeartbeatDTO ParseHeartbeat(String random , String signature , String action , String device){
        response = clientService.HeartBeat(random, signature, action, device);
        Parse parse = new Parse();
        HeartbeatDTO heartbeatDTO = parse.parseResponse(HeartbeatDTO.class,response);
        return heartbeatDTO;
    }

    /**
     * Parse 查询其它
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param type
     * @return
     */
    public InfoDTO ParseInfo(String random , String signature , String action , String device, String type){
        response = clientService.Info(random, signature, action, device, type);
        Parse parse = new Parse();
        InfoDTO infoDTO = parse.parseResponse(InfoDTO.class,response);
        return infoDTO;
    }

    /**
     * Parse 初始化
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public InitDTO ParseInit(String random , String signature , String action , String device){
        response = clientService.Init(random, signature, action, device);
        Parse parse = new Parse();
        InitDTO initDTO = parse.parseResponse(InitDTO.class,response);
        return initDTO;
    }

    /**
     * Parse 登出
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public LogoutDTO ParseLogout(String random , String signature , String action , String device, String cardid){
        response = clientService.Logout(random, signature, action, device, cardid);
        Parse parse = new Parse();
        LogoutDTO logoutDTO = parse.parseResponse(LogoutDTO.class,response);
        return logoutDTO;
    }

    /**
     * Parse 查询会员
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public MemberDTO ParseMember(String random , String signature , String action , String device, String cardid){
        response = clientService.Member(random, signature, action, device, cardid);
        Parse parse = new Parse();
        MemberDTO memberDTO = parse.parseResponse(MemberDTO.class,response);
        return memberDTO;
    }

    /**
     * Parse 付款
     * @return
     */
    public PayDTO ParsePay(){
        return null;
    }

    /**
     * Parse 查询房间
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param cardid
     * @return
     */
    public RoomDTO ParseRoom(String random , String signature , String action , String device, String cardid){
        response = clientService.Room(random, signature, action, device, cardid);
        Parse parse = new Parse();
        RoomDTO roomDTO = parse.parseResponse(RoomDTO.class,response);
        return roomDTO;
    }

    /**
     * Parse 查询房态
     * @param random
     * @param signature
     * @param action
     * @param device
     * @param floor
     * @param type
     * @return
     */
    public StatusDTO ParseStatus(String random , String signature , String action , String device , String floor , String type){
        response = clientService.Status(random, signature, action, device, floor, type);
        Parse parse = new Parse();
        StatusDTO statusDTO = parse.parseResponse(StatusDTO.class,response);
        return statusDTO;
    }

    /**
     * Parse 查询房型
     * @param random
     * @param signature
     * @param action
     * @param device
     * @return
     */
    public TypeDTO ParseType(String random , String signature , String action , String device){
        response = clientService.Type(random, signature, action, device);
        Parse parse = new Parse();
        TypeDTO typeDTO = parse.parseResponse(TypeDTO.class,response);
        return typeDTO;
    }
}
