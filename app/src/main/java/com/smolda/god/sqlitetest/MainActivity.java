package com.smolda.god.sqlitetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static SwipeRefreshLayout mySwipeRefreshLayout;
    public static ListView lv;
    final FeedRepo repo = new FeedRepo(this);
    public static String EXTRA_MESSAGE = "EM";

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("main","create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        //setListAdapter(adapter);

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("swipe", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        //myUpdateOperation();
                        updateIndex();
                    }
                }
        );



        renderList();
        updateHttp();
    }

    public void sendMesg(Integer position) {
        Intent intent = new Intent(getApplicationContext(), FeedView.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        startActivity(intent);
    }

    public void renderList() {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        Boolean sml_only = settings.getBoolean(setting.SMOLENSK_ONLY, false);
        Boolean important_only = settings.getBoolean(setting.IMPORTANT_ONLY, false);


        ArrayList<HashMap<String, String>> feedList =  repo.getFeedList(sml_only,important_only);


        lv = (ListView) findViewById(R.id.listView);
        ListAdapter adapter = new SimpleAdapter( MainActivity.this,feedList, R.layout.view_feed_entry, new String[] { "id","title"}, new int[] {R.id.feed_Id, R.id.feed_title});


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Toast.makeText(getApplicationContext(),
                //        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                //        .show();
                HashMap<String, String> feed = (HashMap<String, String>) lv.getItemAtPosition(position);
                String sid = (String) feed.get("id");
                int rid = Integer.parseInt(sid);
                sendMesg(rid);
            }
        });
        lv.setAdapter(adapter);
    }

    public void updateIndex() {
        updateHttp();
    }

    public void renderAfterUpdateHttp() {
        renderList();
        mySwipeRefreshLayout.setRefreshing(false);
    }


    public void updateHttp() {
        RestClient.get("https://smolda.ru/api/v1/feed", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jArray) {
                Log.i("http", "found " + jArray.length());
                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject json_data = jArray.getJSONObject(i);

                        Feed feed = new Feed();
                        feed.title = json_data.getString("title");
                        feed.body = json_data.getString("body");
                        feed.feed_ID = json_data.getInt("id");
                        boolean has_image = json_data.getBoolean("has_image");
                        if (has_image) {
                            feed.top_image = json_data.getString("top_image");
                        }
                        feed.published = json_data.getString("published");
                        feed.is_smolensk = json_data.getBoolean("is_smolensk");
                        feed.is_important = json_data.getBoolean("is_important");

                        try {
                            int id = repo.insert(feed);
                        } catch (SQLiteConstraintException e) {
                            Log.i("sqlite", e.toString());
                        }

                    } catch (JSONException e) {
                        Log.i("json", e.toString());
                        e.printStackTrace();
                    }
                    //Log.d(name, "Output");
                }
                renderAfterUpdateHttp();


                //ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mobileArray);


                //listView.setAdapter(adapter)

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Context context = getApplicationContext();
                CharSequence text = "Ошибка соединения!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                renderAfterUpdateHttp();
            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), setting.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* public static boolean ImagesEnabled(){
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        return settings.getBoolean(setting.IMPORTANT_ONLY, false);
    }*/


}
