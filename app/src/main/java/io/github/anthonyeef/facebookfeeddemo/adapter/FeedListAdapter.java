package io.github.anthonyeef.facebookfeeddemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import io.github.anthonyeef.facebookfeeddemo.FeedImageView;
import io.github.anthonyeef.facebookfeeddemo.R;
import io.github.anthonyeef.facebookfeeddemo.app.AppController;
import io.github.anthonyeef.facebookfeeddemo.data.FeedItem;

/**
 * Created by anthonyeef on 8/9/15.
 */
public class FeedListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }
    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item,null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        /*initialize the view*/

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);

        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

        FeedItem item = feedItems.get(position);

        /*set the name of the name TextView*/
        name.setText(item.getName());

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        if (!TextUtils.isEmpty(item.getStatus())){
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            statusMsg.setVisibility(View.GONE);
        }

        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">" + item.getUrl() + "</a>"));
            /*make the url clickable*/
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            url.setVisibility(View.GONE);
        }

        /*set the profile source*/
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        /*set the Feed image*/
        if (item.getImage() != null) {
            feedImageView.setImageUrl(item.getImage(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);

            feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                @Override
                public void onError() {

                }

                @Override
                public void onSuccess() {

                }
            });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;

    }
}
