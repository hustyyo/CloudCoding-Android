package com.cloudcoding.Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ====================================
 * Created by michael.carr on 27/06/2014.
 * ====================================
 */
public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF000000;
    private Canvas drawCanvas;
    public Bitmap canvasBitmap;
    private int mWidth;
    private int mHeight;
    private boolean hasBeenDrawnOn;
    private boolean hasImage;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        if (canvasBitmap == null) {
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }
        drawCanvas = new Canvas(canvasBitmap);

    }

    public boolean hasBeenDrawnOn(){
        return hasBeenDrawnOn;
    }

    public boolean hasImage(){
        return hasImage;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    public void clear(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvasBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        invalidate();
    }

    public void setImageBitmap(Bitmap bitmap){

        if (bitmap != null){
            hasImage = true;
        }

        canvasBitmap = bitmap.copy(bitmap.getConfig(), true);
        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        //canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        /*
        if (drawCanvas == null){
            drawCanvas = new Canvas();
            drawCanvas.drawBitmap(bitmap, 0, 0, null);
        } else {
            drawCanvas.drawBitmap(bitmap, 0, 0, null);
        }
        */
        //canvasBitmap = bitmap;

        invalidate();
    }

    public void save(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                hasBeenDrawnOn = true;
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                hasBeenDrawnOn = true;
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                hasBeenDrawnOn = true;
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();

        return true;
    }
}