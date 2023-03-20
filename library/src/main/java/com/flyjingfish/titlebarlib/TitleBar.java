package com.flyjingfish.titlebarlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


public class TitleBar extends RelativeLayout {
    private final ConstraintLayout titleBarContainer;
    private final ImageView titleBarStatusBar;
    private final ImageView backView;
    private final TextView titleView;
    private final FrameLayout customViewContainer;
    private final ShadowLine shadowLine;
    private final FrameLayout rightContainer;
    private final FrameLayout leftContainer;
    private boolean isLayout;
    private boolean isSetTitleGravity;
    private static final TitleGravity DEFAULT_TITLE_GRAVITY = TitleGravity.CENTER;
    private TitleGravity titleGravity;
    private final ImageView backgroundView;

    public enum ShadowType {
        LINE,
        GRADIENT
    }

    public enum TitleGravity {
        CENTER,
        START,
        END
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true);
        backView = rootView.findViewById(R.id.iv_title_bar_back);
        backgroundView = rootView.findViewById(R.id.iv_title_bar_bg);
        titleBarContainer = rootView.findViewById(R.id.fl_title_bar_container);
        titleBarStatusBar = rootView.findViewById(R.id.iv_title_bar_status_bar);
        titleView = rootView.findViewById(R.id.tv_title_bar_title);
        customViewContainer = rootView.findViewById(R.id.fl_custom_view);
        shadowLine = rootView.findViewById(R.id.shadow_line);
        rightContainer = rootView.findViewById(R.id.right_container);
        leftContainer = rootView.findViewById(R.id.left_container);
        int statusBarHeight = StatusBarHelper.getStatusbarHeight(context);
        ViewGroup.LayoutParams layoutParams = titleBarStatusBar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        titleBarStatusBar.setLayoutParams(layoutParams);

        backView.setOnClickListener(v -> ((Activity) context).finish());

