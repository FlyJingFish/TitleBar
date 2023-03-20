package com.flyjingfish.titlebar;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.flyjingfish.titlebarlib.TitleBar;

public class BaseActivity extends AppCompatActivity {
    protected TitleBar titleBar;

    public boolean isShowTitleBar(){
        return true;
    }

    public String getTitleString(){
        return "";
    }

    public boolean titleAboveContent(){
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBar = new TitleBar(this);
        titleBar.setShadow(4, Color.parseColor("#406200EE"), TitleBar.ShadowType.GRADIENT);
        titleBar.setTitleGravity(TitleBar.TitleGravity.CENTER);
        titleBar.setOnBackViewClickListener(v -> finish());
        if (isShowTitleBar()){
            titleBar.show();
        }else {
            titleBar.hide();
        }
        titleBar.setTitle(getTitleString());
        titleBar.setAboveContent(titleAboveContent());
        titleBar.attachToWindow();
    }
}
