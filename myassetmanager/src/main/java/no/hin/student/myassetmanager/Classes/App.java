/**
 * This class extends the Application class and we use it to add aquire a context that can
 * be used for the entire app in all classes.
 * @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
 * @version 1.5
 */


package no.hin.student.myassetmanager.Classes;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;


public class App extends Application {
    public static String TAG = "MyAssetManger-log";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    /**
     * Method for notifying user with a Toast
     *
     * @param resourceId resourceId referenced to a string resource file in values\<filename>.xml.
     */
    public static void notifyUser(int resourceId) {
        Toast.makeText(context, App.getContext().getString(resourceId), Toast.LENGTH_LONG).show();
    }

    /**
     * Method for getting the appication context
     */
    public static Context getContext(){
        return context;
    }
}