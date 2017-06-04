package ng.joey.lib.rest.api;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiAuth;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import ng.joey.lib.java.security.Crypto;
import ng.joey.lib.java.util.Value;
import ng.joey.lib.rest.service.Persistence;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * AdminAPI implementation, for creating clients.
 */

@Api(
        name = "adminApi",
        version = "v0",
        resource = "client",
        auth = @ApiAuth(allowCookieAuth = AnnotationBoolean.TRUE),
        namespace = @ApiNamespace(
                ownerDomain = "api.rest.lib.joey.ng",
                ownerName = "api.rest.lib.joey.ng",
                packagePath = ""
        )
)
public class AdminAPI {

    static Logger logger = Logger.getLogger(AdminAPI.class.getName());

    public static final class Constants {
        private static final Long SYSTEM_ID = 1L;
        private static final String SYSTEM_PROPERTY_PASSWORD = "system.password";
    }

    public static ng.joey.lib.rest.entity.User init(){
        /*Long systemId = Constants.SYSTEM_ID;
        String systemPassword = System.getProperty(Constants.SYSTEM_PROPERTY_PASSWORD);
        try{
            User user = ofy().load().type(User.class).id(systemId).safe();
            if(!Crypto.HASH.BCRYPT.check(systemPassword, user.getPassword())){
                user.setPassword(Crypto.HASH.BCRYPT.hash(systemPassword)).setUpdatedAt(Time.now());
                ofy().save().entities(user).now();
            }
            return user.setPassword(null);
        }catch (com.googlecode.objectify.NotFoundException ignored){
            Long now = Time.now();
            User user = new User()
                    .setId(systemId)
                    .setPassword(Crypto.HASH.BCRYPT.hash(systemPassword));
            user.setCreatedAt(now).setUpdatedAt(now);
            ofy().save().entity(Time.onCreate(user)).now();
            return user.setPassword(null);
        }
        */
        return new ng.joey.lib.rest.entity.User().setId(0L);
    }

    /**
     * Creates a new {@link ng.joey.lib.rest.entity.Client} for the Admin {@link ng.joey.lib.rest.entity.User}.
     * @param request the {@link HttpServletRequest} making this request.
     * @param client the {@link ng.joey.lib.rest.entity.Client} to be created.
     * @param password the admin password, set as the system property: 'system.password'
     * @return the created {@link ng.joey.lib.rest.entity.Client}
     * @throws UnauthorizedException if authorization fails because the system password is incorrect.
     */
    @ApiMethod(
            name = "client.create",
            path = "client",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ng.joey.lib.rest.entity.Client postClient(HttpServletRequest request, ng.joey.lib.rest.entity.Client client, @Named("password") String password) throws UnauthorizedException {
        if(!request.isSecure())
            throw new UnauthorizedException("unauthorized: insecure");
        String systemPassword = System.getProperty(Constants.SYSTEM_PROPERTY_PASSWORD);
        if(!Value.IS.SAME.stringValue(password, systemPassword))
            throw new UnauthorizedException("unauthorized: "+ ng.joey.lib.rest.entity.User.Constants.Fields.PASSWORD+ " incorrect");
        ng.joey.lib.rest.entity.User user = init();
        String secretKey = Crypto.HASH.SHA._256(Crypto.Random.uuidClear());
        String secretKeyPrefixed = "CSK_"+secretKey;
        client.setSecret(Crypto.HASH.BCRYPT.hash(secretKey))
                .setType(ng.joey.lib.rest.entity.Client.ClientType.CONFIDENTIAL)
                .setUserId(Value.TO.stringValue(user.getId()));
        Persistence.ofy().save().entity(client).now();
        return new ng.joey.lib.rest.entity.Client().setSecret(secretKeyPrefixed).setClientName(client.getClientName()).setId(client.getId());
    }

    /**
     * Finds and returns an existing {@link ng.joey.lib.rest.entity.Client} by a corresponding ID.
     * @param request the {@link HttpServletRequest} making this request.
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client} to get.
     * @param password the system password.
     * @return the {@link ng.joey.lib.rest.entity.Client} with the corresponding ID.
     * @throws NotFoundException if no {@link ng.joey.lib.rest.entity.Client} with the corresponding ID is found.
     * @throws UnauthorizedException if the password is incorrect.
     */
    @ApiMethod(
            name = "client.get",
            path = "client",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public ng.joey.lib.rest.entity.Client getClient(HttpServletRequest request, @Named("clientId") Long clientId, @Named("password") String password)
            throws NotFoundException, UnauthorizedException {
        if(!request.isSecure())
            throw new UnauthorizedException("unauthorized: insecure");
        String systemPassword = System.getProperty(Constants.SYSTEM_PROPERTY_PASSWORD);
        if(!Crypto.HASH.BCRYPT.check(password, systemPassword))
            throw new UnauthorizedException("unauthorized: "+ ng.joey.lib.rest.entity.User.Constants.Fields.PASSWORD+ " incorrect");
        try{
            ng.joey.lib.rest.entity.Client client = Persistence.ofy().load().type(ng.joey.lib.rest.entity.Client.class).id(clientId).safe();
            return client.setSecret(null);
        }catch (com.googlecode.objectify.NotFoundException ignored){
            throw new NotFoundException("not found: "+ ng.joey.lib.rest.entity.Client.Constants.Fields.ID + " "+clientId);
        }
    }

    /**
     * Finds and deletes an existing {@link ng.joey.lib.rest.entity.Client} by a corresponding ID.
     * @param request the {@link HttpServletRequest} making this request.
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client} to delete.
     * @param password the system password.
     * @throws NotFoundException if no {@link ng.joey.lib.rest.entity.Client} with the corresponding ID is found.
     * @throws UnauthorizedException if the password is incorrect.
     */
    @ApiMethod(
            name = "client.delete",
            path = "client",
            httpMethod = ApiMethod.HttpMethod.DELETE
    )
    public void deleteClient(HttpServletRequest request, @Named("clientId") Long clientId, @Named("password") String password)
            throws NotFoundException, UnauthorizedException {
        if(!request.isSecure())
            throw new UnauthorizedException("unauthorized: insecure");
        String systemPassword = System.getProperty(Constants.SYSTEM_PROPERTY_PASSWORD);
        if(!Crypto.HASH.BCRYPT.check(password, systemPassword))
            throw new UnauthorizedException("unauthorized: "+ ng.joey.lib.rest.entity.User.Constants.Fields.PASSWORD+ " incorrect");
        try{
            Persistence.ofy().delete().type(ng.joey.lib.rest.entity.Client.class).id(clientId).now();
        }catch (com.googlecode.objectify.NotFoundException ignored){
            throw new NotFoundException("not found: "+ ng.joey.lib.rest.entity.Client.Constants.Fields.ID + " "+clientId);
        }
    }

}
