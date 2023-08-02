package com.flyjingfish.titlebarlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ShadowView extends View {

    private final Paint mBgPaint;
    private int[] gradientColors;
    private float[] gradientPositions;
    private float shadowMaxLength;
    private final float[] ptsBottom = new float[4];

    public ShadowView(Context context) {
        this(context,null);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLACK);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStrokeWidth(shadowMaxLength);
        mBgPaint.setStyle(Paint.Style.STROKE);


        gradientColors = null;
        gradientPositions = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) shadowMaxLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (gradientColors == null || shadowMaxLength <= 0){
            drawBackground(canvas);
            super.onDraw(canvas);
            return;
        }
        drawBackground(canvas);

        int height = getHeight();
        int width = getWidth();

        mBgPaint.setStrokeWidth(shadowMaxLength);
        ptsBottom[0] = 0;
        ptsBottom[1] = height - shadowMaxLength / 2f;
        ptsBottom[2] = width;
        ptsBottom[3] = height - shadowMaxLength / 2f;
        LinearGradient linearGradient = new LinearGradient(0, height - shadowMaxLength, 0, height, gradientColors, gradientPositions, Shader.TileMode.CLAMP);
        mBgPaint.setShader(linearGradient);
        canvas.drawLines(ptsBottom, mBgPaint);
        super.onDraw(canvas);
    }

    public float getShadowMaxLength() {
        return shadowMaxLength;
    }

    public void setShadowMaxLength(float shadowMaxLength) {
        this.shadowMaxLength = shadowMaxLength;
        invalidate();
    }

    public int[] getGradientColors() {
        return gradientColors;
    }

    public void setGradientColors(int[] gradientColors) {
        this.gradientColors = gradientColors;
        invalidate();
    }

    public float[] getGradientPositions() {
        return gradientPositions;
    }

    public void setGradientPositions(float[] gradientPositions) {
        this.gradientPositions = gradientPositions;
        invalidate();
    }

    private Drawable bgDrawable;
    @Override
    public void setBackground(Drawable background) {
        bgDrawable = background;
        invalidate();
    }

    @Override
    public void setBackgroundDrawable(@Nullable Drawable background) {
        setBackground(background);
    }

    @Override
    public void setBackgroundResource(int resId) {
        setBackground(getContext().getResources().getDrawable(resId));
    }

    private void drawBackground(Canvas canvas) {
        final Drawable background = bgDrawable;
        if (background == null) {
            return;
        }

        background.setBounds(0, 0, getRight() - getLeft(), (int) (shadowMaxLength));

        background.draw(canvas);
    }
}