        setTitleGravity(DEFAULT_TITLE_GRAVITY);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setTitleBarPaddings();
        isLayout = true;
        if (isSetTitleGravity) {
            setTitleGravity(titleGravity);
            isSetTitleGravity = false;
        }
    }

    public void attachToWindow() {
        if (getContext() instanceof LifecycleOwner) {
            ((LifecycleOwner) getContext()).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        settingView();
                        source.getLifecycle().removeObserver(this);
                    }
                }
            });
        } else {
            settingView();
        }
    }

    private void settingView() {
        ViewParent viewParent = getParent();
        if (viewParent != null) {
            ((ViewGroup) viewParent).removeView(this);
        }

        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setTitleBarPaddings();

    }

    private void setTitleBarPaddings(){
        ViewParent viewParent = getParent();
        if (!(getContext() instanceof Activity)){
            return;
        }
        View windowView = ((Activity) getContext()).getWindow().getDecorView();
        if (viewParent != windowView){
            return;
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup content = ((Activity) getContext()).findViewById(android.R.id.content);
                int[] contentLat = new int[2];
                content.getLocationOnScreen(contentLat);
                int paddingTop = (int) (contentLat[1] == 0 ? TitleBar.this.getHeight() - shadowLine.getShadowMaxLength() : titleBarContainer.getHeight());
                content.setPadding(0, paddingTop, 0, 0);

                int leftMargin = contentLat[0];
                TitleBar.this.setPadding(leftMargin, 0, leftMargin > 0 ? 0 : TitleBar.this.getWidth() - content.getWidth(), 0);

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public TextView getRightTextView() {
        TextView rightTextView;
        if (rightContainer.getChildCount() == 0 || (rightTextView = rightContainer.findViewById(R.id.tv_right_view)) == null) {
            rightTextView = LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar_right_text_view, rightContainer, true).findViewById(R.id.tv_right_view);
        }
        rightContainer.removeView(rightContainer.findViewById(R.id.iv_right_view));
        return rightTextView;
    }

    public ImageView getRightImageView() {
        ImageView rightImageView;
        if (rightContainer.getChildCount() == 0 || (rightImageView = rightContainer.findViewById(R.id.iv_right_view)) == null) {
            rightImageView = LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar_right_image_view, rightContainer, true).findViewById(R.id.iv_right_view);
        }
        rightContainer.removeView(rightContainer.findViewById(R.id.tv_right_view));
        return rightImageView;
    }

    public ImageView getBackView() {
        return backView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setShadow(float shadowHeightDp, @ColorInt int shadowColor, ShadowType shadowType) {
        int[] colors;
        if (shadowType == ShadowType.GRADIENT) {
            colors = new int[]{shadowColor, Color.TRANSPARENT};
        } else {
            colors = new int[]{shadowColor, shadowColor};
        }
        shadowLine.setGradientColors(colors);
        shadowLine.setShadowMaxLength(ScreenUtils.dp2px(getContext(), shadowHeightDp));
    }

    public void hideShadow() {
        shadowLine.setVisibility(GONE);
    }

    public void showShadow() {
        shadowLine.setVisibility(VISIBLE);
    }

    @Override
    public void setBackground(Drawable background) {
        setTitleBarBackgroundWithStatusBar(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        setTitleBarBackgroundColorWithStatusBar(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        setTitleBarBackgroundResourceWithStatusBar(resid);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        setTitleBarBackgroundWithStatusBar(background);
    }

    public void setTitleBarBackgroundWithStatusBar(Drawable background) {
        backgroundView.setBackground(background);
    }

    public void setTitleBarBackgroundResourceWithStatusBar(int resid) {
        backgroundView.setBackgroundResource(resid);
    }

    public void setTitleBarBackgroundColorWithStatusBar(int color) {
        backgroundView.setBackgroundColor(color);
    }

    public void setTitleBarBackground(Drawable background) {
        titleBarContainer.setBackground(background);
    }

    public void setTitleBarBackgroundResource(int resid) {
        titleBarContainer.setBackgroundResource(resid);
    }

    public void setTitleBarBackgroundColor(int color) {
        titleBarContainer.setBackgroundColor(color);
    }

    public void setCustomView(View view) {
        setCustomView(view, null);
    }

    public void setCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        customViewContainer.removeAllViews();
        if (layoutParams != null) {
            customViewContainer.addView(view, layoutParams);
        } else {
            customViewContainer.addView(view);
        }
    }

    public void setCustomRightView(View view) {
        setCustomRightView(view, null);
    }

    public void setCustomRightView(View view, FrameLayout.LayoutParams layoutParams) {
        rightContainer.removeAllViews();
        if (layoutParams != null) {
            rightContainer.addView(view, layoutParams);
        } else {
            rightContainer.addView(view);
        }
    }

    public void setCustomLeftView(View view) {
        setCustomLeftView(view, null);
    }

    public void setCustomLeftView(View view, FrameLayout.LayoutParams layoutParams) {
        leftContainer.removeAllViews();
        if (layoutParams != null) {
            leftContainer.addView(view, layoutParams);
        } else {
            leftContainer.addView(view);
        }
    }

    public void setTitle(CharSequence text) {
        if (titleView != null) {
            titleView.setText(text);
        }
    }

    public void setTitle(@StringRes int resid) {
        if (titleView != null) {
            titleView.setText(resid);
        }
    }

    public void setTitleColor(@ColorInt int color) {
        if (titleView != null) {
            titleView.setTextColor(color);
        }
    }

    public void setTitleGravity(TitleGravity gravity) {
        titleGravity = gravity;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) titleView.getLayoutParams();
        if (gravity == TitleGravity.START) {
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.startToEnd = R.id.left_container;
            layoutParams.endToStart = R.id.right_container;
            layoutParams.horizontalBias = 0;
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
        } else if (gravity == TitleGravity.END) {
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.startToEnd = R.id.left_container;
            layoutParams.endToStart = R.id.right_container;
            layoutParams.horizontalBias = 1f;
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
        } else {
            if (!isLayout) {
                isSetTitleGravity = true;
                return;
            }
            int margin = Math.max(backView.getWidth(), rightContainer.getWidth());
            layoutParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.endToStart = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.horizontalBias = .5f;
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.leftMargin = margin;
            layoutParams.rightMargin = margin;
        }
        titleView.setLayoutParams(layoutParams);
    }

    public void setOnBackViewClickListener(OnClickListener listener) {
        if (backView != null) {
            backView.setOnClickListener(listener);
        }
    }

    public void setOnBackViewLongClickListener(OnLongClickListener listener) {
        if (backView != null) {
            backView.setOnLongClickListener(listener);
        }
    }

    public void setOnRightViewClickListener(OnClickListener listener) {
        if (rightContainer != null) {
            rightContainer.setOnClickListener(listener);
        }
    }

    public void setOnRightViewLongClickListener(OnLongClickListener listener) {
        if (rightContainer != null) {
            rightContainer.setOnLongClickListener(listener);
        }
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }
}
