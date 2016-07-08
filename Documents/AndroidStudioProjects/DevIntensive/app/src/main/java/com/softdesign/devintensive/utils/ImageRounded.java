package com.softdesign.devintensive.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Transformation;


/**
 * Created by AlexFrei on 27.06.16.
 */
public class ImageRounded implements Transformation {


    /**
     * Трансформирует оригинальную картинку в круглую картинку.

     * @param source ссылка на картинку
     * @return
     */

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;


        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);



        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);


        squaredBitmap.recycle();
        return bitmap;
    }


    @Override
    public String key() {
        return "circle";
    }

}
// TODO: 08.07.16 старый код исправить 09

    /*private final Bitmap mBitmap;
    private final Paint mPaint;
    private final RectF mRectF;
    private final int mBitmapWidth;
    private final int mBitmapHeight;

public ImageRounded (Bitmap bitmap){
    mBitmap = bitmap;
    mRectF = new RectF();
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mPaint.setShader(shader);
    mBitmapWidth = mBitmap.getWidth();
    mBitmapHeight = mBitmap.getHeight();
}

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawOval(mRectF, mPaint);

    }

    @Override
    public void setAlpha(int alpha) {
        if (mPaint.getAlpha() != alpha){
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRectF.set(bounds);
    }

    @Override
    public void setColorFilter(ColorFilter cF) {
        mPaint.setColorFilter(cF);

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap){

        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();

        return output;


    }

*/

