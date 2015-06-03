package no.hin.student.myassetmanager.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;


public class FragmentAddEquipment extends Fragment {
    private static final String TAG = "MyAssetManger-log";
    private Context context;
    private Category selectedCategory = null;
    private Equipment equipment;
    private Boolean addNewEquipment = true;

    public FragmentAddEquipment() {

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
        ImageButton addEquipmentButton = (ImageButton)getView().findViewById(R.id.btnEquipmentSave);

        addEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String brand = ((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).getText().toString();
                    String model = ((TextView) getView().findViewById(R.id.etAddEquipmentModel)).getText().toString();
                    String it_no = ((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).getText().toString();
                    String description = ((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).getText().toString();
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date());
                    String category = "";
                    if (selectedCategory != null)
                        category = selectedCategory.getName();

                    if (brand.equals("") || model.equals("") || it_no.equals("") || category.equals("")) {
                        Toast.makeText(v.getContext(), "Vennligst fyll ut alle felter og velg en kategori.", Toast.LENGTH_LONG).show();
                    } else {
                        if (addNewEquipment) {
                            equipment = new Equipment(category, brand, model, description, it_no, date, null);
                            WebAPI.doAddEquipment(context, equipment);
                        } else {
                            equipment.setBrand(((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).getText().toString());
                            equipment.setModel(((TextView) getView().findViewById(R.id.etAddEquipmentModel)).getText().toString());
                            equipment.setIt_no(((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).getText().toString());
                            equipment.setDescription(((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).getText().toString());
                            WebAPI.doUpdateEquipment(v.getContext(), equipment);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    public void editEquipment(Equipment equipment) {
        this.addNewEquipment = false;
        this.equipment = equipment;

        ((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).setText(this.equipment.getBrand());
        ((TextView) getView().findViewById(R.id.etAddEquipmentModel)).setText(this.equipment.getModel());
        ((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).setText(this.equipment.getIt_no());
        ((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).setText(this.equipment.getDescription());
        populateListViewWithCategories(getView().getContext());
    }

    public void newEquipment() {
        this.addNewEquipment = true;
        this.equipment = null;

        ((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).setText("");
        ((TextView) getView().findViewById(R.id.etAddEquipmentModel)).setText("");
        ((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).setText("");
        ((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).setText("");
        populateListViewWithCategories(getView().getContext());
    }


    public void populateListViewWithCategories(Context context) {
        this.context = context;

        final ListView lwAddEquipmentCategory = (ListView)getView().findViewById(R.id.lwAddEquipmentCategory);
        final AssetManagerAdapter adapter = new AssetManagerAdapter(context, Category.getCategories());
        lwAddEquipmentCategory.setAdapter(adapter);

        lwAddEquipmentCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedCategory =(Category) (lwAddEquipmentCategory.getItemAtPosition(myItemInt));

            }
        });
    }

}
