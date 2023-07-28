package com.flyjingfish.titlebarlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
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
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


public class TitleBar extends RelativeLayout {
    private final ConstraintLayout titleBarContainer;
    private final ImageView titleBarStatusBar;
    private final TextView titleView;
    private final FrameLayout customViewContainer;
    private final ShadowView shadowView;
    private final FrameLayout rightContainer;
    private final FrameLayout leftContainer;
    private boolean aboveContent = true;
    private boolean isLayout;
    private boolean isSetTitleGravity;
    private static final TitleGravity DEFAULT_TITLE_GRAVITY = TitleGravity.CENTER;
    private TitleGravity titleGravity;
    private final ImageView backgroundView;
    private Drawable pendingSetBackground;
    private int rightTextStyle;
    private int rightImageStyle;
    private int backImageStyle;
    private int backTextStyle;
    private TextView backTextView;
    private ImageView backImageView;
    private TextView rightTextView;
    private ImageView rightImageView;
    private final PaddingViewTreeObserver paddingViewTreeObserver = new PaddingViewTreeObserver();

    public enum ShadowType {
        NONE(0),
        LINE(1),
        GRADIENT(2);

        final int type;

        ShadowType(int type) {
            this.type = type;
        }

        public static ShadowType getType(int type) {
            if (type == 1) {
                return LINE;
            } else if (type == 2) {
                return GRADIENT;
            } else {
                return NONE;
            }
        }
    }

    public enum TitleGravity {
        START(0),
        CENTER(1),
        END(2);
        final int type;

        TitleGravity(int type) {
            this.type = type;
        }

        public static TitleGravity getGravity(int gravity) {
            if (gravity == 1) {
                return CENTER;
            } else if (gravity == 2) {
                return END;
            } else {
                return START;
            }
        }
    }

    public enum LeftRightType {
        NONE(0),
        TEXT(1),
        IMAGE(2);

        final int type;

        LeftRightType(int type) {
            this.type = type;
        }

        public static LeftRightType getType(int type) {
            if (type == 1) {
                return TEXT;
            } else if (type == 2) {
                return IMAGE;
            } else {
                return NONE;
            }
        }
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true);
        backgroundView = rootView.findViewById(R.id.iv_title_bar_bg);
        titleBarContainer = rootView.findViewById(R.id.fl_title_bar_container);
        titleBarStatusBar = rootView.findViewById(R.id.iv_title_bar_status_bar);
        int titleStyle = a.getResourceId(R.styleable.TitleBar_title_bar_title_style, R.style.title_bar_title_style);
        titleView = LayoutInflater.from(new ContextThemeWrapper(getContext(), titleStyle)).inflate(R.layout.layout_title_bar_title_view, titleBarContainer, true).findViewById(R.id.tv_title_bar_title);
        setTitleViewParams(titleView, titleStyle);
        customViewContainer = rootView.findViewById(R.id.fl_custom_view);
        shadowView = rootView.findViewById(R.id.shadow_line);
        rightContainer = rootView.findViewById(R.id.right_container);
        leftContainer = rootView.findViewById(R.id.left_container);

        int statusBarHeight = StatusBarHelper.getStatusbarHeight(getContext());
        ViewGroup.LayoutParams layoutParams = titleBarStatusBar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        titleBarStatusBar.setLayoutParams(layoutParams);

        setOnBackViewClickListener(v -> ((Activity) context).finish());

        if (pendingSetBackground != null) {
            setBackground(pendingSetBackground);
        }
        int minHeight = a.getDimensionPixelOffset(R.styleable.TitleBar_android_minHeight, getResources().getDimensionPixelOffset(R.dimen.title_bar_minHeight));
        setMinimumHeight(0);
        titleBarContainer.setMinHeight(minHeight);

        Drawable statusBarBackground = a.getDrawable(R.styleable.TitleBar_title_bar_status_bar_background);
        setStatusBarBackground(statusBarBackground);
        if (isInEditMode() && statusBarBackground != null) {
            titleBarStatusBar.setVisibility(VISIBLE);
        }

        Drawable backgroundWithStatusBar = a.getDrawable(R.styleable.TitleBar_title_bar_background_with_status_bar);
        setTitleBarBackgroundWithStatusBar(backgroundWithStatusBar);

