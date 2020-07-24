package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

public class TouchActivity extends BaseActivity {
    private Button touchTestBtn;
    @Override
    public int getLayoutId() {
        return R.layout.activity_touch;
    }

    @Override
    public void initView() {
        touchTestBtn = (Button) findViewById(R.id.touchTestBtn);

    }

    @Override
    public void initEvent() {
        touchTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touchTestBtn.getText().toString().equals("打开触摸测试")) {
                    Intent intent = new Intent("android.ido.intent.action.touchtest");
                    intent.putExtra("enable", 1);
                    sendBroadcast(intent);
                    touchTestBtn.setText("关闭触摸测试");
                } else {
                    Intent intent = new Intent("android.ido.intent.action.touchtest");
                    intent.putExtra("enable", 0);
                    sendBroadcast(intent);
                    touchTestBtn.setText("打开触摸测试");
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
