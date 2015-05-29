package no.hin.student.myassetmanager.Classes;


import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import no.hin.student.myassetmanager.R;

/**
 * Created by wfa on 07.04.2015.
 *
 * Objekter av denne typen representerer utstyr (nettbrett, smartklokker osv).
 *
 */
public class Equipment extends AssetManagerObjects
{
    private int e_id;           //PrimÃ¦rnÃ¸kkelfelt, autogenereres i databasen.
    private String type;        //Type/gruppe utstyr, f.eks. "Nettbrett". Settes til en av verdiene fra: http://kark.hin.no:8088/d3330log_backend/utstyrstyper.txt
    private String brand;       //F.eks. "Samsung"
    private String model;       //F.eks. "Galaxy Tab S 10.5"
    private String description; //En kort beskrivelse av utstyret (ved behov)
    private String it_no;       //Klistrelappene som vi setter pÃ¥ alt utstyr, f.eks. "IT-4111".
    private String aquired;     //NÃ¥r ble utstyret kjÃ¸pt, dvs. en dato pÃ¥ format "dd.mm.Ã¥Ã¥Ã¥Ã¥"
    private byte[] image;       //Bilde (nedskalert) av utstyret.

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


    public static void deleteEquipment(ArrayAdapter<Equipment> adapterInstance, Equipment equipment) {
        Log.d(adapterInstance.getContext().getString(R.string.LOGTAG), "Delete equipment from list and database");
        adapterInstance.remove(equipment);
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
        return this.getBrand() + " " + this.getModel();
    }

    @Override
    public String getListItemSubTitle() {
        return null;
    }


    @Override
    public int getListItemImage() {
        return Category.getCategoryImage(this.getType());
    }
}