package com.cloudcoding.ViewLib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.cloudcoding.R;

/**
 * Copyright 2014 Perenso
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ClearableButton extends Button implements OnTouchListener, OnFocusChangeListener{

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    public interface CancelListener {
        void didCancel();
    }

    public void setClearListener(CancelListener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private CancelListener listener;

    public ClearableButton(Context context) {
        super(context);
        init();
    }

    public ClearableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //setText("");
                    if (listener != null) {
                        listener.didCancel();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    public void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources()
                    .getDrawable(R.drawable.ic_highlight_remove_white_24dp);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(true);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
    }

    public void setClearIconVisible(boolean visible) {
//        setCompoundDrawables(getCompoundDrawables()[0],
//                getCompoundDrawables()[1], xD, getCompoundDrawables()[3]);

        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);

    }
}


