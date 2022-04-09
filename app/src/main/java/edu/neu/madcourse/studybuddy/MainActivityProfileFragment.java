package edu.neu.madcourse.studybuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import util.User;

public class MainActivityProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView userLogo, userName, fullName;
    private Button logOutButton;

    public MainActivityProfileFragment() {
    }

    public static MainActivityProfileFragment newInstance(String param1, String param2) {
        MainActivityProfileFragment fragment = new MainActivityProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mainactivity_fragment_profile,
                container, false);

        this.userLogo = view.findViewById(R.id.userLogo);
        this.userName = view.findViewById(R.id.userName);
        this.fullName = view.findViewById(R.id.fullName);

        this.logOutButton = view.findViewById(R.id.logoutButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        final TabLayout tabLayout = view.findViewById(R.id.connectionsToolbar);
        tabLayout.addTab(tabLayout.newTab().setText("My Connections"));
        tabLayout.addTab(tabLayout.newTab().setText("Add Connections"));
        tabLayout.addTab(tabLayout.newTab().setText("Added By Connections"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = email.substring(0, email.indexOf("@studybuddy.com"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dr = db.collection("users").document(username);

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists() && documentSnapshot != null) {
                    User currentUser = documentSnapshot.toObject(User.class);

                    String userFullName = currentUser.getFirstName() + " " + currentUser.getLastName();
                    String userInitials =
                            String.valueOf(Character.toUpperCase(currentUser.getFirstName().charAt(0))) +
                                    String.valueOf(Character.toUpperCase(currentUser.getLastName().charAt(0)));
                    fullName.setText(userFullName);
                    userLogo.setText(userInitials);
                }
            }
        });
        this.userName.setText(username);
        return view;
    }

}
