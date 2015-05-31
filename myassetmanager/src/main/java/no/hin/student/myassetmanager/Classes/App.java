package no.hin.student.myassetmanager.Classes;

import android.app.Application;
import android.content.Context;

/**
 * Created by kurt-erik on 31.05.2015.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}