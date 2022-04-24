package edu.neu.madcourse.studybuddy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import util.CustomSnackBar;
import util.User;

public class Profile_AddConnectionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button addConnection;
    private ProgressBar progressBar;
    private EditText addConnectionByUsername;
    private CustomSnackBar snackBar;

    private OnFragmentInteractionListener mListener;

    private static String currUserFullName;

    public Profile_AddConnectionFragment(String currUserFullName) {
        this.currUserFullName = currUserFullName;
    }

    public static Profile_AddConnectionFragment newInstance(String param1, String param2) {
        Profile_AddConnectionFragment fragment = new Profile_AddConnectionFragment(currUserFullName);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        snackBar = new CustomSnackBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_connection, container, false);
        this.addConnection = view.findViewById(R.id.addConnectionButton);
        this.progressBar = view.findViewById(R.id.progressBarAddConnection);
        this.addConnectionByUsername = view.findViewById(R.id.addUsernameConnection);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String username = email.substring(0, email.indexOf("@studybuddy.com"));

        this.addConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(addConnectionByUsername.getText()) || addConnectionByUsername.getText().toString().equals(username)) {
                    snackBar.display(view, getActivity(), "Please enter a valid username!");
                }
                else if (addConnectionByUsername.getText().toString().contains(" ")) {
                    snackBar.display(view, getActivity(), "Username cannot contain spaces!");
                }
                else {

                    final String usernameOfConnectionToAdd = addConnectionByUsername.getText().toString().trim();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(usernameOfConnectionToAdd).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists() && documentSnapshot != null) {

                                        final String connectionToAddFullName = documentSnapshot.toObject(User.class).getFullName();

                                        Query query_connectionToAdd = db
                                                .collection("users").document(usernameOfConnectionToAdd)
                                                .collection("connections").whereEqualTo(username, currUserFullName);

                                        query_connectionToAdd.get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshot_connectionToAdd) {
                                                        if (!queryDocumentSnapshot_connectionToAdd.isEmpty()) {
                                                            snackBar.display(view, getActivity(), "He/She is already a connection, or you've already sent a connection request to them!");
                                                        }

                                                        else {

                                                            Query query_currentUser = db
                                                                    .collection("users").document(username)
                                                                    .collection("connections").whereEqualTo(usernameOfConnectionToAdd, connectionToAddFullName);

                                                            query_currentUser.get()
                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshot_currentUser) {
                                                                            if (!queryDocumentSnapshot_currentUser.isEmpty()) {
                                                                                snackBar.display(view, getActivity(), "He/She is already a connection, or you've already sent a connection request to them!");
                                                                            }

                                                                            else {

                                                                                Map<String, Object> connectionToAddHashMap = new HashMap<>();
                                                                                connectionToAddHashMap.put(username, currUserFullName);

                                                                                db.collection("users").document(usernameOfConnectionToAdd)
                                                                                        .collection("connections").document("received")
                                                                                        .set(connectionToAddHashMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        snackBar.display(view, getActivity(), "Connection request successfully sent");
                                                                                        addConnectionByUsername.getText().clear();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        snackBar.display(view, getActivity(), "Connection request failed to send");
                                                                                    }
                                                                                });
                                                                            }

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            snackBar.display(view, getActivity(), "Error connecting to database.");
                                                                        }
                                                                    });

                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        snackBar.display(view, getActivity(), "Error connecting to database.");
                                                    }
                                                });

                                    }
                                    else {
                                        snackBar.display(view, getActivity(), "Username doesn't exist.");
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    snackBar.display(view, getActivity(), "Error connecting to database.");
                                }
                            });
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
