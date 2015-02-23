package com.air.twitterclient.handler;

import android.util.Log;
import android.widget.TextView;

import com.air.twitterclient.adaptor.TweetArrayAdaptor;
import com.air.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hetang on 2/22/15.
 */
public class TweetJSONResponseHandler extends JsonHttpResponseHandler {
    private TweetArrayAdaptor adaptor;
    private TextView mTitle;

    public TweetJSONResponseHandler(TweetArrayAdaptor adaptor, TextView mTitle) {
        this.adaptor = adaptor;
        this.mTitle = mTitle;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        List<Tweet> tweets = Tweet.fromJSONArray(response);
        adaptor.addAll(tweets);
        Log.d("tweets = ", tweets.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("API Fail", errorResponse.toString(), throwable);
    }
}
