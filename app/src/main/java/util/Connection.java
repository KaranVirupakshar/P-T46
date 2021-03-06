package util;

public class Connection {

    String userName;
    String firstName;
    String lastName;
    String fullName;
    String logo;

    public Connection(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        setLogo();
        setFullName();
    }

    public Connection(String username, String fullName) {
        this.userName = username;
        this.fullName = fullName;
        setFirstLastFromFullName();
        setLogo();

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogo() { return this.logo; }

    private void setLogo() {
        this.logo = firstName.substring(0,1) + lastName.substring(0,1);
    }

    private void setFullName() { fullName = firstName + " " + lastName; }

    public String getFullName() { return fullName; }

    private void setFirstLastFromFullName() {

        int indexOfSpace = this.fullName.indexOf(" ");
        this.firstName = this.fullName.substring(0, indexOfSpace);
        this.lastName = this.fullName.substring(indexOfSpace+1);

    }

}
