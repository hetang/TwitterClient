package com.air.twitterclient.helpers;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.air.twitterclient.activity.TimeLineActivity;
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
    private ListView lvTweetList;
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

    public TweetHelper(TwitterClient client, TweetArrayAdaptor adaptor, ListView lvTweetList, ProgressBar pb, SwipeRefreshLayout swipeContainer) {
        this.client = client;
        this.adaptor = adaptor;
        this.lvTweetList = lvTweetList;
        this.pb = pb;
        this.swipeContainer = swipeContainer;
    }

    public void populateTimeLine() {
        if(pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
        client.getHomeTimeLine(new TweetJSONResponseHandler(adaptor, pb, swipeContainer));
    }

    public void fetchNext(int totalItemCount) {
        Tweet tweet = adaptor.getItem(totalItemCount-1);
        client.fetchNextTweet(tweet.getUid(), new TweetJSONResponseHandler(adaptor, null, null));
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public void setSwipeContainer(SwipeRefreshLayout swipeContainer) {
        this.swipeContainer = swipeContainer;
    }

    public TwitterClient getClient() {
        return client;
    }

    public void setClient(TwitterClient client) {
        this.client = client;
    }

    public TweetArrayAdaptor getAdaptor() {
        return adaptor;
    }

    public void setAdaptor(TweetArrayAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public ListView getLvTweetList() {
        return lvTweetList;
    }

    public void setLvTweetList(ListView lvTweetList) {
        this.lvTweetList = lvTweetList;
    }

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }
}
