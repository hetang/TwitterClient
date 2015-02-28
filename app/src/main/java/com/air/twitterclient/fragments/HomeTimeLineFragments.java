package com.air.twitterclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.air.twitterclient.helpers.TweetHelper;
import com.air.twitterclient.listners.EndlessHomeTweetScrollListener;
import com.air.twitterclient.models.User;
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hetang on 2/27/15.
 */
public class HomeTimeLineFragments extends TweetListFragment {
    private TwitterClient client;
    private TweetHelper helper;
    private OnDataPass listener;

    // Define the events that the fragment will use to communicate
    public interface OnDataPass {
        public void userData(User user);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDataPass) {
            listener = (OnDataPass) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement HomeTimeLineFragments.OnDataPass");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        helper = new TweetHelper();
        helper.setClient(client);
        setupHelper(helper);
        populateUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        setupScrollListener(new EndlessHomeTweetScrollListener(helper));
        setupRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                helper.populateTimeLine();
            }
        });
        helper.populateTimeLine();
        return v;
    }

    private void populateUserInfo() {
        client.getLoggedInUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("User Info", response.toString());
                User user = User.fromJSON(response);
                listener.userData(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error in User Info", errorResponse.toString(), throwable);
            }
        });
    }
}
