package com.air.twitterclient.handler;

import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.air.twitterclient.activity.TimeLineActivity;
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
    private TimeLineActivity timeLineActivity;
    private ProgressBar pb;

    public TweetJSONResponseHandler(TweetArrayAdaptor adaptor, TextView mTitle, TimeLineActivity timeLineActivity, ProgressBar pb) {
        this.adaptor = adaptor;
        this.mTitle = mTitle;
        this.timeLineActivity = timeLineActivity;
        this.pb = pb;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        if(pb != null) {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        if(timeLineActivity != null) {
            timeLineActivity.hideProgressBar();
        }
        List<Tweet> tweets = Tweet.fromJSONArray(response);
        adaptor.addAll(tweets);
        Log.d("tweets = ", tweets.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("API Fail", errorResponse.toString(), throwable);
    }
}
