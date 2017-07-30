package com.kareem.newme;

import android.support.v4.app.Fragment;

/**
 * Created by kareem on 7/30/17.
 */

public abstract class ViewPagerFragment extends Fragment {
    private String fragmentTitle;

    public String getFragmentTitle() {
        return fragmentTitle;
    }

    public void setFragmentTitle(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }
}
