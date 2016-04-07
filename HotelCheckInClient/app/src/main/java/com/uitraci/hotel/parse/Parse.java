package com.uitraci.hotel.parse;

import com.google.gson.Gson;

import com.uitraci.hotel.dto.BaseDTO;

/**
 * @author 刘凯
 * Created on 2016/4/4 23:50.
 */
public class Parse{
    public <T extends BaseDTO> T parseResponse(Class<T> clazz, String response){
        Gson gson = new Gson();
        T t = gson.fromJson(response,clazz);
        return t;
    }

    public <T extends BaseDTO> Object parseDto(T t){
        Object object = null;
        return object;
    }
}
