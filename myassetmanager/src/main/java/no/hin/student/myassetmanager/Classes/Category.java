package no.hin.student.myassetmanager.Classes;

import android.widget.ArrayAdapter;


public class Category {
    private int id;
    private String name;


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
}