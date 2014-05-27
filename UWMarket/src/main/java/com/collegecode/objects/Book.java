package com.collegecode.objects;

import com.parse.ParseObject;

import java.text.ParseException;

/**
 * Created by saurabh on 5/26/14.
 */
public class Book extends Base {

    public String isbn;
    public String price;
    public String author;

    public static Book getBookFromParseObj(ParseObject obj) throws ParseException{
        Book t = new Book();
        t.setBase(obj);
        t.isbn = obj.getString("isbn");
        t.author = obj.getString("author");
        t.price = obj.getString("price");
        return t;
    }

}
