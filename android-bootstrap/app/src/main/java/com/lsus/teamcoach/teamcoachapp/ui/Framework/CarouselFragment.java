package com.lsus.teamcoach.teamcoachapp.ui.Framework;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapPagerAdapterAdmin;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapPagerAdapterCoach;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapPagerAdapterPlayer;
import com.viewpagerindicator.TitlePageIndicator;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Fragment which houses the View pager.
 */
public class CarouselFragment extends Fragment {

//    @InjectView(R.id.tpi_header)
//    protected TitlePageIndicator indicator;

    @InjectView(R.id.vp_pages)
    protected ViewPager pager;

    @InjectView(R.id.pager_title_strip)
    protected PagerTabStrip pagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Views.inject(this, getView());

        //Show team color change
//        pager.setBackgroundColor(R.color.teamcoach_background2);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.teamcoach_background2));

        Singleton singleton = Singleton.getInstance();
        User user = singleton.getCurrentUser();


        Log.d("User in Carousel", user.toString());

        try {
            if (user.getRole().equalsIgnoreCase("Admin")) {
                pager.setAdapter(new BootstrapPagerAdapterAdmin(getResources(), getChildFragmentManager()));
            }
            if (user.getRole().equalsIgnoreCase("Coach")) {
                pager.setAdapter(new BootstrapPagerAdapterCoach(getResources(), getChildFragmentManager()));
            }
            if (user.getRole().equalsIgnoreCase("Player")) {
                pager.setAdapter(new BootstrapPagerAdapterPlayer(getResources(), getChildFragmentManager()));
            }
        }catch (Exception e) {
            e.printStackTrace();

//            try {
//                wait(1000);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }

            if (user.getRole().equalsIgnoreCase("Admin")) {
                pager.setAdapter(new BootstrapPagerAdapterAdmin(getResources(), getChildFragmentManager()));
            }
            if (user.getRole().equalsIgnoreCase("Coach")) {
                pager.setAdapter(new BootstrapPagerAdapterCoach(getResources(), getChildFragmentManager()));
            }
            if (user.getRole().equalsIgnoreCase("Player")) {
                pager.setAdapter(new BootstrapPagerAdapterPlayer(getResources(), getChildFragmentManager()));
            }
        }

//        indicator.setViewPager(pager);
        pager.setCurrentItem(1);

    }
}