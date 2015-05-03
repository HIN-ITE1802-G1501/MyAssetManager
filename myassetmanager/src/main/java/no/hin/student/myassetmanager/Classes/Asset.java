package no.hin.student.myassetmanager.Classes;

public class Asset {
    private String name;


    public Asset(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}