package com.air.twitterclient.handler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ProgressBar;

import com.air.twitterclient.adaptor.TweetArrayAdaptor;
import com.air.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hetang on 2/22/15.
 */
public class TweetJSONResponseHandler extends JsonHttpResponseHandler {
    private SwipeRefreshLayout swipeContainer;
    private TweetArrayAdaptor adaptor;
    private ProgressBar pb;

    public TweetJSONResponseHandler(TweetArrayAdaptor adaptor, ProgressBar pb, SwipeRefreshLayout swipeContainer) {
        this.adaptor = adaptor;
        this.pb = pb;
        this.swipeContainer = swipeContainer;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        if(swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
        if(pb != null) {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        List<Tweet> tweets = Tweet.fromJSONArray(response);
        adaptor.addAll(tweets);
        Log.d("tweets = ", tweets.toString());
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        if(swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
        if(pb != null) {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        try {
            JSONArray lists = response.getJSONArray("statuses");
            List<Tweet> tweets = Tweet.fromJSONArray(lists);
            adaptor.addAll(tweets);
            Log.d("tweets = ", tweets.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if(swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
        if(pb != null) {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        if(errorResponse != null) {
            Log.e("API Fail", errorResponse.toString(), throwable);
        }
    }
}
