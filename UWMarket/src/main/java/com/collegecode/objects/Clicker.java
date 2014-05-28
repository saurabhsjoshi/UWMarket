package com.collegecode.objects;

import com.parse.ParseObject;

import java.text.ParseException;

/**
 * Created by saurabh on 5/27/14.
 */
public class Clicker extends Base {

    public String price;
    public String condition;
    public String img_url;
    public boolean isUploaded = false;

    public Clicker(){
        this.type = BASE_TYPE.CLICKER;
    }

    public static Clicker getClickerfromParseObj(ParseObject obj) throws ParseException{
        Clicker c = new Clicker();
        c.setBase(obj);
        c.price = obj.getString("price");
        c.condition = obj.getString("condition");
        c.img_url = obj.getString("img_url");
        return c;
    }

    public static ParseObject getParseObjfromClicker(Clicker c){
        ParseObject obj = new ParseObject("Clickers");
        c.putBase(obj);
        obj.put("price", c.price);
        obj.put("condition", c.condition);
        obj.put("img_url", c.img_url);

        return obj;
    }
}
