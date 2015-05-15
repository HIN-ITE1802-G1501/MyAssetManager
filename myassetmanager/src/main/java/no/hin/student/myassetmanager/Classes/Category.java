package no.hin.student.myassetmanager.Classes;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.Collection;

import no.hin.student.myassetmanager.Interfaces.MyInterface;


public class Category extends MyObjects {
    private int id;
    private String name;

    private static final String TAG = "MyAssetManger-log";


    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getListItemTitle() {
        return "Mer informasjon om kategorien";
    }

    @Override
    public ImageView getListItemImage() {
        return null;
    }


    public static void showCategories(MyAdapter adapterInstance) {
        adapterInstance.add(new Category(1, "PC"));
        adapterInstance.add(new Category(2, "Telefon"));
        adapterInstance.add(new Category(3, "Switch"));
        adapterInstance.add(new Category(4, "Server"));
    }

    public static void deleteCategory(MyAdapter adapterInstance, Category category) {
        Log.d(TAG, "Delete category from list and database");
        adapterInstance.remove(category);
    }
}