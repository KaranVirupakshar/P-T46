package util;

public class Connection_withCheck extends Connection {

    private boolean checked;

    public Connection_withCheck(String userName, String firstName, String lastName) {
        super(userName, firstName, lastName);
        this.checked = false;
    }

    public Connection_withCheck(String username, String fullName) {
        super(username, fullName);
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
