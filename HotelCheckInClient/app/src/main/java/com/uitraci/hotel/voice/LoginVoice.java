package com.uitraci.hotel.voice;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;
import com.uitraci.hotel.hotel.MainActivity;
import com.uitraci.hotel.hotel.R;
import com.uitraci.hotel.parse.ParseClient;
import com.uitraci.hotel.parse.ParseDTOofClient;
import com.uitraci.hotel.service.ClientService;
import com.uitraci.hotel.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uitraci.hotel.constant.Action;
import com.uitraci.hotel.dto.LoginDTO;

import static android.widget.Toast.*;

/**
 * @author 刘凯
 */
public class LoginVoice extends Activity implements OnClickListener {
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

    private static String token = "cVko8367";
    private static String signature;
    private static ClientService clientService= new ClientService();
    private static ParseClient parseClient = new ParseClient();
    private static ParseDTOofClient parseDTOofClient = new ParseDTOofClient();
    private static Utils utils = new Utils();
    private static LoginDTO loginDTO;

    Button btn_getuid_loginvoice, btn_verify_loginvoice,
            btn_search_loginvoice,
            btn_delete_loginvoice;
    TextView Tv_Register_Voice, tv_uid_lv, tv_showPwd_lv, tv_showMsg_lv;
    EditText edt_uid_loginvoice;
    Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_voice);
        InitView();
        SpeechUtility.createUtility(this, "appid="
                + getString(R.string.app_id));
        // 初始化SpeakerVerifier，InitListener为初始化完成后的回调接口
        mVerify = SpeakerVerifier.createVerifier(this,
                new InitListener() {
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
     *
     */
    public void requestLogin(final String cardid){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String random = utils.RandomGen();
                signature = utils.SHA1(utils.DictOrder(random, token));
//                final String response = clientService.Login(random, signature, Action.LOGIN, "666666", cardid);
                loginDTO = parseClient.ParseLogin(random, signature, Action.LOGIN, "666666", cardid);
                final String returninfo = parseDTOofClient.ParseDTOofLogin(loginDTO);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loginDTO.getResult() == 0) {
                            uid = cardid;
                            edt_uid_loginvoice.setText(returninfo);
//                            Intent intent = new Intent(LoginVoice.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }else{
                            Toast.makeText(LoginVoice.this,"登录失败",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        });
        t.start();
    }

    public void InitView(){
        requestLogin(getIntent().getStringExtra("cardid"));
        Tv_Register_Voice = (TextView)findViewById(R.id.Tv_Register_Voice);
        tv_uid_lv = (TextView)findViewById(R.id.tv_uid_lv);
        tv_showPwd_lv = (TextView)findViewById(R.id.tv_showPwd_lv);
        tv_showMsg_lv = (TextView)findViewById(R.id.tv_showMsg_lv);
        edt_uid_loginvoice = (EditText)findViewById(R.id.edt_uid_loginvoice);
        btn_getuid_loginvoice = (Button)findViewById(R.id.btn_getuid_loginvoice);
        btn_verify_loginvoice = (Button)findViewById(R.id.btn_verify_loginvoice);
        btn_search_loginvoice = (Button)findViewById(R.id.btn_search_loginvoice);
        btn_delete_loginvoice = (Button)findViewById(R.id.btn_delete_loginvoice);

        mToast = makeText(this, "", LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        Tv_Register_Voice.setOnClickListener((OnClickListener) this);
        btn_getuid_loginvoice.setOnClickListener((OnClickListener) this);
        btn_verify_loginvoice.setOnClickListener((OnClickListener) this);
        btn_search_loginvoice.setOnClickListener((OnClickListener) this);
        btn_delete_loginvoice.setOnClickListener((OnClickListener) this);
    }

    // 执行模型操作
    private void performModelOperation(String operation, SpeechListener
            listener) {
        // 清空参数
        mVerify.setParameter(SpeechConstant.PARAMS, null);
        mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);

        mVerify.sendRequest(operation, uid, listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Tv_Register_Voice:
                Intent intent = new Intent(this, RegisterVoice.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;

            case R.id.btn_getuid_loginvoice:
                uid = edt_uid_loginvoice.getText().toString().trim();
                if (uid.equals("")) {
                    edt_uid_loginvoice.setError("用户名不能为空");
                }
                tv_uid_lv.setText(uid);
                saveUserInfo(LoginVoice.this, uid);
                break;

            case R.id.btn_verify_loginvoice:
                // 清空提示信息
                tv_showMsg_lv.setText("");
                // 清空参数
                mVerify.setParameter(SpeechConstant.PARAMS, null);
                mVerify.setParameter(SpeechConstant.ISV_AUDIOPATH, Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/msc/verify.pcm");
                mVerify = SpeakerVerifier.getVerifier();
                // 设置业务类型为验证
                mVerify.setParameter(SpeechConstant.ISV_SST, "verify");

                // 数字密码注册需要传入密码
                String verifyPwd = mVerify.generatePassword(8);
                mVerify.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
                tv_showPwd_lv.setText("请读出：" + verifyPwd);
                mVerify.setParameter(SpeechConstant.ISV_AUTHID, uid);
                mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);
                // 开始验证
                mVerify.startListening(mVerifyListener);
                break;

            case R.id.btn_search_loginvoice:
                performModelOperation("que", mModelOperationListener);
                break;

            case R.id.btn_delete_loginvoice:
                performModelOperation("del", mModelOperationListener);
                break;

            default:
                break;
        }
    }

    SpeechListener mModelOperationListener = new SpeechListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

            String result = new String(buffer);
            try {
                JSONObject object = new JSONObject(result);
                String cmd = object.getString("cmd");
                int ret = object.getInt("ret");

                if ("del".equals(cmd)) {
                    if (ret == ErrorCode.SUCCESS) {
                        showTip("删除成功");
                    } else if (ret == ErrorCode.MSP_ERROR_FAIL) {
                        showTip("删除失败，模型不存在");
                    }
                } else if ("que".equals(cmd)) {
                    if (ret == ErrorCode.SUCCESS) {
                        showTip("模型存在");
                    } else if (ret == ErrorCode.MSP_ERROR_FAIL) {
                        showTip("模型不存在");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCompleted(SpeechError error) {

            if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
                showTip("操作失败：" + error.getErrorCode());
            }
        }
    };

    VerifierListener mVerifyListener = new VerifierListener() {

        @Override
        public void onVolumeChanged(int volume) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onResult(VerifierResult result) {

            tv_showMsg_lv.setText(result.source);
            if (result.ret == 0) {
                // 验证通过
                Intent intent = new Intent(LoginVoice.this,MainActivity.class);
                startActivity(intent);
                finish();
                tv_showMsg_lv.setText("验证通过");
                saveUserInfo(LoginVoice.this, uid);
            } else {
                // 验证不通过
                switch (result.err) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        tv_showMsg_lv.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        tv_showMsg_lv.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        tv_showMsg_lv.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        tv_showMsg_lv.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        tv_showMsg_lv.setText("验证不通过，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        tv_showMsg_lv.setText("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        tv_showMsg_lv.setText("音频长达不到自由说的要求");
                        break;
                    default:
                        tv_showMsg_lv.setText("验证不通过");
                        break;
                }
            }
        }

        // 保留方法，暂不用
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onError(SpeechError error) {

            switch (error.getErrorCode()) {
                case ErrorCode.MSP_ERROR_NOT_FOUND:
                    tv_showMsg_lv.setText("模型不存在，请先注册");
                    break;

                default:
                    showTip("onError Code：" + error.getErrorCode());
                    break;
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

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpeechUtility.createUtility(this, "appid="
                + getString(R.string.app_id));
        // 初始化SpeakerVerifier，InitListener为初始化完成后的回调接口
        mVerify = SpeakerVerifier.createVerifier(this,
                new InitListener() {
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
     *
     * @param context
     * @param uid
     */
    public static void saveUserInfo(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.commit();
    }
}

