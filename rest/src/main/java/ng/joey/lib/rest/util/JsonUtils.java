package ng.joey.lib.rest.util;

import ng.joey.lib.rest.entity.User;

/**
 * Created by Joey Dalughut on 8/9/16 at 12:32 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class JsonUtils extends ng.joey.lib.java.util.JsonUtils {

    static {
        registerSerializer(ng.joey.lib.rest.entity.Token.class, new ng.joey.lib.rest.entity.Token.TokenSerializer(), new ng.joey.lib.rest.entity.Token.TokenDeserializer());
        registerSerializer(User.class, new User.UserSerializer(), new User.UserDeserializer());
    }

}
