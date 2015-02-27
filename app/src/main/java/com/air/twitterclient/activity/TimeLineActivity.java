package com.air.twitterclient.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.air.twitterclient.R;
import com.air.twitterclient.adaptor.TweetArrayAdaptor;
import com.air.twitterclient.fragments.TweetListFragment;
import com.air.twitterclient.helpers.TweetHelper;
import com.air.twitterclient.listners.EndlessScrollListener;
import com.air.twitterclient.models.Tweet;
import com.air.twitterclient.models.User;
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends ActionBarActivity {
    private TwitterClient client;
    private TextView mTitle;
    private User user;
    private TweetHelper helper;
    private TweetListFragment fragment;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        fragment = (TweetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);

        client = TwitterApplication.getRestClient();
        helper = new TweetHelper();
        helper.setClient(client);
        fragment.setupHelper(helper);
        helper.populateTimeLine();
        client.getLoggedInUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("User Info", response.toString());
                user = User.fromJSON(response);
                mTitle.setText(Html.fromHtml("@"+user.getScreenName()));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error in User Info", errorResponse.toString(), throwable);
            }
        });


        Typeface fontJamesFajardo = Typeface.createFromAsset(this.getAssets(), "fonts/JamesFajardo.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.tolBrMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar.setLogo(R.drawable.ic_tweets);
        mTitle.setTypeface(fontJamesFajardo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.muCompose:
                //Toast.makeText(this, "Compose Click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, ComposeActivity.class);
                i.putExtra("userInfo", user);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String response = data.getExtras().getString("response");
            try {
                Tweet tweet = Tweet.fromJSON(new JSONObject(response));
                fragment.addUserComposeTweet(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
