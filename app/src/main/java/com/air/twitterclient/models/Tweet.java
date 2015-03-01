package com.air.twitterclient.models;

import com.air.twitterclient.helpers.TweetHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hetang on 2/22/15.
 * Sample Object looks like:
 * {
 "coordinates":{
 "coordinates":[
 -122.25831,
 37.871609
 ],
 "type":"Point"
 },
 "truncated":false,
 "created_at":"Tue Aug 28 21:08:15 +0000 2012",
 "favorited":false,
 "id_str":"240556426106372096",
 "in_reply_to_user_id_str":null,
 "entities":{
 "urls":[
 {
 "expanded_url":"http://blogs.ischool.berkeley.edu/i290-abdt-s12/",
 "url":"http://t.co/bfj7zkDJ",
 "indices":[
 79,
 99
 ],
 "display_url":"blogs.ischool.berkeley.edu/i290-abdt-s12/"
 }
 ],
 "hashtags":[

 ],
 "user_mentions":[
 {
 "name":"Cal",
 "id_str":"17445752",
 "id":17445752,
 "indices":[
 60,
 64
 ],
 "screen_name":"Cal"
 },
 {
 "name":"Othman Laraki",
 "id_str":"20495814",
 "id":20495814,
 "indices":[
 70,
 77
 ],
 "screen_name":"othman"
 }
 ]
 },
 "text":"lecturing at the \"analyzing big data with twitter\" class at @cal with @othman  http://t.co/bfj7zkDJ",
 "contributors":null,
 "id":240556426106372096,
 "retweet_count":3,
 "in_reply_to_status_id_str":null,
 "geo":{
 "coordinates":[
 37.871609,
 -122.25831
 ],
 "type":"Point"
 },
 "retweeted":false,
 "possibly_sensitive":false,
 "in_reply_to_user_id":null,
 "place":{
 "name":"Berkeley",
 "country_code":"US",
 "country":"United States",
 "attributes":{

 }
 }
 }
 */
public class Tweet {
    private String body;
    private long uid;
    private String createdAt;
    private User user;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.optString("text");
            tweet.uid = jsonObject.optLong("id",0);
            tweet.createdAt = TweetHelper.getRelativeTimeAgo(jsonObject.optString("created_at"));
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray response) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        for(int i=0;i<response.length();i++) {
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}


