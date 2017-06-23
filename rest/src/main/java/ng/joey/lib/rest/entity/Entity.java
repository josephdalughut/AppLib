package ng.joey.lib.rest.entity;

import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNull;
import ng.joey.lib.java.util.Value;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joey Dalughut on 8/9/16 at 12:33 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 *
 * A class which models the basic features of a simple persistent entity.
 *
 */
public class Entity {

    @Index @IgnoreSave(IfNull.class) private Long createdAt, updatedAt;
    @Ignore private Long retrievedAt;
    @Ignore private Map<String, String> data;

    public static class Constants {
        public static class Fields {
            public static final String CREATED_AT = "createdAt";
            public static final String UPDATED_AT = "updatedAt";
            public static final String RETRIEVED_AT = "retrievedAt";
            public static final String DATA = "data";
        }
    }

    /**
     * Get the time in milliseconds this entity was created.
     * @return the time in milliseconds when this entity was created.
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * Set the time in milliseconds this entity was created.
     * @param createdAt the time in milliseconds this entity was created.
     * @return self
     */
    public Entity setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get the time in milliseconds this entity was updated.
     * @return the time in milliseconds this entity was updated.
     */
    public Long getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set the time in milliseconds this entity was updated.
     * @param updatedAt the time in milliseconds this entity was updated.
     * @return self
     */
    public Entity setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get the time in milliseconds this entity was retrieved.
     * @return the time in milliseconds this entity was retrieved.
     */
    public Long getRetrievedAt() {
        return retrievedAt;
    }

    /**
     * Set the time in milliseconds when this entity was retrieved
     * @param retrievedAt the time in milliseconds this entity was retrieved
     * @return self
     */
    public Entity setRetrievedAt(Long retrievedAt) {
        this.retrievedAt = retrievedAt;
        return this;
    }

    /**
     * @return a non-null map containing accompanying data for this entity in the form of string
     * key-value pairs
     */
    public Map<String, String> getData() {
        if(Value.IS.nullValue(data))
            setData(new HashMap<String, String>());
        return data;
    }

    /**
     * Sets the accompanying data for this entity in the form of a map of key-value string pairs
     * @param data
     * @return
     */
    public Entity setData(Map<String, String> data) {
        this.data = data;
        return this;
    }

}
