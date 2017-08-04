package com.kareem.newme.Chatting.Messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kareem.newme.Chatting.UserMessage.UserMessage;
import com.kareem.newme.Constants;
import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MessagesFragment extends Fragment implements OnListFragmentInteractionListener {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;
    private MyMessageRecyclerViewAdapter myMessageRecyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessagesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_message_list, container, false);
        // Set the adapter
        View view = fragmentView.findViewById(R.id.messages_recycle_view);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            myMessageRecyclerViewAdapter = new MyMessageRecyclerViewAdapter(this);
            recyclerView.setAdapter(myMessageRecyclerViewAdapter);
            sync();
        }
        return fragmentView;
    }

    private void sync() {
       String intentString = getActivity().getIntent().getStringExtra(Constants.USER_MESSAGE);
        String id = intentString == null
                ? RunTimeItems.loggedUser.getId()
                : new Gson().fromJson(intentString, UserMessage.class).getId();
        FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myMessageRecyclerViewAdapter.getmValues().clear();
                for (DataSnapshot snapshot: dataSnapshot.child("messages").getChildren()
                        ) {
                    Message  userMessage = snapshot.getValue(Message.class);
                    myMessageRecyclerViewAdapter.getmValues().add(userMessage);
                }
                myMessageRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListFragmentInteraction(Message item) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
