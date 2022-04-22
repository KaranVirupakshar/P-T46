package edu.neu.madcourse.studybuddy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import edu.neu.madcourse.studybuddy.Group;
import util.CustomSnackBar;

public class MainActivityFindGroupFragment extends Fragment {
    private Button searchBotton;
    private EditText zipcode, subject;
    private CheckBox checkedZip, checkedLocation;
    private FirebaseFirestore db;
    CustomSnackBar snackBar;
    private List<Group> groups;
    private final int PERMISSION_ID = 123;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String locationZip;


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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
        }

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

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {


                            try {
                                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                locationZip = String.valueOf(addressList.get(0).getPostalCode());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            getCurrentLocation();
        }
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
            getCurrentLocation();
            if (locationZip == null) {
                snackBar
                        .display(view, getContext(), "No location access", R.color.lightBlue);
                return;
            }
            query = dbStudyGroup
                    .whereEqualTo("location", locationZip)
                    .whereEqualTo("subject", subject.getText().toString());
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    groups = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        groups.add(document.toObject(edu.neu.madcourse.studybuddy.Group.class));
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

            // disable find by zip
            checkedZip.setEnabled(false);
            checkedZip.setFocusable(false);
        }
        else {
            //enable zip code input
            zipcode.setEnabled(true);

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
