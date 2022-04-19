package edu.neu.madcourse.studybuddy.models;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

/**
 * A model class that represents a group.
 */
public class Group {
    String title;
    String subject;
    String location;
    String description;
    List<DayOfWeek> days;
    Time startTime;
    Time endTime;

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public List<DayOfWeek> getDays() {
        return days;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    /**
     * A constructor for the group.
     * @param title The title of the group.
     * @param subject The subject which the group discusses.
     * @param location The location where the group meets.
     * @param description The description of the group.
     * @param days The days when the groups meet.
     * @param startTime The start time when the group meets.
     * @param endTime The end time of the group meeting.
     */
    public Group(String title, String subject, String location,
                 String description, List<DayOfWeek> days, Time startTime,
                 Time endTime) {
        this.title = title;
        this.subject = subject;
        this.location = location;
        this.description = description;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
