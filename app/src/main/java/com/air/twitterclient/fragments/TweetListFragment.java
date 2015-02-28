package com.air.twitterclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.air.twitterclient.R;
import com.air.twitterclient.adaptor.TweetArrayAdaptor;
import com.air.twitterclient.helpers.TweetHelper;
import com.air.twitterclient.listners.EndlessHomeTweetScrollListener;
import com.air.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hetang on 2/27/15.
 */
public class TweetListFragment extends Fragment {
    private List<Tweet> tweets;
    private TweetArrayAdaptor adaptor;
    private ListView lvTweetList;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeContainer;
    private TweetHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        adaptor = new TweetArrayAdaptor(getActivity(), tweets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        pb = (ProgressBar) v.findViewById(R.id.pbTweetsLoading);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.scTweetRefresh);
        lvTweetList = (ListView) v.findViewById(R.id.lvTimeLine);
        lvTweetList.setAdapter(adaptor);

        // Setup refresh listener which triggers new data loading

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        this.helper.setAdaptor(adaptor);
        this.helper.setPb(pb);
        this.helper.setSwipeContainer(swipeContainer);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setupHelper(TweetHelper helper) {
        this.helper = helper;
    }

    public void addUserComposeTweet(Tweet tweet) {
        tweets.add(0,tweet);
        adaptor.notifyDataSetChanged();
    }

    public void setupScrollListener(AbsListView.OnScrollListener listener) {
        lvTweetList.setOnScrollListener(listener);
    }

    public void setupRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeContainer.setOnRefreshListener(listener);
    }
}
