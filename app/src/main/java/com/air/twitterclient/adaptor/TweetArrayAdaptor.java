package com.air.twitterclient.adaptor;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.twitterclient.R;
import com.air.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hetang on 2/22/15.
 */
public class TweetArrayAdaptor extends ArrayAdapter<Tweet> {

    public TweetArrayAdaptor(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            Typeface fontRingm = Typeface.createFromAsset(getContext().getAssets(), "fonts/RINGM.ttf");
            Typeface fontJamesFajardo = Typeface.createFromAsset(getContext().getAssets(), "fonts/JamesFajardo.ttf");

            // Do not attach to parent yet. Hence, we are passing false here
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,
                    parent, false);

            viewHolder.ivProfileImg = (ImageView) convertView.findViewById(R.id.ivProfileImg);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.txtUserName.setTypeface(fontJamesFajardo);
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.txtBody);
            viewHolder.txtBody.setTypeface(fontRingm);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            viewHolder.txtTime.setTypeface(fontRingm);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtUserName.setText(Html.fromHtml(tweet.getUser().getScreenName()));
        viewHolder.txtBody.setText(Html.fromHtml(tweet.getBody()));
        viewHolder.txtTime.setText(tweet.getCreatedAt());
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageURL()).fit().placeholder(R.drawable.ic_loading).into(viewHolder.ivProfileImg);
        return convertView;
    }

    private static class ViewHolder {
        ImageView ivProfileImg;
        TextView txtUserName;
        TextView txtBody;
        TextView txtTime;
    }
}
