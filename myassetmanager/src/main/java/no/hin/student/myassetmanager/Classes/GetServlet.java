package no.hin.student.myassetmanager.Classes;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;

class GetServlet extends AsyncTask<String, Void, String> {
    String TAG = "Project";

    @Override
    protected String doInBackground(String... params) {
        try{
            String urlParams = URLEncoder.encode("device", "UTF-8") + "=" + URLEncoder.encode("test", "UTF-8");
            String serverURL = "http://kark.hin.no:8088/d3330log_backend" + "?" + urlParams;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(serverURL);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                Log.d("TTTTT",EntityUtils.toString(response.getEntity()));

            } else {
            }
        }catch(Exception e){
            return e.toString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null)
            Log.d(TAG, "Ok");
    }
}