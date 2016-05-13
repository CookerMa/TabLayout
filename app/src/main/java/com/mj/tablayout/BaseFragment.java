package com.mj.tablayout;


import android.support.v4.app.Fragment;

/**
 * Created by vienan on 16/4/10.
 */
public abstract class BaseFragment extends Fragment {

    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible=true;
            onVisible();
        }else {
            isVisible=false;
            onInvisible();
        }
    }


    /**
     * visible->lazyLoad
     */
    private void onVisible() {
        lazyLoad();
    }

    private void onInvisible() {

    }

    protected abstract void lazyLoad();


}
