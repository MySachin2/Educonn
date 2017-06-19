package com.phacsin.educonn_school.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Bineesh P Babu on 04-01-2017.
 */

public class HelveticaButton extends AppCompatButton {
    public HelveticaButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public HelveticaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeue.ttf");
            setTypeface(tf);
        }
    }

}

