package util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.neu.madcourse.studybuddy.Profile_AddConnectionFragment;
import edu.neu.madcourse.studybuddy.Profile_ConnectionListFragment;
import edu.neu.madcourse.studybuddy.Profile_ConnectionRequestsFragment;


public class ConnectionActivityPagerAdapter extends FragmentStatePagerAdapter {

    private Profile_ConnectionListFragment tab1;
    private Profile_AddConnectionFragment tab2;
    private Profile_ConnectionRequestsFragment tab3;
    private int numberOfSubActivities;
    private String currUserFullName;

    public ConnectionActivityPagerAdapter(FragmentManager fm, int numberOfSubActivities, String currUserFullName) {
        super(fm);
        this.numberOfSubActivities = numberOfSubActivities;
        this.currUserFullName = currUserFullName;
        this.tab1 = new Profile_ConnectionListFragment();
        this.tab2 = new Profile_AddConnectionFragment(this.currUserFullName);
        this.tab3 = new Profile_ConnectionRequestsFragment(this.currUserFullName);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfSubActivities;
    }
}

