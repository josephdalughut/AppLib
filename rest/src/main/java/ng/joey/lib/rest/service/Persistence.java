package ng.joey.lib.rest.service;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by Joey Dalughut on 8/9/16 at 2:45 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */


public class Persistence {

    public static ObjectifyFactory factory(){
        return ObjectifyService.factory();
    }

    public static Objectify ofy(){
        return ObjectifyService.ofy();
    }

}
