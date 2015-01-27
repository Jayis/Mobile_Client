package com.jayis4176.mobile_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private String in_username;
    private String in_password;
    private String in_csrf = null;
    private String login_site = "http://106.187.36.145:3000/accounts/login/";
    private String auth_site = "http://106.187.36.145:3000/accounts/auth/";
    private String list_site = "http://106.187.36.145:3000/list_json/";

    public List<NameValuePair> form_data;
    public static DefaultHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        form_data = new ArrayList<NameValuePair>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void try_login(View view) {
        username = (EditText) findViewById(R.id.username);
        in_username = username.getText().toString();
        password = (EditText) findViewById(R.id.password);
        in_password = password.getText().toString();

        form_data.add(new BasicNameValuePair("username", in_username));
        form_data.add(new BasicNameValuePair("password", in_password));

        new background_login().execute();

    }

    public void show_result (String result) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(result);
        setContentView(textView);
    }

    public void go_to_main_page () {
        Intent intent = new Intent (this, MainPageActivity.class);
        startActivity(intent);
    }

    public static DefaultHttpClient share_client () {
        return client;
    }

    private class background_login extends AsyncTask<String, Integer, Integer>
    {
        String web_result = "";

        @Override
        protected Integer doInBackground(String... params) {

            try {
                // request /accounts/login/ to get csrf
                client = new DefaultHttpClient();
                HttpGet http_login_request = new HttpGet(login_site);
                HttpResponse response = client.execute(http_login_request);
                getCSRF(client);

                // send post to /accounts/auth/
                HttpPost http_auth_request = new HttpPost(auth_site);
                //-- prepare login parameter
                form_data.add(new BasicNameValuePair("csrftoken", in_csrf));
                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(form_data, HTTP.UTF_8);
                http_auth_request.setEntity(ent);

                response = client.execute(http_auth_request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    web_result = EntityUtils.toString(resEntity);
                }

                HttpGet http_list_request = new HttpGet(list_site);
                response = client.execute(http_list_request);
                resEntity = response.getEntity();
                if (resEntity != null) {
                    web_result = EntityUtils.toString(resEntity);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute (Integer result) {
            //show_result(web_result);
            go_to_main_page();
        }
    }

    /*
    private String getCookie(DefaultHttpClient httpClient) {
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!TextUtils.isEmpty(cookieName)
                    && !TextUtils.isEmpty(cookieValue)) {
                sb.append(cookieName + "=" );
                sb.append(cookieValue + ";" );
            }
        }
        //Log. e("cookie", sb.toString());
        //Util. savePreference( "cookie", sb.toString());
        return sb.toString();
    }
/**/
    private boolean getCSRF(DefaultHttpClient httpClient) {
        boolean success = false;

        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            if (cookie.getName() == "csrftoken") {
                in_csrf = cookie.getValue();
                success = true;
            }
        }

        return success;
    }
}
