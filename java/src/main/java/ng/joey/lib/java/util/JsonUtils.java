package ng.joey.lib.java.util;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by Joey Dalughut on 8/7/16 at 7:44 AM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class JsonUtils {

    private static GsonBuilder BUILDER = null;

    static {
        registerSerializer(Locale.class, new Locale.LocaleSerializer(), new Locale.LocaleDeserializer());
        registerSerializer(Locale.Subdivision.class, new Locale.Subdivision.SubdivisionSerializer(), new Locale.Subdivision.SubDivisionDeserializer());
    }

    public static void registerSerializer(Type type, Object typeSerializer, Object typeDeserializer){
        getBuilder().registerTypeAdapter(type, typeSerializer);
        getBuilder().registerTypeAdapter(type, typeDeserializer);
    }

    public static GsonBuilder getBuilder(){
        return Value.IS.nullValue(BUILDER) ? (BUILDER = new GsonBuilder()) : BUILDER;
    }
}
