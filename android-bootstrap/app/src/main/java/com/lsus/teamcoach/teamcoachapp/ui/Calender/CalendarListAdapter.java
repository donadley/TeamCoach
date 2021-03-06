package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.*;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.AlternatingColorListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caroline on 3/11/2015.
 */
public class CalendarListAdapter extends AlternatingColorListAdapter<CalendarEvent> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    protected Singleton singleton = Singleton.getInstance();

    public CalendarListAdapter(final LayoutInflater inflater, final List<CalendarEvent> items,
                            final boolean selectable) {
        super(R.layout.calendar_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public CalendarListAdapter(final LayoutInflater inflater, final List<CalendarEvent> items) {
        super(R.layout.calendar_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_eventDate, R.id.tv_eventTeam, R.id.tv_eventName,
                            R.id.tv_eventTime};
    }


    @Override
    protected void update(final int position, final CalendarEvent item) {
        super.update(position, item);

                setText(0, item.getEventDate());
                setText(1, item.getEventTeam());
                setText(2, item.getEventName());
                setText(3, item.getTimeSpan());



    }
}
