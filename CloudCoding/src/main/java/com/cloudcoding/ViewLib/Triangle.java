package com.cloudcoding.ViewLib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * ==================================
 * Created by michael.carr on 15/12/2014.
 * ==================================
 */
public class Triangle extends LinearLayout {

    final static String TAG = "Slice";
    Paint mPaint;
    Path mPath;

    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    public Triangle(Context context) {
        super(context);
        Create(context);
    }

    public Triangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        Create(context);
    }

    private void Create(Context context) {
        Log.i(TAG, "Creating ...");

        mPaint = new Paint();
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.RED);

        Point point = new Point();
        point.x = 80;
        point.y = 80;

        mPath = Calculate(point, 70, Direction.SOUTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "Drawing ...");

        canvas.drawPath(mPath, mPaint);
    }

    private Path Calculate(Point p1, int width, Direction direction) {
        Log.i(TAG, "Calculating ...");

        Point p2 = null, p3 = null;

        if (direction == Direction.NORTH) {
            p2 = new Point(p1.x + width, p1.y);
            p3 = new Point(p1.x + (width / 2), p1.y - width);
        } else if (direction == Direction.SOUTH) {
            p2 = new Point(p1.x + width, p1.y);
            p3 = new Point(p1.x + (width / 2), p1.y + width);
        } else if (direction == Direction.EAST) {
            p2 = new Point(p1.x, p1.y + width);
            p3 = new Point(p1.x - width, p1.y + (width / 2));
        } else if (direction == Direction.WEST) {
            p2 = new Point(p1.x, p1.y + width);
            p3 = new Point(p1.x + width, p1.y + (width / 2));
        }

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);

        return path;
    }
}


