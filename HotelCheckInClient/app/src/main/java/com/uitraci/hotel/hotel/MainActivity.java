package com.uitraci.hotel.hotel;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * @author 谢波
 * create by 2016/3/18 0018 13:08
 */
public class MainActivity extends AppCompatActivity {

    private ImageView main_iv_member_checkin;
    private ImageView main_iv_tourist_checkin;
    private ImageView main_iv_repeatorder;
    private ImageView main_iv_exitroom;
    private ImageView main_iv_selectroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);

        //初始化一些控件
        InitView();
    }

    private void InitView() {
        setTitle("酒店自助入住终端");
        //响应会员入住事件
        main_iv_member_checkin = (ImageView) findViewById(R.id.main_iv_member_checkin);
        main_iv_member_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了会员入住按钮之后跳转到刷卡界面
                Intent intent = new Intent(MainActivity.this, SwingCardActivity.class);
                intent.putExtra("FromWhere", "member_checkin");
                startActivity(intent);

            }
        });

        //响应散客入住事件
        main_iv_tourist_checkin = (ImageView) findViewById(R.id.main_iv_tourist_checkin);
        main_iv_tourist_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了散客入住按钮之后跳转到刷卡界面
//                Intent intent = new Intent(MainActivity.this, SwingCardActivity.class);
//                intent.putExtra("FromWhere", "tourist_checkin");
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, SelectTimeActivity.class);
                intent.putExtra("FromWhere", "tourist_checkin");
                startActivity(intent);
                //finish();

            }
        });

        //响应续订房间事件
        main_iv_repeatorder = (ImageView) findViewById(R.id.main_iv_repeatorder);
        main_iv_repeatorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了续订房间按钮之后跳转到刷卡界面
                Intent intent = new Intent(MainActivity.this, SwingCardActivity.class);
                intent.putExtra("FromWhere", "repeatorder");
                startActivity(intent);

            }
        });

        //响应退房事件
        main_iv_exitroom = (ImageView) findViewById(R.id.main_iv_exitroom);
        main_iv_exitroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了退房按钮之后跳转到刷卡界面
                Intent intent = new Intent(MainActivity.this, SwingCardActivity.class);
                intent.putExtra("FromWhere", "exitroom");
                startActivity(intent);

            }
        });

        //响应查询房间信息事件
        main_iv_selectroom = (ImageView) findViewById(R.id.main_iv_selectroom);
        main_iv_selectroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了查询按钮之后跳转到显示房态图界面
                Intent intent = new Intent(MainActivity.this, SelectRoomActivity.class);
                intent.putExtra("FromWhere", "selectroom");
                startActivity(intent);
            }
        });
    }


    //跳转到刷卡界面
    private void enterToSwingcard() {
        Intent intent = new Intent(MainActivity.this, SwingCardActivity.class);
        startActivity(intent);
    }

    //跳转到显示房态图界面
    private void enterToRoomState() {

    }
}
