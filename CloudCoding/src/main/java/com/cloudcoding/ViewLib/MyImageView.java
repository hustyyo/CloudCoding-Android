package com.cloudcoding.ViewLib;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by steveyang on 27/11/16.
 */

public class MyImageView extends ImageView {


    public MyImageView(Context context) {
        super(context);
        buttonEffect();
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        buttonEffect();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buttonEffect();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        buttonEffect();
    }

    public void buttonEffect(){
        this.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0333333, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
