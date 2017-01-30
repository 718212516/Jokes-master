package com.mperez.tellmeajoke;

/**
 * Created by mperez on 1/29/2016.
 */
import android.os.AsyncTask;

import com.mperez.jokecloud.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

public class GCEAsync extends AsyncTask<Void, Void, String> {
    private static MyApi myApiService = null;

    private Callback callback;

    public interface Callback{
        void onFinished(String result);
    }

    public GCEAsync(Callback callback){
        this.callback = callback;
    }


    @Override
    protected String doInBackground(Void... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }
        try {
            return myApiService.getJokeFromLib().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null){
            callback.onFinished(result);
        }
    }
}