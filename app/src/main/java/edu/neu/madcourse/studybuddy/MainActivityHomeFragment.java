package edu.neu.madcourse.studybuddy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.studybuddy.groupArtificats.GroupCard;
import edu.neu.madcourse.studybuddy.groupArtificats.GroupCardViewAdapter;
import edu.neu.madcourse.studybuddy.models.Group;
import util.CustomSnackBar;

public class MainActivityHomeFragment extends Fragment {
    private FloatingActionButton addGroupButton;
    //The Recycler view stuff is defined here
    private List<GroupCard> groupCards;

    private RecyclerView recyclerView;
    private GroupCardViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    /*************************************************/
    private EditText title, description, subject, location;
    private Button newGroup_cancel, newGroup_Add;
    private AlertDialog.Builder newGroupDialog;
    private AlertDialog alertDialog;
    private EditText startHour, startMinute, endHour, endMinute;
    private CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private FirebaseFirestore db;
    CustomSnackBar snackBar;

    public MainActivityHomeFragment() {

    }

    public static  MainActivityHomeFragment newInstance(String param1, String param2) {
        MainActivityHomeFragment fragment = new MainActivityHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mainactivity_fragment_home,
                container, false);

        addGroupButton = view.findViewById(R.id.addGroup);
        recyclerView = view.findViewById(R.id.homePageRecyclerView);
        snackBar = new CustomSnackBar();

        // Floating button listener
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enablePopUp(view);
            }
        });
        db = FirebaseFirestore.getInstance();
        return view;
    }

    public void enablePopUp(View view) {
        // Fetching all the required information for group creation
        newGroupDialog = new AlertDialog.Builder(view.getContext());
        final View popUp = getLayoutInflater().inflate(R.layout.grouppopup, null);
        title = (EditText) popUp.findViewById(R.id.title);
        subject = (EditText) popUp.findViewById(R.id.subject);
        description = (EditText) popUp.findViewById(R.id.description);
        location = (EditText) popUp.findViewById(R.id.location);

        startHour = (EditText) popUp.findViewById(R.id.startHour);
        startMinute = (EditText) popUp.findViewById(R.id.startMinute);
        endHour = (EditText) popUp.findViewById(R.id.endHour);
        endMinute = (EditText) popUp.findViewById(R.id.endMinute);

        newGroup_Add = (Button) popUp.findViewById(R.id.saveButton);
        newGroup_cancel = (Button) popUp.findViewById(R.id.cancelButton);

        monday = (CheckBox) popUp.findViewById(R.id.monday);
        tuesday = (CheckBox) popUp.findViewById(R.id.tuesday);
        wednesday = (CheckBox) popUp.findViewById(R.id.wednesday);
        thursday = (CheckBox) popUp.findViewById(R.id.thursday);
        friday = (CheckBox) popUp.findViewById(R.id.friday);
        saturday = (CheckBox) popUp.findViewById(R.id.saturday);
        sunday = (CheckBox) popUp.findViewById(R.id.sunday);

        newGroupDialog.setView(popUp);
        alertDialog = newGroupDialog.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // SAVE button listener
        newGroup_Add.setOnClickListener(new View.OnClickListener() {
            Time startTime;
            Time endTime;
            @Override
            public void onClick(View view) {
                // Days list is created based on how many days are checked
                List<DayOfWeek> days = new ArrayList<>();

                if (monday.isChecked()) {
                    days.add(DayOfWeek.MONDAY);
                }

                if (tuesday.isChecked()) {
                    days.add(DayOfWeek.TUESDAY);
                }

                if (wednesday.isChecked()) {
                    days.add(DayOfWeek.WEDNESDAY);
                }

                if (thursday.isChecked()) {
                    days.add(DayOfWeek.THURSDAY);
                }

                if (friday.isChecked()) {
                    days.add(DayOfWeek.FRIDAY);
                }

                if (saturday.isChecked()) {
                    days.add(DayOfWeek.SATURDAY);
                }

                if (sunday.isChecked()) {
                    days.add(DayOfWeek.SUNDAY);
                }


                // Validity of each field is checked
                if (title.getText().length() == 0) {
                    snackBar
                            .display(view, getContext(), "Please enter title of the group", R.color.lightBlue);
                    return;
                }
                else if (subject.getText().length() == 0) {
                    snackBar
                            .display(view, getContext(), "Please enter subject of the group", R.color.lightBlue);
                    return;
                }
                else if (location.getText().length() == 0){
                    snackBar
                            .display(view, getContext(), "Please enter the zipcode", R.color.lightBlue);
                    return;
                }

                try {
                    Integer.valueOf(location.getText().toString());
                    if (location.getText().length() != 5) {
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


                if (description.getText().length() == 0) {
                    snackBar
                            .display(view, getContext(), "Please enter description of the group", R.color.lightBlue);
                    return;
                }
                else if (days.size() == 0) {
                    snackBar
                            .display(view, getContext(), "Please select atleast one day", R.color.lightBlue);
                    return;
                }

                try {
                    int startHourInteger = Integer.valueOf(startHour.getText().toString());
                    int startTimeInteger = Integer.valueOf(startMinute.getText().toString());

                    int endHourInteger = Integer.valueOf(endHour.getText().toString());
                    int endTimeInteger = Integer.valueOf(endMinute.getText().toString());

                    // Check if end time is grater than start time
                    startTime = new Time(startHourInteger, startTimeInteger, 0);
                    endTime = new Time(endHourInteger, endTimeInteger, 0);

                    if (startTime.compareTo(endTime) == 1) {
                        snackBar
                                .display(view, getContext(), "Please enter end time grater than start time", R.color.lightBlue);
                        return;
                    }
                }
                catch (Exception e) {
                    snackBar
                            .display(view, getContext(), "Please enter valid timings", R.color.lightBlue);
                    return;
                }


                CollectionReference dbStudyGroup = db.collection("studyGroups");

                Group group = new Group(
                        title.getText().toString(),
                        subject.getText().toString(),
                        location.getText().toString(),
                        description.getText().toString(),
                        days,
                        new Date(startTime.getTime()),
                        new Date(endTime.getTime())
                        );

                dbStudyGroup.add(group).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Your Study group has been created", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Some error occurred! Try again", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.dismiss();
            }
        });

        newGroup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close pop up
                alertDialog.dismiss();
            }
        });
    }

}
