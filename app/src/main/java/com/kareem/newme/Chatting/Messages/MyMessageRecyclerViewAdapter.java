package com.kareem.newme.Chatting.Messages;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Message> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMessageRecyclerViewAdapter( OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (RunTimeItems.loggedUser.getId().equals(mValues.get(position).senderId+"")) return R.layout.message_list_item_remote;
        else  return R.layout.message_list_item_active;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<Message> getmValues() {
        return mValues;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public Message mItem;

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
