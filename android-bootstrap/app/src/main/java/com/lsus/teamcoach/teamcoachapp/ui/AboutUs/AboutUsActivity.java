package com.lsus.teamcoach.teamcoachapp.ui.AboutUs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapFragmentActivity;
import com.lsus.teamcoach.teamcoachapp.util.UIUtils;

/**
 * Created by Don on 4/29/2015.
 */
public class AboutUsActivity extends BootstrapFragmentActivity{


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);

        setTitle(R.string.about_us);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // Source:
            // http://developer.android.com/training/implementing-navigation/ancestral.html
            // This is the home button in the top left corner of the screen.
            case android.R.id.home:
                final Intent upIntent = NavUtils.getParentActivityIntent(this);
                // If parent is not properly defined in AndroidManifest.xml upIntent will be null
                // TODO hanlde upIntent == null
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
