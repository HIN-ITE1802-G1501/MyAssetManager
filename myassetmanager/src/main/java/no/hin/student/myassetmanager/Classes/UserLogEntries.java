package no.hin.student.myassetmanager.Classes;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by wfa on 07.04.2015.
 */
public class UserLogEntries {
    private  User user;                     //Aktuell bruker
    private List<LogEntry> logEntries;     //En liste med utlÃ¥n for aktuell bruker.

    public UserLogEntries() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    public String toJSONString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
