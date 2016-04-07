package com.uitraci.hotel.voice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;
import com.uitraci.hotel.hotel.R;
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 刘凯
 */
public class RegisterVoice extends Activity implements OnClickListener {

    private static final int PWD_TYPE_NUM = 3;
    // 当前声纹密码类型，3为数字密码
    public int pwdType = PWD_TYPE_NUM;
    // 声纹识别对象
    public SpeakerVerifier mVerify;
    // 声纹uid
    public String uid;
    // 数字声纹密码
    public String mNumPwd = "";
    // 数字声纹密码段，默认有5段
    public String[] mNumPwdSegs;

    public SharedPreferences sp;

    EditText edt_result_rv;
    Button btn_getpassword_rv, btn_stoprecord_rv, btn_rv, btn_cancelrv,btn_returnlv;
    TextView tv_uid_rv, tv_showPwd_rv, tv_showMsg_rv, tv_showRegFbk_rv;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_voice);
        SpeechUtility
                .createUtility(this, "appid=" + getString(R.string.app_id));

        initview();

//        sp = getSharedPreferences("config", 0);
        uid = getIntent().getStringExtra("uid");
        Toast.makeText(this,"uid="+uid,Toast.LENGTH_SHORT).show();
//        tv_uid_rv.setText(uid);
        // 初始化SpeakerVerifier，InitListener为初始化完成后的回调接口
        mVerify = SpeakerVerifier.createVerifier(this, new InitListener() {

            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    showTip("引擎初始化成功");
                } else {
                    showTip("引擎初始化失败，错误码：" + errorCode);
                }
            }
        });
    }

    /**
     * 绑定组件,设置监听
     */
    @SuppressLint("ShowToast")
    public void initview() {
        edt_result_rv = (EditText) findViewById(R.id.edt_result_rv);
        btn_getpassword_rv = (Button) findViewById(R.id.btn_getpassword_rv);
        btn_stoprecord_rv = (Button) findViewById(R.id.btn_stoprecord_rv);
        btn_rv = (Button) findViewById(R.id.btn_rv);
        btn_cancelrv = (Button) findViewById(R.id.btn_cancelrv);
        btn_returnlv = (Button) findViewById(R.id.btn_returnlv);
        tv_uid_rv = (TextView) findViewById(R.id.tv_uid_rv);
        tv_showPwd_rv = (TextView) findViewById(R.id.tv_showPwd_rv);
        tv_showMsg_rv = (TextView) findViewById(R.id.tv_showMsg_rv);
        tv_showRegFbk_rv = (TextView) findViewById(R.id.tv_showRegFbk_rv);

        btn_getpassword_rv.setOnClickListener((OnClickListener) this);
        btn_stoprecord_rv.setOnClickListener((OnClickListener) this);
        btn_rv.setOnClickListener((OnClickListener) this);
        btn_cancelrv.setOnClickListener((OnClickListener) this);
        btn_returnlv.setOnClickListener((OnClickListener) this);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /**
     * 清空文本
     */
    public void initTextview() {
        edt_result_rv.setText("");
        tv_showPwd_rv.setText("");
        tv_showMsg_rv.setText("");
        tv_showRegFbk_rv.setText("");
    }

    // 执行模型操作
    @SuppressWarnings("unused")
    private void performModelOperation(String operation, SpeechListener listener) {
        // 清空参数
        mVerify.setParameter(SpeechConstant.PARAMS, null);
        mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);

        mVerify.sendRequest(operation, uid, listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getpassword_rv:
                // 获取密码之前要终止之前的注册过程
                mVerify.cancel(false);
                initTextview();
                // 清空参数
                mVerify.setParameter(SpeechConstant.PARAMS, null);
                mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);
                mVerify.getPasswordList(mPwdListenter);
                break;

            case R.id.btn_stoprecord_rv:
                mVerify.stopListening();
                break;

            case R.id.btn_rv:
                // 清空参数
                mVerify.setParameter(SpeechConstant.PARAMS, null);
                mVerify.setParameter(SpeechConstant.ISV_AUDIOPATH, Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/msc/test.pcm");
                // 数字密码注册需要传入密码
                if (TextUtils.isEmpty(mNumPwd)) {
                    showTip("请获取密码后进行操作");
                    return;
                }
                mVerify.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
                ((TextView) findViewById(R.id.tv_showPwd_rv)).setText("请读出："
                        + mNumPwd.substring(0, 8));
                tv_showMsg_rv.setText("训练 第" + 1 + "遍，剩余4遍");
                mVerify.setParameter(SpeechConstant.ISV_AUTHID, uid);
                // 设置业务类型为注册
                mVerify.setParameter(SpeechConstant.ISV_SST, "train");
                // 设置声纹密码类型
                mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);
                // 开始注册
                mVerify.startListening(mRegisterListener);
                break;

            case R.id.btn_cancelrv:
                mVerify.cancel(false);
                initTextview();
                break;

            case R.id.btn_returnlv:
                finish();

            default:
                break;
        }
    }

    String[] items;
    SpeechListener mPwdListenter = new SpeechListener() {
        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

            String result = new String(buffer);
            StringBuffer numberString = new StringBuffer();
            try {
                JSONObject object = new JSONObject(result);
                if (!object.has("num_pwd")) {
                    initTextview();
                    return;
                }

                JSONArray pwdArray = object.optJSONArray("num_pwd");
                numberString.append(pwdArray.get(0));
                for (int i = 1; i < pwdArray.length(); i++) {
                    numberString.append("-" + pwdArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mNumPwd = numberString.toString();
            mNumPwdSegs = mNumPwd.split("-");
            edt_result_rv.setText("您的密码：\n" + mNumPwd);

        }

        @Override
        public void onCompleted(SpeechError error) {

            if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
                showTip("获取失败：" + error.getErrorCode());
            }
        }
    };





    VerifierListener mRegisterListener = new VerifierListener() {

        @Override
        public void onVolumeChanged(int volume) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onResult(VerifierResult result) {
            ((TextView) findViewById(R.id.tv_showMsg_rv)).setText(result.source);
            if (result.ret == ErrorCode.SUCCESS) {
                switch (result.err) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        tv_showMsg_rv.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
                        tv_showRegFbk_rv.setText("训练达到最大次数");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        tv_showRegFbk_rv.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        tv_showRegFbk_rv.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        tv_showRegFbk_rv.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        tv_showRegFbk_rv.setText("训练失败，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        tv_showRegFbk_rv.setText("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        tv_showMsg_rv.setText("音频长达不到自由说的要求");
                    default:
                        tv_showRegFbk_rv.setText("");
                        break;
                }

                if (result.suc == result.rgn) {
                    tv_showMsg_rv.setText("注册成功");
                    edt_result_rv.setText("您的数字密码声纹ID：\n" + result.vid);
                    finish();
                } else {
                    int nowTimes = result.suc + 1;
                    int leftTimes = result.rgn - nowTimes;
                    tv_showPwd_rv.setText("请读出："
                            + mNumPwdSegs[nowTimes - 1]);

                    tv_showMsg_rv.setText("训练 第" + nowTimes + "遍，剩余"
                            + leftTimes + "遍");
                }

            } else {

                tv_showMsg_rv.setText("注册失败，请重新开始。");
            }
        }

        // 保留方法，暂不用
        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onError(SpeechError error) {
            if (error.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
                showTip("模型已存在，如需重新注册，请先删除");
            } else {
                showTip("onError Code：" + error.getErrorCode());
            }
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            showTip("开始说话");
        }
    };

    @Override
    protected void onDestroy() {
        if (null != mVerify) {
            mVerify.stopListening();
            mVerify.destroy();
        }
        super.onDestroy();
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}

