package edu.neu.madcourse.studybuddy;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.AcceptRejectButtonListener;
import util.CustomSnackBar;
import util.Connection;

public class Profile_ConnectionRequestsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Connection> pendingConnectionRequests;
    private RecyclerView myConnectionRequestsRecyclerView;
    private TextView emptyRecyclerViewConnectionRequests;
    private AcceptRejectButtonListener acceptRejectButtonListener;
    private FirebaseFirestore db;
    private CustomSnackBar snackBar;
    private String email;
    private String userName;
    private static String currUserFullName;

    public Profile_ConnectionRequestsFragment(String currUserFullName) {
        this.currUserFullName = currUserFullName;
    }

    public static Profile_ConnectionRequestsFragment newInstance(int columnCount) {
        Profile_ConnectionRequestsFragment fragment = new Profile_ConnectionRequestsFragment(currUserFullName);
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

        this.pendingConnectionRequests = new ArrayList<>();

        this.db = FirebaseFirestore.getInstance();
        this.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        this.userName = email.substring(0, email.indexOf("@studybuddy.com"));

        snackBar = new CustomSnackBar();

        this.acceptRejectButtonListener = new AcceptRejectButtonListener() {
            @Override
            public void onAccept(int position) {

                WriteBatch stepsToAcceptConnectionRequest = db.batch();
                final String connectionRequestUsernameToAccept = pendingConnectionRequests.get(position).getUserName();

                String fullNameOfConnectionToAccept = pendingConnectionRequests.get(position).getFullName();

                Map<String, Object> receivedConnectionRequest = new HashMap<>();
                receivedConnectionRequest.put(connectionRequestUsernameToAccept, FieldValue.delete());

                DocumentReference dfReceivedConnectionRequest = db
                        .collection("users").document(userName)
                        .collection("connections").document("received");

                stepsToAcceptConnectionRequest.update(dfReceivedConnectionRequest, new HashMap<>(receivedConnectionRequest));

                DocumentReference dfCurrentConnections = db
                        .collection("users").document(userName)
                        .collection("connections").document("current");

                receivedConnectionRequest.put(connectionRequestUsernameToAccept, fullNameOfConnectionToAccept);
                stepsToAcceptConnectionRequest.set(dfCurrentConnections,new HashMap<>(receivedConnectionRequest), SetOptions.merge());
                receivedConnectionRequest.remove(connectionRequestUsernameToAccept);

                DocumentReference dfAddedConnectionCurrentConnections = db
                        .collection("users").document(connectionRequestUsernameToAccept)
                        .collection("connections").document("current");

                receivedConnectionRequest.put(userName, currUserFullName);
                stepsToAcceptConnectionRequest.set(dfAddedConnectionCurrentConnections,new HashMap<>(receivedConnectionRequest), SetOptions.merge());
                stepsToAcceptConnectionRequest.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            snackBar.display(getView(), getActivity().getApplicationContext(), "Accepted " + connectionRequestUsernameToAccept);
                        } else {
                            snackBar.display(getView(), getActivity().getApplicationContext(), "Error accepting " + connectionRequestUsernameToAccept);
                        }
                    }
                });
            }

            @Override
            public void onReject(int position) {
                final String usernameOfConnectionToReject = pendingConnectionRequests.get(position).getUserName();

                Map<String, Object> receivedConnectionRequest = new HashMap<>();
                receivedConnectionRequest.put(usernameOfConnectionToReject, FieldValue.delete());

                DocumentReference dfReceivedConnectionRequest = db.collection("users").document(userName)
                        .collection("connections").document("received");
                dfReceivedConnectionRequest.update(receivedConnectionRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            snackBar.display(getView(), getActivity().getApplicationContext(), "Rejected " + usernameOfConnectionToReject);
                        } else {
                            snackBar.display(getView(), getActivity().getApplicationContext(), "Error: Failed to reject " + usernameOfConnectionToReject);
                        }
                    }
                });
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_request_list, container, false);
        this.myConnectionRequestsRecyclerView = view.findViewById(R.id.recyclerViewMyConnectionRequests);
        this.emptyRecyclerViewConnectionRequests = view.findViewById(R.id.emptyRecyclerViewMyConnectionRequests);
        this.myConnectionRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.setUpListenerAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.myConnectionRequestsRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        this.myConnectionRequestsRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    private void setUpListenerAdapter() {
        final DocumentReference df = this.db
                .collection("users").document(userName)
                .collection("connections").document("received");

        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot mapUsernameToFullName, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    return;
                }
                if(mapUsernameToFullName.exists() && mapUsernameToFullName != null) {

                    Set<String> setOfConnectionUsernames = mapUsernameToFullName.getData().keySet();
                    if (setOfConnectionUsernames.size() == 0) {
                        pendingConnectionRequests.clear();
                    }

                    else {
                        for (int i = 0; i < pendingConnectionRequests.size(); i++) {
                            if (setOfConnectionUsernames.contains(pendingConnectionRequests.get(i).getUserName())) {
                                setOfConnectionUsernames.remove(pendingConnectionRequests.get(i).getUserName());
                            }
                        }
                        for (String usernameToAdd : setOfConnectionUsernames) {
                            pendingConnectionRequests.add(
                                    new Connection(usernameToAdd, mapUsernameToFullName.get(usernameToAdd).toString()));
                        }
                    }


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
