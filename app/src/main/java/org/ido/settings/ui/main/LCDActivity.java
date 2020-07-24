package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ThreadUtils;

import org.ido.iface.DeviceCtrl;
import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LCDActivity extends BaseActivity {

    private List<String> rotates;
    private List<String> dpis;
    private List<String> land;
    private List<String> portrait;

    private Button btnRotate;
    private Button btnDPI;
    private Button btnNav;
    private Button btnStatusBar;
    private Button btnHDMILand;
    private Button btnHDMIPortrait;
    private Button btnLock;
    private Button btnUnlock;
    private DeviceCtrl deviceCtrl = new DeviceCtrl();

    @Override
    public int getLayoutId() {
        return R.layout.activity_lcd;
    }

    @Override
    public void initView() {
        btnRotate = findViewById(R.id.btnRotate);
        btnDPI = findViewById(R.id.btnDPI);
        btnNav = findViewById(R.id.btnNav);
        btnStatusBar = findViewById(R.id.btnStatusBar);
        btnHDMILand = findViewById(R.id.btnHDMILand);
        btnHDMIPortrait = findViewById(R.id.btnHDMIPortrait);
        btnLock = findViewById(R.id.btnLock);
        btnUnlock = findViewById(R.id.btnUnlock);
    }

    @Override
    public void initEvent() {
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(LCDActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        btnRotate.setText(String.format("屏幕旋转（%s)", rotates.get(options1)));

                        Intent mIntent = new Intent("android.ido.intent.action.lcdrotation");
                        mIntent.putExtra("rotation", rotates.get(options1));//0,90,180,270
                        sendBroadcast(mIntent);
                        systemReboot();
                    }
                }).isDialog(ScreenUtils.isLandscape()).build();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
                if (ScreenUtils.isLandscape()) {
                    params.leftMargin = 200;
                    params.rightMargin = 200;
                }
                pvOptions.getDialogContainerLayout().setLayoutParams(params);

                pvOptions.setPicker(dpis);
                pvOptions.setPicker(rotates);
                pvOptions.show(btnRotate);

            }
        });

        btnDPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(LCDActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        btnDPI.setText(String.format("屏幕密度（%s)", dpis.get(options1)));

                        Intent mIntent1 = new Intent("android.ido.intent.action.lcddensity");
                        mIntent1.putExtra("density", dpis.get(options1));//160，240，260，320
                        sendBroadcast(mIntent1);
                        systemReboot();

                    }
                }).isDialog(ScreenUtils.isLandscape()).build();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
                if (ScreenUtils.isLandscape()) {
                    params.leftMargin = 200;
                    params.rightMargin = 200;
                }
                pvOptions.getDialogContainerLayout().setLayoutParams(params);

                pvOptions.setPicker(dpis);
                pvOptions.show(btnRotate);
            }
        });
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (deviceCtrl.get_no_navbar() == 0) {
                    Intent intent = new Intent("android.ido.intent.action.navigation.hide");
                    intent.putExtra("save", false);//保存下次开机后为隐藏
                    sendBroadcast(intent);
                } else if (deviceCtrl.get_no_navbar() == 1) {
                    Intent intent = new Intent("android.ido.intent.action.navigation.show");
                    intent.putExtra("save", true); //保存下次开机后为显示
                    sendBroadcast(intent);
                }
                setButtonText();
            }
        });

        btnStatusBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceCtrl.get_no_statusbar() == 0) {
                    Intent intent = new Intent("android.ido.intent.action.statusbar.hide");
                    sendBroadcast(intent);
                    systemReboot();
                } else if (deviceCtrl.get_no_statusbar() == 1) {
                    Intent intent = new Intent("android.ido.intent.action.statusbar.show");
                    intent.putExtra("hasexpand", true);//true：可下拉，false:禁止下拉
                    sendBroadcast(intent);
                    systemReboot();
                }

            }
        });
        btnHDMILand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(LCDActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        btnHDMILand.setText(String.format("HDMI横屏（%s)", land.get(options1)));

                        Intent mIntent1 = new Intent("android.ido.intent.action.hdmirotation");
                        mIntent1.putExtra("rotation", "land");
                        sendBroadcast(mIntent1);
                        Intent mIntent2 = new Intent("android.ido.intent.action.lcdrotation");
                        mIntent2.putExtra("rotation", land.get(options1));//0,180
                        sendBroadcast(mIntent2);
                        systemReboot();
                    }
                }).isDialog(ScreenUtils.isLandscape()).build();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
                if (ScreenUtils.isLandscape()) {
                    params.leftMargin = 200;
                    params.rightMargin = 200;
                }
                pvOptions.getDialogContainerLayout().setLayoutParams(params);

                pvOptions.setPicker(dpis);
                pvOptions.setPicker(land);
                pvOptions.show(btnHDMILand);

            }
        });
        btnHDMIPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(LCDActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        btnHDMIPortrait.setText(String.format("HDMI竖屏（%s)", portrait.get(options1)));

                        Intent mIntent1 = new Intent("android.ido.intent.action.hdmirotation");
                        mIntent1.putExtra("rotation", "portrait");
                        sendBroadcast(mIntent1);
                        Intent mIntent2 = new Intent("android.ido.intent.action.lcdrotation");
                        mIntent2.putExtra("rotation", portrait.get(options1));//90,270
                        sendBroadcast(mIntent2);
                        systemReboot();
                    }
                }).isDialog(ScreenUtils.isLandscape()).build();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
                if (ScreenUtils.isLandscape()) {
                    params.leftMargin = 200;
                    params.rightMargin = 200;
                }
                pvOptions.getDialogContainerLayout().setLayoutParams(params);

                pvOptions.setPicker(dpis);
                pvOptions.setPicker(portrait);
                pvOptions.show(btnHDMIPortrait);

            }
        });
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent("android.ido.action.lcd.orientation.locked");
                mIntent.putExtra("locked", "1");  //"1"：锁定UI方向，"0"：取消锁定UI方向
                sendBroadcast(mIntent);
            }
        });
        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent("android.ido.action.lcd.orientation.locked");
                mIntent.putExtra("locked", "0");  //"1"：锁定UI方向，"0"：取消锁定UI方向
                sendBroadcast(mIntent);
            }
        });
    }


    @Override
    public void initData() {
        rotates = new ArrayList<>();
        rotates.add("0");
        rotates.add("90");
        rotates.add("180");
        rotates.add("270");
        dpis = new ArrayList<>();
        dpis.add("160");
        dpis.add("240");
        dpis.add("260");
        dpis.add("320");
        land = new ArrayList<>();
        land.add("0");
        land.add("180");
        portrait = new ArrayList<>();
        portrait.add("90");
        portrait.add("270");

        int rotate = deviceCtrl.get_sf_hwrotation();//SPStaticUtils.getInt(ROTATES, 0);
        int dpi = deviceCtrl.get_lcd_density();//SPStaticUtils.getInt(DPI, 0);
        btnRotate.setText(String.format("屏幕旋转（%s)", rotate));
        btnDPI.setText(String.format("屏幕密度（%s)", dpi));

        if (deviceCtrl.get_no_navbar() == 0) {
            btnNav.setText("隐藏导航栏");
        } else if (deviceCtrl.get_no_navbar() == 1) {
            btnNav.setText("显示导航栏");
        } else {
            btnNav.setText("导航栏 参数错误");
            btnNav.setEnabled(false);
        }

        if (deviceCtrl.get_no_statusbar() == 0) {
            btnStatusBar.setText("隐藏状态栏");
        } else if (deviceCtrl.get_no_statusbar() == 1) {
            btnStatusBar.setText("显示状态栏");
        } else {
            btnStatusBar.setText("状态栏 参数错误");
            btnStatusBar.setEnabled(false);
        }
        setButtonText();
    }

    private void setButtonText(){
        ThreadUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                if (deviceCtrl.get_no_navbar() == 0) {
                    btnNav.setText("隐藏导航栏");
                } else if (deviceCtrl.get_no_navbar() == 1) {
                    btnNav.setText("显示导航栏");
                } else {
                    btnNav.setText("导航栏 参数错误");
                    btnNav.setEnabled(false);
                }

                if (deviceCtrl.get_no_statusbar() == 0) {
                    btnStatusBar.setText("隐藏状态栏");
                } else if (deviceCtrl.get_no_statusbar() == 1) {
                    btnStatusBar.setText("显示状态栏");
                } else {
                    btnStatusBar.setText("状态栏 参数错误");
                    btnStatusBar.setEnabled(false);
                }
            }
        }, 1000);
    }

    //confirm:ture-会弹出是否关机的确认窗口，false-无弹框，直接重启
    public void systemReboot() {
        Intent intent = new Intent("android.ido.intent.action.set.reboot");
        intent.putExtra("confirm", true);
        sendBroadcast(intent);
    }
}
