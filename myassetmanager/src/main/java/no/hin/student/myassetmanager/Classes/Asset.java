package no.hin.student.myassetmanager.Classes;

public class Asset {
    private int id;
    private String name;


    public Asset(int id, String name) {
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
}