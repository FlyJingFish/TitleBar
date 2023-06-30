package com.flyjingfish.titlebar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyjingfish.titlebarlib.TitleBar;

public class MainActivity extends BaseActivity {

    private TitleBar titleBar2;

    @Override
    public String getTitleString() {
        return "这里是标题";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        titleBar2 = findViewById(R.id.title_bar);
//        StatusBarHelper.translucent(this);
//        StatusBarHelper.setStatusBarLightMode(this);
        View view = new View(this);
        view.setBackgroundColor(Color.RED);
//        titleBar2.setCustomView(view );
//        titleBar2.setBackgroundColor(Color.RED);
//        titleBar.setShadow(20,Color.BLACK, TitleBar.ShadowType.GRADIENT);
        Log.d("TitleBar","onCreate-getStatusbarHeight"+StatusBarHelper.getStatusbarHeight(this));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                StatusBarHelper.translucent(this);
                StatusBarHelper.setStatusBarLightMode(this);
                titleBar.setStatusBarBackgroundColor(Color.BLUE);
                titleBar.setTitleGravity(TitleBar.TitleGravity.START);
                titleBar.setTitleBarBackgroundColorWithStatusBar(Color.BLACK);
                titleBar.getRightImageView().setVisibility(View.GONE);
                titleBar.setDisplayShadow(true);
                titleBar.setAboveContent(false);
                titleBar.getBackTextView().setText("back");

                break;
            case R.id.btn_2:
                titleBar.setTitleGravity(TitleBar.TitleGravity.CENTER);
                titleBar.getRightTextView().setVisibility(View.GONE);
                titleBar.setTitleBarBackgroundColorWithStatusBar(Color.WHITE);
                titleBar.getRightImageView().setOnClickListener(v -> Toast.makeText(v.getContext(),"more",Toast.LENGTH_SHORT).show());
//                titleBar.hideShadow();
                titleBar.setAboveContent(true);
                ViewGroup content = findViewById(android.R.id.content);
                int[] contentLat = new int[2];
                content.getLocationOnScreen(contentLat);
                Log.e("getLocationOnScreen",contentLat[0]+"=="+contentLat[1]);
                Rect rect = new Rect();
                content.getLocalVisibleRect(rect);
                Log.e("getLocationOnScreen",content.getTop()+"");
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