package com.air.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.twitterclient.R;
import com.air.twitterclient.models.User;
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hetang on 2/28/15.
 */
public class UserHeaderFragment extends Fragment {
    private User user;
    private TwitterClient client;
    private ImageView ivUserProfileBGImg;
    private ImageView ivUserProfileImg;
    private TextView txtUserScreenName;
    private TextView txtUserTagLine;

    private TextView txtTweetCount;
    private TextView txtTweetCountLabel;

    private TextView txtFollowingCount;
    private TextView txtFollowingCountLabel;

    private TextView txtFollowersCount;
    private TextView txtFollowersLabel;

    private String screenName;

    public static UserHeaderFragment getInstance(String screenName) {
        UserHeaderFragment fragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserHeaderFragment getInstance(User user) {
        UserHeaderFragment fragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable("userInfo", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("userInfo");
        if (user == null) {
            screenName = getArguments().getString("screenName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_header, parent, false);
        setupView(v);
        if(user != null) {
            populateHeader(user);
        } else {
            populateUserInfo(screenName);
        }
        return v;
    }

    private void populateUserInfo(String screenName) {
        client = TwitterApplication.getRestClient();
        client.getUserDetails(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("User Info", response.toString());
                user = User.fromJSON(response);
                populateHeader(user);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("User Info", response.toString());
                if (response.length() > 0) {
                    try {
                        user = User.fromJSON(response.getJSONObject(0));
                        populateHeader(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error in User Info", errorResponse.toString(), throwable);
            }
        });
    }

    private void setupView(View v) {
        ivUserProfileImg = (ImageView) v.findViewById(R.id.ivUserProfileImg);
        ivUserProfileBGImg = (ImageView) v.findViewById(R.id.ivUserProfileBGImg);
        txtUserScreenName = (TextView) v.findViewById(R.id.txtUserScreenName);
        txtUserTagLine = (TextView) v.findViewById(R.id.txtUserTagLine);
        txtTweetCount = (TextView) v.findViewById(R.id.txtTweetCount);
        txtTweetCountLabel = (TextView) v.findViewById(R.id.txtTweetCountLabel);
        txtFollowingCount = (TextView) v.findViewById(R.id.txtFollowingCount);
        txtFollowingCountLabel = (TextView) v.findViewById(R.id.txtFollowingCountLabel);
        txtFollowersCount = (TextView) v.findViewById(R.id.txtFollowersCount);
        txtFollowersLabel = (TextView) v.findViewById(R.id.txtFollowersLabel);
    }

    private void populateHeader(User user) {
        if(user != null) {
            Picasso.with(getActivity()).load(user.getProfileImageURL()).fit().placeholder(R.drawable.ic_loading).into(ivUserProfileImg);
            if(user.getProfileBackgroundURL() != null) {
                Picasso.with(getActivity()).load(user.getProfileBackgroundURL()).fit().into(ivUserProfileBGImg);
            }
            txtUserScreenName.setText(user.getScreenName());
            txtUserTagLine.setText(user.getTagLine());
            txtTweetCount.setText(String.valueOf(user.getTweetCount()));
            txtFollowingCount.setText(String.valueOf(user.getFollowingCount()));
            txtFollowersCount.setText(String.valueOf(user.getFollowersCount()));
        }
    }
}
