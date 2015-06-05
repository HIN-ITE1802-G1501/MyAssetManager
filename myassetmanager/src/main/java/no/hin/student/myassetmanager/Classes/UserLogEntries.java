/**
* This is the UserLogEntries class that represents a list of all users that includes all LogEntries
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Classes;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import no.hin.student.myassetmanager.R;


public class UserLogEntries extends AssetManagerObjects {
    private User user;                     // Current user
    private List<LogEntry> logEntries;     // List of loans for the current user


    /**
     * Default constructor fot the UserLogEntries object
     */
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
            // Loop for finding which equipment is in or out
            List<LogEntry> logEntries = getLogEntries();
            for (LogEntry logEntry : logEntries) {
                String equipmentText = App.getContext().getString(R.string.CLASS_USERLOGENTRIES_EQUIPMENT);
                String outText = App.getContext().getString(R.string.CLASS_USERLOGENTRIES_OUT);
                String inText = App.getContext().getString(R.string.CLASS_USERLOGENTRIES_OUT);

                if (!result.equals(""))
                    result = result + "\n";

                result = result + equipmentText + ": " + EquipmentStatus.getEquipmentById(logEntry.getE_id()).getType() + "\n" + outText + ": " + logEntry.getOut() + (logEntry.getIn().equals("") ? "" : "\n" + inText + ": " + logEntry.getIn()) + "\n";
            }
        } catch (Exception e) {
            Log.d(App.TAG, e.toString());
        }
        return result;
    }

    @Override
    public int getListItemImage() {
        return R.drawable.user;
    }
}
