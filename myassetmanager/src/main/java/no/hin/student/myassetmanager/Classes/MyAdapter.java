package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import no.hin.student.myassetmanager.R;
//  private <T extends MyObjects> void initializeList(Class<T> classType)
public class MyAdapter extends BaseAdapter {
    private static ArrayList<MyObjects> arrayList;

    private LayoutInflater mInflater;

    public MyAdapter(Context context, ArrayList results) {
        arrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return arrayList.size();
    }

//    public <T extends MyObjects> void add(Class<T> myObject) {
    public void add(MyObjects myObject) {
        arrayList.add(myObject);
    }

    public void remove(MyObjects myObject) {
        arrayList.remove(myObject);
    }

    public Object getItem(int position) {
        return arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_layout1, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtCityState = (TextView) convertView.findViewById(R.id.surname);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.email);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(arrayList.get(position).getName());
        holder.txtCityState.setText(arrayList.get(position).getName());
        holder.txtPhone.setText(arrayList.get(position).getInfo());

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
        TextView txtPhone;
    }
}
