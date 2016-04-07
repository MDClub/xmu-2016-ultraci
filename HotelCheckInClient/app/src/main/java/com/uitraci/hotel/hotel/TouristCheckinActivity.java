package com.uitraci.hotel.hotel;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 散客信息登记
 * @author 谢波
 * create by 2016/3/20 0020 12:43
 */
public class TouristCheckinActivity extends AppCompatActivity {

    private static final String TAG = "TouristCheckinActivity";
    private EditText tourist_checkin_et_phone;
    private SurfaceView tourist_checkin_sv_photo;
    private Button tourist_checkin_btn_takephoto;
    //Camera object
    private Camera mCamera;
    //Preview surface handle for callback
    private SurfaceHolder surfaceHolder;
    //Note if preview windows is on.
    private boolean previewing;

    int mCurrentCamIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_tourist_checkin);

        //监听GridView中9个按钮，控制EditText的文本输入，监听退出键和确认信息键

        InitView();

    }

    private void InitView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("非会员信息登记");
        actionBar.setDisplayHomeAsUpEnabled(true);
        tourist_checkin_et_phone = (EditText) findViewById(R.id.tourist_checkin_et_phone);
        tourist_checkin_et_phone.setInputType(InputType.TYPE_NULL);
        findViewById(R.id.tourist_checkin_btn_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "0");
            }
        });
        findViewById(R.id.tourist_checkin_btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "1");
            }
        });
        findViewById(R.id.tourist_checkin_btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "2");
            }
        });
        findViewById(R.id.tourist_checkin_btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "3");
            }
        });
        findViewById(R.id.tourist_checkin_btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "4");
            }
        });
        findViewById(R.id.tourist_checkin_btn_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "5");
            }
        });
        findViewById(R.id.tourist_checkin_btn_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "6");
            }
        });
        findViewById(R.id.tourist_checkin_btn_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "7");
            }
        });
        findViewById(R.id.tourist_checkin_btn_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "8");
            }
        });
        findViewById(R.id.tourist_checkin_btn_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText(tourist_checkin_et_phone.getText().toString() + "9");
            }
        });
        findViewById(R.id.tourist_checkin_btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourist_checkin_et_phone.setText("");
            }
        });
        //撤销按钮
        findViewById(R.id.tourist_checkin_btn_revoke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = tourist_checkin_et_phone.getText().toString();
                if (str.length() > 0)
                {
                    tourist_checkin_et_phone.setText(str.substring(0, str.length()-1));
                }
            }
        });
        //退出按钮
        findViewById(R.id.tourist_checkin_btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //确认信息按钮
        findViewById(R.id.tourist_checkin_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到付款界面
                Intent intent = new Intent(TouristCheckinActivity.this, PayActivity.class);

                String enterTime = getIntent().getStringExtra("enterTime");
                String exitTime = getIntent().getStringExtra("exitTime");
                int selected_room_index = getIntent().getIntExtra("selected_roomindex", 0);
                int room_price = getIntent().getIntExtra("room_price", 0);

                intent.putExtra("FromWhere", "tourist_checkin");
                intent.putExtra("enterTime",enterTime);
                intent.putExtra("exitTime",exitTime);
                intent.putExtra("selected_room_index",selected_room_index);
                intent.putExtra("room_price",room_price);



                startActivity(intent);
                finish();
            }
        });
        //拍摄身份证按钮
        tourist_checkin_btn_takephoto = (Button) findViewById(R.id.tourist_checkin_btn_takephoto);
        tourist_checkin_btn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewing)
                    mCamera.takePicture(shutterCallback, rawPictureCallback,
                            jpegPictureCallback);
            }
        });
        //拍摄身份证SurfaceView
        tourist_checkin_sv_photo = (SurfaceView) findViewById(R.id.tourist_checkin_sv_photo);
        surfaceHolder = tourist_checkin_sv_photo.getHolder();
        surfaceHolder.addCallback(new SurfaceViewCallback());
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

        }
    };

    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString()
                    + File.separator
                    + "PicTest_" + System.currentTimeMillis() + ".jpg";
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            try {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(file));
                bos.write(arg0);
                bos.flush();
                bos.close();
                scanFileToPhotoAlbum(file.getAbsolutePath());
                Toast.makeText(TouristCheckinActivity.this, "[Test] Photo take and store in" + file.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(TouristCheckinActivity.this, "Picture Failed" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        };
    };

    public void scanFileToPhotoAlbum(String path) {

        MediaScannerConnection.scanFile(TouristCheckinActivity.this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }


    private final class SurfaceViewCallback implements android.view.SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
        {
            if (previewing) {
                mCamera.stopPreview();
                previewing = false;
            }

            try {
                mCamera.setPreviewDisplay(arg0);
                mCamera.startPreview();
                previewing = true;
                setCameraDisplayOrientation(TouristCheckinActivity.this, mCurrentCamIndex, mCamera);
            } catch (Exception e) {}
        }
        public void surfaceCreated(SurfaceHolder holder) {
//				mCamera = Camera.open();
            //change to front camera
            mCamera = openFrontFacingCameraGingerbread();
            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // Autofocus mode is supported
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            previewing = false;
        }
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    //根据横竖屏自动调节preview方向，Starting from API level 14, this method can be called when preview is active.
    private static void setCameraDisplayOrientation(Activity activity,int cameraId, Camera camera)
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        //degrees  the angle that the picture will be rotated clockwise. Valid values are 0, 90, 180, and 270.
        //The starting position is 0 (landscape).
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else
        {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
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
}
