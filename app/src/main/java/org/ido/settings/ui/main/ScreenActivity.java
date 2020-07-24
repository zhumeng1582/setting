package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

public class ScreenActivity extends BaseActivity {
    private Button btnScreen1;
    private Button btnScreen2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_screen;
    }

    @Override
    public void initView() {
        btnScreen1 = findViewById(R.id.btnScreen1);
        btnScreen2 = findViewById(R.id.btnScreen2);
    }

    @Override
    public void initEvent() {
        btnScreen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.SCREENSHOT");
                sendBroadcast(intent);
            }
        });

        btnScreen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initData() {

    }
}