        ShadowType shadowType = ShadowType.getType(a.getInt(R.styleable.TitleBar_title_bar_shadow_type, ShadowType.NONE.type));
        Drawable shadowColor = a.getDrawable(R.styleable.TitleBar_title_bar_shadow);
        float shadowHeight = a.getDimension(R.styleable.TitleBar_title_bar_shadow_height, getResources().getDimension(R.dimen.title_bar_shadow_default_height));
        if (shadowColor instanceof ColorDrawable) {
            setShadowPixel(shadowHeight, ((ColorDrawable) shadowColor).getColor(), shadowType);
        } else if (shadowColor == null && shadowType != ShadowType.NONE) {
            setShadowPixel(shadowHeight, ContextCompat.getColor(context, R.color.title_bar_shadow_default_color), shadowType);
        } else {
            setShadowPixel(shadowHeight, shadowColor, shadowType);
        }

        LeftRightType rightType = LeftRightType.getType(a.getInt(R.styleable.TitleBar_title_bar_right_type, LeftRightType.NONE.type));
        LeftRightType leftType = LeftRightType.getType(a.getInt(R.styleable.TitleBar_title_bar_back_type, LeftRightType.IMAGE.type));
        TitleGravity titleGravity = TitleGravity.getGravity(a.getInt(R.styleable.TitleBar_title_bar_title_gravity, DEFAULT_TITLE_GRAVITY.type));
        setTitleGravity(titleGravity);

        CharSequence titleText = a.getText(R.styleable.TitleBar_title_bar_title);
        if (titleText != null) {
            titleView.setText(titleText);
        }
        rightTextStyle = a.getResourceId(R.styleable.TitleBar_title_bar_right_textView_style, R.style.title_bar_right_text_style);
        rightImageStyle = a.getResourceId(R.styleable.TitleBar_title_bar_right_imageView_style, R.style.title_bar_right_image_style);
        backImageStyle = a.getResourceId(R.styleable.TitleBar_title_bar_back_imageView_style, R.style.title_bar_back_image_style);
        backTextStyle = a.getResourceId(R.styleable.TitleBar_title_bar_back_textView_style, R.style.title_bar_back_text_style);

        int backLayoutRes = a.getResourceId(R.styleable.TitleBar_title_bar_back_layout, 0);
        int titleLayoutRes = a.getResourceId(R.styleable.TitleBar_title_bar_title_layout, 0);
        int rightLayoutRes = a.getResourceId(R.styleable.TitleBar_title_bar_right_layout, 0);

        if (titleLayoutRes != 0) {
            View titleLayout = LayoutInflater.from(context).inflate(titleLayoutRes, null, false);
            setCustomView(titleLayout);
        }


        if (backLayoutRes != 0) {
            View backLayout = LayoutInflater.from(context).inflate(backLayoutRes, null, false);
            setCustomLeftView(backLayout);
        } else if (leftType == LeftRightType.TEXT) {
            CharSequence leftText = a.getText(R.styleable.TitleBar_title_bar_back_text);
            if (leftText != null) {
                getBackTextView().setText(leftText);
            } else {
                getBackTextView();
            }
        } else if (leftType == LeftRightType.IMAGE) {
            Drawable leftDrawable = a.getDrawable(R.styleable.TitleBar_title_bar_back_src);
            if (leftDrawable != null) {
                getBackImageView().setImageDrawable(leftDrawable);
            } else {
                getBackImageView();
            }
        }

        if (rightLayoutRes != 0) {
            View rightLayout = LayoutInflater.from(context).inflate(rightLayoutRes, null, false);
            setCustomRightView(rightLayout);
        } else if (rightType == LeftRightType.TEXT) {
            CharSequence rightText = a.getText(R.styleable.TitleBar_title_bar_right_text);
            if (rightText != null) {
                getRightTextView().setText(rightText);
            } else {
                getRightTextView();
            }
        } else if (rightType == LeftRightType.IMAGE) {
            Drawable rightDrawable = a.getDrawable(R.styleable.TitleBar_title_bar_right_src);
            if (rightDrawable != null) {
                getRightImageView().setImageDrawable(rightDrawable);
            } else {
                getRightImageView();
            }
        }

        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(paddingViewTreeObserver);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(paddingViewTreeObserver);
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

