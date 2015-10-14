package com.pundroid.bestmoviesapp.slidingmenu;

/**
 * Created by pumba30 on 21.08.2015.
 */
public class NavDrawerItem {
    private String mTitle;
    private int mIcon;
    private int mBackground;

    public NavDrawerItem() {

    }

    public NavDrawerItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    public NavDrawerItem(String title, int icon, int background) {
        mTitle = title;
        mIcon = icon;
        mBackground = background;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getBackground() {
        return mBackground;
    }

    public void setBackground(int background) {
        mBackground = background;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }


}
