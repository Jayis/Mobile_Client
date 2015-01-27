package com.jayis4176.mobile_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;


public class MainPageActivity extends ActionBarActivity {

    private String list_site = "http://106.187.36.145:3000/list_json/";
    public DefaultHttpClient client;

    public final static String EXTRA_STR_JSON = "com.jayis4176.mobile_client.JSON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = LoginActivity.share_client();

        setContentView(R.layout.activity_main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void see_song_list (View view) {
        new background_see_song_list().execute();
    }

    public void show_result (String result) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(result);
        setContentView(textView);
    }

    public void show_song_list (String json_str) {
        Intent intent = new Intent(this, SongListActivity.class);

        intent.putExtra(EXTRA_STR_JSON, json_str);

        startActivity(intent);
    }

    private class background_see_song_list extends AsyncTask<String, Integer, Integer>
    {
        private String web_result = "";

        @Override
        protected Integer doInBackground(String... params) {
            try {
                // get song list
                HttpGet http_list_request = new HttpGet(list_site);
                HttpResponse response = client.execute(http_list_request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    web_result = EntityUtils.toString(resEntity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute (Integer result) {
            //show_result(web_result);
            show_song_list (web_result);
        }

    }
}
