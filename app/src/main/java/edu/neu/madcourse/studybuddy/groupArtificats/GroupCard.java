package edu.neu.madcourse.studybuddy.groupArtificats;

import android.view.View;
import android.widget.AdapterView;

/**
 * A class that represents the group a student has
 */
public class GroupCard implements AdapterView.OnItemClickListener {

    String title;
    String date;
    String location;

    public GroupCard(String title, String date, String location) {
        this.title = title;
        this.date = date;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
