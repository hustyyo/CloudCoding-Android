package com.cloudcoding.ViewLib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cloudcoding.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by steve.yang on 16/11/16.
 */

@SuppressLint("AppCompatCustomView")
public class TextViewFontAwesome extends TextView {

    public static final String FONT = "fontawesome.otf";//"fontawesome_webfont.ttf";
    private static Typeface mFont;

    public TextViewFontAwesome(Context context) {
        super(context);

        applyCustomFont(context,null);
    }

    public TextViewFontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context,attrs);
    }

    public TextViewFontAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context,attrs);
    }


    private void applyCustomFont(Context context, AttributeSet attrs) {

        Typeface customFont = getTypefaceFromFile(context);
        setTypeface(customFont);

    }

    private static Typeface getTypefaceFromFile(Context context) {
        if(mFont == null) {
            File font = new File(context.getFilesDir(), FONT);
            if (!font.exists()) {
                copyFontToInternalStorage(context, font);
            }
            mFont = Typeface.createFromFile(font);
        }
        return mFont;
    }

    private static void copyFontToInternalStorage(Context context, File font) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.fontawesome);//fontawesome_webfont
            byte[] buffer = new byte[1024*800];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(font));
            int readByte;
            while ((readByte = is.read(buffer)) > 0) {
                bos.write(buffer, 0, readByte);
            }
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


