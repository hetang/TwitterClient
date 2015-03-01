package com.air.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by hetang on 2/22/15.
 */
public class User implements Parcelable {

    private long uid;

    private String name;
    private String screenName;
    private String profileImageURL;
    private String profileBackgroundURL;
    private String tagLine;

    private int tweetCount;
    private int followingCount;
    private int followersCount;

    public String getTagLine() {
        return tagLine;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public String getProfileBackgroundURL() {
        return profileBackgroundURL;
    }

    public User() {}

    // Parcelling part
    public User(Parcel in){
        String[] data = new String[9];

        in.readStringArray(data);
        this.uid = Long.parseLong(data[0]);
        this.name = data[1];
        this.screenName = data[2];
        this.profileImageURL = data[3];
        this.profileBackgroundURL = data[4];
        this.tagLine = data[5];
        this.tweetCount = Integer.parseInt(data[6]);
        this.followersCount = Integer.parseInt(data[7]);
        this.followingCount = Integer.parseInt(data[8]);
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        user.name = jsonObject.optString("name");
        user.uid = jsonObject.optLong("id", 0);
        user.screenName = jsonObject.optString("screen_name");
        user.profileImageURL = jsonObject.optString("profile_image_url");

        user.profileBackgroundURL = jsonObject.optString("profile_banner_url");
        if(user.profileBackgroundURL == null || "".equals(user.profileBackgroundURL) || "null".equals(user.profileBackgroundURL)) {
            user.profileBackgroundURL = jsonObject.optString("profile_background_image_url");
        }

        user.tagLine = jsonObject.optString("description");
        user.followersCount = jsonObject.optInt("followers_count", 0);
        user.followingCount = jsonObject.optInt("friends_count", 0);
        user.tweetCount = jsonObject.optInt("statuses_count", 0);
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(this.uid),
                this.name,
                this.screenName,
                this.profileImageURL,
                this.profileBackgroundURL,
                this.tagLine,
                String.valueOf(this.tweetCount),
                String.valueOf(this.followersCount),
                String.valueOf(this.followingCount)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
