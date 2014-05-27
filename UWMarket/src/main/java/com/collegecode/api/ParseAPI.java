package com.collegecode.api;

import android.content.Context;
import android.os.AsyncTask;

import com.collegecode.objects.Book;
import com.parse.ParseObject;

/**
 * Created by saurabh on 5/25/14.
 */
public class ParseAPI {

    private Context context;

    public ParseAPI(Context context){
        this.context = context;
    }
    public void uploadBook(Book book, OnParseComplete listener){
        uploadBook_Async ast = new uploadBook_Async();
        ast.listenter = listener;
    }

    private class uploadBook_Async extends AsyncTask<Book, Void, Void>{
        Exception e = null;
        OnParseComplete listenter;
        Book b;

        @Override
        protected Void doInBackground(Book... books) {
            b = books[0];
            try{
                ImgurApi imgur = new ImgurApi(context);
                String res = imgur.uploadImageinForeGround(b.title, b.img_url);

                if(!res.equals("error")){
                    b.img_url = res;
                    ParseObject obj = Book.getParseObjfromBook(b);
                    //Save to parse
                    obj.save();
                }
                else
                    e = new Exception("Oops! Could not upload image. Please try again.");

            }catch (Exception e1){e1.printStackTrace();}
            return null;
        }

        protected void onPostExecute(Void voids){
            listenter.OnTaskComplete(e, b);
        }
    }
}
