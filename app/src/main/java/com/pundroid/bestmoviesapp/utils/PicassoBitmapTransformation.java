package com.pundroid.bestmoviesapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.squareup.picasso.Transformation;

/**
 * Created by pumba30 on 25.10.2015.
 */
public class PicassoBitmapTransformation implements Transformation {
    private Context mContext;
    private int mDesiredWidth;


    public PicassoBitmapTransformation(Context context, int desiredWidth) {
        mContext = context;
        mDesiredWidth = desiredWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int desiredWidth = setDesiredWidth(mDesiredWidth);
        int targetHeight = (int) (desiredWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(source, desiredWidth, targetHeight, false);
        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "transformation" + " DesiredWidth";
    }

    // get display width and and divide it
    // by proportional to the size that want to get in the image
    public int setDesiredWidth(int desiredWidth) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / desiredWidth;
    }

}
