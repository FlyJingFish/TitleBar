package com.flyjingfish.titlebarlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ClickTextView extends AppCompatTextView {
    public ClickTextView(@NonNull Context context) {
        this(context,null);
    }

    public ClickTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClickTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        ViewGroup parent;
        if ((parent = (ViewGroup) getParent()) != null) {
            if (l == null) {
                parent.setOnClickListener(null);
                return;
            }
            parent.setOnClickListener(v -> l.onClick(ClickTextView.this));
        }
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        ViewGroup parent;
        if ((parent = (ViewGroup) getParent()) != null) {
            if (l == null) {
                parent.setOnLongClickListener(null);
                return;
            }
            parent.setOnLongClickListener(v -> l.onLongClick(ClickTextView.this));
        }
    }

}
