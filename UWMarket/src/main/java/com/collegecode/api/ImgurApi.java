package com.collegecode.api;

import android.content.Context;
import android.os.AsyncTask;

import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;

/**
 * Created by Saurabh on 5/25/14.
 * This class manages interaction with Imgur
 */
public class ImgurApi {

    Context context;

    public ImgurApi(Context context){
        this.context = context;
    }

    public void uploadImage(String title, String location, OnImgurComplete listener){
        uploadImage_Async uia = new uploadImage_Async();
        uia.listener = listener;
        uia.execute(location);
    }

    private class uploadImage_Async extends AsyncTask<String, Void, Void>{
        public OnImgurComplete listener;
        private Exception e = null;
        @Override
        protected Void doInBackground(String... strings) {
            try
            {
                File file = new File(strings[0]);

                String url = "https://api.imgur.com/3/image";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);


                httpPost.setHeader("Authorization", "Client-ID " + Secrets.IMGUR_APP_ID);

                final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("type", new StringBody("file"));
                //For testing
                entity.addPart("title", new StringBody("Pasta!"));
                entity.addPart("image", new FileBody(file));

                httpPost.setEntity(entity);

                final HttpResponse response = httpClient.execute(httpPost,localContext);
                final String response_string = EntityUtils.toString(response.getEntity());

                System.out.println("DID IT? " + response_string);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void voids){listener.OnTaskComplete(e, "done");}
    }

}
