package com.flyjingfish.titlebar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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
        TextView textView = new TextView(this);
        textView.setText("left");
        TextView textView1 = new TextView(this);
        textView1.setText("right");
        titleBar.setCustomLeftView(textView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        titleBar.setCustomRightView(textView1,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
