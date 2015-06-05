/**
* This is the EqupmentStatus class that keeps track of the status of the assets.
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class EquipmentStatus {
    private static ArrayList<Equipment> allEquipment = new ArrayList<>();           // Array that contains all equipment
    private static ArrayList<Equipment> availableEquipment = new ArrayList<>();     // Array that contains available equipment
    private static ArrayList<Equipment> inUseEquipment = new ArrayList<>();         // Array that contains equipment in use

    /**
     * Method for triggering update from database
     *
     * @param context for the App
     */
    public static void getUpdateFromDatabase(Context context) {
        WebAPI.doGetEquipmentAllForStatus(context);
        WebAPI.doGetEquipmentAvailable(context);
        WebAPI.doGetEquipmentInUse(context);
        Log.e(App.TAG, "getUpdateFromDatabase called");
    }

    /**
     * Method for setting all equipments
     *
     * @param equipment ArrayList of equipments
     */
    public static void setAllEquipment(ArrayList<Equipment> equipment) {
        allEquipment = equipment;
        Log.e(App.TAG, "setAllEquipment called. Count: " + allEquipment.size());
    }

    /**
     * Method for setting available equipments
     *
     * @param equipment ArrayList of equipments
     */
    public static void setAvailableEquipment(ArrayList<Equipment> equipment) {
        availableEquipment = equipment;
        Log.e(App.TAG, "setAvailableEquipment called. Count: " + availableEquipment.size());
    }

    /**
     * Method for setting equipments in use
     *
     * @param equipment ArrayList of equipments
     */
    public static void setInUseEquipment(ArrayList<Equipment> equipment) {
        inUseEquipment = equipment;
        Log.e(App.TAG, "setInUseEquipment called. Count: " + inUseEquipment.size());
    }

    /**
     * Method for checking if an equipment is in use
     *
     * @param equipment to check if it's in use
     * @return returns true if it's in use or false if it's not
     */
    public static boolean isEquipmentAvailable(Equipment equipment) {
        Log.d(App.TAG, "allEquipment count: " + allEquipment.size() + ", availableEquipment count: " + availableEquipment.size());

        for (Equipment e : availableEquipment)
            if (e.getE_id() == equipment.getE_id())
                return true;

        return false; // If true is not already returned, equipment is unavailable
    }

    /**
     * Method for getting a ArrayList with all equipment
     *
     * @return returns ArrayList with all equipment
     */
    public static ArrayList<Equipment> getAllEquipment() {
        return allEquipment;
    }

    /**
     * Method for getting a ArrayList with available equipment
     *
     * @return returns ArrayList with available equipment
     */
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

    /**
     * Method for getting a ArrayList with equipment in use
     *
     * @return returns ArrayList with equipments in use
     */
    public static ArrayList<Equipment> getInUseEquipment() {
        return inUseEquipment;
    }


    /**
     * Method for getting a ArrayList with equipment in use by category
     *
     * @param category is the name of the categories with should get status list from
     * @return returns ArrayList with equipments in use
     */
    public static ArrayList<Equipment> getInUseEquipment(String category) {
        ArrayList<Equipment> result = new ArrayList<Equipment>();
        for (Equipment equipment : inUseEquipment) {
            if (equipment.getType().equals(category))
                result.add(equipment);
        }
        return result;
    }

    /**
     * Method for counting available equipment by category
     *
     * @param category is the name of the categories we should count
     * @return returns integer value that represents the number of available equipment
     */
    public static int countEquipmentAvailable(String category) {
        int result = 0;
        for (Equipment equipment : availableEquipment) {
            if (equipment.getType().equals(category))
                result++;
        }
        return result;
    }

    /**
     * Method for counting equipment in use by category
     *
     * @param category is the name of the categories we should count
     * @return returns integer value that represents the number of equipment in use
     */
    public static int countEquipmentInUse(String category) {
        int result = 0;
        for (Equipment equipment : inUseEquipment) {
            if (equipment.getType().equals(category))
                result++;
        }
        return result;
    }

    /**
     * Method for getting an equipment by it's e_id
     *
     * @param id is the name of the categories we should count
     * @return returns the Equipment object that represents the ID
     */
    public static Equipment getEquipmentById(int id) {

        for (Equipment equipment : allEquipment) {
            if (equipment.getE_id() == id)
                return equipment;
        }
        return null;
    }
}
