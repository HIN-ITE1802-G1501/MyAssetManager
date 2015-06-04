package no.hin.student.myassetmanager.Classes;


import android.view.View;
import com.google.gson.Gson;


public class Equipment extends AssetManagerObjects
{
    private int e_id;           // Primary key for equipment
    private String type;        // Equipment group from http://kark.hin.no:8088/d3330log_backend/utstyrstyper.txt
    private String brand;       // Equipment branding, example "Microsoft"
    private String model;       // Equipment model "Surface 3 Pro"
    private String description; // Equipment description
    private String it_no;       // Equipment number "IT-4111".
    private String aquired;     // Equipment aquired date "dd.mm.YYYY"
    private byte[] image;       // Equipment picture max 150px width or height

    public Equipment() {
        this.type = "";
        this.brand = "";
        this.model = "";
        this.description = "";
        this.it_no = "";
        this.aquired = "";
        this.image = null;
    }

    public Equipment(int e_id, String type, String brand, String model, String description, String it_no, String aquired) {
        this.e_id = e_id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.it_no = it_no;
        this.aquired = aquired;
    }

    public Equipment(int e_id, String type, String brand, String model, String description, String it_no, String aquired, byte[] image) {
        this.e_id = e_id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.it_no = it_no;
        this.aquired = aquired;
        this.image = image;
    }

    public Equipment(String type, String brand, String model, String description, String it_no, String aquired, byte[] image) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.it_no = it_no;
        this.aquired = aquired;
        this.image = image;
    }

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIt_no() {
        return it_no;
    }

    public void setIt_no(String it_no) {
        this.it_no = it_no;
    }

    public String getAquired() {
        return aquired;
    }

    public void setAquired(String aquired) {
        this.aquired = aquired;
    }

    public String toJSONString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public static void postDeleteEquipment() {
        WebAPI.doGetUsers(App.getContext(), WebAPI.Method.GET_USERS);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "e_id=" + e_id +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                ", it_no='" + it_no + '\'' +
                ", aquired='" + aquired + '\'' +
                '}';
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getListItemTitle() {
        return this.getType();
    }

    @Override
    public String getListItemSubTitle(View view) {
        return this.getBrand() + " " + this.getModel();
    }


    @Override
    public int getListItemImage() {
        return Category.getCategoryImage(this.getType());
    }
}