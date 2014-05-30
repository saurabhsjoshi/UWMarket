package com.collegecode.api;

import android.content.Context;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Saurabh on 5/25/14.
 * This class manages interaction with Imgur
 */
public class ImgurApi {

    Context context;

    public ImgurApi(Context context){
        this.context = context;
    }

    private boolean copyFile(File src,File dst)throws IOException{
        if(src.getAbsolutePath().equals(dst.getAbsolutePath())){
            return true;

        }else{
            InputStream is=new FileInputStream(src);
            OutputStream os=new FileOutputStream(dst);
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))>0){
                os.write(buff,0,len);
            }
            is.close();
            os.close();
        }
        return true;
    }

    public String uploadImageinForeground(String title, String location) throws IOException, JSONException, URISyntaxException {
        if(location == null)
            return "";

        File file = new File(context.getFilesDir() + "/" + title);
        File file2 = new File(new URI(location));

        if(copyFile(file2, file)){
            String url = "https://api.imgur.com/3/image";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Authorization", "Client-ID " + Secrets.IMGUR_APP_ID);

            final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("type", new StringBody("file"));
            entity.addPart("title", new StringBody(title));
            entity.addPart("image", new FileBody(file));

            httpPost.setEntity(entity);

            final HttpResponse response = httpClient.execute(httpPost,localContext);

            JSONObject res = new JSONObject(EntityUtils.toString(response.getEntity()));

            res = res.getJSONObject("data");

            return res.getString("link");
        }
        else {return  "";}
    }
}
