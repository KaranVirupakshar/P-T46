package edu.neu.madcourse.studybuddy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityHomeFragment extends Fragment {
    private FloatingActionButton addGroupButton;
    private EditText title, description, subject, location;
    private Button newGroup_cancel, newGroup_Add;
    private AlertDialog.Builder newGroupDialog;
    private AlertDialog alertDialog;


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

        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enablePopUp(view);
            }
        });
        return view;
    }

    public void enablePopUp(View view) {
        newGroupDialog = new AlertDialog.Builder(view.getContext());
        final View popUp = getLayoutInflater().inflate(R.layout.grouppopup, null);
        title = (EditText) popUp.findViewById(R.id.title);
        subject = (EditText) popUp.findViewById(R.id.subject);
        description = (EditText) popUp.findViewById(R.id.description);
        location = (EditText) popUp.findViewById(R.id.description);

        newGroup_Add = (Button) popUp.findViewById(R.id.saveButton);
        newGroup_cancel = (Button) popUp.findViewById(R.id.cancelButton);

        newGroupDialog.setView(popUp);
        alertDialog = newGroupDialog.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        newGroup_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save new group to firebase and close pop up

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
