/**
* This is the Category class that represents an asset category.
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Classes;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import no.hin.student.myassetmanager.R;


public class Category extends AssetManagerObjects {
    private int id;                 // Category ID
    private String name;            // Category Name
    public static String url = "http://kark.hin.no:8088/d3330log_backend/utstyrstyper.txt";
    public static String folder;    // Category storage folder


    /**
     * Default constructor for Category
     *
     * @param id represents Category ID
     * @param name represents Category name
     */
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
        return this.getName();
    }

    @Override
    public String getListItemSubTitle(View view) {
        return App.getContext().getString(R.string.CLASS_CATEGORY_STATUS_IN) + ": " + Integer.toString(EquipmentStatus.countEquipmentAvailable(this.getName())) +
                ", " + App.getContext().getString(R.string.CLASS_CATEGORY_STATUS_OUT) + ": " + Integer.toString(EquipmentStatus.countEquipmentInUse(this.getName()));
    }

    /**
    * Enum fo defining category icons
    */
    private enum Icons {
        Annet, Dataskjerm, Dockingstasjon, HDMIsvitsj, Hub, Laptop, Legosett, Nettbrett, Oculus, PC, Ruter, Smartklokker, Svitsj, TVskjerm;
    }


    @Override
    public int getListItemImage() {
        return getCategoryImage(this.name);
    }


    /**
     * Method for getting category image
     *
     * @param categoryName string value for category name (example: "Router")
     */
    public static int getCategoryImage(String categoryName) {
        try {
            Icons icons = Icons.valueOf(categoryName.replace("-",""));

            switch (icons) {
                case Annet:
                    return R.drawable.other;
                case Dataskjerm:
                    return R.drawable.screen;
                case Dockingstasjon:
                    return R.drawable.docking;
                case HDMIsvitsj:
                    return R.drawable.hdmi;
                case Hub:
                    return R.drawable.hub;
                case Laptop:
                    return R.drawable.laptop;
                case Legosett:
                    return R.drawable.lego;
                case Nettbrett:
                    return R.drawable.tablet;
                case Oculus:
                    return R.drawable.oculus;
                case PC:
                    return R.drawable.pc;
                case Ruter:
                    return R.drawable.router;
                case Smartklokker:
                    return R.drawable.smartwatch;
                case Svitsj:
                    return R.drawable.nswitch;
                case TVskjerm:
                    return R.drawable.screen;
            }
        }catch (Exception e) {

        }
        return R.drawable.other;
    }

    public static ArrayList<AssetManagerObjects> getCategories() {
        folder = Environment.getExternalStorageDirectory() + "/Download";
        ArrayList categoryStrings = readCategoriesFromFile(folder + "/products.txt");
        Collections.sort(categoryStrings, String.CASE_INSENSITIVE_ORDER);
        ArrayList<AssetManagerObjects> categories = new ArrayList<>();

        for (int i = 0; i < categoryStrings.size(); i++)
            categories.add(new Category(i, categoryStrings.get(i).toString()));

        return categories;
    }


    /**
     * Method for downloading the categoey file to storage
     */
    public static void downloadCategoriesFile() {
        folder = Environment.getExternalStorageDirectory() + "/Download";

        new Thread(new Runnable() {
            public void run() {

                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;

                try {
                    URL url = new URL(Category.url);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    int lengthOfFile = connection.getContentLength();
                    inputStream = url.openStream();
                    File testDirectory = new File(folder);

                    if (!testDirectory.exists()) {
                        testDirectory.mkdir();
                    }
                    fileOutputStream = new FileOutputStream(folder + "/products.txt");
                    byte data[] = new byte[1024];
                    long total = 0;
                    int count = 0;
                    while ((count = inputStream.read(data)) != -1) {
                        total += count;
                        int progress_temp = (int) total * 100 / lengthOfFile;
                        fileOutputStream.write(data, 0, count);
                    }
                }
                catch (Exception e) {
                    Log.e(App.TAG, "Unable to downloadCategoriesFile" + e.getMessage());
                }
                finally {
                    try {
                        inputStream.close();
                        fileOutputStream.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


    /**
     * Method for reading categories from file to an array
     *
     * @param fileName is the file that contains categories
     */
    public static ArrayList readCategoriesFromFile(String fileName) {
       String line = "";
       ArrayList data = new ArrayList();

       try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);//Can also use a Scanner to read the file

           try {
               while((line = br.readLine()) != null) {
                   data.add(line);
                   Log.d(App.TAG, "Reading line " + line);
               }
           }
           finally {
               br.close();
           }

           }
       catch(FileNotFoundException fN) {
            fN.printStackTrace();
           }
       catch(IOException e) {
            System.out.println(e);
           }

       return data;
     }

}