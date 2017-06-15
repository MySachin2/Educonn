package com.phacsin.student.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Bineesh P Babu on 07-01-2017.
 */

public class CustomSpinnerFont extends com.jaredrummler.materialspinner.MaterialSpinner {
    public CustomSpinnerFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomSpinnerFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSpinnerFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
            setTypeface(tf);
        }
    }

}

