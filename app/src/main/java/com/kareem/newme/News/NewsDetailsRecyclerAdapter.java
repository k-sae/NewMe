package com.kareem.newme.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Chatting.Messages.Message;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.Comment;
import com.kareem.newme.Model.Like;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NewsDetailsRecyclerAdapter extends RecyclerView.Adapter<NewsDetailsRecyclerAdapter.ViewHolder> {

    private News news;
    private Context context;
    private String newsId;
    private Boolean isLiked = false;
    private int likeId;
    private TextView likeButton;
    private int likeCount;

    public NewsDetailsRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_details_list_item, parent, false);
        else view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //TODO dont forget the image
        if (position == 0) setNewsLayout(holder.mView);
        else setCommentsLayout(holder.mView, position - 1);
    }


    private void setCommentsLayout(View v, final int position) {
        final EditText content = (EditText) v.findViewById(R.id.comments_list_content);
        TextView name = (TextView) v.findViewById(R.id.comments_list_name);
        TextView date = (TextView) v.findViewById(R.id.comments_list_date);
        final Comment comment = news.getComments().get(position);
        content.setText(comment.getContent());
        content.setEnabled(false);
        name.setText(comment.getUserName());
        date.setText(comment.getDate());
        final View edit_button = v.findViewById(R.id.comments_list_edit_button);
        View delete_button = v.findViewById(R.id.comments_list_del_button);

        if (RunTimeItems.loggedUser != null
                &&
                (RunTimeItems.loggedUser.getUserType().equals(Constants.ADMIN_TYPE)
                        || String.valueOf(comment.getUserId()).equals(RunTimeItems.loggedUser.getId()))) {

            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content.isEnabled()) {
                        content.setEnabled(false);
                        editComment(position, content.getText().toString());
                    } else {
                        content.setEnabled(true);
                        content.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(content, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });
            content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        content.setEnabled(false);

                    }
                }
            });
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(position);
                }
            });

        } else {
            edit_button.setVisibility(View.GONE);
            delete_button.setVisibility(View.GONE);
        }
        //TODO
        // 1- set listeners
        //idk just it seems i forgot something :)
    }

    private void editComment(int position, String newContent) {
        Comment comment = news.getComments().get(position);
        comment.setContent(newContent);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("req", "editComment");
        stringMap.put("newId", newsId);
        stringMap.put("commentId", position + "");
        stringMap.put("comment", new Gson().toJson(comment));
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL, stringMap, context) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {

            }
        };
        volleyRequest.start();
    }

    private void delete(int position) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("req", "deleteComment");
        stringMap.put("newId", newsId);
        stringMap.put("commentId", position + "");
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL, stringMap, context) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {

            }
        };
        volleyRequest.start();
    }

    private void setNewsLayout(View view) {
        TextView newsTitle = (TextView) view.findViewById(R.id.news_details_textView_title);
        TextView newsDetails = (TextView) view.findViewById(R.id.news_details_textView_content);
        TextView newsDate = (TextView) view.findViewById(R.id.news_details_date);
        ImageView imageView = (ImageView) view.findViewById(R.id.news_details_image_view);
        TextView commentsCount = (TextView) view.findViewById(R.id.comments_count_news_list);
        // Set their text
        newsTitle.setText(news.getTitle());
        newsDetails.setText(news.getContent());
        newsDate.setText(news.getDate());
        Picasso.with(context).load(news.getImage_url()).error(R.mipmap.default_image_news).into(imageView);
        commentsCount.setText(news.getComments().size() + "");
        setNewsButtonsLayoutAndListeners(view);
    }

    private void setNewsButtonsLayoutAndListeners(View view) {
        //like button
        //edit
        //delete
        likeButton = (TextView) view.findViewById(R.id.news_details_image_view_like);

        if (RunTimeItems.loggedUser != null) {
            isLiked = false;
            for (int i = 0; i < news.getLikes().size(); i++) {
                Like like = news.getLikes().get(i);
                if (RunTimeItems.loggedUser.getId().equals(like.getUserId().toString())) {
                    isLiked = true;
                    likeId = i;
                    break;
                }

            }
            refreshLikeButtonLayout();
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendLikeRequest();
                }
            });
        }

        //delete button
        setSpinner((Spinner) view.findViewById(R.id.action_spinner));
    }

    public void setSpinner(final Spinner spinner) {
        if (RunTimeItems.loggedUser != null && RunTimeItems.loggedUser.getUserType().equals(Constants.ADMIN_TYPE))
        //TODO
        {
            spinner.setVisibility(View.VISIBLE);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                    R.layout.spinner_item,
                    new ArrayList<String>());
            arrayAdapter.add(context.getResources().getString(R.string.choose_action));
            arrayAdapter.add(context.getResources().getString(R.string.edit));
            arrayAdapter.add(context.getResources().getString(R.string.delete));
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 1) sendEditRequest();
                    else if (position == 2) sendDeleteRequest();
                    if (position!= 0)
                    spinner.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
    }


    @SuppressLint("SetTextI18n")
    private void refreshLikeButtonLayout() {
        if (isLiked) {
            likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.heart_filled, 0, 0, 0);
        } else
            likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
        likeButton.setText(likeCount + "");
    }

    private void sendLikeRequest() {
        isLiked = !isLiked;
        likeCount = isLiked ? likeCount + 1 : likeCount - 1;
        refreshLikeButtonLayout();
        Map<String, String> stringMap = new HashMap<>();
        if (!isLiked) stringMap.put("req", "dislikeNew");
        else
            stringMap.put("req", "likeNew");
        stringMap.put("newId", newsId);
        //TODO fix this
        if (!isLiked) stringMap.put("likeId", String.valueOf(likeId));
        else
            stringMap.put("like", new Gson().toJson(new Like(Integer.valueOf(RunTimeItems.loggedUser.getId()))));
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL, stringMap, context) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {

            }
        };
        volleyRequest.start();
    }

    private void sendDeleteRequest() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("req", "deleteNew");
        stringMap.put("newId", newsId);
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL, stringMap, context) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {
            }
        };
        volleyRequest.start();
    }

    private void sendEditRequest() {
        Intent intent = new Intent(context, NewsEditor.class);
        intent.putExtra(Constants.NEWS_DATA, new Gson().toJson(news));
        intent.putExtra(Constants.NEWS_ID, newsId);
        context.startActivity(intent);
    }



    @Override
    public int getItemCount() {
        return news.getComments().size() + 1;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
        likeCount = news.getLikes().size();
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsId() {
        return newsId;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public final TextView mContentView;
        public News mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