    /**
     * 添加到 Window 层
     */
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

    private void setTitleBarPaddings() {
        if (!(getContext() instanceof Activity)) {
            return;
        }
        ViewParent viewParent = getParent();
        View windowView = ((Activity) getContext()).getWindow().getDecorView();
        if (viewParent == windowView) {
            titleBarStatusBar.setVisibility(VISIBLE);
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new PaddingViewTreeObserver() {
            @Override
            public void onGlobalLayout() {
                super.onGlobalLayout();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    private class PaddingViewTreeObserver implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            ViewParent viewParent = getParent();
            View windowView = ((Activity) getContext()).getWindow().getDecorView();
            ViewGroup content = ((Activity) getContext()).findViewById(android.R.id.content);
            int[] contentLat = new int[2];
            content.getLocationOnScreen(contentLat);
            int leftMargin = contentLat[0];
            int oldVisibility = titleBarStatusBar.getVisibility();
            if (viewParent == windowView) {
                int paddingTop = (int) (contentLat[1] == 0 ? TitleBar.this.getHeight() - shadowView.getShadowMaxLength() : titleBarContainer.getHeight());
                content.setPadding(0, aboveContent ? paddingTop : 0, 0, 0);
                if (oldVisibility != VISIBLE) {
                    titleBarStatusBar.setVisibility(VISIBLE);
                }
                TitleBar.this.setPadding(leftMargin, 0, leftMargin > 0 ? 0 : TitleBar.this.getWidth() - content.getWidth(), 0);
            } else {
                int[] titleBarLat = new int[2];
                TitleBar.this.getLocationOnScreen(titleBarLat);
                int newVisibility = contentLat[1] == 0 && titleBarLat[1] == 0 ? VISIBLE : GONE;
                if (oldVisibility != newVisibility) {
                    titleBarStatusBar.setVisibility(newVisibility);
                }
            }
        }
    }


    public int getRightTextStyle() {
        return rightTextStyle;
    }

    /**
     * 设置此项之后必须 再次调用 {@link TitleBar#getRightTextView()} 才可生效
     *
     * @param rightTextStyle 右侧 TextView 的 style
     */
    public void setRightTextStyle(@StyleRes int rightTextStyle) {
        this.rightTextStyle = rightTextStyle;
    }

    public int getRightImageStyle() {
        return rightImageStyle;
    }

    /**
     * 设置此项之后必须 再次调用 {@link TitleBar#getRightImageView()} 才可生效
     *
     * @param rightImageStyle 右侧 ImageView 的 style
     */
    public void setRightImageStyle(@StyleRes int rightImageStyle) {
        this.rightImageStyle = rightImageStyle;
    }

    public int getBackImageStyle() {
        return backImageStyle;
    }

    /**
     * 设置此项之后必须 再次调用 {@link TitleBar#getBackImageView()} 才可生效
     *
     * @param backImageStyle 左侧 ImageView 的 style
     */
    public void setBackImageStyle(@StyleRes int backImageStyle) {
        this.backImageStyle = backImageStyle;
    }

    public int getBackTextStyle() {
        return backTextStyle;
    }

    /**
     * 设置此项之后必须 再次调用 {@link TitleBar#getBackTextView()} 才可生效
     *
     * @param backTextStyle 左侧 TextView 的 style
     */
    public void setBackTextStyle(@StyleRes int backTextStyle) {
        this.backTextStyle = backTextStyle;
    }

    /**
     * 获取右侧 TextView
     *
     * @return TextView
     */
    public TextView getBackTextView() {
        if (backTextView == null || (int) backTextView.getTag(R.id.title_bar_back_text_style) != backTextStyle) {
            leftContainer.removeAllViews();
            backImageView = null;
            backTextView = null;
            backTextView = LayoutInflater.from(new ContextThemeWrapper(getContext(), backTextStyle)).inflate(R.layout.layout_title_bar_back_text_view, leftContainer, true).findViewById(R.id.tv_title_bar_back);
            setContainerViewParams(backTextView, backTextStyle);
            backTextView.setTag(R.id.title_bar_back_text_style, backTextStyle);
        }
        return backTextView;
    }

    /**
     * 获取右侧 ImageView
     *
     * @return ImageView
     */
    public ImageView getBackImageView() {
        if (backImageView == null || (int) backImageView.getTag(R.id.title_bar_back_image_style) != backImageStyle) {
            leftContainer.removeAllViews();
            backImageView = null;
            backTextView = null;
            backImageView = LayoutInflater.from(new ContextThemeWrapper(getContext(), backImageStyle)).inflate(R.layout.layout_title_bar_back_image_view, leftContainer, true).findViewById(R.id.iv_title_bar_back);
            setContainerViewParams(backImageView, backImageStyle);
            backImageView.setTag(R.id.title_bar_back_image_style, backImageStyle);
        }
        return backImageView;
    }

    /**
     * 获取右侧 TextView
     *
     * @return TextView
     */
    public TextView getRightTextView() {
        if (rightTextView == null || (int) rightTextView.getTag(R.id.title_bar_right_text_style) != rightTextStyle) {
            rightContainer.removeAllViews();
            rightImageView = null;
            rightTextView = null;
            rightTextView = LayoutInflater.from(new ContextThemeWrapper(getContext(), rightTextStyle)).inflate(R.layout.layout_title_bar_right_text_view, rightContainer, true).findViewById(R.id.tv_right_view);
            setContainerViewParams(rightTextView, rightTextStyle);
            rightTextView.setTag(R.id.title_bar_right_text_style, rightTextStyle);
        }
        return rightTextView;
    }

    /**
     * 获取右侧 ImageView
     *
     * @return ImageView
     */
    public ImageView getRightImageView() {
        if (rightImageView == null || (int) rightImageView.getTag(R.id.title_bar_right_image_style) != rightImageStyle) {
            rightContainer.removeAllViews();
            rightImageView = null;
            rightTextView = null;
            rightImageView = LayoutInflater.from(new ContextThemeWrapper(getContext(), rightImageStyle)).inflate(R.layout.layout_title_bar_right_image_view, rightContainer, true).findViewById(R.id.iv_right_view);
            setContainerViewParams(rightImageView, rightImageStyle);
            rightImageView.setTag(R.id.title_bar_right_image_style, rightImageStyle);
        }
        return rightImageView;
    }

    private void setContainerViewParams(View view, int style) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        @SuppressLint("CustomViewStyleable") TypedArray a = getContext().obtainStyledAttributes(style, R.styleable.TitleBar_Layout);
        int width = a.getLayoutDimension(R.styleable.TitleBar_Layout_android_layout_width, layoutParams.width);
        int height = a.getLayoutDimension(R.styleable.TitleBar_Layout_android_layout_height, layoutParams.height);
        int gravity = a.getInt(R.styleable.TitleBar_Layout_android_layout_gravity, layoutParams.gravity);
        int marginStart = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginStart, -1);
        int marginEnd = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginEnd, -1);
        int leftMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginLeft, layoutParams.leftMargin);
        int rightMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginRight, layoutParams.rightMargin);
        int topMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginTop, layoutParams.topMargin);
        int bottomMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginBottom, layoutParams.bottomMargin);
        int margin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_margin, -1);
        int marginHorizontal = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginHorizontal, margin);
        int marginVertical = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginVertical, margin);
        if (view instanceof ImageView) {
            if (view.getId() == R.id.iv_title_bar_back) {
                Drawable drawable = a.getDrawable(R.styleable.TitleBar_Layout_android_src);
                if (drawable != null) {
                    ((ImageView) view).setImageDrawable(drawable);
                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_title_bar_back);
                }
            } else if (view.getId() == R.id.iv_right_view) {
                Drawable drawable = a.getDrawable(R.styleable.TitleBar_Layout_android_src);
                if (drawable != null) {
                    ((ImageView) view).setImageDrawable(drawable);
                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_title_bar_more);
                }
            }
        } else if (view instanceof TextView) {
            if (view.getId() == R.id.tv_right_view) {
                float textSize = a.getDimension(R.styleable.TitleBar_Layout_android_textSize, getResources().getDimension(R.dimen.title_bar_rightTextView_textSize));
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                int textColor = a.getColor(R.styleable.TitleBar_Layout_android_textColor, getResources().getColor(R.color.title_bar_rightTextView_textColor));
                ((TextView) view).setTextColor(textColor);
            }
        }
        a.recycle();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = gravity;
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = bottomMargin;
        if (marginVertical != -1) {
            layoutParams.topMargin = marginVertical;
            layoutParams.bottomMargin = marginVertical;
        }
        if (marginHorizontal != -1) {
            layoutParams.leftMargin = marginHorizontal;
            layoutParams.rightMargin = marginHorizontal;
        }
        if (marginStart != -1) {
            layoutParams.setMarginStart(marginStart);
        }
        if (marginEnd != -1) {
            layoutParams.setMarginEnd(marginEnd);
        }
        view.setLayoutParams(layoutParams);


    }

    private void setTitleViewParams(View view, int style) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        @SuppressLint("CustomViewStyleable") TypedArray a = getContext().obtainStyledAttributes(style, R.styleable.TitleBar_Layout);
        int width = a.getLayoutDimension(R.styleable.TitleBar_Layout_android_layout_width, layoutParams.width);
        int height = a.getLayoutDimension(R.styleable.TitleBar_Layout_android_layout_height, layoutParams.height);
        int marginStart = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginStart, -1);
        int marginEnd = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginEnd, -1);
        int leftMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginLeft, layoutParams.leftMargin);
        int rightMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginRight, layoutParams.rightMargin);
        int topMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginTop, layoutParams.topMargin);
        int bottomMargin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginBottom, layoutParams.bottomMargin);
        int margin = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_margin, -1);
        int marginHorizontal = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginHorizontal, margin);
        int marginVertical = a.getDimensionPixelOffset(R.styleable.TitleBar_Layout_android_layout_marginVertical, margin);
        if (view instanceof TextView) {
            if (view.getId() == R.id.tv_title_bar_title) {
                float textSize = a.getDimension(R.styleable.TitleBar_Layout_android_textSize, getResources().getDimension(R.dimen.title_bar_title_textSize));
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                int textColor = a.getColor(R.styleable.TitleBar_Layout_android_textColor, getResources().getColor(R.color.title_bar_title_textColor));
                ((TextView) view).setTextColor(textColor);
            }
        }
        a.recycle();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = bottomMargin;
        if (marginVertical != -1) {
            layoutParams.topMargin = marginVertical;
            layoutParams.bottomMargin = marginVertical;
        }
        if (marginHorizontal != -1) {
            layoutParams.leftMargin = marginHorizontal;
            layoutParams.rightMargin = marginHorizontal;
        }
        if (marginStart != -1) {
            layoutParams.setMarginStart(marginStart);
        }
        if (marginEnd != -1) {
            layoutParams.setMarginEnd(marginEnd);
        }
        view.setLayoutParams(layoutParams);


    }

    /**
     * 获取返回 ImageView
     *
     * @return ImageView
     */
    @Deprecated
    public ImageView getBackView() {
        return getBackImageView();
    }

    /**
     * 获取标题 TextView
     *
     * @return TextView
     */
    @NonNull
    public TextView getTitleView() {
        return titleView;
    }

    /**
     * 设置底部 Shadow 样式
     *
     * @param shadowHeightDp shadow 高度
     * @param shadowColor    shadow 颜色
     * @param shadowType     shadow 样式 {@link ShadowType#LINE} 实线 / {@link ShadowType#GRADIENT} 渐变线
     */
    public void setShadow(float shadowHeightDp, @ColorInt int shadowColor, ShadowType shadowType) {
        setShadowPixel(ScreenUtils.dp2px(getContext(), shadowHeightDp), shadowColor, shadowType);
    }

    /**
     * 设置底部 Shadow 样式
     *
     * @param shadowHeightDp shadow 高度
     * @param shadowDrawable 资源图
     */
    public void setShadow(float shadowHeightDp, Drawable shadowDrawable) {
        setShadowPixel(ScreenUtils.dp2px(getContext(), shadowHeightDp), shadowDrawable, ShadowType.LINE);
    }

    /**
     * @param shadowHeightDp    shadow 高度
     * @param shadowDrawableRes 资源图id
     */
    public void setShadow(float shadowHeightDp, @DrawableRes int shadowDrawableRes) {
        setShadow(shadowHeightDp, ContextCompat.getDrawable(getContext(), shadowDrawableRes));
    }

    private void setShadowPixel(float shadowHeightPx, @ColorInt int shadowColor, ShadowType shadowType) {
        int[] colors;
        if (shadowType == ShadowType.GRADIENT) {
            colors = new int[]{shadowColor, Color.TRANSPARENT};
        } else {
            colors = new int[]{shadowColor, shadowColor};
        }
        shadowView.setGradientColors(colors);
        shadowView.setShadowMaxLength(shadowHeightPx);
        setDisplayShadow(shadowType != ShadowType.NONE);
    }

    private void setShadowPixel(float shadowHeightPx, Drawable shadowDrawable, ShadowType shadowType) {
        shadowView.setGradientColors(null);
        shadowView.setBackground(shadowDrawable);
        shadowView.setShadowMaxLength(shadowHeightPx);
        setDisplayShadow(shadowType != ShadowType.NONE);
    }

    /**
     * 设置是否展示 Shadow
     *
     * @param showShadow shadow 是否可见
     */
    public void setDisplayShadow(boolean showShadow) {
        shadowView.setVisibility(showShadow ? VISIBLE : GONE);
    }

    @Override
    public Drawable getBackground() {
        if (titleBarContainer != null) {
            return titleBarContainer.getBackground();
        }
        return super.getBackground();
    }

    /**
     * 已被弃用，可以看下 {@link TitleBar#setTitleBarBackground}
     *
     * @param background Drawable
     */
    @Deprecated
    @Override
    public void setBackground(Drawable background) {
        if (backgroundView == null) {
            pendingSetBackground = background;
            return;
        }
        setTitleBarBackground(background);
    }

    /**
     * 已被弃用，可以看下 {@link TitleBar#setTitleBarBackgroundColor}
     *
     * @param color 颜色
     */
    @Deprecated
    @Override
    public void setBackgroundColor(@ColorInt int color) {
        setTitleBarBackgroundColor(color);
    }

    /**
     * 已被弃用，可以看下 {@link TitleBar#setTitleBarBackgroundResource}
     *
     * @param resid 资源id
     */
    @Deprecated
    @Override
    public void setBackgroundResource(@DrawableRes int resid) {
        setTitleBarBackgroundResource(resid);
    }

    /**
     * 已被弃用，可以看下 {@link TitleBar#setTitleBarBackground}
     *
     * @param background Drawable
     */
    @Deprecated
    @Override
    public void setBackgroundDrawable(Drawable background) {
        setTitleBarBackground(background);
    }

    /**
     * 设置标题栏背景 （包含了状态栏）
     *
     * @param background 背景 Drawable
     */
    public void setTitleBarBackgroundWithStatusBar(Drawable background) {
        backgroundView.setBackground(background);
    }

    /**
     * 设置标题栏背景 （包含了状态栏）
     *
     * @param resid 背景资源图
     */
    public void setTitleBarBackgroundResourceWithStatusBar(@DrawableRes int resid) {
        backgroundView.setBackgroundResource(resid);
    }

    /**
     * 设置标题栏背景 （包含了状态栏）
     *
     * @param color 背景颜色
     */
    public void setTitleBarBackgroundColorWithStatusBar(@ColorInt int color) {
        backgroundView.setBackgroundColor(color);
    }

    /**
     * 设置标题栏背景 （不包含状态栏）
     *
     * @param background 背景 Drawable
     */
    public void setTitleBarBackground(Drawable background) {
        titleBarContainer.setBackground(background);
    }

    /**
     * 设置标题栏背景 （不包含状态栏）
     *
     * @param resid 背景资源图
     */
    public void setTitleBarBackgroundResource(@DrawableRes int resid) {
        titleBarContainer.setBackgroundResource(resid);
    }

    /**
     * 设置标题栏背景 （不包含状态栏）
     *
     * @param color 背景颜色
     */
    public void setTitleBarBackgroundColor(@ColorInt int color) {
        titleBarContainer.setBackgroundColor(color);
    }

    /**
     * 设置自定义View（中间的）
     *
     * @param view 自定义View
     */
    public void setCustomView(View view) {
        setCustomView(view, null);
    }

    /**
     * 设置自定义View（中间的）
     *
     * @param view 自定义View
     */
    public void setCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        customViewContainer.removeAllViews();
        if (layoutParams != null) {
            customViewContainer.addView(view, layoutParams);
        } else {
            customViewContainer.addView(view);
        }
    }

    /**
     *
     * @return 获取自定义View（中间的）
     */
    @Nullable
    public View getCustomView() {
        if (customViewContainer.getChildCount() > 0) {
            return customViewContainer.getChildAt(0);
        }
        return null;
    }

    /**
     * 设置右侧自定义View
     *
     * @param view 自定义View
     */
    public void setCustomRightView(View view) {
        setCustomRightView(view, null);
    }

    /**
     * 设置右侧自定义View
     *
     * @param view 自定义View
     */
    public void setCustomRightView(View view, FrameLayout.LayoutParams layoutParams) {
//        isSetTitleGravity = true;
        rightContainer.setOnClickListener(null);
        rightContainer.setOnLongClickListener(null);
        rightContainer.removeAllViews();
        rightImageView = null;
        rightTextView = null;
        if (layoutParams != null) {
            rightContainer.addView(view, layoutParams);
        } else {
            rightContainer.addView(view);
        }
    }

    /**
     *
     * @return 获取右侧的自定义View
     */
    @Nullable
    public View getCustomRightView() {
        if (rightContainer.getChildCount() > 0) {
            return rightContainer.getChildAt(0);
        }
        return null;
    }

    /**
     * 设置左侧自定义View
     *
     * @param view 自定义View
     */
    public void setCustomLeftView(View view) {
        setCustomLeftView(view, null);
    }

    /**
     * 设置左侧自定义View
     *
     * @param view 自定义View
     */
    public void setCustomLeftView(View view, FrameLayout.LayoutParams layoutParams) {
//        isSetTitleGravity = true;
        leftContainer.setOnClickListener(null);
        leftContainer.setOnLongClickListener(null);
        leftContainer.removeAllViews();
        backImageView = null;
        backTextView = null;
        if (layoutParams != null) {
            leftContainer.addView(view, layoutParams);
        } else {
            leftContainer.addView(view);
        }
    }

    /**
     *
     * @return 获取左侧的自定义View
     */
    @Nullable
    public View getCustomLeftView() {
        if (leftContainer.getChildCount() > 0) {
            return leftContainer.getChildAt(0);
        }
        return null;
    }

    /**
     * 设置是否展示左侧View （通常是指 返回按钮）
     *
     * @param showLeftView 是否展示左侧 View
     */
    public void setDisplayLeftView(boolean showLeftView) {
        leftContainer.setVisibility(showLeftView ? VISIBLE : GONE);
    }

    /**
     * 设置是否展示右侧View
     *
     * @param showRightView 是否展示右侧 View
     */
    public void setDisplayRightView(boolean showRightView) {
        rightContainer.setVisibility(showRightView ? VISIBLE : GONE);
    }

    /**
     * 设置标题
     *
     * @param text 标题
     */
    public void setTitle(CharSequence text) {
        if (titleView != null) {
            titleView.setText(text);
        }
    }

    /**
     * 设置标题
     *
     * @param resid 标题id
     */
    public void setTitle(@StringRes int resid) {
        if (titleView != null) {
            titleView.setText(resid);
        }
    }

    /**
     * 设置标题颜色
     *
     * @param color 颜色
     */
    public void setTitleColor(@ColorInt int color) {
        if (titleView != null) {
            titleView.setTextColor(color);
        }
    }

    /**
     * 设置标题位置
     *
     * @param gravity 位置 {@link TitleGravity}
     */
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
            }
            int margin = Math.max(leftContainer.getWidth(), rightContainer.getWidth());
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

    /**
     * 设置返回点击监听
     *
     * @param listener 监听器
     */
    public void setOnBackViewClickListener(OnClickListener listener) {
        if (leftContainer != null) {
            leftContainer.setOnClickListener(listener);
        }
    }

    /**
     * 设置返回长按监听
     *
     * @param listener 监听器
     */
    public void setOnBackViewLongClickListener(OnLongClickListener listener) {
        if (leftContainer != null) {
            leftContainer.setOnLongClickListener(listener);
        }
    }

    /**
     * 设置右侧点击监听
     *
     * @param listener 监听器
     */
    public void setOnRightViewClickListener(OnClickListener listener) {
        if (rightContainer != null) {
            rightContainer.setOnClickListener(listener);
        }
    }

    /**
     * 设置右侧长按监听
     *
     * @param listener 监听器
     */
    public void setOnRightViewLongClickListener(OnLongClickListener listener) {
        if (rightContainer != null) {
            rightContainer.setOnLongClickListener(listener);
        }
    }

    /**
     * 隐藏 titleBar
     */
    public void hide() {
        setVisibility(GONE);
    }

    /**
     * 显示 titleBar
     */
    public void show() {
        setVisibility(VISIBLE);
    }

    public boolean isAboveContent() {
        return aboveContent;
    }

    /**
     * TitleBar 是否在内容上边
     *
     * @param aboveContent true则TitleBar和布局成上下结构，false则TitleBar覆盖在布局上方
     */
    public void setAboveContent(boolean aboveContent) {
        this.aboveContent = aboveContent;
        setTitleBarPaddings();
    }

    /**
     * 此方法已被修改，整个View的高度设定将应用于 返回一览 ，这个外层的高度始终是{@link ViewGroup.LayoutParams#WRAP_CONTENT}<br>
     * 详情可看{@link TitleBar#setTitleBarLayoutParams(ViewGroup.LayoutParams)}
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        setTitleBarLayoutParams(params);
    }

    /**
     * 不建议使用这个方法，因为这可能包含你不需要的东西。详情可看{@link TitleBar#getTitleBarLayoutParams}
     *
     * @return
     */
    @Deprecated
    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return super.getLayoutParams();
    }

    /**
     * 获取实际 {@link ViewGroup.LayoutParams} 参数，因为整个TitleBar实际包含了<上 中 下> 三部分
     * 那么我们实际上主要关注的是返回按钮那一栏，不关心 shadow 和 状态栏，所以建议用这个
     * <ul>
     *  <li> 上 -> 状态栏占位所用的View
     *  <li> 中 -> 就是返回按钮一栏
     *  <li> 下 -> 显示 shadow 的View
     * </ul>
     *
     * @return
     */
    public ViewGroup.LayoutParams getTitleBarLayoutParams() {
        ViewGroup.LayoutParams containerLayoutParams = titleBarContainer.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = super.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = containerLayoutParams.height;
        }
        return layoutParams;
    }

    /**
     * 设置实际 {@link ViewGroup.LayoutParams} 参数，原因可看{@link TitleBar#getTitleBarLayoutParams()}<br>
     * TitleBar 高度永远是{@link ViewGroup.LayoutParams#WRAP_CONTENT}，实际上高度设定给了 返回按钮一栏
     */
    public void setTitleBarLayoutParams(ViewGroup.LayoutParams params) {
        if (titleBarContainer != null) {
            ViewGroup.LayoutParams layoutParams = titleBarContainer.getLayoutParams();
            layoutParams.height = params.height;
            titleBarContainer.setLayoutParams(layoutParams);
        }
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        super.setLayoutParams(params);
    }

    /**
     * 设置整个包含状态栏和shadow 的TitleBar的 {@link ViewGroup.LayoutParams}，不建议用这个
     *
     * @param params
     */
    public void setRealLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    /**
     * @return shadow高度
     */
    public int getShadowHeight() {
        return shadowView.getHeight();
    }

    /**
     * @return 状态栏高度
     */
    public int getStatusBarViewHeight() {
        return titleBarStatusBar.getHeight();
    }

    /**
     * 这个高度是包含状态栏，shadow和返回按钮一栏的总高度
     *
     * @return 整个TitleBar 的高度
     */
    public int getTitleBarHeight() {
        return getHeight();
    }

    /**
     * 设置状态栏背景
     *
     * @param background Drawable
     */
    public void setStatusBarBackground(Drawable background) {
        titleBarStatusBar.setBackground(background);
    }

    /**
     * 设置状态栏背景
     *
     * @param resid 资源图
     */
    public void setStatusBarBackgroundResource(@DrawableRes int resid) {
        titleBarStatusBar.setBackgroundResource(resid);
    }

    /**
     * 设置状态栏背景
     *
     * @param color 颜色
     */
    public void setStatusBarBackgroundColor(@ColorInt int color) {
        titleBarStatusBar.setBackgroundColor(color);
    }

    /**
     * 获取状态栏View
     *
     * @return ImageView
     */
    public ImageView getTitleBarStatusBar() {
        return titleBarStatusBar;
    }
}
