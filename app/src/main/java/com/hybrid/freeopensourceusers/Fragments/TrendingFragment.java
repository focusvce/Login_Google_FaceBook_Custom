package com.hybrid.freeopensourceusers.Fragments;



import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hybrid.freeopensourceusers.Adapters.RecyclerTrendingAdapter;
import com.hybrid.freeopensourceusers.PojoClasses.PostFeed;
import com.hybrid.freeopensourceusers.R;
import com.hybrid.freeopensourceusers.Sqlite.DatabaseOperations;
import com.hybrid.freeopensourceusers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {


    DatabaseOperations dp;

    String URL = "http://www.focusvce.com/api/v1/nologin";
    public VolleySingleton mVolleySingleton;
    public RequestQueue mRequestQueue;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public ArrayList<PostFeed> newsFeedsList = new ArrayList<>();
    public RecyclerView trendingRecyclerView;
    public RecyclerTrendingAdapter mRecyclerTrendingAdapter;
    public static final String POST_FEED = "post_feed";

    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        trendingRecyclerView    =   (RecyclerView)view.findViewById(R.id.recycler_view);
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerTrendingAdapter = new RecyclerTrendingAdapter(getContext());
        trendingRecyclerView.setAdapter(mRecyclerTrendingAdapter);
        dp = new DatabaseOperations(getContext());

        if(savedInstanceState != null){
            newsFeedsList = savedInstanceState.getParcelableArrayList(POST_FEED);
            mRecyclerTrendingAdapter.setFeed(newsFeedsList);
        }
        else {
            Toast.makeText(getContext(), "First time working ", Toast.LENGTH_SHORT).show();
            ArrayList<PostFeed> newsFeeds = new ArrayList<>();
            Cursor cr = dp.getInfo_HmtlTest(dp);
            if (cr.getCount() != 0) {
                cr.moveToFirst();
                do {
                    PostFeed newsFeed = new PostFeed(cr.getString(0),cr.getString(1),cr.getString(2),cr.getString(3),cr.getString(4),cr.getInt(5),
                            cr.getInt(6),cr.getInt(7),cr.getInt(8),cr.getString(9),cr.getString(10));
                    Log.e("MYSQL",newsFeed.toString());
                    newsFeeds.add(newsFeed);
                } while (cr.moveToNext());
                mRecyclerTrendingAdapter.setFeed(newsFeeds);
            }
            else Toast.makeText(getContext(),"Count 0",Toast.LENGTH_SHORT).show();
        }
        sendJsonrequest();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(POST_FEED, newsFeedsList);
    }

    private void sendJsonrequest() {
        mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                newsFeedsList = parseJsonResponse(response);
                mRecyclerTrendingAdapter.setFeed(newsFeedsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    private ArrayList<PostFeed> parseJsonResponse(JSONObject response) {
        dp.delete_post(dp);
        ArrayList<PostFeed> newsFeedsList = new ArrayList<>();
        if( response != null && response.length()!=0){

            try {
                JSONArray objectArray = response.getJSONArray("posts");
                for (int i=objectArray.length()-1 ; i>=0 ; i--){

                    int pid = objectArray.getJSONObject(i).getInt("Post_id");
                    int uid = objectArray.getJSONObject(i).getInt("User_id");
                    String link = objectArray.getJSONObject(i).getString("Link");
                    String title = objectArray.getJSONObject(i).getString("Title");
                    String description = objectArray.getJSONObject(i).getString("Description");
                    int up = objectArray.getJSONObject(i).getInt("Likes");
                    int comment_count = objectArray.getJSONObject(i).getInt("no_comments");
                    String postpic = objectArray.getJSONObject(i).getString("Picture_url");
                    String dop = objectArray.getJSONObject(i).getString("Date_of_post");
                    String user_name = objectArray.getJSONObject(i).getString("User_name");
                    String user_pic = objectArray.getJSONObject(i).getString("User_pic");

                    PostFeed postFeedObject = new PostFeed();
                    postFeedObject.setPid(pid);
                    postFeedObject.setUid(uid);
                    postFeedObject.setLink(link);
                    postFeedObject.setTitle(title);
                    postFeedObject.setDescription(description);
                    postFeedObject.setUp(up);
                    postFeedObject.setComment_count(comment_count);
                    postFeedObject.setPostPicUrl(postpic);
                    Date dateOfPost = dateFormat.parse(dop);
                    postFeedObject.setDop(dateOfPost);
                    postFeedObject.setUser_name(user_name);
                    postFeedObject.setUser_pic(user_pic);
                    dp.putInfo_HtmlTest(dp, title, link, description, dop, user_name, pid, up, comment_count, uid, postpic,user_pic);


                    newsFeedsList.add(postFeedObject);
                }

            }catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        return newsFeedsList;
    }

}
