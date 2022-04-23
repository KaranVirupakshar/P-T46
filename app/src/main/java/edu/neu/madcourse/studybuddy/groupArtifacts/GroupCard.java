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
    String groupId;
    public GroupCard(String title, String date, String location) {
        this.title = title;
        this.subject = date;
        this.location = location;
    }

    /**
     * A constructor for the card.
     * @param title The title for the group.
     * @param subject The subject of the group.
     * @param location The location where the group meets.
     * @param groupId The groupId of the group.
     */
    public GroupCard(String title, String subject, String location, String groupId) {
        this.title = title;
        this.subject = subject;
        this.location = location;
        this.groupId = groupId;
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

    public String getGroupId() {
        return groupId;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public String
    toString() {
        return "GroupCard{" +
                "title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", location='" + location + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
