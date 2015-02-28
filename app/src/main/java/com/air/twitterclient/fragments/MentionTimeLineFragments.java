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
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;

/**
 * Created by hetang on 2/27/15.
 */
public class MentionTimeLineFragments extends TweetListFragment {
    private TwitterClient client;
    private TweetHelper helper;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        helper = new TweetHelper();
        helper.setClient(client);
        setupHelper(helper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        setupScrollListener(new EndlessMentionsTweetScrollListener(helper));
        setupRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                helper.populateMentionsTimeLine();
            }
        });
        helper.populateMentionsTimeLine();
        return v;
    }
}
