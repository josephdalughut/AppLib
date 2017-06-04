package ng.joey.lib.rest.entity;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import com.googlecode.objectify.condition.IfNull;


@com.googlecode.objectify.annotation.Entity
public class Client extends Entity {

    @Id
    private Long id;
    @Index @IgnoreSave(IfNull.class) private String userId;
    @Unindex @IgnoreSave(IfNull.class) private String secret,clientName, type;

    public static class Constants {
        public static class Fields {
            public static final String ID = "id";
            public static final String CLIENT_NAME = "clientName";
            public static final String SECRET = "secret";
            public static final String TYPE = "type";
            public static final String USER_ID = "userId";
        }
    }

    public Long getId() {
        return id;
    }

    public Client setId(Long id) {
        this.id = id;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public Client setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public Client setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Client setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public Client setType(String type) {
        this.type = type;
        return this;
    }

    public static class ClientType {
        public static final String CONFIDENTIAL = "confidential", PUBLIC = "public";
    }



}
