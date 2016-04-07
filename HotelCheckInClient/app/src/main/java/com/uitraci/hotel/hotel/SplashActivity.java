package com.uitraci.hotel.hotel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.uitraci.hotel.voice.LoginVoice;

public class SplashActivity extends Activity {

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        //检查版本升级
        checkUpdate();

//        enterToLogin();


        //延迟2秒进入主页面
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                enterToLogin();
                //检查是否第一次启动应用，是则进入向导页面，否则进入主页面
//                boolean is_first_start = sp.getBoolean("is_first_start", true);
//                if (is_first_start)
//                {
//                    //跳转到向导界面
//                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                } else{
//
//
//                }
            }
        }.start();

    }

    private void checkUpdate() {
        //TODO:检查更新
    }

    private void enterToLogin() {
        Intent intent = new Intent(this, SwingCardActivity.class);
        intent.putExtra("FromWhere","staff_login_out");
        startActivity(intent);
        finish();

    }

}
