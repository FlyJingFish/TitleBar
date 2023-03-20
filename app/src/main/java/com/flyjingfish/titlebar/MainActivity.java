package com.flyjingfish.titlebar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.flyjingfish.titlebarlib.StatusBarHelper;
import com.flyjingfish.titlebarlib.TitleBar;

public class MainActivity extends BaseActivity {

    @Override
    public String getTitleString() {
        return "首页";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        titleBar = findViewById(R.id.title_bar);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                StatusBarHelper.translucent(this);
                StatusBarHelper.setStatusBarLightMode(this);
                titleBar.setTitleGravity(TitleBar.TitleGravity.START);
                titleBar.setTitleBarBackgroundColorWithStatusBar(Color.RED);
                titleBar.getRightImageView().setVisibility(View.GONE);
                titleBar.showShadow();
                break;
            case R.id.btn_2:
                titleBar.setTitleGravity(TitleBar.TitleGravity.CENTER);
                titleBar.getRightTextView().setVisibility(View.GONE);
                titleBar.setTitleBarBackgroundColorWithStatusBar(Color.WHITE);
                titleBar.getRightImageView().setOnClickListener(v -> Toast.makeText(v.getContext(),"more",Toast.LENGTH_SHORT).show());
                titleBar.hideShadow();
                break;
            case R.id.btn_3:
                titleBar.getRightTextView().setVisibility(View.VISIBLE);
                titleBar.setTitleGravity(TitleBar.TitleGravity.END);
                titleBar.getRightTextView().setText("11111");
                titleBar.getRightTextView().setTextColor(Color.BLUE);
                break;
        }
    }
}