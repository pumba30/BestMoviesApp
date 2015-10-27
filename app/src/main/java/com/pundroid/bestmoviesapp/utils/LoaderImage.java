package com.pundroid.bestmoviesapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by pumba30 on 26.10.2015.
 */
public class LoaderImage {
    private MemLruCache mCache;
    private static LoaderImage sInstance;

    public static LoaderImage getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LoaderImage(context);
        }
        return sInstance;
    }

    private LoaderImage(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 8; // 1/8th of total ram
        mCache = new MemLruCache(limitKb);
    }

    public boolean isExistBitmap(String key) {
        if (getBitmapFromMemoryCache(key) != null) {
            return true;
        }
        return false;
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mCache.get(key);
    }


    private class MemLruCache extends LruCache<String, Bitmap> {

        public MemLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return super.sizeOf(key, value);
        }
    }
}
