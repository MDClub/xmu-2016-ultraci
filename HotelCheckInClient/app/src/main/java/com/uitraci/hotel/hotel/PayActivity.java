package com.uitraci.hotel.hotel;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uitraci.hotel.constant.Action;
import com.uitraci.hotel.dto.CheckinDTO;
import com.uitraci.hotel.parse.ParseClient;
import com.uitraci.hotel.parse.ParseDTOofClient;
import com.uitraci.hotel.service.ClientService;
import com.uitraci.hotel.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 付款界面
 * @author 谢波
 * create by 2016/3/22 0022 11:05
 * @author KAI
 * edit on 2016/03/31 16:00
 */
public class PayActivity extends AppCompatActivity {

    private static String token = "cVko8367";
    private static String signature;
    private static ClientService clientService= new ClientService();
    private static ParseClient parseClient = new ParseClient();
    private static ParseDTOofClient parseDTOofClient = new ParseDTOofClient();
    private static Utils utils = new Utils();

    private String customer;
    private String room;
    private String time;

    /**liukai**/
    private TextView payactivity_tv_minute;
    private TextView payactivity_tv_second;
    private Button payactivity_btn_confirm;

    private int minute = 15;
    private int second = 0;
    private int countsecond = 0 ;
    private int sumsecond = minute*60+second ;
    /**liukai**/

    private TextView payactivity_tv_roomnum;
    private String fromWhere;
    private TextView payactivity_tv_usertype;
    private TextView payactivity_tv_roomtype;
    private TextView payactivity_tv_entertime;
    private TextView payactivity_tv_exittime;


    private AlertDialog dialog;
    private TextView payactivity_tv_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_pay);
        fromWhere = getIntent().getStringExtra("FromWhere");

        InitView();

        handler.postDelayed(runnable,1000);//liukai
    }

    private void InitView() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("付款界面");
        actionBar.setDisplayHomeAsUpEnabled(true);

        /**liukai**/
        payactivity_tv_minute = (TextView) findViewById(R.id.payactivity_tv_minute);
        payactivity_tv_second = (TextView) findViewById(R.id.payactivity_tv_second);
        payactivity_btn_confirm = (Button) findViewById(R.id.payactivity_btn_confirm);

        payactivity_tv_minute.setText(String.format("%02d",minute));
        payactivity_tv_second.setText(String.format("%02d", second));
        /**liukai**/

        findViewById(R.id.payactivity_btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payactivity_tv_roomnum = (TextView) findViewById(R.id.payactivity_tv_roomnum);//房间ID
        payactivity_tv_usertype = (TextView) findViewById(R.id.payactivity_tv_usertype);//用户类型
        payactivity_tv_roomtype = (TextView) findViewById(R.id.payactivity_tv_roomtype);//房间类型
        payactivity_tv_entertime = (TextView) findViewById(R.id.payactivity_tv_entertime);//入住时间
        payactivity_tv_exittime = (TextView) findViewById(R.id.payactivity_tv_exittime);//退房时间
        payactivity_tv_money = (TextView) findViewById(R.id.payactivity_tv_money);

        //根据不同的数据来源更新界面上的值
        if (fromWhere.equals("member_checkin"))
        {
            int room_index = getIntent().getIntExtra("selected_roomindex", 0);
            String enterTime = getIntent().getStringExtra("enterTime");
            String exitTime = getIntent().getStringExtra("exitTime");
            int room_price = getIntent().getIntExtra("room_price", 0);


            Date time1 = null;
            Date time2 = null;
            try {
                time1 = new SimpleDateFormat("yyyy-MM-dd").parse(enterTime);
                time2 = new SimpleDateFormat("yyyy-MM-dd").parse(exitTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long day = (time1.getTime()-time2.getTime())/(24*60*60*1000);


            room = String.valueOf(room_index);
            customer = "3";
            time = exitTime;


            payactivity_tv_roomnum.setText(room_index+"");
            payactivity_tv_usertype.setText("会员");
            //缺个手机号
            payactivity_tv_entertime.setText(enterTime);
            payactivity_tv_exittime.setText(exitTime);
            payactivity_tv_money.setText("￥"+-room_price*day+"元");


        }else if (fromWhere.equals("tourist_checkin"))
        {
            int room_index = getIntent().getIntExtra("selected_roomindex", 0);
            String enterTime = getIntent().getStringExtra("enterTime");
            String exitTime = getIntent().getStringExtra("exitTime");
            int room_price = getIntent().getIntExtra("room_price", 0);



            Date time1 = null;
            Date time2 = null;
            try {
                time1 = new SimpleDateFormat("yyyy-MM-dd").parse(enterTime);
                time2 = new SimpleDateFormat("yyyy-MM-dd").parse(exitTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long day = (time1.getTime()-time2.getTime())/(24*60*60*1000);

            room = String.valueOf(room_index);
            customer = "3";
            time = exitTime;

            payactivity_tv_roomnum.setText(room_index+"");
            payactivity_tv_usertype.setText("非会员");

            payactivity_tv_entertime.setText(enterTime);
            payactivity_tv_exittime.setText(exitTime);
            payactivity_tv_money.setText("￥"+-room_price*day+"元");
        }

        findViewById(R.id.payactivity_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayAlertDialog();
            }
        });
    }

    /**
     * edit by liukai
     */
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countsecond++;
            if (second == 0){
                second = 59 ;
                minute--;
                payactivity_tv_second.setText(String.format("%02d",second));
                payactivity_tv_minute.setText(String.format("%02d",minute));
            }
            else{
                second--;
                payactivity_tv_second.setText(String.format("%02d",second));
                payactivity_tv_minute.setText(String.format("%02d",minute));
            }
            if (countsecond == sumsecond){
                handler.removeCallbacks(this,0);
                payactivity_btn_confirm.setEnabled(false);
                payactivity_btn_confirm.setBackgroundColor(Color.GRAY);
                payactivity_btn_confirm.setText("停止支付");
            }
            if (countsecond != sumsecond)            {
                handler.postDelayed(this,1000);
            }
        }
    };
    
    private void PayAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
        //加载自定义的布局文件
        View view = View.inflate(PayActivity.this, R.layout.dialog_in_payactivity, null);

        TextView dialog_pay_tv_tip = (TextView)view.findViewById(R.id.dialog_pay_tv_tip);
        dialog_pay_tv_tip.setText("是否确认付款");

        //取消按钮
        view.findViewById(R.id.dialog_pay_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认按钮
        view.findViewById(R.id.dialog_pay_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayActivity.this, "付款成功", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                finish();
                //uploadCheckinInfo();
            }
        });

        dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    //处理actionbar上面的返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadCheckinInfo(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String random = utils.RandomGen();
                 signature = utils.SHA1(utils.DictOrder(random, token));
                CheckinDTO checkinDTO = parseClient.ParseCheckin(random, signature, Action.CHECK_IN, "666666", customer, room, time);
                if (checkinDTO.getResult() == 0){
                    Toast.makeText(PayActivity.this,"入住成功",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PayActivity.this,"楼层信息解析失败",Toast.LENGTH_LONG).show();
                }
            }
        });
        t.start();
    }
}
