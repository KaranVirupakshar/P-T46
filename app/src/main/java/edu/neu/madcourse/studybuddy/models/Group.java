package edu.neu.madcourse.studybuddy.models;


import java.util.ArrayList;
import java.util.List;
/**
 * A model class that represents a group.
 */
public class Group {
    List<String> days;
    String description;
    String endTime;
    String location;
    String startTime;
    String subject;
    String title;

    /**
     * A constructor for the class for a group.
     * @param days The days the group meets.
     * @param description The description of the group.
     * @param endTime The endtime the group meets.
     * @param location The location where the group meets.
     * @param startTime The start time when the group meets.
     * @param subject The subject the group discusses.
     * @param title The title of the group.
     */
    public Group(List<String> days, String description, String endTime, String location, String startTime, String subject, String title) {
        this.days = days;
        this.description = description;
        this.endTime = endTime;
        this.location = location;
        this.startTime = startTime;
        this.subject = subject;
        this.title = title;
    }

    /**
     * A constructor for the main attributes of the group.
     * @param days The days the group meets.
     * @param description The description of the group.
     * @param location The location where the group meets.
     * @param subject The subject the group discusses.
     * @param title The title of the group.
     */
    public Group(List<String> days, String description, String location, String subject, String title) {
        this.days = days;
        this.description = description;
        this.location = location;
        this.subject = subject;
        this.title = title;
    }
}
