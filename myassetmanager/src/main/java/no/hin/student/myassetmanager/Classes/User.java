package no.hin.student.myassetmanager.Classes;


import android.widget.ArrayAdapter;

public class User extends MyObjects {
    private int id;
    private String username;
    private String firstname;
    private String lastname;


    public User(int id, String username, String firstname, String lastname) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }


    public static void showUsers(ArrayAdapter<User> adapterInstance) {
        adapterInstance.add(new User(1, "kekarlsen", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User(2, "aleks", "Aleksander", "Valle"));
        adapterInstance.add(new User(3, "billy", "Bill", "Gates"));
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}
