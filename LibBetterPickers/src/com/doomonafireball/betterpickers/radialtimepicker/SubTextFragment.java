package com.doomonafireball.betterpickers.radialtimepicker;

import android.view.View;
import android.widget.TextView;

import com.doomonafireball.betterpickers.R;

/**
 * Created by steveyang on 20/3/18.
 */

public class SubTextFragment {
    TextView sub_text_title;
    TextView sub_text_value;
    String sub_text_titleStr = "";
    String sub_text_valueStr = "";
    int titleTextColor = -1;
    int valueTextColor = -1;
    View sub_text_layout;

    public void setSubText(String title, int titleColor, String value, int valueColor) {
        sub_text_titleStr = title;
        sub_text_valueStr = value;
        titleTextColor = titleColor;
        valueTextColor = valueColor;
        setText();
    }

    public void initView(View view) {

        sub_text_layout = view.findViewById(R.id.sub_text_layout);
        sub_text_title = view.findViewById(R.id.sub_text_title);
        sub_text_value = view.findViewById(R.id.sub_text_value);

        setText();
    }

    void setText() {

        if(sub_text_title != null){
            sub_text_title.setText(sub_text_titleStr);
            if(titleTextColor != -1){
                sub_text_title.setTextColor(titleTextColor);
            }
        }

        if(sub_text_value != null){
            sub_text_value.setText(sub_text_valueStr);
            if(valueTextColor != -1){
                sub_text_value.setTextColor(valueTextColor);
            }
        }

//        if(sub_text_title != null){
//            if(sub_text_title.getText().length() == 0){
//                if(sub_text_layout != null){
//                    sub_text_layout.setVisibility(View.GONE);
//                }
//            }
//        }
    }
}
