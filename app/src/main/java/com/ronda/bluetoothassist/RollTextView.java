package com.ronda.bluetoothassist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class RollTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RollTextView(Context context) {
        super(context);
    }

    public RollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
