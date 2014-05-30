package com.collegecode.api;

import android.content.Context;
import android.os.AsyncTask;

import com.collegecode.objects.Book;
import com.collegecode.objects.Clicker;
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
        ast.execute(book);
    }

    public void uploadClicker(Clicker clicker, OnParseComplete listener){
        uploadClicker_Async ast = new uploadClicker_Async();
        ast.listenter = listener;
        ast.execute(clicker);

    }

    public void saveBookLocaly(Book b){

    }

    private class uploadBook_Async extends AsyncTask<Book, Void, Void>{
        Exception e = null;
        OnParseComplete listenter;
        Book b;

        @Override
        protected Void doInBackground(Book... books) {
            b = books[0];
            saveBookLocaly(b);
            try{
                ImgurApi imgur = new ImgurApi(context);
                String res = imgur.uploadImageinForeground(b.isbn, b.img_url);

                if(!res.equals("error")){
                    b.img_url = res;
                    ParseObject obj = Book.getParseObjfromBook(b);
                    //Save to parse
                    obj.save();
                }
                else{
                    e = new Exception("Oops! Could not upload image. Please try again.");
                    return null;
                }

            }catch (Exception e1){e = e1; e1.printStackTrace();}
            return null;
        }

        protected void onPostExecute(Void voids){
            listenter.OnTaskComplete(e, b);
        }
    }

    private class uploadClicker_Async extends AsyncTask<Clicker, Void, Void>{
        Exception e = null;
        OnParseComplete listenter;
        Clicker b;

        @Override
        protected Void doInBackground(Clicker... clickers) {
            b = clickers[0];
            try{
                ImgurApi imgur = new ImgurApi(context);
                String res = imgur.uploadImageinForeground(b.title, b.img_url);

                if(!res.equals("error")){
                    b.img_url = res;
                    ParseObject obj = Clicker.getParseObjfromClicker(b);
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
