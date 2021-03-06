package com.air.twitterclient.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.air.twitterclient.R;
import com.air.twitterclient.fragments.HomeTimeLineFragments;
import com.air.twitterclient.fragments.MentionTimeLineFragments;
import com.air.twitterclient.fragments.TweetListFragment;
import com.air.twitterclient.models.Tweet;
import com.air.twitterclient.models.User;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeLineActivity extends ActionBarActivity implements HomeTimeLineFragments.OnDataPass {
    private String prevQuery;
    private String currentQuery = "";

    private TextView mTitle;
    private User user;
    private TweetListFragment homeFragment;
    private ViewPager pager;
    private TweetsPagerAdaptor pagerAdaptor;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        Typeface fontJamesFajardo = Typeface.createFromAsset(this.getAssets(), "fonts/JamesFajardo.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.tolBrMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar.setLogo(R.drawable.ic_tweets);
        mTitle.setTypeface(fontJamesFajardo);
        mTitle.setText(getResources().getString(R.string.title));

        pager = (ViewPager) findViewById(R.id.vwPgrTweets);
        pagerAdaptor = new TweetsPagerAdaptor(getSupportFragmentManager());
        pager.setAdapter(pagerAdaptor);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tweetsTabs);
        tabStrip.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_time_line, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        final Context self = this;
        /*TextView textView = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);*/

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                currentQuery = query;
                prevQuery = query;
                searchItem.collapseActionView();
                Intent searchTimeLineIntent = new Intent(self, SearchTimeLineActivity.class);
                searchTimeLineIntent.putExtra("query", currentQuery);
                startActivity(searchTimeLineIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (prevQuery != null) {
                    EditText searchText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                    searchText.setText(prevQuery);
                    searchText.setSelection(searchText.getText().length());
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
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
            case R.id.muProfile:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("userInfo", user);
                //profileIntent.putExtra("screen_name", user.getScreenName());
                startActivity(profileIntent);
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
                homeFragment.addUserComposeTweet(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void userData(User user) {
        this.user = user;
        mTitle.setText(Html.fromHtml("@" + user.getScreenName()));
    }

    public class TweetsPagerAdaptor extends FragmentPagerAdapter /*implements PagerSlidingTabStrip.IconTabProvider*/ {
        private String[] tabTitle = {"Home", "Mention"};
        //private int tabIcons[] = {R.drawable.ic_launcher, R.drawable.ic_compose};
        public TweetsPagerAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    TweetListFragment tweetFragment = new HomeTimeLineFragments();
                    homeFragment = tweetFragment;
                    return tweetFragment;
                case 1:
                    return new MentionTimeLineFragments();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }

        /*@Override
        public int getPageIconResId(int position) {
            return tabIcons[position];
        }*/
    }
}
