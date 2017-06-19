package com.phacsin.educonn_school.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Bineesh P Babu on 16-12-2016.
 */

public class FontCheckBox extends AppCompatCheckBox {
    public FontCheckBox(Context context) {
        super(context);
        init();
    }
    public FontCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Chinrg.ttf");
            setTypeface(tf);
        }
    }
}
