package com.uitraci.hotel.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Created by LuoXin on 2016/4/5.
 */
public class BtPrintService {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final byte[][] commands = {{0x1b, 0x40}, // 0.复位打印机
            {0x1b, 0x4d, 0x00}, // 1.标准ASCII字体
            {0x1b, 0x4d, 0x01}, // 2.压缩ASCII字体
            {0x1d, 0x21, 0x00}, // 3.字体不放大
            {0x1d, 0x21, 0x11}, // 4.宽高加倍
            {0x1b, 0x45, 0x00}, // 5.取消加粗模式
            {0x1b, 0x45, 0x01}, // 6.选择加粗模式
            {0x1d, 0x42, 0x00}, // 7.取消黑白反显
            {0x1d, 0x42, 0x01}, // 8.选择黑白反显
    };

    private static BtPrintService btService;

    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private InputStream is;
    private OutputStream os;

    private BtPrintService() {
        adapter = BluetoothAdapter.getDefaultAdapter();

    }

    public static BtPrintService getBtService() {
        if (btService == null) {
            btService = new BtPrintService();
        }
        return btService;
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    public List<Map<String, String>> getBondedDevices() {
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        Iterator<BluetoothDevice> it = adapter.getBondedDevices().iterator();
        while (it.hasNext()) {
            BluetoothDevice device = it.next();
            Map<String, String> obj = new HashMap<String, String>();
            obj.put("name", device.getName());
            obj.put("address", device.getAddress());
            retList.add(obj);
        }
        return retList;
    }

    public void connect(String address) throws IOException {
        BluetoothDevice device = adapter.getRemoteDevice(address);
        socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
        socket.connect();
        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    public void disconnect() throws IOException {
        if (os != null) {
            os.close();
            os = null;
        }
        if (is != null) {
            is.close();
            is = null;
        }
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    public void send(String data, String charset) {
        try {
            send(data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void send(final byte[] data) {
        new Thread() {
            public void run() {
                try {
                    os.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void printTicket() {
        //酒店名称（大字体加粗）
        send(commands[1]);
        send(commands[4]);
        send(commands[6]);
        //单据名称（大字体）
        send(commands[5]);
        send("入住单", "GBK");
        //节1：打印时间和单据编号
        send(commands[3]);
        //节2：客人信息（会员：姓名、电话、身份证，散客：电话）
        //节3：房间信息（房号、房型、房价、入住时间、退房时间）
        //节4：宾客须知
        //节5：谢谢惠顾（酒店信息）
    }
}
