

package com.lsus.teamcoach.teamcoachapp.ui.Framework;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.ui.News.NewsFragment;
import com.lsus.teamcoach.teamcoachapp.ui.News.NewsListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Roster.RosterFragment;

/**
 * Pager adapter
 */
public class BootstrapPagerAdapterPlayer extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public BootstrapPagerAdapterPlayer(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    //important
    //Number of Fragments on the Carousel has to be set
    @Override
    public int getCount() { return 2; }

    // Gets each fragment for the Carousel
    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new NewsFragment();
                break;
            case 1:
                result = new RosterFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    //Gets the title of each page/fragemnt on the Carousel
    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_messages);
            case 1:
                return resources.getString(R.string.page_roster);
            default:
                return null;
        }
    }
}
