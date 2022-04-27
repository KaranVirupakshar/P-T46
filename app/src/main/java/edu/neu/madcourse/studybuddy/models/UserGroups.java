package edu.neu.madcourse.studybuddy.models;

/**
 * A class that maps a user and the various groups they belong to.
 */
public class UserGroups {
    String user;
    String [] groups;

    public UserGroups(String user, String[] groups) {
        this.user = user;
        this.groups = groups;
    }

    public String getUser() {
        return user;
    }

    public String[] getGroups() {
        return groups;
    }
}
