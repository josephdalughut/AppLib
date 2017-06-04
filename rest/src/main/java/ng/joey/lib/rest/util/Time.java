package ng.joey.lib.rest.util;

import ng.joey.lib.rest.entity.Entity;

/**
 * Created by joeyblack on 10/19/16.
 */

public class Time extends ng.joey.lib.java.util.Time {

    public static Entity onCreate(Entity entity){
        Long now = Time.now();
        return entity.setCreatedAt(now).setUpdatedAt(now);
    }

}
