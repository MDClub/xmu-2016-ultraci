package com.uitraci.hotel.parse;

import java.util.List;

import com.uitraci.hotel.constant.ErrorCode;
import com.uitraci.hotel.dto.CheckinDTO;
import com.uitraci.hotel.dto.CheckoutDTO;
import com.uitraci.hotel.dto.FloorDTO;
import com.uitraci.hotel.dto.GuestDTO;
import com.uitraci.hotel.dto.HeartbeatDTO;
import com.uitraci.hotel.dto.InfoDTO;
import com.uitraci.hotel.dto.InitDTO;
import com.uitraci.hotel.dto.LoginDTO;
import com.uitraci.hotel.dto.LogoutDTO;
import com.uitraci.hotel.dto.MemberDTO;
import com.uitraci.hotel.dto.RoomDTO;
import com.uitraci.hotel.dto.StatusDTO;
import com.uitraci.hotel.dto.TypeDTO;

/**
 * @author 刘凯
 * Created on 2016/4/7 01:09.
 */
public class ParseDTOofClient {
    ParseClient parseClient = new ParseClient();

    /**
     * 解析登录信息
     * @param loginDTO
     * @return
     */
    public String ParseDTOofLogin(LoginDTO loginDTO){
        return loginDTO.getName();
//        int result = loginDTO.getResult();
//        switch (result){
//            case ErrorCode.OK:
//                return loginDTO.getName();
//            case ErrorCode.AUTH_FAIL:
//                return LogTemplate.AUTH_FAIL;
//            case ErrorCode.INVALID_REQ:
//                return LogTemplate.INVALID_ACTION;
//            default:
//                return null;
//        }
    }

    /**
     * 解析登出
     * @param logoutDTO
     * @return
     */
    public String ParseDTOofLogout(LogoutDTO logoutDTO) {
        return logoutDTO.getName();
    }

    /**
     * 解析会员
     * @param memberDTO
     * @return
     */
    public String ParseDTOofMember(MemberDTO memberDTO){
        int result = memberDTO.getResult();
        if (result == ErrorCode.OK){
//            int Id = memberDTO.getId();
//            String Name = memberDTO.getName();
//            String Idcard = memberDTO.getIdcard();
//            String Mobile = memberDTO.getMobile();
            return "会员卡";
        }
        return null;
    }

    /**
     * 解析房间
     * @param roomDTO
     * @return
     */
    public String ParseDTOofRoom(RoomDTO roomDTO){
        int result = roomDTO.getResult();
        if (result == ErrorCode.OK){
//            String Mobile = roomDTO.getMobile();
//            String Name = roomDTO.getName();
//            String Checkin = roomDTO.getCheckin();
//            String Checkout = roomDTO.getCheckout();
//            String Type = roomDTO.getType();
            return "查询房间成功";
        }
        return "查询房间失败";
    }

    /**
     * 解析房型信息
     * @param typeDTO
     * @return
     */
    public List<TypeDTO.Type> ParseDTOofType(TypeDTO typeDTO){
        List<TypeDTO.Type> list =  typeDTO.getTypes();
//        for (TypeDTO.Type type:list){
//            int id = type.getId();
//            String name = type.getName();
//            Double deposit = type.getDeposit();
//            Double price = type.getPrice();
//        }
        return list;
    }

    /**
     * 解析楼层信息
     * @param floorDTO
     * @return
     */
    public List<FloorDTO.Floor> ParseDTOofFloor(FloorDTO floorDTO){
        List<FloorDTO.Floor> list =  floorDTO.getFloors();
//        for (FloorDTO.Floor floor:list){
//            int id = floor.getId();
//            String name = floor.getName();
//        }
        return list;
    }

    /**
     * 解析房态信息
     * @param statusDTO
     * @return
     */
    public List<StatusDTO.Status> ParseDTOofStatus(StatusDTO statusDTO){
        List<StatusDTO.Status> list =  statusDTO.getStatuses();
//        for (StatusDTO.Status status:list){
//            int id = status.getId();
//            String name = status.getName();
//            int available = status.getAvailable();
//            int floor = status.getFloor();
//            int type = status.getType();
//        }
        return list;
    }

    /**
     * 解析选房
     * @param checkinDTO
     * @return
     */
    public String ParseDTOofCheckin(CheckinDTO checkinDTO){
        int result = checkinDTO.getResult();
        if (result == ErrorCode.OK){
            return "选房成功";
        }
        return "选房失败";
    }

    /**
     * 解析退房
     * @param checkoutDTO
     * @return
     */
    public String ParseDTOofCheckout(CheckoutDTO checkoutDTO){
        int result = checkoutDTO.getResult();
        if (result == ErrorCode.OK){
            return "退房成功";
        }
        return "退房失败";
    }

    /**
     * 解析散客
     * @param guestDTO
     * @return
     */
    public String ParseDTOofGuest(GuestDTO guestDTO){
        int result = guestDTO.getResult();
        if (result == ErrorCode.OK){
            return "入住成功！您的账号为"+guestDTO.getId();
        }
        return "入住失败";
    }

    /**
     * 解析心跳
     * @param heartbeatDTO
     * @return
     */
    public String ParseDTOofHeartbeat(HeartbeatDTO heartbeatDTO){
        int result = heartbeatDTO.getResult();
        if (result == ErrorCode.OK){
            return "加强锻炼，拥有一颗健康的小心脏！";
        }
        return "心脏出了点小问题，请稍候...";
    }

    /**
     * 解析其它信息
     * @param infoDTO
     * @return
     */
    public String ParseDTOofInfo(InfoDTO infoDTO){
        int result = infoDTO.getResult();
        if (result == ErrorCode.OK){
            return infoDTO.getContent();
        }
        return "查询其它信息失败！";
    }

    /**
     * 解析初始化
     * @param initDTO
     * @return
     */
    public String ParseDTOofInit(InitDTO initDTO){
//        InitDTO.Upgrade upgrade = initDTO.getUpgrade();
//        return upgrade;
        int result = initDTO.getResult();
        if(result == ErrorCode.OK){
            InitDTO.Upgrade upgrade = initDTO.getUpgrade();
            String Md5 = upgrade.getMd5();
            long Size = upgrade.getSize();
            String Url = upgrade.getUrl();
            int Version = upgrade.getVersion();
            String InitInfo = "Md5："+Md5+"\n"+"Size："+Size+"\n"+"Url："+Url+"\n"+"Version："+Version;
            return InitInfo;
        }
        return "初始化失败！";
    }
}
