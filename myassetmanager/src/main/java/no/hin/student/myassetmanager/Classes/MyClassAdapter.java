package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by kurt-erik on 06.05.2015.
 */
public class MyClassAdapter extends ArrayAdapter<MyObjects> {
    public MyClassAdapter(Context context, int textViewResourceId, ArrayList<MyObjects> items) {
        super(context, textViewResourceId, items);
    }
}
