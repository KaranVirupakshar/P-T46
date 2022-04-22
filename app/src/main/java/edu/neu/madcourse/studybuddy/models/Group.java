package edu.neu.madcourse.studybuddy;

import com.google.type.DateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

public class Group {
    String title;
    String subject;
    String location;
    String description;
    List<DayOfWeek> days;
    Date startTime;
    Date endTime;

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

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Group () {

    }

    public Group(String title, String subject, String location,
                 String description, List<DayOfWeek> days, Date startTime,
                 Date endTime) {
        this.title = title;
        this.subject = subject;
        this.location = location;
        this.description = description;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
