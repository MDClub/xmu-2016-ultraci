package com.uitraci.hotel.hotel;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

/**
 * 续住界面
 * @author 谢波
 * create by 2016/3/26 0026 20:32
 * @author 刘凯
 * edit at 2016/03/31 16:00
 */
public class RepeatOrderActivity extends AppCompatActivity {
    private Calendar calendar_repeatorder;
    private Button repeatorder_btn_selectdate;

    /**liukai**/
    private TextView repeatorder_tv_minute;
    private TextView repeatorder_tv_second;
    private Button repeatorder_btn_confirm;

    private int minute = 15;
    private int second = 0;
    private int countsecond = 0 ;
    private int sumsecond = minute*60+second ;
    /**liukai**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_repeat_order);
        //初始化一些控件
        InitView();

        handler.postDelayed(runnable, 1000);//liukai
    }

    private void InitView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("续住");
        actionBar.setDisplayHomeAsUpEnabled(true);
        /**liukai**/
        repeatorder_tv_minute = (TextView) findViewById(R.id.repeatorder_tv_minute);
        repeatorder_tv_second = (TextView) findViewById(R.id.repeatorder_tv_second);
        repeatorder_btn_confirm = (Button) findViewById(R.id.repeatorder_btn_confirm);

        repeatorder_tv_minute.setText(String.format("%02d",minute));
        repeatorder_tv_second.setText(String.format("%02d",second));
        /**liukai**/

        calendar_repeatorder = Calendar.getInstance(Locale.CHINA);

        //退出按钮
        findViewById(R.id.repeatorder_btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //选择日期按钮
        repeatorder_btn_selectdate = (Button)findViewById(R.id.repeatorder_btn_selectdate);
        repeatorder_btn_selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RepeatOrderActivity.this,
                        DateSet,
                        calendar_repeatorder.get(Calendar.YEAR),
                        calendar_repeatorder.get(Calendar.MONTH),
                        calendar_repeatorder.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("选择续住时间");
                datePickerDialog.show();
            }
        });

        //TODO:确认支付按钮
        findViewById(R.id.repeatorder_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 日期设置匿名类
     */
    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 每次保存设置的日期
            calendar_repeatorder.set(Calendar.YEAR, year);
            calendar_repeatorder.set(Calendar.MONTH, monthOfYear);
            calendar_repeatorder.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            repeatorder_btn_selectdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

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
                repeatorder_tv_second.setText(String.format("%02d",second));
                repeatorder_tv_minute.setText(String.format("%02d",minute));
            }
            else{
                second--;
                repeatorder_tv_second.setText(String.format("%02d",second));
                repeatorder_tv_minute.setText(String.format("%02d",minute));
            }
            if (countsecond == sumsecond){
                handler.removeCallbacks(this,0);
                repeatorder_btn_confirm.setEnabled(false);
                repeatorder_btn_confirm.setBackgroundColor(Color.GRAY);
                repeatorder_btn_confirm.setText("停止支付");
            }
            if (countsecond != sumsecond)            {
                handler.postDelayed(this,1000);
            }
        }
    };
    //处理actionbar上面的返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
