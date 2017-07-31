package com.kareem.newme;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
@Deprecated
public  class BasicNotifier extends AsyncTask<String, Void,Void>
{
    private DataSetListener dataSetListener;
    public BasicNotifier(DataSetListener dataSetListener) {
        super();
        this.dataSetListener = dataSetListener;
    }

    private volatile boolean shutDown = false;
    private int length = 0;
    @Override
    protected Void doInBackground(String... sortMode)   {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String dataJson;
        StringBuilder buffer= new StringBuilder();
        URL url;
        while (!shutDown)
        try
        {
             url = new URL(sortMode[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            Log.e("news", "doInBackground: received");
            dataJson = buffer.toString();
            if (length != dataJson.length())
            dataSetListener.onDataSetChanged(dataJson);
            urlConnection.disconnect();
            reader.close();
            Thread.sleep(30000);
        }catch (Exception e)
        {
            Log.e("exeption", e.getMessage());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
    public boolean isShutDown() {
        return shutDown;
    }

    public void close() {
        this.shutDown = true;
    }

}