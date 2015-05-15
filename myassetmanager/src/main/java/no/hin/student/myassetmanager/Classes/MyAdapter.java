package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
            holder.ivListItemImage = (ImageView) convertView.findViewById(R.id.image);
            holder.ivListItemTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivListItemImage.setImageResource(R.mipmap.ic_launcher);
        holder.ivListItemTitle.setText(arrayList.get(position).getListItemTitle());


        return convertView;
    }

    static class ViewHolder {
        ImageView ivListItemImage;
        TextView ivListItemTitle;
    }
}
