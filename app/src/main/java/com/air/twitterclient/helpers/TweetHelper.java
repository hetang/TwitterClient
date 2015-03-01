package com.air.twitterclient.helpers;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.widget.ProgressBar;

import com.air.twitterclient.adaptor.TweetArrayAdaptor;
import com.air.twitterclient.handler.TweetJSONResponseHandler;
import com.air.twitterclient.models.Tweet;
import com.air.twitterclient.rest.TwitterClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hetang on 2/22/15.
 */
public class TweetHelper {
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    private TweetArrayAdaptor adaptor;
    private ProgressBar pb;

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

    public TweetHelper() {}

    public TweetHelper(TwitterClient client, TweetArrayAdaptor adaptor, ProgressBar pb, SwipeRefreshLayout swipeContainer) {
        this.client = client;
        this.adaptor = adaptor;
        this.pb = pb;
        this.swipeContainer = swipeContainer;
    }

    public void setSwipeContainer(SwipeRefreshLayout swipeContainer) {
        this.swipeContainer = swipeContainer;
    }

    public void setClient(TwitterClient client) {
        this.client = client;
    }

    public void setAdaptor(TweetArrayAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public void populateTimeLine() {
        if(pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
        if(adaptor != null) {
            adaptor.clear();
        }
        client.getHomeTimeLine(new TweetJSONResponseHandler(adaptor, pb, swipeContainer));
    }

    public void populateMentionsTimeLine() {
        if(pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
        if(adaptor != null) {
            adaptor.clear();
        }
        client.getMentionTimeLine(new TweetJSONResponseHandler(adaptor, pb, swipeContainer));
    }

    public void getUserTimeLines(String screenName) {
        if(pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
        if(adaptor != null) {
            adaptor.clear();
        }
        client.getUserTimeLine(screenName, new TweetJSONResponseHandler(adaptor, pb, swipeContainer));
    }

    public void fetchNext(int totalItemCount) {
        Tweet tweet = adaptor.getItem(totalItemCount-1);
        client.fetchNextTweet(tweet.getUid(), new TweetJSONResponseHandler(adaptor, null, null));
    }

    public void fetchNextMentionsTweets(int totalItemCount) {
        Tweet tweet = adaptor.getItem(totalItemCount-1);
        client.fetchNextMentionsTweet(tweet.getUid(), new TweetJSONResponseHandler(adaptor, null, null));
    }

    public void fetchNextUserTimeLines(int totalItemCount) {
        Tweet tweet = adaptor.getItem(totalItemCount-1);
        client.fetchNextUserTimeLineTweet(tweet.getUid(), new TweetJSONResponseHandler(adaptor, null, null));
    }

    public void populateSearchTimeLine(String query) {
        if(pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
        if(adaptor != null) {
            adaptor.clear();
        }
        client.getSearchTimeLineTweets(query, new TweetJSONResponseHandler(adaptor, pb, swipeContainer));
    }

    public void fetchNextSearchTweets(String query, int totalItemCount) {
        Tweet tweet = adaptor.getItem(totalItemCount-1);
        client.fetchNextSearchTimeLineTweet(query, tweet.getUid(), new TweetJSONResponseHandler(adaptor, null, null));
    }
}
