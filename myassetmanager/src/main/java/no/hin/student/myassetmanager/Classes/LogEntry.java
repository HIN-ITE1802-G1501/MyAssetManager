package no.hin.student.myassetmanager.Classes;


import android.view.View;

import com.google.gson.Gson;

import no.hin.student.myassetmanager.R;



public class LogEntry extends AssetManagerObjects {
    private int le_id;
    private int u_id;
    private int e_id;
    private String out;
    private String in;
    private String comment;

    public LogEntry() {
        this.le_id = 0;
        this.u_id = 0;
        this.e_id = 0;
        this.out = "";
        this.in = "";
        this.comment = "";
    }

    public LogEntry(int le_id, int u_id, int e_id, String out, String in, String comment) {
        this.le_id = le_id;
        this.u_id = u_id;
        this.e_id = e_id;
        this.out = out;
        this.in = in;
        this.comment = comment;
    }

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
        return comment;
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
        return EquipmentStatus.getEquipmentById(getE_id());
    }

    @Override
    public String getListItemSubTitle(View view) {
        return (getIn().equals("") ? "" : "Levert inn: " + getIn() + "\n") + "Levert ut: " + getOut() + "\n" + (getComment().equals("") ? "" : getComment() );
    }

    @Override
    public int getListItemImage() {
        return (getIn().equals("")) ? R.drawable.logentry_out : R.drawable.logentry_in;
    }
}
