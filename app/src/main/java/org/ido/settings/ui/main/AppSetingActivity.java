package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

import java.util.List;

public class AppSetingActivity extends BaseActivity {
    private Button btnInstall;
    private TextView textFileName;
    private static int REQUESTCODE_FROM_ACTIVITY = 1000;

    @Override
    public int getLayoutId() {
        return R.layout.activity_app;
    }

    @Override
    public void initView() {
        btnInstall = findViewById(R.id.btnInstall);
        textFileName = findViewById(R.id.textFileName);
    }

    @Override
    public void initEvent() {
        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

    public void pickDocClicked() {
        new LFilePicker()
                .withActivity(AppSetingActivity.this)
//                .withMaxNum(1)
                .withMutilyMode(false)
                .withBackgroundColor("#008577")
//                .withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                .withStartPath("/sdcard/")//指定初始显示路径
                .withFileFilter(new String[]{".apk"})
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                if (CollectionUtils.isNotEmpty(list)) {
                    install(list.get(0));
                    textFileName.setText(String.format("文件名：%s", list.get(0)));
                }
//                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void initData() {

    }

    private void install(String fileName) {
        Intent intent =
                new Intent("android.intent.action.SILENT_INSTALL_PACKAGE");
        intent.putExtra("apkFilePath", fileName);//安装apk 绝对路径
        intent.putExtra("autostart", true);//true:安装完成后自动运行
        sendBroadcast(intent);
    }
}
