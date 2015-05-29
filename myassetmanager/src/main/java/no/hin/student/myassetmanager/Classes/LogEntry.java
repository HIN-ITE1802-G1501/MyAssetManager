package no.hin.student.myassetmanager.Classes;


import com.google.gson.Gson;

import no.hin.student.myassetmanager.R;


/**
 * Created by wfa on 07.04.2015.
 *
 * Objekter av denne typen representerer "utlÃ¥n".
 * Et "utlÃ¥n" bestÃ¥r av en brukerid, utstyrsid, dato_ut og (etter hvert) dato_inn + evt. kommentar.
 */
public class LogEntry extends AssetManagerObjects {
    private int le_id;      //PrimÃ¦rnÃ¸kkelfelt, autogenereres i databasen.
    private int u_id;       //BrukerID, tilsvarer u_id i User-tabellen.
    private int e_id;       //UstyrsID, tilsvarer e_id i Equipment-tabellen.
    private String out;     //Dato for utlÃ¥n, format "dd.mm.Ã¥Ã¥Ã¥Ã¥"
    private String in;      //Dato for innlevering, format "dd.mm.Ã¥Ã¥Ã¥Ã¥"
    private String comment; //Evt. kommentar.

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
        return getU_id() + " " + getComment();
    }

    @Override
    public String getListItemSubTitle() {
        return null;
    }

    @Override
    public int getListItemImage() {
        return R.drawable.logentry;
    }
}
