package ng.joey.lib.java.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joey on 01/03/16 at 7:31 PM.
 * Project : Medio.
 * Copyright (c) 2016 Meengle. All rights reserved.
 */
public class CookieUtils {

    public static String toCookie(String key, String value){
        if(Value.IS.ANY.emptyValue(key, value)) return null;
        return key + "="+value;
    }

    public static String toCookies(String... strings){
        List<String> list = new ArrayList<>();
        for(String string : strings){
            if(!Value.IS.emptyValue(string))
                list.add(string);
        }
        return Value.TO.stringValue(";", list);
    }

}
