package com.lsus.teamcoach.teamcoachapp.ui.News;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 4/24/2015.
 */
public class NewsFragment extends Fragment implements View.OnClickListener {

    protected NewsListFragment newsListFragment;
    protected AddNewsFragment addNewsFragment;

    @InjectView(R.id.btn_add_news) Button btn_add_news;
    @InjectView(R.id.btn_refresh) Button btn_refresh;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btn_add_news.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

        newsListFragment = new NewsListFragment();
        newsListFragment.setRetainInstance(true);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack("newsListFragment");
        ft.replace(R.id.news_framelayout, newsListFragment)
                .commit();

        Singleton singleton = Singleton.getInstance();
        if(!singleton.getCurrentUser().getRole().equalsIgnoreCase("player")) {
            btn_add_news.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        if(view.getId() == btn_add_news.getId()) {

            addNewsFragment = new AddNewsFragment();
            addNewsFragment.setParent(this);
            addNewsFragment.setNewsListFragment(newsListFragment);
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack("addNewsFragment");
            ft.replace(newsListFragment.getId(), addNewsFragment)
                    .commit();

            btn_add_news.setVisibility(View.GONE);
        } else if(view.getId() == btn_refresh.getId()){
            newsListFragment.refresh();
        }
    }

    public void showButton(){
        btn_add_news.setVisibility(View.VISIBLE);
    }

    public void hideButton(){
        btn_add_news.setVisibility(View.GONE);
    }
}
