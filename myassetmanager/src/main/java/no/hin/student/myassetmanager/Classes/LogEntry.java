/**
* This is the LogEntry class that represents a logentry for an asset.
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.3
*/

package no.hin.student.myassetmanager.Classes;


import android.view.View;

import com.google.gson.Gson;

import no.hin.student.myassetmanager.R;



public class LogEntry extends AssetManagerObjects {
    private int le_id;          // LogEntry ID
    private int u_id;           // User ID
    private int e_id;           // Equipment ID
    private String out;         // Date for equipment loan
    private String in;          // Date for equipment return
    private String comment;     // Comment for

    /**
     * Default constructor for LogEntry
     */
    public LogEntry() {
        this.le_id = 0;
        this.u_id = 0;
        this.e_id = 0;
        this.out = "";
        this.in = "";
        this.comment = "";
    }

    /**
     * Constructor for LogEntry with all parameters
     *
     * @param le_id represents the LogEntry ID
     * @param u_id represents the User ID for the logentry
     * @param e_id represents the equipment ID for the logentry
     * @param out represents the loan date for the logentry
     * @param in represents the return date for the logentry
     * @param comment represents a comment for the logentry
     */
    public LogEntry(int le_id, int u_id, int e_id, String out, String in, String comment) {
        this.le_id = le_id;
        this.u_id = u_id;
        this.e_id = e_id;
        this.out = out;
        this.in = in;
        this.comment = comment;
    }

    /**
     * Constructor for LogEntry without LogEntry ID
     *
     * @param u_id represents the User ID for the logentry
     * @param e_id represents the equipment ID for the logentry
     * @param out represents the loan date for the logentry
     * @param in represents the return date for the logentry
     * @param comment represents a comment for the logentry
     */
    public LogEntry(int u_id, int e_id, String out, String in, String comment) {
        this.u_id = u_id;
        this.e_id = e_id;
        this.out = out;
        this.in = in;
        this.comment = comment;
    }

    public int getLe_id() {
        return le_id;
    }

    public void setLe_id(int le_id) {
        this.le_id = le_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getComment() {
        return comment.trim();
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        Equipment equipment = EquipmentStatus.getEquipmentById(getE_id());
        return equipment.getBrand() + " " + equipment.getModel();
    }

    @Override
    public String getListItemSubTitle(View view) {
        Equipment equipment = EquipmentStatus.getEquipmentById(getE_id());
        return "Kategori: " + equipment.getType() + "\n" + (getIn().equals("") ? "" : "Levert inn: " + getIn() + "\n") + "Levert ut: " + getOut() + (getComment().equals("") ? "" : "\n" + getComment() );
    }

    @Override
    public int getListItemImage() {
        return (getIn().equals("")) ? R.drawable.logentry_out : R.drawable.logentry_in;
    }
}
