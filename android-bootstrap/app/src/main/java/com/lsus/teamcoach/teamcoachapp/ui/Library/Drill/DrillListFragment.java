package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryFragment;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Views;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_INFO_PARENT;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class DrillListFragment extends ItemListFragment<Drill> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    private String age;
    private String type;
    private String library;
    private LibraryFragment parent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        parent.setHeaderVisibility(true);
        parent.setHeader(age + " " + type + " Drills");
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
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
    public void onResume(){
        this.refresh();
        super.onResume();
    }

    @Override
    public Loader<List<Drill>> onCreateLoader(final int id, final Bundle args) {
        return new ThrowableLoader<List<Drill>>(getActivity(), items) {

            @Override
            public List<Drill> loadData() throws Exception {
                if (getActivity() != null) {
                    return serviceProvider.getService(getActivity()).getDrills(age, type);
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Drill> createAdapter(final List<Drill> items) {
        return new DrillListRatingAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Drill item = ((Drill) l.getItemAtPosition(position));
        Intent drillInfoIntent = new Intent(getActivity(), DrillInfoActivity.class);
        drillInfoIntent.putExtra(DRILL, item);
        drillInfoIntent.putExtra(DRILL_INFO_PARENT, "Drill List");
        startActivity(drillInfoIntent);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_drills;
    }

    public void setDrillData(String age, String type){
        this.age = age;
        this.type = type;
    }

    public String getAge(){
        return age;
    }

    public String getType(){
        return type;
    }

    public String getLibrary(){
        return library;
    }

    public void setLibrary(String library){
        this.library = library;
    }

    public void setParent(LibraryFragment parent) { this.parent = parent; }
}