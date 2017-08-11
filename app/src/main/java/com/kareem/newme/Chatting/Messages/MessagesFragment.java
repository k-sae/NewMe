package com.kareem.newme.Chatting.Messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kareem.newme.Chatting.UserMessage.UserMessage;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.NavigationActivityCallBack;
import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;
import com.kareem.newme.UserRoleFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MessagesFragment extends UserRoleFragment implements OnListFragmentInteractionListener {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String id;
    private OnListFragmentInteractionListener mListener;
    private MyMessageRecyclerViewAdapter myMessageRecyclerViewAdapter;
    private RecyclerView recyclerView;
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
            recyclerView = (RecyclerView) view;
            myMessageRecyclerViewAdapter = new MyMessageRecyclerViewAdapter(this);
            recyclerView.setAdapter(myMessageRecyclerViewAdapter);
            sync();
        }
        setCommentWriter(fragmentView);
        return fragmentView;
    }
    private void setCommentWriter(View view)
    {
        final EditText comment_editText = (EditText) view.findViewById(R.id.commentWriter_EditText);
        final View send_button = view.findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = comment_editText.getText().toString();
                comment_editText.setText("");
                sendMessage(s);
            }
        });
    }
    private void sendMessage(String s)
    {
        Message message = new Message(Integer.valueOf(RunTimeItems.loggedUser.getId()), s, "0-0-0");
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "sendMessage");
        stringMap.put("message", new Gson().toJson(message));
        stringMap.put("toUser", id);
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,getActivity()) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {

            }
        };
        volleyRequest.start();
    }
    private void sync() {
       String intentString = getActivity().getIntent().getStringExtra(Constants.USER_MESSAGE);
        id = intentString == null
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(myMessageRecyclerViewAdapter.getmValues().size());
                    }
                }, 1);
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

    @Override
    public void onUserRoleChanged() {
        ((NavigationActivityCallBack)getActivity()).setActive(R.id.nav_login);
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
