package no.hin.student.myassetmanager.Classes;

import android.util.Log;
import android.widget.ArrayAdapter;


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
    public String toString() {
        return name;
    }


    public static void showCategories(ArrayAdapter<Category> adapterInstanceCategory) {
        adapterInstanceCategory.add(new Category(1, "PC"));
        adapterInstanceCategory.add(new Category(2, "Telefon"));
        adapterInstanceCategory.add(new Category(3, "Switch"));
        adapterInstanceCategory.add(new Category(4, "Server"));
    }

    public static void deleteCategory(ArrayAdapter<Category> adapterInstanceCategory, Category category) {
        Log.d(TAG, "Delete category from list and database");
        adapterInstanceCategory.remove(category);
    }
}