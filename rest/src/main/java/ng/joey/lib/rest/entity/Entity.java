package ng.joey.lib.rest.entity;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNull;
import ng.joey.lib.java.util.Value;

import java.lang.reflect.Type;
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
    @Ignore private Map<String, String> accompanyingData;

    public static class Constants {
        public static class Fields {
            public static final String CREATED_AT = "createdAt";
            public static final String UPDATED_AT = "updatedAt";
            public static final String RETRIEVED_AT = "retrievedAt";
            public static final String ACCOMPANYING_DATA = "accompanyingData";
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
    public Map<String, String> getAccompanyingData() {
        if(Value.IS.nullValue(accompanyingData))
            setAccompanyingData(new HashMap<String, String>());
        return accompanyingData;
    }

    /**
     * Sets the accompanying data for this entity in the form of a map of key-value string pairs
     * @param accompanyingData
     * @return
     */
    public Entity setAccompanyingData(Map<String, String> accompanyingData) {
        this.accompanyingData = accompanyingData;
        return this;
    }

    /**
     * A serializer class which handles converting objects of type {@link Entity} to Json Strings
     * @param <T> the class type of the {@link Entity} class this serializer is meant for.
     */
    public static abstract class EntitySerializer<T extends Entity> implements JsonSerializer<T> {
        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject o = serialize(src, new JsonObject());
            o.addProperty(Constants.Fields.CREATED_AT, src.getCreatedAt());
            o.addProperty(Constants.Fields.UPDATED_AT, src.getUpdatedAt());
            o.addProperty(Constants.Fields.RETRIEVED_AT, src.getRetrievedAt());
            o.addProperty(Constants.Fields.ACCOMPANYING_DATA, new Gson().toJson(src.getAccompanyingData(), Data.class));
            return o;
        }

        public abstract JsonObject serialize(T src, JsonObject object);

    }

    /**
     * A deserializer class which handles converting jsonStrings back to their {@link Entity} form.
     * This class should be extended and implement the abstract deserialize(JsonObject j) method.
     * @param <T> the class type of the {@link Entity} this class meant for.
     */
    public static abstract class EntityDeserializer<T extends Entity> implements JsonDeserializer<T>{
        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject o = json.getAsJsonObject();
            T t = deserialize(o);
            t.setCreatedAt(Value.TO.longValue(Constants.Fields.CREATED_AT, o))
                    .setUpdatedAt(Value.TO.longValue(Constants.Fields.UPDATED_AT, o))
                    .setRetrievedAt(Value.TO.longValue(Constants.Fields.RETRIEVED_AT, o))
                    .setAccompanyingData(new Gson().fromJson(Value.TO.stringValue(Constants.Fields.ACCOMPANYING_DATA, o), Data.class));
            return t;
        }

        public abstract T deserialize(JsonObject object);
    }

    private static class Data extends HashMap<String, String>{
    }
}
