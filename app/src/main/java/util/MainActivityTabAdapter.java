package util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import edu.neu.madcourse.studybuddy.MainActivityHomeFragment;
import edu.neu.madcourse.studybuddy.MainActivityProfileFragment;
import edu.neu.madcourse.studybuddy.MainActivityFindGroupFragment;


public class MainActivityTabAdapter extends FragmentStatePagerAdapter {

    private MainActivityHomeFragment tab1;
    private MainActivityFindGroupFragment tab2;
    private MainActivityProfileFragment tab3;
    private int numberOfSubActivities;

    public MainActivityTabAdapter(FragmentManager fm, int numberOfSubActivities) {
        super(fm);
        this.numberOfSubActivities = numberOfSubActivities;
        this.tab1 = new MainActivityHomeFragment();
        this.tab2 = new MainActivityFindGroupFragment();
        this.tab3 = new MainActivityProfileFragment();
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
