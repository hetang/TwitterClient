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
import com.air.twitterclient.fragments.SearchTimeLineFragments;

public class SearchTimeLineActivity extends ActionBarActivity {
    private TextView mTitle;
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_time_line);
        setupView();
        query = getIntent().getStringExtra("query");
        if(savedInstanceState == null) {
            // Within the activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SearchTimeLineFragments fragmentUserTimeLine = SearchTimeLineFragments.newInstance(query);
            ft.replace(R.id.rlSearchTimeLine, fragmentUserTimeLine);
            ft.commit();
        }
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tolBrSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_search_title);

        toolbar.setNavigationIcon(R.drawable.ic_back_white);

        toolbar.setLogo(R.drawable.ic_tweets);
        final SearchTimeLineActivity context = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });

        mTitle.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_time_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
