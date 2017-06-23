package ng.joey.lib.java.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import sun.rmi.runtime.Log;
import sun.util.resources.cldr.xog.LocaleNames_xog;

/**
 * @author Joey Dalu
 */
public class JsonUtils {

    private static GsonBuilder BUILDER = null;

    static {
        getBuilder().registerTypeAdapter(Locale.class, new Locale.LocaleSerializer());
        getBuilder().registerTypeAdapter(Locale.class, new Locale.LocaleDeserializer());
        getBuilder().registerTypeAdapter(Locale.Subdivision.class, new Locale.Subdivision.SubdivisionSerializer());
        getBuilder().registerTypeAdapter(Locale.Subdivision.class, new Locale.Subdivision.SubDivisionDeserializer());
    }



    public static GsonBuilder getBuilder(){
        return Value.IS.nullValue(BUILDER) ? (BUILDER = new GsonBuilder()) : BUILDER;
    }

    public static class AbstractSerializer<T> implements JsonDeserializer<T> {

        Class<T> tClass;

        public AbstractSerializer(Class<T> tClass){
            this.tClass = tClass;
        }
        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            //Log.d(LOG_TAG, "Deserializing "+json);
            JsonObject o = json.getAsJsonObject();
            try {
                T t = tClass.newInstance();
                Method[] methods = tClass.getDeclaredMethods();
                for(Method method: methods){
                    if(!method.getName().matches("set") && method.getName().startsWith("set")){
                        String methodName = method.getName();
                        String fieldNameCapped = methodName.replaceFirst("set", "");
                        String fieldName = fieldNameCapped.replaceFirst(fieldNameCapped.substring(0, 1), fieldNameCapped.substring(0, 1).toLowerCase());
                        //Log.d(LOG_TAG, "Field name: "+fieldName);
                        if(!o.has(fieldName)) {
                            //Log.d(LOG_TAG, "Field not existent");
                            continue;
                        }
                        JsonElement f = o.get(fieldName);
                        if(f.isJsonNull()) {
                            //Log.d(LOG_TAG, "Field is json null");
                            continue;
                        }
                        Class<?> paramType = method.getParameterTypes()[0];
                        //Log.d(LOG_TAG, "Field value: "+f);
                        String type = paramType.getSimpleName();
                        //Log.d(LOG_TAG, "Field type: "+type);
                        if(f.isJsonPrimitive()) {
                            switch (type) {
                                case "Long":case "long":
                                    //Log.d(LOG_TAG, "Invoking long method");
                                    method.invoke(t, f.getAsLong());
                                    break;
                                case "Double":case "double":
                                    method.invoke(t, f.getAsDouble());
                                    //Log.d(LOG_TAG, "Invoking double method");
                                    break;
                                case "Integer":case "int":
                                    method.invoke(t, f.getAsInt());
                                    break;
                                case "String":
                                    method.invoke(t, f.getAsString());
                                    break;
                                case "Float":case "float":
                                    method.invoke(t, f.getAsFloat());
                                    break;
                                case "Short":case "short":
                                    method.invoke(t, f.getAsShort());
                                    break;
                                case "Byte":case "byte":
                                    method.invoke(t, f.getAsByte());
                                    break;
                                case "Boolean":case "boolean":
                                    method.invoke(t, f.getAsBoolean());
                                    break;
                                case "Character":case "char":
                                    method.invoke(t, f.getAsCharacter());
                                    break;
                            }
                        }else if(f.isJsonObject() || f.isJsonArray()){

                            switch (type){
                                case "JsonMap":
                                    method.invoke(t, new Gson().fromJson(f, paramType));
                                    break;
                                default:
                                    method.invoke(t, getBuilder().create().fromJson(f, paramType));
                                    break;
                            }

                        }else{

                        }
                    }
                }
                return t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
