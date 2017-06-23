package ng.joey.lib.rest.util;

import ng.joey.lib.rest.entity.Client;
import ng.joey.lib.rest.entity.Entity;
import ng.joey.lib.rest.entity.Token;
import ng.joey.lib.rest.entity.User;

public class JsonUtils extends ng.joey.lib.java.util.JsonUtils {

    static {
        getBuilder().registerTypeAdapter(Token.class, new AbstractSerializer<>(Token.class));
        getBuilder().registerTypeAdapter(User.class, new AbstractSerializer<>(User.class));
        getBuilder().registerTypeAdapter(Entity.class, new AbstractSerializer<>(Entity.class));
        getBuilder().registerTypeAdapter(Client.class, new AbstractSerializer<>(Client.class));
    }

}
