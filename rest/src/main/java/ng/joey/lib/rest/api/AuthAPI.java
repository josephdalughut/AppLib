package ng.joey.lib.rest.api;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiAuth;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.NotFoundException;
import ng.joey.lib.java.security.Crypto;
import ng.joey.lib.java.util.Value;
import ng.joey.lib.rest.service.Persistence;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Authorization API implementation for Google App engine applications.
 */
@Api(
        name = "authApi",
        version = "v0",
        resource = "token",
        auth = @ApiAuth(allowCookieAuth = AnnotationBoolean.TRUE),
        namespace = @ApiNamespace(
                ownerDomain = "api.rest.lib.joey.ng",
                ownerName = "api.rest.lib.joey.ng",
                packagePath = ""
        )
)
public class AuthAPI {

    /**
     * Authorizes a {@link ng.joey.lib.rest.entity.User}, returning Refresh and Access {@link ng.joey.lib.rest.entity.Token}'s for their use.
     * @param request the {@link HttpServletRequest} making this request.
     * @param userId an Identifier for this user. Should not be their ID field, but any other such
     *               as email or username.
     * @param userIdFieldName the name of the field passed as the Identifier, needed to filter the
     *                        {@link ng.joey.lib.rest.entity.User} by that field. For example, if you passed the {@link ng.joey.lib.rest.entity.User}'s
     *                        username, then pass in 'username' as the name of the field.
     * @param userPassword the password of the {@link ng.joey.lib.rest.entity.User}
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client} making this request.
     * @param clientSecret the SECRET of the {@link ng.joey.lib.rest.entity.Client} making this request.
     * @return a map containing two {@link ng.joey.lib.rest.entity.Token}'s Access and Refresh {@link ng.joey.lib.rest.entity.Token}'s.
     * @throws UnauthorizedException if the {@link ng.joey.lib.rest.entity.User} or {@link ng.joey.lib.rest.entity.Client} fails authorization, or if
     * this request wasn't made via https.
     * @see ng.joey.lib.rest.entity.Token
     * @see ng.joey.lib.rest.entity.Client
     * @see ng.joey.lib.rest.entity.User
     */
    /*
    @ApiMethod(
            name = "authorize",
            path = "authorize",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Map<String, Token> getToken(HttpServletRequest request, @Named("userId") String userId,
                                       @Named("userIdFieldName") String userIdFieldName,
                                       @Named("userPassword") String userPassword,
                                       @Named("clientId") String clientId,
                                       @Named("clientSecret") String clientSecret) throws UnauthorizedException {
        if(Value.IS.nullValue(request) || !request.isSecure())
            throw new UnauthorizedException("unauthorized: bad request: https required");
        if(Value.IS.ANY.emptyValue(userId, userPassword))
            throw new UnauthorizedException("unauthorized: bad request: user password required");
        User user;
        try {
            user = ofy().load().type(User.class).filter(userIdFieldName, userId).first().safe();
        }catch (NotFoundException ignored){
            throw new UnauthorizedException("unauthorized: not found: user id");
        }
        return getToken(request, userPassword, user, clientId, clientSecret);
    }
    */

