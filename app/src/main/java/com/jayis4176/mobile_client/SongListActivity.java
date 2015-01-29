package com.jayis4176.mobile_client;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;


public class SongListActivity extends ActionBarActivity {
/*
    private ListView listView;
    private String[] list = {"鉛筆","原子筆","鋼筆","毛筆","彩色筆"};
    private ArrayAdapter<String> listAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        listView = (ListView)findViewById(R.id.listview);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(getApplicationContext(),                                                          "你選擇的是"+list[position], Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**/

    private String str_JSON = "";
    private JSONArray file_list;
    private JSONObject json_obj;
    private String tmp_string = "";
    private ArrayAdapter<String> listAdapter;
    private ListView listview;
    private List<String> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        listview = (ListView) findViewById(R.id.listview);

        Intent intent = getIntent();
        str_JSON = intent.getStringExtra(MainPageActivity.EXTRA_STR_JSON);

        try{
            json_obj = new JSONObject(str_JSON);
            file_list = json_obj.getJSONArray("file_list");

            for (int i = 0; i < file_list.length(); i++) {
                //tmp_string = tmp_string + item2str(file_list.getJSONObject(i));
                files.add(item2str(file_list.getJSONObject(i)));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        listAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, files.toArray(new String[0]));
        listview.setAdapter(listAdapter);

        //show_result(tmp_string);
    }
/**/
    public String item2str (JSONObject item) {
        String show_str = "";
        try {
            show_str =
                "NAME: " + item.getString("filename") + "\n" +
                "TITLE: " + item.getString("title") + "\n" +
                "ALBUM: " + item.getString("album") + "\n" +
                "ARTIST: " + item.getString("artist") + "\n";
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return show_str;
    }

    public void show_result (String result) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(result);
        textView.setVerticalScrollBarEnabled(true);
        setContentView(textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_list, menu);
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
}
