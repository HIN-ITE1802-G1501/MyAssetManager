package no.hin.student.myassetmanager;

public class Category {
    private String name;


    public Category(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}