package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import no.hin.student.myassetmanager.Interfaces.MyInterface;
import no.hin.student.myassetmanager.R;


public class Category extends MyObjects {
    private int id;
    private String name;
    public static String url = "http://kark.hin.no:8088/d3330log_backend/utstyrstyper.txt";
    public static String folder;

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
    public String getListItemTitle() {
        return this.getName();
    }

    private enum Icons {
        Annet, Dataskjerm, Dockingstasjon, HDMIsvitsj, Hub, Laptop, Legosett, Nettbrett, Oculus, PC, Ruter, Smartklokker, Svitsj, TVskjerm;
    }


    @Override
    public int getListItemImage() {
        return getCategoryImage(this.name);
    }


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

    public static void showCategories(MyAdapter adapterInstance) {
        folder = Environment.getExternalStorageDirectory() + "/Download";
        ArrayList arr = readCategoriesFromFile(folder + "/products.txt");
        Collections.sort(arr, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < arr.size(); i++) {
            adapterInstance.add(new Category(i, arr.get(i).toString()));
        }
    }

    public static void download() {
        folder = Environment.getExternalStorageDirectory() + "/Download";

        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(Category.url);
                    URLConnection conexion = url.openConnection();
                    conexion.connect();
                    int lenghtOfFile = conexion.getContentLength();
                    InputStream is = url.openStream();
                    File testDirectory = new File(folder);
                    if (!testDirectory.exists()) {
                        testDirectory.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(folder + "/products.txt");
                    byte data[] = new byte[1024];
                    long total = 0;
                    int count = 0;
                    while ((count = is.read(data)) != -1) {
                        total += count;
                        int progress_temp = (int) total * 100 / lenghtOfFile;
                        fos.write(data, 0, count);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    Log.e(TAG, "Unable to download" + e.getMessage());
                }
            }
        }).start();
    }


    public static ArrayList readCategoriesFromFile(String fileName) {
       String line = "";
       ArrayList data = new ArrayList();
       try {
            FileReader fr = new FileReader(fileName);
           BufferedReader br = new BufferedReader(fr);//Can also use a Scanner to read the file
            while((line = br.readLine()) != null) {
                 data.add(line);
                    Log.d(TAG, "Reading line " + line);
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