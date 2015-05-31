package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class EquipmentStatus {
    private static ArrayList<Equipment> allEquipment = new ArrayList<>();
    private static ArrayList<Equipment> availableEquipment = new ArrayList<>();
    private static ArrayList<Equipment> inUseEquipment = new ArrayList<>();

    public static void getUpdateFromDatabase(Context context) {
        //WebAPI.doGetEquipmentAll(context);
        WebAPI.doGetEquipmentAvailable(context);
        WebAPI.doGetEquipmentInUse(context);
        Log.e("EquipmentStatus", "getUpdateFromDatabase called");
    }

    public static void setAllEquipment(ArrayList<Equipment> equipment) {
        allEquipment = equipment;
        Log.e("EquipmentStatus", "setAllEquipment called. Count: " + allEquipment.size());
    }

    public static void setAvailableEquipment(ArrayList<Equipment> equipment) {
        availableEquipment = equipment;
        Log.e("EquipmentStatus", "setAvailableEquipment called. Count: " + availableEquipment.size());
    }

    public static void setInUseEquipment(ArrayList<Equipment> equipment) {
        inUseEquipment = equipment;
        Log.e("EquipmentStatus", "setInUseEquipment called. Count: " + inUseEquipment.size());
    }

    public static boolean isEquipmentAvailable(Equipment equipment) {
        Log.d("In EquipmentStatus", "allEquipment count: " + allEquipment.size() + ", availableEquipment count: " + availableEquipment.size());

        for (Equipment e : availableEquipment)
            if (e.getE_id() == equipment.getE_id())
                return true;

        return false; // If true is not already returned, equipment is unavailable
    }

    public static ArrayList<Equipment> getAllEquipment() {
        return allEquipment;
    }

    public static ArrayList<Equipment> getAvailableEquipment() {
        return availableEquipment;
    }

    public static ArrayList<Equipment> getAvailableEquipment(String category) {
        ArrayList<Equipment> result = new ArrayList<Equipment>();
        for (Equipment equipment : availableEquipment) {
            if (equipment.getType().equals(category))
                result.add(equipment);
        }
        return result;
    }

    public static ArrayList<Equipment> getInUseEquipment() {
        return inUseEquipment;
    }

    public static ArrayList<Equipment> getInUseEquipment(String category) {
        ArrayList<Equipment> result = new ArrayList<Equipment>();
        for (Equipment equipment : inUseEquipment) {
            if (equipment.getType().equals(category))
                result.add(equipment);
        }
        return result;
    }
}
