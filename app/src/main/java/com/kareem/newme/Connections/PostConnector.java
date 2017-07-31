package com.kareem.newme.Connections;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by kareem on 28/04/17.
 */

public abstract class PostConnector extends AsyncTask<Void, Void,String> {
    private URL url;
    //example on adding param
    // params.put("DateOfFlight", "03/2017");
    private Map<String, String> params;
    public PostConnector(URL url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return fetchData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String fetchData() throws IOException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String websiteData = "";
        for (int c; (c = in.read()) >= 0; )
        websiteData+= (char)c;
        return websiteData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onFinish(s);
    }
    public abstract void onFinish(String data);
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
