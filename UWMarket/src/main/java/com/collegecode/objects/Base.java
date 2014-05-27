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

    //Function to set basic items about the item

    public void setBase(ParseObject obj) throws ParseException{
        this.title = obj.getString("Name");
        this.objectId = obj.getString("objectId");
        this.fbId = obj.getString("fbId");
    }

    //Functions to get basic info about the item
    public String getTitle(){
        return title;
    }

    public String getFbId(){
        return fbId;
    }

    public String getObjectId(){
        return objectId;
    }
}
