package ng.joey.lib.rest.entity;


import com.google.gson.JsonObject;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.condition.IfNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ng.joey.lib.java.util.Value;
import ng.joey.lib.rest.api.EatApi;

/**
 * Created by root on 3/7/17.
 * endpoint access token
 */

@Entity
@Cache
public class Eat extends ng.joey.lib.rest.entity.Entity{

    public static class Constants {
        public static final class Fields {
            public static final String id = "id";
            public static final String endpoint = "endpoint";
            public static final String userId = "userId";
            public static final String name = "names";
            public static final String description = "description";
            public static final String category = "category";
            public static final String baseEndpoint = "baseEndpoint";
            public static final String icon = "icon";
        }
    }

    @Id private Long id;
    @Index @IgnoreSave(IfNull.class) private String endpoint;
    @Index @IgnoreSave(IfNull.class) private Long userId;
    @Ignore private String name, description, category, baseEndpoint, icon;

    public Long getId() {
        return id;
    }

    public Eat setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Eat setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Eat setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Eat setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Eat setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Eat setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getBaseEndpoint() {
        return baseEndpoint;
    }

    public Eat setBaseEndpoint(String baseEndpoint) {
        this.baseEndpoint = baseEndpoint;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public Eat setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    @OnLoad
    public void onLoad(){
        EatApi.getEatBuilder().build(this);
    }

    public static interface EatBuilder {
        public Eat build(Eat eat);
    }

    public static class EATSerializer extends EntitySerializer<Eat>{
        @Override
        public JsonObject serialize(Eat eat, JsonObject jsonObject) {
            jsonObject.addProperty(Constants.Fields.id, eat.getId());
            jsonObject.addProperty(Constants.Fields.description, eat.getDescription());
            jsonObject.addProperty(Constants.Fields.endpoint, eat.getEndpoint());
            jsonObject.addProperty(Constants.Fields.name, eat.getName());
            jsonObject.addProperty(Constants.Fields.baseEndpoint, eat.getBaseEndpoint());
            jsonObject.addProperty(Constants.Fields.userId, eat.getUserId());
            jsonObject.addProperty(Constants.Fields.category, eat.getCategory());
            jsonObject.addProperty(Constants.Fields.icon, eat.getIcon());
            return jsonObject;
        }
    }

    public static class EATDeserializer extends EntityDeserializer<Eat>{
        @Override
        public Eat deserialize(JsonObject jsonObject) {
            return new Eat()
                    .setId(Value.TO.longValue(Constants.Fields.id, jsonObject))
                    .setUserId(Value.TO.longValue(Constants.Fields.userId, jsonObject))
                    .setDescription(Value.TO.stringValue(Constants.Fields.description, jsonObject))
                    .setEndpoint(Value.TO.stringValue(Constants.Fields.endpoint, jsonObject))
                    .setIcon(Value.TO.stringValue(Constants.Fields.icon, jsonObject))
                    .setName(Value.TO.stringValue(Constants.Fields.name, jsonObject))
                    .setBaseEndpoint(Value.TO.stringValue(Constants.Fields.baseEndpoint, jsonObject))
                    .setCategory(Value.TO.stringValue(Constants.Fields.category, jsonObject));
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Endpoint{
        String[] names();
    }

}
