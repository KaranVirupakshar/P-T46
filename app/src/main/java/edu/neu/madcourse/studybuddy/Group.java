package edu.neu.madcourse.studybuddy;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

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
