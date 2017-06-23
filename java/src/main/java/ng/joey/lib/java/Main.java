package ng.joey.lib.java;

import java.io.IOException;
import java.util.List;

import ng.joey.lib.java.util.JsonUtils;
import ng.joey.lib.java.util.Locale;

public class Main {

    public static void main(String[] args){
        List<Locale> locales = null;
        try {
            locales = Locale.getLocales();
            for(Locale locale: locales){
                System.out.println(JsonUtils.getBuilder().create().toJson(locale));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
