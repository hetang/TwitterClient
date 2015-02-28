package com.air.twitterclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.air.twitterclient.R;
import com.air.twitterclient.models.User;
import com.air.twitterclient.rest.TwitterApplication;
import com.air.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {
    private User user;
    private TwitterClient client;

    private ImageView ivUserProfileImg;
    private TextView txtName;
    private TextView txtScreenName;
    private TextView mTitle;
    private EditText edTxtTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();

        user = (User) getIntent().getParcelableExtra("userInfo");
        ivUserProfileImg = (ImageView) findViewById(R.id.ivUserProfileImg);
        txtName = (TextView) findViewById(R.id.txtName);
        txtScreenName = (TextView) findViewById(R.id.txtScreenName);
        edTxtTweet = (EditText) findViewById(R.id.edTxtTweet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tolBrCompose);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_compose_title);

        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setLogo(R.drawable.ic_tweets);
        final ComposeActivity context = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });

        mTitle.setText("");
        txtName.setText(user.getName());
        txtScreenName.setText("@" + user.getScreenName());
        Picasso.with(this).load(user.getProfileImageURL()).fit().placeholder(R.drawable.ic_loading).into(ivUserProfileImg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.muCompose:
                final ComposeActivity context = this;
                //Toast.makeText(this, "Tweet Click", Toast.LENGTH_SHORT).show();
                client.postTweet(edTxtTweet.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Tweet Post Response", response.toString());
                        Intent data = new Intent();
                        data.putExtra("response", response.toString());
                        context.setResult(RESULT_OK, data);
                        context.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("Error in User Info", errorResponse.toString(), throwable);
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
