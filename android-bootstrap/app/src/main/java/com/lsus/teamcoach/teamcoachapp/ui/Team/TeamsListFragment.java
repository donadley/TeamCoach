package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Don on 3/7/2015
 */
public class TeamsListFragment extends ListFragment {

    @Inject protected BootstrapServiceProvider serviceProvider;

    /**
     * List items provided to
     */
    protected List<Team> items = Collections.emptyList();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);


    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(createAdapter(getTeamItems()));
//        setEmptyText(R.string.no_teams);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
    protected void configureList(final Activity activity, final ListView listView) {
//        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    public Loader<List<Team>> onCreateLoader(final int id, final Bundle args) {

        final List<Team> initialItems = items;
        return new ThrowableLoader<List<Team>>(getActivity(), items) {

            @Override
            public List<Team> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    return getTeamItems();
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

//    @Override
    protected SingleTypeAdapter<Team> createAdapter(final List<Team> items) {
        return new TeamsListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Team item = ((Team) l.getItemAtPosition(position));

        Toaster.showShort(this.getActivity(), "You clicked: " + item);

    }


    /**
     * Gets the list of all the coaches teams . THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    public List<Team> getTeamItems() {
        Singleton singleton = Singleton.getInstance();
        User user = singleton.getCurrentUser();

        ArrayList<Team> menuItems = new ArrayList<Team>();

        if(user.getTeams() == null) {
            //Inform the User to add a team
//            Team team = new Team();
//            team.setTeamName("Name");
//            menuItems.add(team);
        }else{
            menuItems = (user.getTeams());
        }

        return menuItems;
    }
}