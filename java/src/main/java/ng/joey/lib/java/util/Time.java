package ng.joey.lib.java.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


/**
 * Created by Joey on 22/02/16 at 11:27 PM.
 * Project : Medio.
 * Copyright (c) 2016 Meengle. All rights reserved.
 */
public class Time {

    public static Long now(){
        return DateTime.now(DateTimeZone.UTC).getMillis();
    }

    public static DateTime nowDateTime(){
        return DateTime.now(DateTimeZone.UTC);
    }

}
