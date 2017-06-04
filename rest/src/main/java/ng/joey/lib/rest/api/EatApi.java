package ng.joey.lib.rest.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ng.joey.lib.java.util.Time;
import ng.joey.lib.java.util.Value;
import ng.joey.lib.rest.entity.Eat;

import static ng.joey.lib.rest.service.Persistence.ofy;

/**
 *
 */
@Api(
        name = "eatApi",
        version = "v0",
        resource = "eat",
        namespace = @ApiNamespace(
                ownerDomain = "api.rest.lib.joey.ng",
                ownerName = "api.rest.lib.joey.ng",
                packagePath = ""
        )
)
public class EatApi {

    @ApiMethod(
            name = "list",
            path = "list",
            httpMethod = ApiMethod.HttpMethod.POST)
    public CollectionResponse<Eat> list(HttpServletRequest request, @Nullable @Named("cursor") String cursor) throws UnauthorizedException {
        Long userId = Value.TO.longValue(AuthAPI.authenticate(request).getUserId());
        Query<Eat> query = ofy().load().type(Eat.class).filter(Eat.Constants.Fields.userId, userId).limit(100);
        if(!Value.IS.emptyValue(cursor))
            query.startAt(Cursor.fromWebSafeString(cursor));
        List<Eat> eatList = new ArrayList<>();
        QueryResultIterator<Eat> iterator = query.iterator();
        while (iterator.hasNext())
            eatList.add(iterator.next());
        return new CollectionResponse.Builder<Eat>().setItems(eatList).setNextPageToken(iterator.getCursor().toWebSafeString()).build();
    }

    @ApiMethod(
            name = "create",
            path = "create",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Eat create(HttpServletRequest request, @Named("endpointClass") String endpointClass,
                      @Named("methodName") String methodName, @Named("userId") Long userId,
                      @Named("adminPassword") String adminPassword) throws UnauthorizedException, NotFoundException, ConflictException {
        if(!Value.IS.SAME.stringValue(adminPassword, System.getProperty("system.password")))
            throw new UnauthorizedException("unauthorized");
        String[] eatEndpoints;
        try{
            eatEndpoints = getEatEndpoints(Class.forName(endpointClass), methodName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException("endpoint class not found");
        }
        if(Value.IS.emptyValue(eatEndpoints))
            throw new NotFoundException("endpoint not found");
        try {
            ofy().load().type(Eat.class).filter(Eat.Constants.Fields.userId, userId).filter(Eat.Constants.Fields.endpoint + " in", eatEndpoints).first().safe();
            throw new ConflictException("exception: conflict");
        }catch (com.googlecode.objectify.NotFoundException ignored){}
        Eat eat = new Eat()
                .setEndpoint(eatEndpoints[eatEndpoints.length-1])
                .setUserId(userId);
        eat.setCreatedAt(Time.now());
        ofy().save().entity(eat).now();
        return eat;
    }

    private static String[] getEatEndpoints(Class methodClass, String methodName){
        Method[] methods = methodClass.getMethods();
        Method method = null;
        for(Method m: methods){
            if(m.getName().equals(methodName)){
                method = m;
                break;
            }
        }
        if(Value.IS.nullValue(method))
            return null;
        String[] eatEndpoints;
        try {
            eatEndpoints = method.getAnnotation(Eat.Endpoint.class).names();
            if(Value.IS.emptyValue(eatEndpoints))
                return null;
        }catch (Exception ignored){
            return null;
        }
        return eatEndpoints;
    }

    public static String[] getEatEndpoints(String methodClassName, String methodName) throws ClassNotFoundException {
        return getEatEndpoints(Class.forName(methodClassName), methodName);
    }

    public static void authenticate(Class methodClass, String methodName, Long userId) throws ForbiddenException {
        String[] eatEndpoints = getEatEndpoints(methodClass, methodName);
        if(Value.IS.emptyValue(eatEndpoints))
            return;
        try {
            ofy().load().type(Eat.class).filter(Eat.Constants.Fields.userId, userId).filter(Eat.Constants.Fields.endpoint + " in", eatEndpoints).keys().first().safe();
        }catch (com.googlecode.objectify.NotFoundException ignored){
            throw new ForbiddenException("access denied");
        }
    }

    public static void authenticate(Class methodClass, String methodName, String apiModifier, Long userId ) throws ForbiddenException {
        String[] eatEndpoints = getEatEndpoints(methodClass, methodName);
        if(Value.IS.emptyValue(eatEndpoints))
            return;
        for(int i = 0; i < eatEndpoints.length; i++){
            eatEndpoints[i] = eatEndpoints[i]+"."+apiModifier;
        }
        try {
            ofy().load().type(Eat.class).filter(Eat.Constants.Fields.userId, userId).filter(Eat.Constants.Fields.endpoint + " in", eatEndpoints).keys().first().safe();
        }catch (com.googlecode.objectify.NotFoundException ignored){
            throw new ForbiddenException("access denied");
        }
    }

    private static Eat.EatBuilder eatBuilder = new Eat.EatBuilder() {
        @Override
        public Eat build(Eat eat) {
            return eat;
        }
    };

    public static void setEatBuilder(Eat.EatBuilder eatBuilder){
        EatApi.eatBuilder = eatBuilder;
    }

    public static Eat.EatBuilder getEatBuilder(){
        return eatBuilder;
    }



}