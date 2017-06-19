package com.phacsin.student.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by Bineesh P Babu on 05-01-2017.
 */

public class NiceFont extends TextView {

    public NiceFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NiceFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NiceFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Nice.ttf");
            setTypeface(tf);
        }
    }


}