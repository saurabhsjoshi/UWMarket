package com.collegecode.objects;

import com.parse.ParseObject;

import java.text.ParseException;

/**
 * Created by saurabh on 5/26/14.
 * All objects to be added to parse SHOULD extend this class.
 */

public class Base {
    public String title;
    public String fbId;
    public String objectId;
    public String price;

    public boolean isUploaded = false;
    public BASE_TYPE type;

    //Different type of stuff will be added here
    public enum BASE_TYPE{BOOK, CLICKER};

    //Function to set basic items about the item
    public void setBase(ParseObject obj) throws ParseException{
        this.title = obj.getString("Name");
        this.objectId = obj.getString("objectId");
        this.fbId = obj.getString("fbId");
        this.price = obj.getString("price");
    }

    public void setType(BASE_TYPE t){
        this.type = t;
    }

    //Functions to get basic info about the item

    public void putBase(ParseObject obj){
        obj.put("title", getTitle());
        //obj.put("fbId", getFbId());
        obj.put("fbId", "temp");
        obj.put("price", getPrice());
    }
    public String getTitle(){
        return this.title;
    }

    public String getFbId(){
        return this.fbId;
    }

    public String getPrice(){return this.price;}

    public String getObjectId(){
        return this.objectId;
    }
}
