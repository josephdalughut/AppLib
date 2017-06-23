package ng.joey.lib.rest.entity;

import com.google.gson.JsonObject;
import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.IfNull;

import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/9/16 at 1:25 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
@com.googlecode.objectify.annotation.Entity
public class User  extends ng.joey.lib.rest.entity.Entity {

    @Id private Long id;
    @Unindex @IgnoreSave(IfNull.class) private String password;

    public static class Constants {
        public static class Fields {
            public static final String ID = "id";
            public static final String PASSWORD = "password";
        }
    }

    /**
     * Get the identifier for this instance of {@link User}
     * @return the identifier for this instance of {@link User}
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the identifier for this instance of {@link User}
     * @param id the identifier for this instance of {@link User} (a {@link Long} value)
     * @return self
     */
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }


}
