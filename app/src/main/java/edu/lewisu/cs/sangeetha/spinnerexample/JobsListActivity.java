package edu.lewisu.cs.sangeetha.spinnerexample;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class JobsListActivity extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);
        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");
        // Log.i("URL", url);
        JSONAsyncTask jsonAsyncTask = new JSONAsyncTask();
        jsonAsyncTask.execute(url);

    }

    class JSONAsyncTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        Dialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(JobsListActivity.this, "", "Downloading Job Data. Please wait...", true, false);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... urlString) {
            String jsonData = "";
            ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> tempResult;

            try {

                //String urlString1 = "https://api.usa.gov/jobs/search.json?size=100&query=nursing+jobs&tags=federal";
                // URL url = new URL(urlString1);
                URL url = new URL(urlString[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = "";
                line = bufferedReader.readLine();
                while (line != null) {
                    jsonData += line;
                    line = bufferedReader.readLine();

                }
                //Parse data

                // Log.i("JSON DATA", jsonData.toString());

                JSONArray jobs = new JSONArray(jsonData);
                //  Log.i("JSON ARRAY", jobs.toString());


                for (int i = 0; i < jobs.length(); i++) {

                    JSONObject jobObject = jobs.getJSONObject(i);
                    String title = jobObject.getString("position_title");
                    String orgName = jobObject.getString("organization_name");
                    String location = jobObject.getString("locations");
                    //String minSalary = jobObject.getString("minimum");
                    //String maxSalary = jobObject.getString("maximum");
                    Double minSalary1 = jobObject.getDouble("minimum");
                    Double maxSalary1 = jobObject.getDouble("maximum");

                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
                    format.setCurrency(Currency.getInstance(Locale.US));
                    String minSalary = format.format(minSalary1);
                    String maxSalary = format.format(maxSalary1);

                    tempResult = new HashMap<String, String>();
                    tempResult.put("title", title);
                    tempResult.put("orgname", orgName);
                    tempResult.put("locations", location);
                    tempResult.put("maximum", maxSalary);
                    tempResult.put("minimum", minSalary);
                    results.add(tempResult);
                    //Log.i("Results", results.toString());

                }
                return results;

            } catch (Exception e) {
                Log.e("error", e.toString());

            }
            return null;
        }

        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {

            Toast.makeText(getApplicationContext(), hashMaps.size() + " results found", Toast.LENGTH_LONG).show();

            ListAdapter adapter = new SimpleAdapter(
                    JobsListActivity.this,
                    hashMaps,
                    R.layout.row,
                    new String[]{"title", "orgname", "locations", "minimum", "maximum"},
                    new int[]{R.id.titleText, R.id.orgNameText, R.id.locations, R.id.min_salary, R.id.max_salary});
            JobsListActivity.this.setListAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }


    }


}
