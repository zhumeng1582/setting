package org.ido.settings.ui.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initEvent();
        initData();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initEvent();

    public abstract void initData();
}
