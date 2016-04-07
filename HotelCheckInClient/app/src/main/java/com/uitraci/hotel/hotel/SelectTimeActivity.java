package com.uitraci.hotel.hotel;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uitraci.hotel.utils.DatePickerController;
import com.uitraci.hotel.utils.DayPickerView;
import com.uitraci.hotel.utils.LimitQueue;

import java.util.Queue;

/**
 * 选择入住时间和退房时间界面
 * @author 谢波
 * create by 2016/3/31 0031 16:02
 */
public class SelectTimeActivity extends AppCompatActivity implements DatePickerController {

    private AlertDialog dialog;
    private DayPickerView dayPickerView;
    private String fromWhere;
    private int flag_date_is_range = 0;//用来判断选择的入住时间和退房时间是不是一个区间
    private LimitQueue<String> dateQueue = new LimitQueue<String>(2);

    private TextView selecttime_tv_minute;
    private TextView selecttime_tv_second;
    private Button selecttime_btn_confirm;

    private int minute = 15;
    private int second = 0;
    private int countsecond = 0 ;
    private int sumsecond = minute*60+second ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_select_time);

        InitView();

        handler.postDelayed(runnable, 1000);//liukai
    }

    private void InitView() {
        fromWhere = getIntent().getStringExtra("FromWhere");
        dayPickerView = (DayPickerView) findViewById(R.id.pickerView);
        dayPickerView.setmController(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("选择入住时间和退房时间");
        actionBar.setDisplayHomeAsUpEnabled(true);

        /**liukai**/
        selecttime_tv_minute = (TextView) findViewById(R.id.selecttime_tv_minute);
        selecttime_tv_second = (TextView) findViewById(R.id.selecttime_tv_second);
        selecttime_btn_confirm = (Button) findViewById(R.id.selecttime_btn_confirm);

        selecttime_tv_minute.setText(String.format("%02d", minute));
        selecttime_tv_second.setText(String.format("%02d", second));
        /**liukai**/

        //确认选择按钮
        //TODO:要把选择的时间数据传递给下一个界面
        findViewById(R.id.selecttime_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromWhere.equals("member_checkin"))//如果是从会员入住界面跳转过来的
                {
                    if (flag_date_is_range == 1)
                    {
                        Intent intent = new Intent(SelectTimeActivity.this, SelectRoomActivity.class);
                        intent.putExtra("FromWhere", "member_checkin");

                        //获取到两个时间
                        String[] time = {"0", "0"};
                        int temp = 0;
                        for (String string : dateQueue){
                            time[temp] = string;
                            temp++;
                        }
                        String enterTime = time[0];
                        String exitTime = time[1];
                        intent.putExtra("enterTime", enterTime);
                        intent.putExtra("exitTime", exitTime);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SelectTimeActivity.this, "请选择正确的入住日期和退房日期", Toast.LENGTH_SHORT).show();
                    }

                }else if(fromWhere.equals("tourist_checkin"))//如果是从散客入住界面跳转过来的
                {
                    if (flag_date_is_range == 1) {
                        Intent intent = new Intent(SelectTimeActivity.this, SelectRoomActivity.class);
                        intent.putExtra("FromWhere", "tourist_checkin");
                        //获取到两个时间
                        String[] time = {"0", "0"};
                        int temp = 0;
                        for (String string : dateQueue){
                            time[temp] = string;
                            temp++;
                        }
                        String enterTime = time[0];
                        String exitTime = time[1];
                        intent.putExtra("enterTime", enterTime);
                        intent.putExtra("exitTime", exitTime);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SelectTimeActivity.this, "请选择正确的入住日期和退房日期", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    //这个返回值已经没什么用了
    @Override
    public int getMaxYear()
    {
        return 2017;
    }

    //TODO：感觉这里可以添加一个flag参数，判断用户点击的是不是一个区间
    @Override
    public void onDayOfMonthSelected(int year, int month, int day, int flag)
    {
        Log.e("Day Slected", day + " / " + month + " / " + year);
        flag_date_is_range = flag;
        dateQueue.offer(year+"-"+month+"-"+day);
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
                selecttime_tv_second.setText(String.format("%02d",second));
                selecttime_tv_minute.setText(String.format("%02d",minute));
            }
            else{
                second--;
                selecttime_tv_second.setText(String.format("%02d",second));
                selecttime_tv_minute.setText(String.format("%02d",minute));
            }
            if (countsecond == sumsecond){
                handler.removeCallbacks(this,0);
                selecttime_btn_confirm.setEnabled(false);
                selecttime_btn_confirm.setBackgroundColor(Color.GRAY);
                selecttime_btn_confirm.setText("时间到，停止操作");
                show_fail_dialog();
            }
            if (countsecond != sumsecond)            {
                handler.postDelayed(this,1000);
            }
        }
    };

    /**
     * 时间到了之后弹出的额对话框
     */
    private void show_fail_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectTimeActivity.this);
        View view = View.inflate(SelectTimeActivity.this, R.layout.dialog_show_failed, null);

        TextView dialog_failed_tv_tip = (TextView) view.findViewById(R.id.dialog_failed_tv_tip);
        dialog_failed_tv_tip.setText("时间已到，请回到主页面");

        Button dialog_failed_btn_ok = (Button) view.findViewById(R.id.dialog_failed_btn_ok);
        dialog_failed_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }
}