    /**
     * Exchanges a refresh {@link ng.joey.lib.rest.entity.Token} for a new pair of access and refresh {@link ng.joey.lib.rest.entity.Token}'s.
     * @param request the {@link HttpServletRequest} making this request.
     * @param refreshToken an existing Refresh {@link ng.joey.lib.rest.entity.Token}
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client} making this request.
     * @param clientSecret the SECRET of the {@link ng.joey.lib.rest.entity.Client} making this request.
     * @return a map containing two {@link ng.joey.lib.rest.entity.Token}'s Access and Refresh {@link ng.joey.lib.rest.entity.Token}'s.
     * @throws UnauthorizedException if the Refresh {@link ng.joey.lib.rest.entity.Token} or the {@link ng.joey.lib.rest.entity.Client} doesn't check out,
     * or if this request wasn't made via https.
     * @see ng.joey.lib.rest.entity.Token
     * @see ng.joey.lib.rest.entity.Client
     */
    @ApiMethod(
            name = "token",
            path = "token",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public ng.joey.lib.rest.entity.Token refreshToken(HttpServletRequest request, @Named("refreshToken") String refreshToken,
                                                      @Named("clientId") Long clientId, @Named("clientSecret") String clientSecret) throws UnauthorizedException, com.google.api.server.spi.response.NotFoundException {

        if(Value.IS.nullValue(request) || !request.isSecure())
            throw new UnauthorizedException("unauthorized: bad request: https required");
        if(Value.IS.ANY.emptyValue(refreshToken, clientId, clientSecret))
            throw new UnauthorizedException("unauthorized: bad request: refreshToken | clientId | clientSecret required");

        isClient(clientId, clientSecret);
        ng.joey.lib.rest.entity.Token oldRefreshToken;
        try{
            oldRefreshToken = Persistence.ofy().load().type(ng.joey.lib.rest.entity.Token.class).id(refreshToken).safe();
        }catch (NotFoundException ignored){
            throw new com.google.api.server.spi.response.NotFoundException("unauthorized: not found: refresh token");
        }
        DateTime now = ng.joey.lib.rest.util.Time.nowDateTime();
        DateTime expiresAt = now.plusHours(4);
        DateTime fourWeeks = now.plusWeeks(4);
        final ng.joey.lib.rest.entity.Token accessToken = new ng.joey.lib.rest.entity.Token()
                .setId(Crypto.Random.uuidClear())
                .setClientId(clientId)
                .setUserId(oldRefreshToken.getUserId())
                .setExpiresAt(expiresAt.getMillis())
                .setScope(oldRefreshToken.getScope())
                .setType(ng.joey.lib.rest.entity.Token.Type.ACCESS_TOKEN);
        ng.joey.lib.rest.util.Time.onCreate(accessToken);
        final ng.joey.lib.rest.entity.Token newRefreshToken = new ng.joey.lib.rest.entity.Token()
                .setId(Crypto.Random.uuidClear())
                .setClientId(clientId)
                .setUserId(accessToken.getUserId())
                .setExpiresAt(fourWeeks.getMillis())
                .setScope(accessToken.getScope())
                .setType(ng.joey.lib.rest.entity.Token.Type.REFRESH_TOKEN);
        newRefreshToken.setCreatedAt(accessToken.getCreatedAt()).setUpdatedAt(accessToken.getUpdatedAt());
        Persistence.ofy().save().entities(accessToken, newRefreshToken).now();

        // TODO figure out how to invalidate old refresh tokens
        // Persistence.ofy().delete().entity(oldRefreshToken);
        Map<String, String> data = new HashMap<>();
        data.put(ng.joey.lib.rest.entity.Token.Type.REFRESH_TOKEN, ng.joey.lib.rest.util.JsonUtils.getBuilder().create().toJson(newRefreshToken));
        accessToken.setAccompanyingData(data);
        return (ng.joey.lib.rest.entity.Token) accessToken.setAccompanyingData(data);
    }

    @ApiMethod(
            name = "expose.token",
            path = "expose/token",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public ng.joey.lib.rest.entity.Token exposeToken() throws com.google.api.server.spi.response.NotFoundException {
        throw new com.google.api.server.spi.response.NotFoundException("");
    }

    /**
     * Creates and Returns an access and refresh token after authenticating a {@link ng.joey.lib.rest.entity.User} and a {@link ng.joey.lib.rest.entity.Client}.
     * @param request the {@link HttpServletRequest} making this call.
     * @param datastoreUser the datastore version of the {@link ng.joey.lib.rest.entity.User} to be authenticated.
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client} to be authenticated.
     * @param clientSecret the secret of the {@link ng.joey.lib.rest.entity.Client} to be authenticated.
     * @return a map containing two tokens, keys: "access" and "refresh" for the access and refresh tokens respectively.
     * @throws UnauthorizedException if the {@link ng.joey.lib.rest.entity.User} or {@link ng.joey.lib.rest.entity.Client} fails authentication.
     */
    public static Map<String, ng.joey.lib.rest.entity.Token> getToken(HttpServletRequest request, ng.joey.lib.rest.entity.User datastoreUser, Long clientId, String clientSecret) throws UnauthorizedException {
        if(Value.IS.nullValue(request) || !request.isSecure())
            throw new UnauthorizedException("unauthorized: bad request: https required");
        if(Value.IS.nullValue(datastoreUser))
            throw new UnauthorizedException("unauthorized: bad request: user password required");
        if(Value.IS.ANY.emptyValue(clientId, clientSecret))
            throw new UnauthorizedException("unauthorized: bad request: client required");
        isConfidentialClient(clientId, clientSecret);
        DateTime now = ng.joey.lib.rest.util.Time.nowDateTime();
        DateTime expiresAt = now.plusHours(4);
        DateTime fourWeeks = now.plusWeeks(4);
        final ng.joey.lib.rest.entity.Token accessToken = new ng.joey.lib.rest.entity.Token()
                .setId(Crypto.Random.uuidClear())
                .setClientId(clientId)
                .setUserId(Value.TO.stringValue(datastoreUser.getId()))
                .setScope(ng.joey.lib.rest.entity.Token.Scope.CONFIDENTIAL)
                .setExpiresAt(expiresAt.getMillis())
                .setType(ng.joey.lib.rest.entity.Token.Type.ACCESS_TOKEN);
        ng.joey.lib.rest.util.Time.onCreate(accessToken);
        final ng.joey.lib.rest.entity.Token refreshToken = new ng.joey.lib.rest.entity.Token()
                .setId(Crypto.Random.uuidClear())
                .setScope(accessToken.getScope())
                .setClientId(clientId)
                .setUserId(Value.TO.stringValue(datastoreUser.getId()))
                .setExpiresAt(fourWeeks.getMillis())
                .setType(ng.joey.lib.rest.entity.Token.Type.REFRESH_TOKEN);
        refreshToken.setCreatedAt(accessToken.getCreatedAt()).setUpdatedAt(accessToken.getUpdatedAt());
        Persistence.ofy().save().entities(accessToken, refreshToken).now();
        return new HashMap<String, ng.joey.lib.rest.entity.Token>(){{
            put(ng.joey.lib.rest.entity.Token.Type.ACCESS_TOKEN, accessToken);
            put(ng.joey.lib.rest.entity.Token.Type.REFRESH_TOKEN, refreshToken);
        }};
    }



    /**
     * Authenticates a {@link HttpServletRequest}, returning the {@link ng.joey.lib.rest.entity.Token} tied to it.
     * @param request the {@link HttpServletRequest} to authenticate.
     * @return the {@link ng.joey.lib.rest.entity.Token} tied to the request.
     * @throws UnauthorizedException if authentication fails. This occurs when the request isn't via
     * https, if the authorization header wasn't set, if the token doesn't exist, or if it has expired.
     * @see ng.joey.lib.rest.entity.Token
     */
    public static ng.joey.lib.rest.entity.Token authenticate(HttpServletRequest request) throws UnauthorizedException {
        if(Value.IS.nullValue(request) || !request.isSecure())
            throw new UnauthorizedException("unauthorized: bad request: https required");
        String authorization = request.getHeader("Authorization");
        if(Value.IS.emptyValue(authorization))
            throw new UnauthorizedException("unauthorized: bad request: Authorization required");
        authorization = authorization.replace("Bearer", "").trim();
        if(Value.IS.emptyValue(authorization))
            throw new UnauthorizedException("unauthorized: bad request: Authorization required");
        try {
            ng.joey.lib.rest.entity.Token token = Persistence.ofy().load().type(ng.joey.lib.rest.entity.Token.class).id(authorization).safe();
            DateTime tokenTime = new DateTime().withMillis(token.getExpiresAt());
            if(tokenTime.isBefore(ng.joey.lib.rest.util.Time.now()))
                throw new NotFoundException();
            if(!Value.IS.SAME.stringValue(token.getType(), ng.joey.lib.rest.entity.Token.Type.ACCESS_TOKEN))
                throw new UnauthorizedException("unauthorized: not access token");
            return token;
        }catch (NotFoundException ignored){
            throw new UnauthorizedException("unauthorized: not found: token expired");
        }
    }

    /**
     * Authenticates a {@link ng.joey.lib.rest.entity.Client}, returning it only if it checks out and is a confidential {@link ng.joey.lib.rest.entity.Client}
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client}
     * @param clientSecret the secret key of the {@link ng.joey.lib.rest.entity.Client}
     * @return the authenticated {@link ng.joey.lib.rest.entity.Client}
     * @throws UnauthorizedException if authentication fails. This happens if the {@link ng.joey.lib.rest.entity.Client} doesn't exist,
     * isn't confidential or if the clients secret doesn't check out.
     * @see ng.joey.lib.rest.entity.Client
     */
    public static ng.joey.lib.rest.entity.Client isConfidentialClient(Long clientId, String clientSecret) throws UnauthorizedException {
        String secretKey = clientSecret.replaceFirst("CSK_", "");
        ng.joey.lib.rest.entity.Client client;
        try{
            client = Persistence.ofy().load().type(ng.joey.lib.rest.entity.Client.class).id(clientId).safe();
            if(!Value.IS.SAME.stringValue(client.getType(), ng.joey.lib.rest.entity.Client.ClientType.CONFIDENTIAL))
                throw new UnauthorizedException("unauthorized: forbidden: confidential client required");
        }catch (NotFoundException ignored){
            throw new UnauthorizedException("unauthorized: not found: client required");
        }
        if(!Crypto.HASH.BCRYPT.check(secretKey, client.getSecret()))
            throw new UnauthorizedException("unauthorized: bad request: client password");
        return client;
    }

    /**
     * Authenticates a {@link ng.joey.lib.rest.entity.Client}, returning it only if it checks out and is a confidential {@link ng.joey.lib.rest.entity.Client}
     * @param clientId the ID of the {@link ng.joey.lib.rest.entity.Client}
     * @param clientSecret the secret key of the {@link ng.joey.lib.rest.entity.Client}
     * @return the authenticated {@link ng.joey.lib.rest.entity.Client}
     * @throws UnauthorizedException if authentication fails. This happens if the {@link ng.joey.lib.rest.entity.Client} doesn't exist,
     * isn't confidential or if the clients secret doesn't check out.
     * @see ng.joey.lib.rest.entity.Client
     */
    public static ng.joey.lib.rest.entity.Client isClient(Long clientId, String clientSecret) throws UnauthorizedException {
        String secretKey = clientSecret.replaceFirst("CSK_", "");
        ng.joey.lib.rest.entity.Client client;
        try{
            client = Persistence.ofy().load().type(ng.joey.lib.rest.entity.Client.class).id(clientId).safe();
        }catch (NotFoundException ignored){
            throw new UnauthorizedException("unauthorized: not found: client required");
        }
        if(!Crypto.HASH.BCRYPT.check(secretKey, client.getSecret()))
            throw new UnauthorizedException("unauthorized: bad request: client password");
        return client;
    }


}
