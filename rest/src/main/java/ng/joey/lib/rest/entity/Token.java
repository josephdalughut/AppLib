package ng.joey.lib.rest.entity;

import com.google.gson.JsonObject;
import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.IfNull;
import com.googlecode.objectify.condition.ValueIf;

import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/9/16 at 1:14 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
@com.googlecode.objectify.annotation.Entity
@Cache(expirationSeconds = 14400)
public class Token extends ng.joey.lib.rest.entity.Entity {

    @Id private String id;
    @Index @IgnoreSave(IfNull.class) private Long  clientId;
    @Index @IgnoreSave(IfNull.class) private Long expiresAt;
    @Index @IgnoreSave(IfNull.class) private String userId, type;
    @Unindex @IgnoreSave(IfNull.class) private String secret;
    @Index @IgnoreSave(IfNull.class) private String scope;
    @Index @IgnoreSave(IfNull.class) private String refreshToken;

    public static class Constants {

        public static final String ACCESS_TOKEN_MAP_ID = "accessToken";
        public static final String ACCESS_TOKEN_EXPIRY_MAP_ID = "accessTokenExpiresAt";
        public static final String REFRESH_TOKEN_EXPIRY_MAP_ID = "refreshTokenExpiresAt";
        public static final String REFRESH_TOKEN_MAP_ID = "refreshToken";

        public static class Fields {
            public static final String ID = "id";
            public static final String USER_ID = "userId";
            public static final String CLIENT_ID = "clientId";
            public static final String TYPE = "type";
            public static final String EXPIRES_AT = "expiresAt";
            public static final String SECRET = "secret";
            public static final String SCOPE = "scope";
        }
    }

    /**
     * Get the identifier for this entity.
     * @return an identifier for this entity of type {@link Long}
     */
    public String getId() {
        return id;
    }

    /**
     * Set the identifier for this entity.
     * @param id the identifier for this entity of type {@link Long}
     * @return self
     */
    public Token setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get the type for this session
     * @return this session instance
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type for this session
     * @param type the type identifier for this session instance
     * @return  a string identifier which is the type of this session instance
     */
    public Token setType(String type) {
        this.type = type;
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public Token setClientId(Long clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Get the time in milliseconds this {@link Token} would expire
     * @return the time in milliseconds this {@link Token} would expire
     */
    public Long getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the time in milliseconds this {@link Token} would expire
     * @param expiresAt the time in milliseconds this {@link Token} would expire
     * @return self
     */
    public Token setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Token setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public Token setScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Get the secret for this {@link Token}. The secret is a random uuid string generated to add more
     * complexity to a generated {@link Token}
     * @return the secret for this {@link Token}
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Set the secret for this {@link Token}. The secret is a random uuid string generated to add more
     * complexity to a generated {@link Token}
     * @param secret the randomly generated uuid which would serve as the secret for this {@link Token}
     * @return self
     */
    public Token setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    /**
     * Generate a secret for this {@link Token}
     * @return self
     */
    public Token generateSecret(){
        return setSecret(Value.Random.UUID());
    }


    public static class Type {
        public static final String REQUEST_TOKEN = "request_token";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String ACCESS_TOKEN = "access_token";
    }

    public static class Scope {
        public static final String CONFIDENTIAL = "confidential";
    }

    public static class IfAccessToken extends ValueIf<Number>
    {
        @Override
        public boolean matchesValue(Number value) {
            return value != null && value.doubleValue() != -1;
        }
    }

}

