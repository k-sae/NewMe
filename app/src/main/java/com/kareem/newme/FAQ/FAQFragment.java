package com.kareem.newme.FAQ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;
import com.kareem.newme.UserRoleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FAQFragment extends UserRoleFragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyFAQRecyclerViewAdapter myFAQRecyclerViewAdapter;
    private ExpandableListAdapter expandableListAdapter;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeLayout;
    private Activity parent;
    private boolean isInit;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FAQFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FAQFragment newInstance(int columnCount) {
        FAQFragment fragment = new FAQFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_faq_list, container, false);
        fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, FAQEditor.class);
                startActivityForResult(intent, Constants.FAQ_ACTIVITY_RESULT);
            }
        });


        // Set the adapter
        ExpandableListView expandableListView = (ExpandableListView) fragmentView.findViewById(R.id.expandableListView);
        expandableListAdapter = new ExpandableListAdapter(parent);
        expandableListView.setAdapter(expandableListAdapter);
        grabData();
        isInit = true;
        onUserRoleChanged();
        swipeLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setRefreshing(true);
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        grabData();
    }

    private void grabData()
    {
        if (expandableListAdapter == null) return;
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "getFaq");
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,parent) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(parent, "Network error", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                expandableListAdapter.getExpandableListTitle().clear();
                expandableListAdapter.getExpandableListDetail().clear();
                FAQArray faqs = new Gson().fromJson(response, FAQArray.class);
                for (FAQ faq: faqs.getFaqs()
                     ) {
                    expandableListAdapter.getExpandableListTitle().add(faq.getQuestion());
                    ArrayList<String> ans =  new ArrayList<>(1);
                    ans.add(faq.getAnswer());
                    expandableListAdapter.getExpandableListDetail().put(faq.getQuestion(), ans);
                }

                expandableListAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        };
        volleyRequest.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onUserRoleChanged() {
        if (RunTimeItems.loggedUser != null && RunTimeItems.loggedUser.getUserType().equals(Constants.ADMIN_TYPE))
            fab.setVisibility(View.VISIBLE);
        else fab.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        grabData();
    }
}
