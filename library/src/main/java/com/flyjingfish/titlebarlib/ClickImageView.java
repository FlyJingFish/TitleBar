package com.flyjingfish.titlebarlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class ClickImageView extends AppCompatImageView {
    public ClickImageView(@NonNull Context context) {
        this(context,null);
    }

    public ClickImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClickImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        ViewGroup parent;
        if ((parent = (ViewGroup) getParent()) != null){
            parent.setOnClickListener(l);
        }
    }
}
