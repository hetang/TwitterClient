package com.air.twitterclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.air.twitterclient.R;
import com.air.twitterclient.fragments.UserHeaderFragment;
import com.air.twitterclient.fragments.UserTimeLineFragment;
import com.air.twitterclient.models.User;

public class ProfileActivity extends ActionBarActivity {
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupView();
        if(savedInstanceState == null) {
            User user = getIntent().getParcelableExtra("userInfo");
            String screenName = getIntent().getStringExtra("screen_name");
            populateHeader(user, screenName);
        }
    }
    private void setupView() {
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
    private void populateHeader(User user, String screenName) {
        // Within the activity
        FragmentTransaction ftHeader = getSupportFragmentManager().beginTransaction();
        UserHeaderFragment fragmentUserHeader = null;
        if(user != null) {
            fragmentUserHeader = UserHeaderFragment.getInstance(user);
        } else {
            fragmentUserHeader = UserHeaderFragment.getInstance(screenName);
        }
        ftHeader.replace(R.id.rlUserHeader, fragmentUserHeader);
        ftHeader.commit();

        // Within the activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserTimeLineFragment fragmentUserTimeLine = UserTimeLineFragment.newInstance(user != null ? user.getScreenName() : screenName);
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
