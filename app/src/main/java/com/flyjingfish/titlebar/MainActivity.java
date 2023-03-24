package com.flyjingfish.titlebar;

import android.graphics.Color;
import android.os.Bundle;
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
        titleBar2 = findViewById(R.id.title_bar);
        View view = new View(this);
        view.setBackgroundColor(Color.RED);
//        titleBar2.setCustomView(view );
//        titleBar2.setBackgroundColor(Color.RED);
        titleBar.setShadow(20,Color.BLACK, TitleBar.ShadowType.GRADIENT);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                StatusBarHelper.translucent(this);
                StatusBarHelper.setStatusBarLightMode(this);
                titleBar.setTitleGravity(TitleBar.TitleGravity.START);
                titleBar.setTitleBarBackgroundColorWithStatusBar(Color.RED);
                titleBar.getRightImageView().setVisibility(View.GONE);
                titleBar.setDisplayShadow(true);
                titleBar.setAboveContent(false);

                break;
            case R.id.btn_2:
                titleBar.setTitleGravity(TitleBar.TitleGravity.CENTER);
                titleBar.getRightTextView().setVisibility(View.GONE);
                titleBar.setTitleBarBackgroundColorWithStatusBar(Color.WHITE);
                titleBar.getRightImageView().setOnClickListener(v -> Toast.makeText(v.getContext(),"more",Toast.LENGTH_SHORT).show());
//                titleBar.hideShadow();
                titleBar.setAboveContent(true);
                break;
            case R.id.btn_3:
                titleBar.getRightTextView().setVisibility(View.VISIBLE);
                titleBar.setTitleGravity(TitleBar.TitleGravity.END);
                titleBar.getRightTextView().setText("11111");
                titleBar.getRightTextView().setTextColor(Color.BLUE);

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) titleBar.getTitleBarLayoutParams();
                layoutParams.topMargin = (int) ScreenUtils.dp2px(this,30);
                titleBar.setLayoutParams(layoutParams);
                break;
        }
    }
}