package org.ido.settings.ui.main;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;
import org.ido.settings.utils.DeviceUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {
    private Button btnOnOff;
    private Button btnLCD;
    private Button btnScreen;
    private Button btnNetwork;
    private Button btnAppSetting;
    private Button btnTimeSetting;
    private Button btnPort;
    private Button btnTouch;
    private Button btnBeer;
    private Button btnLeft;
    private Button btnRight;
    private Button btnElse;

    private MediaPlayer mpLeft;
    private MediaPlayer mpRight;

    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        btnOnOff = findViewById(R.id.btnOnOff);
        btnLCD = findViewById(R.id.btnLCD);
        btnScreen = findViewById(R.id.btnScreen);
        btnNetwork = findViewById(R.id.btnNetwork);
        btnAppSetting = findViewById(R.id.btnAppSetting);
        btnTimeSetting = findViewById(R.id.btnTimeSetting);
        btnPort = findViewById(R.id.btnPort);
        btnTouch = findViewById(R.id.btnTouch);
        btnBeer = findViewById(R.id.btnBeer);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnElse = findViewById(R.id.btnElse);
    }

    @Override
    public void initEvent() {
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SetOffAndOnActivity.class));
            }
        });
        btnElse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OtherActivity.class));
            }
        });
        btnLCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LCDActivity.class));
            }
        });
        btnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SCREENSHOT");
                sendBroadcast(intent);
            }
        });
        btnTimeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, TimeActivity.class));
                showTimePicker(btnTimeSetting, "", new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String dateTime = TimeUtils.date2String(date);

                        String year = dateTime.substring(0, 4);
                        String month = dateTime.substring(5, 7);
                        String day = dateTime.substring(8, 10);
                        String hour = dateTime.substring(11, 13);
                        String minute = dateTime.substring(14, 16);
                        String second = dateTime.substring(17, 19);

                        int[] time = {Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second)};
                        Intent mIntent1 = new Intent("android.ido.intent.action.settime");
                        mIntent1.putExtra("time", time);
                        sendBroadcast(mIntent1);
                    }
                });
            }
        });
        btnPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PortActivity.class));
            }
        });
        btnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NetworkActivity.class));
            }
        });
        btnAppSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AppSetingActivity.class));
                PermissionUtils.permission(PermissionConstants.STORAGE)
                        .rationale(new PermissionUtils.OnRationaleListener() {
                            @Override
                            public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
                                ToastUtils.showShort("请在设置中开启文件权限");
                            }
                        })
                        .callback(new PermissionUtils.SingleCallback() {
                            @Override
                            public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
                                if (isAllGranted) {
                                    pickDocClicked();
                                } else {
                                    ToastUtils.showShort("文件权限被拒绝");
                                }
                            }
                        }).request();
            }
        });
        btnTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnTouch.getText().toString().equals("打开触摸测试")) {
                    Intent intent = new Intent("android.ido.intent.action.touchtest");
                    intent.putExtra("enable", 1);
                    sendBroadcast(intent);
                    btnTouch.setText("关闭触摸测试");
                } else {
                    Intent intent = new Intent("android.ido.intent.action.touchtest");
                    intent.putExtra("enable", 0);
                    sendBroadcast(intent);
                    btnTouch.setText("打开触摸测试");
                }
            }
        });
        btnBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtil.setBuzzerOn(true);
                ThreadUtils.runOnUiThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DeviceUtil.setBuzzerOn(false);
                    }
                }, 150);
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLeft();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRight();
            }
        });

//
    }

    private void showTimePicker(View v, String time, OnTimeSelectListener listener) {

        TimePickerView pvTim = new TimePickerBuilder(this, listener)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .setSubmitText("确定")
                .setCancelText("取消")
                .isDialog(ScreenUtils.isLandscape())//是否显示为对话框样式
                .build();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        if (ScreenUtils.isLandscape()) {
            params.leftMargin = 200;
            params.rightMargin = 200;
        }
        pvTim.getDialogContainerLayout().setLayoutParams(params);
        pvTim.show(v);
    }


    public void playLeft() {

        if (mpLeft != null && mpLeft.isPlaying()) {
            mpLeft.pause();
        } else {
            try {
                if (mpLeft != null) {
                    mpLeft.release();
                    mpLeft = null;
                }
                AssetFileDescriptor fdLeft = getAssets().openFd("dbl.wav");
                mpLeft = new MediaPlayer();
                mpLeft.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mpLeft.setDataSource(fdLeft.getFileDescriptor(), fdLeft.getStartOffset(), fdLeft.getLength());
                mpLeft.prepare();
                mpLeft.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void playRight() {

        if (mpRight != null && mpRight.isPlaying()) {
            mpRight.pause();
        } else {
            try {
                if (mpRight != null) {
                    mpRight.release();
                    mpRight = null;
                }
                AssetFileDescriptor fdRight = getAssets().openFd("dbr.wav");
                mpRight = new MediaPlayer();
                mpRight.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mpRight.setDataSource(fdRight.getFileDescriptor(), fdRight.getStartOffset(), fdRight.getLength());
                mpRight.prepare();
                mpRight.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void stopMusic() {
        if (mpLeft != null) {
            mpLeft.stop();
            mpLeft.release();
            mpLeft = null;
        }

        if (mpRight != null) {
            mpRight.stop();
            mpRight.release();
            mpRight = null;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
    }
    public void pickDocClicked() {
        FileChooser fileChooser = new FileChooser(MainActivity.this, new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                ToastUtils.showShort("正在安装...");
                install(filePath);
            }
        });
        fileChooser.setTitle("请选择安装包");
        fileChooser.setDoneText("确定");
//        fileChooser.setBackIconRes(R.);
        fileChooser.setThemeColor(R.color.colorPrimaryDark);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_APK);
        fileChooser.showFile(true);  //是否显示文件
        fileChooser.open();

    }

    private void install(String fileName) {
        Intent intent =
                new Intent("android.intent.action.SILENT_INSTALL_PACKAGE");
        intent.putExtra("apkFilePath", fileName);//安装apk 绝对路径
        intent.putExtra("autostart", true);//true:安装完成后自动运行
        sendBroadcast(intent);
    }
}
