package edu.neu.madcourse.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import util.CustomSnackBar;

public class MainActivityFindGroupFragment extends Fragment {
    private Button searchBotton;
    private EditText zipcode, subject;
    private CheckBox checkedZip, checkedLocation;
    private FirebaseFirestore db;
    CustomSnackBar snackBar;
    private List<Group> groups;


    public MainActivityFindGroupFragment() {

    }

    public static MainActivityFindGroupFragment newInstance(String param1, String param2) {
        MainActivityFindGroupFragment fragment = new MainActivityFindGroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mainactivity_fragment_find_groups,
                container, false);

        searchBotton = (Button) view.findViewById(R.id.groupSearchButton);
        subject = (EditText) view.findViewById(R.id.searchSubject);
        zipcode = (EditText) view.findViewById(R.id.searchZipCode);
        checkedLocation = (CheckBox) view.findViewById(R.id.findByLocation);
        checkedZip = (CheckBox) view.findViewById(R.id.findByZip);
        snackBar = new CustomSnackBar();

        checkedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFindByLocation();
            }
        });

        checkedZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFindByZip();
            }
        });

        searchBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSuitableGroups(view);
            }
        });

        db = FirebaseFirestore.getInstance();

        return view;
    }

    private void searchSuitableGroups(View view) {
        if (subject.getText().length() == 0) {
            snackBar
                    .display(view, getContext(), "Please enter the subject information", R.color.lightBlue);
            return;
        }
        if (!checkedZip.isChecked() && !checkedLocation.isChecked()) {
            snackBar
                    .display(view, getContext(), "Please select one of the 2 search criteria", R.color.lightBlue);
            return;
        }

        CollectionReference dbStudyGroup = db.collection("studyGroups");
        Query query;

        if (checkedZip.isChecked()) {
            if (zipcode.getText().length() == 0) {
                snackBar
                        .display(view, getContext(), "Please enter a zipcode", R.color.lightBlue);
                return;
            }
            try {
                Integer.valueOf(zipcode.getText().toString());
                if (zipcode.getText().length() != 5) {
                    snackBar
                            .display(view, getContext(), "Please enter a valid zipcode", R.color.lightBlue);
                    return;
                }
            }
            catch (Exception e) {
                snackBar
                        .display(view, getContext(), "Please enter a valid zipcode", R.color.lightBlue);
                return;
            }

            query = dbStudyGroup
                    .whereEqualTo("location", zipcode.getText().toString())
                    .whereEqualTo("subject", subject.getText().toString());
        }
        else  {
            query = dbStudyGroup
                    .whereEqualTo("location", "33417")
                    .whereEqualTo("subject", subject.getText().toString());
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    groups = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        groups.add(document.toObject(Group.class));
                    }
                    if (groups.size() == 0){
                        snackBar
                                .display(view, getContext(), "No matching groups found", R.color.lightBlue);
                    }
                }
            }
        });

    }

    private void onClickFindByLocation() {
        if (checkedLocation.isChecked()) {
            // disable zip code input
            zipcode.setEnabled(false);
            zipcode.setFocusable(false);

            // disable find by zip
            checkedZip.setEnabled(false);
            checkedZip.setFocusable(false);
        }
        else {
            //enable zip code input
            zipcode.setEnabled(true);
            zipcode.setFocusable(true);

            // enable find by zip
            checkedZip.setEnabled(true);
            checkedZip.setFocusable(false);
        }
    }

    private void onClickFindByZip() {
        if (checkedZip.isChecked()) {
            //disable find by location
            checkedLocation.setFocusable(false);
            checkedLocation.setEnabled(false);
        }
        else {
            // enable find by location
            checkedLocation.setFocusableInTouchMode(true);
            checkedLocation.setEnabled(true);
        }
    }

}
