package com.pundroid.bestmoviesapp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;

/**
 * Created by pumba30 on 25.10.2015.
 */
public class ScrollViewExt extends ScrollView {
    private DetailMovieActivityFragment.ScrollViewListener scrollViewListener = null;

    public ScrollViewExt(Context context) {
        super(context);
    }

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(DetailMovieActivityFragment.ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
