package edu.neu.madcourse.studybuddy.groupArtifacts;

import android.view.View;
import android.widget.AdapterView;

/**
 * A class that represents the group a student has
 */
public class GroupCard implements AdapterView.OnItemClickListener {

    String title;
    String subject;
    String location;

    public GroupCard(String title, String date, String location) {
        this.title = title;
        this.subject = date;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
