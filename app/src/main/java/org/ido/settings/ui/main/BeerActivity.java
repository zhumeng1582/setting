package org.ido.settings.ui.main;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ThreadUtils;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BeerActivity extends BaseActivity {
    private Button btnBeer;
    @Override
    public int getLayoutId() {
        return R.layout.activity_beer;
    }

    @Override
    public void initView() {
        btnBeer = findViewById(R.id.btnBeer);
    }

    @Override
    public void initEvent() {
        btnBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuzzerOn(true);
                ThreadUtils.runOnUiThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setBuzzerOn(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void initData() {

    }
    private void setBuzzerOn(boolean on) {
        String path = "/dev/bz0";
        if (new File(path).exists()) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(path);
                if (on)
                    writer.write("ON ");
                else
                    writer.write("OFF ");
                writer.flush();
            } catch (Exception ex) {
                Log.e("setBuzzerOn", "" + ex);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Log.e("setBuzzerOn", "" + ex);
                    }
                }
            }
        } else {
            Log.d("setBuzzerOn", "buzzer node is not found!");
        }
    }
}
