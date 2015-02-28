package com.air.twitterclient.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.twitterclient.R;
import com.air.twitterclient.fragments.UserTimeLineFragment;
import com.air.twitterclient.models.User;
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {
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
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupView();
        if(savedInstanceState == null) {
            user = (User) getIntent().getParcelableExtra("userInfo");
            if (user == null) {
                String screenName = (String) getIntent().getStringExtra("screen_name");
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
            } else {
                populateHeader(user);
            }
        }
    }
    private void setupView() {
        ivUserProfileImg = (ImageView) findViewById(R.id.ivUserProfileImg);
        ivUserProfileBGImg = (ImageView) findViewById(R.id.ivUserProfileBGImg);
        txtUserScreenName = (TextView) findViewById(R.id.txtUserScreenName);
        txtUserTagLine = (TextView) findViewById(R.id.txtUserTagLine);
        txtTweetCount = (TextView) findViewById(R.id.txtTweetCount);
        txtTweetCountLabel = (TextView) findViewById(R.id.txtTweetCountLabel);
        txtFollowingCount = (TextView) findViewById(R.id.txtFollowingCount);
        txtFollowingCountLabel = (TextView) findViewById(R.id.txtFollowingCountLabel);
        txtFollowersCount = (TextView) findViewById(R.id.txtFollowersCount);
        txtFollowersLabel = (TextView) findViewById(R.id.txtFollowersLabel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tolBrProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_profile_title);

        toolbar.setNavigationIcon(R.drawable.ic_back_white);

        toolbar.setLogo(R.drawable.ic_tweets);
        final ProfileActivity context = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });

        mTitle.setText("");
    }
    private void populateHeader(User user) {
        if(user != null) {
            Picasso.with(this).load(user.getProfileImageURL()).fit().placeholder(R.drawable.ic_loading).into(ivUserProfileImg);
            if(user.getProfileBackgroundURL() != null) {
                Picasso.with(this).load(user.getProfileBackgroundURL()).fit().into(ivUserProfileBGImg);
            }
            txtUserScreenName.setText(user.getScreenName());
            txtUserTagLine.setText(user.getTagLine());
            txtTweetCount.setText(String.valueOf(user.getTweetCount()));
            txtFollowingCount.setText(String.valueOf(user.getFollowingCount()));
            txtFollowersCount.setText(String.valueOf(user.getFollowersCount()));
        }

        // Within the activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserTimeLineFragment fragmentUserTimeLine = UserTimeLineFragment.newInstance(user != null ? user.getScreenName() : null);
        ft.replace(R.id.rlUserLayout, fragmentUserTimeLine);
        ft.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
