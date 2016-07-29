package com.hybrid.freeopensourceusers.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.hybrid.freeopensourceusers.ApplicationContext.MyApplication;
import com.hybrid.freeopensourceusers.PojoClasses.PostFeed;
import com.hybrid.freeopensourceusers.R;
import com.hybrid.freeopensourceusers.Volley.VolleySingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerTrendingAdapter extends RecyclerView.Adapter<RecyclerTrendingAdapter.ViewholderPostFeed> {


    public ArrayList<PostFeed> newsFeedArrayList = new ArrayList<>();
    public LayoutInflater layoutInflater = null;
    public DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd 'at' h:mm a");
    public VolleySingleton volleySingleton;
    public ImageLoader imageLoader;
    public RequestQueue requestQueue;
    public MyApplication myApplication;


    public RecyclerTrendingAdapter(Context context) {
        // Below is for getting application context
        myApplication = MyApplication.getInstance();
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        imageLoader = volleySingleton.getImageLoader();


    }

    public void setFeed(ArrayList<PostFeed> newsFeedArrayList) {
        this.newsFeedArrayList = newsFeedArrayList;
        notifyDataSetChanged();
    }


    @Override
    public ViewholderPostFeed onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.trending_row_layout, parent, false);

        ViewholderPostFeed viewholderPostFeed = new ViewholderPostFeed(view);

        return viewholderPostFeed;
    }

    @Override
    public void onBindViewHolder(final ViewholderPostFeed holder, int position) {

        final PostFeed postFeed = newsFeedArrayList.get(position);
        holder.user_name.setText(postFeed.getUser_name());
        Date gotDate = postFeed.getDop();
        String formatedDate = dateFormat.format(gotDate);
        holder.user_share_time.setText(formatedDate);
        holder.post_title.setText(postFeed.getTitle());
        holder.post_description.setText(postFeed.getDescription());
        holder.like_count.setText(postFeed.getUp()+"");
        holder.comment_count.setText(postFeed.getComment_count()+"");
        String avatar = postFeed.getUser_pic();
        String postpic = postFeed.getPostPicUrl();
        if(avatar!=null){

            imageLoader.get(avatar, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.circleImageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(myApplication.getBaseContext(),"Error loading image", Toast.LENGTH_LONG).show();
                    holder.circleImageView.setImageResource(R.drawable.ic_person_black);
                }
            });

        }else {
            // default
            holder.circleImageView.setImageResource(R.drawable.ic_person_black);
        }
        if(postpic!=null){
            Log.e("PICNULL","PIC IS NULL");
            imageLoader.get(postpic, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.post_pic.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(myApplication.getBaseContext(),"Error loading image", Toast.LENGTH_LONG).show();
                    holder.post_pic.setImageResource(R.drawable.no_image);
                }
            });

        }else {
            // default
            holder.post_pic.setImageResource(R.drawable.quila2);
        }

    }

    @Override
    public int getItemCount() {
        return newsFeedArrayList.size();
    }

    static class ViewholderPostFeed extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView user_name, user_share_time, post_title, post_description, like_count, comment_count;
        public ImageView post_pic, plus_like, minus_dislike, comment_button;
        public RelativeLayout post_header;
        public LinearLayout post_body;


        public ViewholderPostFeed(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.user_profile_image);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_share_time = (TextView) itemView.findViewById(R.id.user_share_time);
            post_title = (TextView) itemView.findViewById(R.id.post_title);
            post_description = (TextView) itemView.findViewById(R.id.post_description);
            like_count = (TextView) itemView.findViewById(R.id.like_count);
            comment_count = (TextView) itemView.findViewById(R.id.comment_count);
            post_pic = (ImageView) itemView.findViewById(R.id.post_pic);
            plus_like = (ImageView) itemView.findViewById(R.id.plus_like);
            minus_dislike = (ImageView) itemView.findViewById(R.id.minus_dislike);
            comment_button = (ImageView) itemView.findViewById(R.id.comment_button);
            post_header = (RelativeLayout) itemView.findViewById(R.id.post_header);
            post_body = (LinearLayout) itemView.findViewById(R.id.post_body);
        }
    }

    /*
     CircleImageView circleImageView;
    TextView user_name, user_share_time, post_title, post_description, like_count, comment_count;
    ImageView post_pic, plus_like, minus_dislike, comment_button;
    RelativeLayout post_header;
    LinearLayout post_body;
    */

}
