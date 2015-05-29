package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

import no.hin.student.myassetmanager.R;

public class AssetManagerAdapter extends BaseAdapter {
    private static ArrayList<AssetManagerObjects> arrayList;

    private LayoutInflater mInflater;

    public AssetManagerAdapter(Context context, ArrayList results) {
        arrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return arrayList.size();
    }


    public void add(AssetManagerObjects myObject) {
        arrayList.add(myObject);
    }

    public void remove(AssetManagerObjects myObject) {
        arrayList.remove(myObject);
    }

    public Object getItem(int position) {
        return arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_layout1, null);
                holder = new ViewHolder();
                holder.ivListItemImage = (ImageView) convertView.findViewById(R.id.image);
                holder.ivListItemTitle = (TextView) convertView.findViewById(R.id.title);
                holder.ivListItemSubTitle = (TextView) convertView.findViewById(R.id.subtitle);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivListItemImage.setImageResource(arrayList.get(position).getListItemImage());
            holder.ivListItemTitle.setText(arrayList.get(position).getListItemTitle());
            holder.ivListItemSubTitle.setText(arrayList.get(position).getListItemTitle());

        } catch (Exception e) {
            Log.d("", e.toString());
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView ivListItemImage;
        TextView ivListItemTitle;
        TextView ivListItemSubTitle;
    }
}
