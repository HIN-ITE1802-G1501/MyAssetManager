package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.R;



public class FragmentList extends Fragment {

    public FragmentList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public AssetManagerAdapter populateListViewWithCategories(AssetManagerAdapter adapter, Context context, AdapterView.OnItemClickListener clickListener) {
        ListView lvList = (ListView)getView().findViewById(R.id.lvList);
        ((TextView)getView().findViewById(R.id.tvTitle)).setText("Kategori");
        adapter = new AssetManagerAdapter(context, Category.getCategories());
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(clickListener);
        registerForContextMenu(lvList);

        return adapter;
    }




}
