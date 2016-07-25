package khaanavali.customer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;

import khaanavali.customer.adapter.LocationAdapter;
import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
/**
 * Created by dganeshappa on 7/14/2016.
 */
public class PlacesActivity extends AppCompatActivity{

    private static final String PLACES_API_BASE2 = "http://kuruva.herokuapp.com/v1/admin/coverageArea";
    private static final String TAG_SUBAREAS = "subAreas";
    private static final String LOG_TAG = "Autocomplete";
    private ArrayList<String> mCityCoverage;
    //  ArrayAdapter<String> myAdapter;
    ListView listView;
    SearchView search;
    LocationAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        mCityCoverage =  new ArrayList<String>();
        listView = (ListView) findViewById(R.id.area_listView);
        listView.setAdapter(dataAdapter);

        getCityCoverage();
        search = (SearchView)findViewById(R.id.searchView1);
        search.setQueryHint("Search Location");

        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                dataAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                dataAdapter.filter(query);
                return false;
            }
        });
        setToolBar("Select Location");
    }

    public void onReceiveCity()
    {
        dataAdapter = new LocationAdapter(this,
                R.layout.area_list,mCityCoverage);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionManager session = new SessionManager(getApplicationContext());
                session.setlastareasearched(mCityCoverage.get(position));
                Intent intent = new Intent();
                intent.putExtra("area", mCityCoverage.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
    public void getCityCoverage()
    {
        mCityCoverage.clear();
        new JSONAsyncTask().execute(Constants.GET_COVERAGE_AREAS);
    }

    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        ListView mListView;
        Activity mContex;
        public  JSONAsyncTask()
        {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PlacesActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONArray jarray = new JSONArray(data);

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        if(object.has(TAG_SUBAREAS)) {
                            JSONArray subAreasArray = object.getJSONArray(TAG_SUBAREAS);
                            for (int j = 0; j < subAreasArray.length(); j++) {
                                JSONObject city_object = subAreasArray.getJSONObject(j);
                                mCityCoverage.add(city_object.get("name").toString());
                            }
                        }
                    }
                    return true;
                }
            }  catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else
            {
                onReceiveCity();
            }

        }
    }
    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
