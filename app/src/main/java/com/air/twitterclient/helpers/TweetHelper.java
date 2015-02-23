package com.air.twitterclient.helpers;

import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.air.twitterclient.adaptor.TweetArrayAdaptor;
import com.air.twitterclient.handler.TweetJSONResponseHandler;
import com.air.twitterclient.models.Tweet;
import com.air.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by hetang on 2/22/15.
 */
public class TweetHelper {
    private TwitterClient client;
    private TweetArrayAdaptor adaptor;
    private ListView lvTweetList;
    private TextView mTitle;

    public TweetHelper(TwitterClient client, TweetArrayAdaptor adaptor, ListView lvTweetList, TextView mTitle) {
        this.client = client;
        this.adaptor = adaptor;
        this.lvTweetList = lvTweetList;
        this.mTitle = mTitle;
    }
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try{
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void populateTimeLine() {
        client.getHomeTimeLine(new TweetJSONResponseHandler(adaptor, mTitle));
    }

    public void fetchNext(int totalItemCount) {
        Tweet tweet = adaptor.getItem(totalItemCount-1);
        client.fetchNextTweet(tweet.getUid(), new TweetJSONResponseHandler(adaptor, mTitle));
    }
}
