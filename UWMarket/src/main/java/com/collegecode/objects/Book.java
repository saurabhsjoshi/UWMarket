package com.collegecode.objects;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;

/**
 * Created by saurabh on 5/26/14.
 */
public class Book extends Base {
    public String isbn;
    public String price;
    public String author;
    public String img_url;
    public String condition;
    public String description;
    public boolean isUploaded = false;

    //Make sure you know this is a book
    public Book(){
        this.type = BASE_TYPE.BOOK;
    }

    //Get Book Object from Parse Object
    public static Book getBookfromParseObj(ParseObject obj) throws ParseException{
        Book t = new Book();
        t.setBase(obj);
        t.setType(BASE_TYPE.BOOK);
        t.isbn = obj.getString("isbn");
        t.author = obj.getString("author");
        t.price = obj.getString("price");
        t.img_url = obj.getString("obj_url");
        t.description = obj.getString("description");
        t.condition = obj.getString("condition");
        return t;
    }

    //Get ParseObject from a Book Object
    public static ParseObject getParseObjfromBook(Book b){
        ParseObject obj = new ParseObject("Books");
        b.putBase(obj);
        obj.put("fbId", ParseUser.getCurrentUser().get("fbId"));
        obj.put("description", b.description);
        obj.put("condition", b.condition);
        obj.put("isbn", b.isbn);
        obj.put("author", b.author);
        obj.put("price", b.price);
        obj.put("img_url", b.img_url);
        return obj;
    }

}
