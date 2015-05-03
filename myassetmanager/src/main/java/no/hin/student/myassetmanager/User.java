package no.hin.student.myassetmanager;


public class User {
    private String username;
    private String firstname;
    private String lastname;


    public User(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }


    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}
