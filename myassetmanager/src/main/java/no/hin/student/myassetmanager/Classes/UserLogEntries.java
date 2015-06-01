package no.hin.student.myassetmanager.Classes;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import java.security.spec.ECField;
import java.util.List;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.R;

/**
 * Created by wfa on 07.04.2015.
 */
public class UserLogEntries extends AssetManagerObjects {
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

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getListItemTitle() {
        return getUser().getFirstname() + " " + getUser().getLastname();
    }

    @Override
    public String getListItemSubTitle(View view) {
        String result = "";
        try {
            List<LogEntry> logEntries = getLogEntries();
            for (LogEntry logEntry : logEntries) {

                if (!result.equals(""))
                    result = result + "\n";
                result = result + "Utstyr: " + EquipmentStatus.getEquipmentById(logEntry.getE_id()).getType() + "\nUtlån: " + logEntry.getOut() + (logEntry.getIn().equals("") ? "" : "\nLevert: " + logEntry.getIn()) + "\n";
            }
        } catch (Exception e) {
            Log.d("-log", e.toString());
        }
        return result;
    }

    @Override
    public int getListItemImage() {
        return R.drawable.user;
    }
}
