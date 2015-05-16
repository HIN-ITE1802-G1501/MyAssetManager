package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;


class LogInAsyncTask extends AsyncTask<Pair<List<NameValuePair>, HttpClient>, Void, String> {
    private Context context = null;


    //Konstruktør:
    public LogInAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Pair<List<NameValuePair>, HttpClient>... params) {
        //Henter første parameter, dvs et Pair-objekt:
        Pair pair = params[0];
        //Pair-objektet består av et List<NameValuePair> - objekt og et HttpClient - objekt:
        List<NameValuePair> urlParams = (List<NameValuePair>)pair.first;
        HttpClient httpClient = (HttpClient)pair.second;
        try {
            //Login-forespørsel (NB! Sereradressen bør ikke ‘hardkodes’ so her):
            String serverURL = "http://kark.hin.no:8088/d3330log_backend/logIn";
            HttpPost httpPost = new HttpPost(serverURL);
            httpPost.setEntity(new UrlEncodedFormEntity(urlParams));
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            }
            return "Feil: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
        } catch (ClientProtocolException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //((MainActivity)context).showLoginResult(result);
    }
}