package no.hin.student.myassetmanager.Classes;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;


public class App extends Application {
    public static String TAG = "MyAssetManger-log";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static void notifyUser(int resourceId) {
        Toast.makeText(mContext, App.getContext().getString(resourceId), Toast.LENGTH_LONG).show();
    }


    public static Context getContext(){
        return mContext;
    }
}