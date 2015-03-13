package com.lsus.teamcoach.teamcoachapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.AgeGroup;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class DrillTypeListFragment extends ItemListFragment<String> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter()
                .addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.drill_type_list_label, null));
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<String>> onCreateLoader(final int id, final Bundle args) {
        final List<String> initialItems = items;
        return new ThrowableLoader<List<String>>(getActivity(), items) {

            @Override
            public List<String> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    return getMenuItems();
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<String> createAdapter(final List<String> items) {
        return new TeamMenuListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final String item = ((String) l.getItemAtPosition(position));

        Toaster.showLong(this.getActivity(), "You clicked: " + item);


        //---------------------------------------------------------------------------------------
        //Original code.
        //---------------------------------------------------------------------------------------
//        final CheckIn checkIn = ((CheckIn) l.getItemAtPosition(position));
//
//        final String uri = String.format("geo:%s,%s?q=%s",
//                checkIn.getLocation().getLatitude(),
//                checkIn.getLocation().getLongitude(),
//                checkIn.getName());
//
//        // Show a chooser that allows the user to decide how to display this data, in this case, map data.
//        startActivity(Intent.createChooser(
//                        new Intent(Intent.ACTION_VIEW, Uri.parse(uri)), getString(R.string.choose))
//        );
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
    }

    /**
     * Gets the list of all age groups. THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    public List<String> getMenuItems() {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Passing");
        menuItems.add("Shooting");
        menuItems.add("Goalkeeping");

        return menuItems;
    }
}
