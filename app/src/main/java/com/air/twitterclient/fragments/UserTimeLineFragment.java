package com.air.twitterclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.air.twitterclient.helpers.TweetHelper;
import com.air.twitterclient.listners.EndlessMentionsTweetScrollListener;
import com.air.twitterclient.listners.EndlessUserTimeLineTweetScrollListener;
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;

/**
 * Created by hetang on 2/28/15.
 */
public class UserTimeLineFragment extends TweetListFragment{

    private TwitterClient client;
    private TweetHelper helper;
    private String screenName;

    public static UserTimeLineFragment newInstance(String screenName){
        UserTimeLineFragment fragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenName = getArguments().getString("screenName");
        client = TwitterApplication.getRestClient();
        helper = new TweetHelper();
        helper.setClient(client);
        setupHelper(helper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        setupScrollListener(new EndlessUserTimeLineTweetScrollListener(helper));
        setupRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                helper.getUserTimeLines(screenName);
            }
        });
        helper.getUserTimeLines(screenName);
        return v;
    }
}
