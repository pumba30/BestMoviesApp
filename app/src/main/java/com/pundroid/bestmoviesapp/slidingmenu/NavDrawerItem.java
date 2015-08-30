package com.pundroid.bestmoviesapp.slidingmenu;

/**
 * Created by pumba30 on 21.08.2015.
 */
public class NavDrawerItem {
    private String title;
    private int icon;
    private int background;

    public NavDrawerItem() {

    }

    public NavDrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, int icon, int background) {
        this.title = title;
        this.icon = icon;
        this.background = background;
    }

    public String getTitle() {
        return this.title;
    }

    public int getIcon() {
        return this.icon;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
