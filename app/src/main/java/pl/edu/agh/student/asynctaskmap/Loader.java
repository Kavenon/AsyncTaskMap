package pl.edu.agh.student.asynctaskmap;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Loader extends AsyncTask<Void, Integer, JSONArray> {

    private static final String DATA_URL = "https://api.myjson.com/bins/e28rp";
    private GoogleMap googleMap;
    private ProgressBar progressBar;

    public Loader(GoogleMap googleMap, ProgressBar progressBar) {
        this.googleMap = googleMap;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        googleMap.clear();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }


    @Override
    protected JSONArray doInBackground(Void... params) {
        try {
            for(int i = 1; i<5; i++){
                double v = (double) i / 5;
                publishProgress((int)(v*100));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("Ap","error io", e);
                }
            }
            return getArray(DATA_URL);
        } catch (IOException e) {
            Log.e("Ap","error io", e);
        } catch (JSONException e) {
            Log.e("Ap","error json", e);
        }
        return new JSONArray();
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        for(int i=0; i<jsonArray.length(); i++){

            try {
                createCar(jsonArray, i).addToMap(googleMap);
            }
            catch (JSONException e) {
                Log.e("Ap","error json", e);
            }

        }
        progressBar.setProgress(100);
    }

    private Car createCar(JSONArray jsonArray, int i) throws JSONException {

        JSONObject jsonObject = jsonArray.getJSONObject(i);
        double lat =  jsonObject.getDouble("lat");
        double lon = jsonObject.getDouble("lon");
        String name = jsonObject.getString("name");

        return new Car(lat,lon,name);
    }

    private static JSONArray getArray(String urlString) throws IOException, JSONException {

        URL url = new URL(urlString);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        String jsonString = sb.toString();
        return new JSONArray(jsonString);
    }

}
