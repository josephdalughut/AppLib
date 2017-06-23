package ng.joey.lib.java.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


/**
 * @author Joey Dalu
 */
public class Time {

    public static Long now(){
        return DateTime.now(DateTimeZone.UTC).getMillis();
    }

    public static DateTime nowDateTime(){
        return DateTime.now(DateTimeZone.UTC);
    }

}
