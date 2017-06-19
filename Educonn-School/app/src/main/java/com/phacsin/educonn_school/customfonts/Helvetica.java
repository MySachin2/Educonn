package com.phacsin.educonn_school.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bineesh P Babu on 27-08-2016.
 */
public class Helvetica extends AppCompatTextView {

    public Helvetica(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Helvetica(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Helvetica(Context context) {
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
