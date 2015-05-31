package no.hin.student.myassetmanager.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;


public class FragmentAddEquipment extends Fragment {

    private Context context;

    public FragmentAddEquipment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_equipment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        addButtonClickListener();
    }

    private void addButtonClickListener() {
        Button addEquipmentButton = (Button)getView().findViewById(R.id.buttonAddEquipment);

        addEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = ((TextView)getView().findViewById(R.id.textViewAddEquipmentCategory)).getText().toString();
                String brand = ((TextView)getView().findViewById(R.id.editTextAddEquipmentBrand)).getText().toString();
                String model = ((TextView)getView().findViewById(R.id.editTextAddEquipmentModel)).getText().toString();
                String description = ((TextView)getView().findViewById(R.id.editTextAddEquipmentDescription)).getText().toString();
                String it_no = ((TextView)getView().findViewById(R.id.editTextAddEquipmentItNo)).getText().toString();
                String date = new SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date());

                Equipment equipment = new Equipment(category, brand, model, description, it_no, date, null); // Image temporary null

                WebAPI.doAddEquipment(context, equipment);
            }
        });
    }

    public void populateListViewWithCategories(Context context) {
        this.context = context;

        ListView listViewCategories = (ListView)getView().findViewById(R.id.listViewAddEquipmentCategory);
        final TextView textViewCategory = (TextView)getView().findViewById(R.id.textViewAddEquipmentCategory);
        final AssetManagerAdapter adapter = new AssetManagerAdapter(context, Category.getCategories());
        listViewCategories.setAdapter(adapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textViewCategory.setText(((Category) adapter.getItem(position)).getName());
            }
        });
    }

}
