package io.github.anthonyeef.facebookfeeddemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.github.anthonyeef.facebookfeeddemo.adapter.FeedListAdapter;
import io.github.anthonyeef.facebookfeeddemo.app.AppController;
import io.github.anthonyeef.facebookfeeddemo.data.FeedItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        /*try to get data from cache*/
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            /*try to get the data from cache*/
            try {
                String data = new String(entry.data, "utf-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            } else {
            /*try to get data from fresh volley request*/
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET, URL_FEED, (String)null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response:" + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
            /*add request to Volley request queue*/
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
        }

    private void parseJsonFeed(JSONObject response) {
        try{
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                /*in case the image is null*/
                String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                item.setImage(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                /*in case the url is null*/
                String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
                item.setUrl(feedUrl);

                /*add item*/
                feedItems.add(item);
            }

            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
