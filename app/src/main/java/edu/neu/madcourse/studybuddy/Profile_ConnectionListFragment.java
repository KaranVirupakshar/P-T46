package edu.neu.madcourse.studybuddy;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;
import java.util.Set;


import javax.annotation.Nullable;

import fastscroll.app.fastscrollalphabetindex.AlphabetIndexFastScrollRecyclerView;
import util.Connection;
import util.ConnectionRecyclerViewAdapter;

public class Profile_ConnectionListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;
    private ArrayList<Connection> connectionList;
    private AlphabetIndexFastScrollRecyclerView myConnectionsRecyclerView;
    private TextView emptyRecyclerView;
    private ConnectionRecyclerViewAdapter connectionRecyclerViewAdapter;
    public Profile_ConnectionListFragment() {
    }

    public static Profile_ConnectionListFragment newInstance(int columnCount) {
        Profile_ConnectionListFragment fragment = new Profile_ConnectionListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.connectionList = new ArrayList<>();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connection_list, container, false);

        this.myConnectionsRecyclerView = view.findViewById(R.id.recyclerViewMyConnections);
        this.emptyRecyclerView = view.findViewById(R.id.emptyRecyclerViewMyConnectionsList);

        this.myConnectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpListenerAdapter();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myConnectionsRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        myConnectionsRecyclerView.addItemDecoration(dividerItemDecoration);
        myConnectionsRecyclerView.setIndexBarColor(R.color.colorAccent);
        myConnectionsRecyclerView.setIndexBarTransparentValue(0);
        myConnectionsRecyclerView.setIndexBarVisibility(false);

        return view;
    }

    private void setUpListenerAdapter() {
        System.out.println(FirebaseAuth.getInstance().getCurrentUser());
        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = email.substring(0, email.indexOf("@studybuddy.com"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference df = db
                .collection("users").document(username)
                .collection("connections").document("current");

        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot mapUsernameToFullName,
                                @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    return;
                }
                if(mapUsernameToFullName.exists() && mapUsernameToFullName != null) {
                    Set<String> setOfConnectionUsernames = mapUsernameToFullName.getData().keySet();
                    if (setOfConnectionUsernames.size() == 0) {
                        connectionList.clear();
                    }
                    else {
                        for (int i = 0; i < connectionList.size(); i++) {
                            if (setOfConnectionUsernames.contains(connectionList.get(i).getUserName())) {
                                setOfConnectionUsernames.remove(connectionList.get(i).getUserName());
                            }

                        }
                        for (String usernameToAdd : setOfConnectionUsernames) {
                            connectionList.add(
                                    new Connection(usernameToAdd, mapUsernameToFullName.get(usernameToAdd).toString()));
                        }

                    }

                    emptyRecyclerView.setVisibility(connectionList.size() == 0 ? View.VISIBLE : View.GONE);
                    myConnectionsRecyclerView.setIndexBarTransparentValue(connectionList.size() != 0 ? 1 : 0);

                    myConnectionsRecyclerView.setIndexBarVisibility(connectionList.size() == 0 ? false : true);
                    connectionRecyclerViewAdapter = new ConnectionRecyclerViewAdapter(connectionList, mListener);
                    connectionRecyclerViewAdapter.notifyDataSetChanged();
                    myConnectionsRecyclerView.setAdapter(connectionRecyclerViewAdapter);

                }

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
    }

}
