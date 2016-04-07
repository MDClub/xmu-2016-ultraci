package com.uitraci.hotel.hotel;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uitraci.hotel.voice.LoginVoice;

/**
 * 刷卡
 *
 * @author 谢波
 *         create by 2016/3/18 0018 17:02
 * @author LuoXin
 *         revision at 2016/4/4
 */
public class SwingCardActivity extends AppCompatActivity {
    private AlertDialog dialog;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_swing_card);

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mFilters = new IntentFilter[] { ndef };
        mTechLists = new String[][] { new String[] { NfcA.class.getName() } };


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    private void enterToSelectTimeFromMenber() {
        Intent intent = new Intent(SwingCardActivity.this, SelectTimeActivity.class);
        intent.putExtra("FromWhere", "member_checkin");
        startActivity(intent);
        finish();
    }

    private void enterTOSelectTimeFromTourist() {
        Intent intent = new Intent(SwingCardActivity.this, SelectTimeActivity.class);
        intent.putExtra("FromWhere", "tourist_checkin");
        startActivity(intent);
        finish();
    }

    private void enterToVoice(String cardid) {
        Intent intent = new Intent(this, LoginVoice.class);
        intent.putExtra("cardid", cardid);
        startActivity(intent);
        finish();
    }

    private void enterToHome() {
        finish();
        Toast.makeText(SwingCardActivity.this, "退房成功", Toast.LENGTH_SHORT).show();
    }

    private String getIntentStringExtra() {
        return getIntent().getStringExtra("FromWhere");
    }

    /**
     * 刷卡失败对话框
     */
    private void show_fail_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SwingCardActivity.this);
        View view = View.inflate(SwingCardActivity.this, R.layout.dialog_show_failed, null);

        TextView dialog_failed_tv_tip = (TextView) view.findViewById(R.id.dialog_failed_tv_tip);
        dialog_failed_tv_tip.setText("刷卡失败，请稍后重试");

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

    /**
     * 退房对话框
     */
    private void show_exit_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SwingCardActivity.this);
        View view = View.inflate(SwingCardActivity.this, R.layout.dialog_in_payactivity, null);

        TextView dialog_pay_tv_tip = (TextView)view.findViewById(R.id.dialog_pay_tv_tip);
        dialog_pay_tv_tip.setText("确认要退房吗");

        Button dialog_pay_btn_ok = (Button) view.findViewById(R.id.dialog_pay_btn_ok);
        dialog_pay_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                enterToHome();
            }
        });

        view.findViewById(R.id.dialog_pay_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    //进入选择续订日期界面
    private void enterToSelectRepeatTime() {
        Intent intent = new Intent(SwingCardActivity.this, RepeatOrderActivity.class);
        startActivity(intent);
        intent.putExtra("FromWhere", "repeatorder");
        finish();
    }

    //TODO:NFC卡是否有效,刷NFC卡的代码在这写
    private boolean is_Validate_NFC() {
        return true;
    }

    public void onNewIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // do something with tagFromIntent
        byte[] id = tagFromIntent.getId();
        Toast.makeText(this, byte2HexString(id), Toast.LENGTH_LONG).show();
        //刷卡成功跳转到其他界面，刷卡不成功弹出提示对话框
//        if (is_Validate_NFC()) {

            String fromWhere = getIntentStringExtra();
            if (fromWhere.equals("staff_login_out")) {
                enterToVoice(byte2HexString(id));
            } else if (fromWhere.equals("member_checkin")) {            //如果是从会员入住跳转过来的
                enterToSelectTimeFromMenber();
            } else if (fromWhere.equals("tourist_checkin")) {     //如果是从散客入住跳转过来的
                enterTOSelectTimeFromTourist();
            } else if (fromWhere.equals("repeatorder")) {         //如果是从续订跳转过来的
                enterToSelectRepeatTime();
            } else if (fromWhere.equals("exitroom")) {            //如果是从退房跳转过来的
                enterToHome();
            }

//        } else {
//            //弹出对话框:刷卡失败
//            show_fail_dialog();
//        }
    }

    public String byte2HexString(byte[] bytes) {
        String ret = "";
        if (bytes != null) {
            for (Byte b : bytes) {
                ret += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return ret;
    }
}
