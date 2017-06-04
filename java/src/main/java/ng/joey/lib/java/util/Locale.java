package ng.joey.lib.java.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joey Dalu on 8/16/16 at 1:47 PM
 */
public class Locale {

    /*
    public static void main(String[] args){
        List<Locale> locales = null;
        try {
            locales = getLocales();
            System.out.println("Locales size: "+locales.size()+" and first is "+locales.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exception loading locales, is: "+e.getMessage());
        }
    }
    */

    public static class Subdivision {
        public static final class Constants {
            public static final class Fields {
                public static final String subdivisionCategory = "subdivision_category";
                public static final String code3166_2 = "code_3166_2";
                public static final String subdivisionName = "subdivision_name";
                public static final String languageCode = "language_code";
                public static final String romanizationSystem = "romanization_system";
                public static final String parentSubdivision = "parent_subdivision";
            }
        }

        private String subdivisionCategory;
        private String code3166_2;
        private String subdivisionName;
        private String languageCode;
        private String romanizationSystem;
        private String parentSubdivision;

        public String getSubdivisionCategory() {
            return subdivisionCategory;
        }

        public Subdivision setSubdivisionCategory(String subdivisionCategory) {
            this.subdivisionCategory = subdivisionCategory;
            return this;
        }

        public String getCode3166_2() {
            return code3166_2;
        }

        public Subdivision setCode3166_2(String code3166_2) {
            this.code3166_2 = code3166_2;
            return this;
        }

        public String getSubdivisionName() {
            return subdivisionName;
        }

        public Subdivision setSubdivisionName(String subdivisionName) {
            this.subdivisionName = subdivisionName;
            return this;
        }

        public String getLanguageCode() {
            return languageCode;
        }

        public Subdivision setLanguageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public String getRomanizationSystem() {
            return romanizationSystem;
        }

        public Subdivision setRomanizationSystem(String romanizationSystem) {
            this.romanizationSystem = romanizationSystem;
            return this;
        }

        public String getParentSubdivision() {
            return parentSubdivision;
        }

        public Subdivision setParentSubdivision(String parentSubdivision) {
            this.parentSubdivision = parentSubdivision;
            return this;
        }

        public static class SubdivisionSerializer implements JsonSerializer<Subdivision>{
            @Override
            public JsonElement serialize(Subdivision src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject o = new JsonObject();
                o.addProperty(Constants.Fields.subdivisionCategory, src.getSubdivisionCategory());
                o.addProperty(Constants.Fields.subdivisionName, src.getSubdivisionName());
                o.addProperty(Constants.Fields.code3166_2, src.getCode3166_2());
                o.addProperty(Constants.Fields.languageCode, src.getLanguageCode());
                o.addProperty(Constants.Fields.romanizationSystem, src.getRomanizationSystem());
                o.addProperty(Constants.Fields.parentSubdivision, src.getParentSubdivision());
                return o;
            }
        }

        public static class SubDivisionDeserializer implements JsonDeserializer<Subdivision>{
            @Override
            public Subdivision deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject o = json.getAsJsonObject();
                return deserialize(o);
            }

            public static Subdivision deserialize(JsonObject o){
                return new Subdivision()
                        .setSubdivisionCategory(Value.TO.stringValue(Constants.Fields.subdivisionCategory, o))
                        .setSubdivisionName(Value.TO.stringValue(Constants.Fields.subdivisionName, o))
                        .setCode3166_2(Value.TO.stringValue(Constants.Fields.code3166_2, o))
                        .setLanguageCode(Value.TO.stringValue(Constants.Fields.languageCode, o))
                        .setRomanizationSystem(Value.TO.stringValue(Constants.Fields.romanizationSystem, o))
                        .setParentSubdivision(Value.TO.stringValue(Constants.Fields.parentSubdivision, o));
            }
        }

        public static List<Subdivision> getSubDivisions(Locale locale) throws IOException{
            String filePath = "/subdivisions/"+locale.getISO3166_1Alpha2().toLowerCase()+".txt";
            try {
                List<Subdivision> subdivisions = new ArrayList<>();
                JsonParser parser = new JsonParser();
                InputStream str = Subdivision.class.getResourceAsStream(filePath);
                JsonArray array = null;
                InputStreamReader reader = new InputStreamReader(str);
                array = (JsonArray) parser.parse(reader);
                for (Object o : array) {
                    Subdivision subdivision = SubDivisionDeserializer.deserialize((JsonObject) o);
                    subdivisions.add(subdivision);
                }
                return subdivisions;
            }catch (Exception e){
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
        }

    }

    public static final class Constants {

        public static final class Fields {
            public static final String name = "name";
            public static final String officialNameEn = "official_name_en";
            public static final String officialNameFr = "official_name_fr";
            public static final String ISO3166_1Alpha2 = "ISO3166-1-Alpha-2";
            public static final String ISO3166_1Alpha3 = "ISO3166-1-Alpha-3";
            public static final String ISO3166_1Numeric = "ISO3166-1-numeric";
            public static final String internationalTelUnionCode = "ITU";
            public static final String machineReadableCatalogingCode = "MARC";
            public static final String worldMeterologicalOrgAbbr = "WMO";
            public static final String distinguishableVehicleSign = "DS";
            public static final String dialingCode = "Dial";
            public static final String fifaCode = "FIFA";
            public static final String fipsCode = "FIPS";
            public static final String globalAdminUnitLayersCode = "GAUL";
            public static final String internationalOlympicsCode = "IOC";
            public static final String ISO4217CurrencyAlphabeticCode = "ISO4217-currency_alphabetic_code";
            public static final String ISO4217CurrencyCountryName = "ISO4217-currency_country_name";
            public static final String ISO4217CurrencyMinorUnit = "ISO4217-currency_minor_unit";
            public static final String ISO4217CurrencyName = "ISO4217-currency_name";
            public static final String ISO4217CurrencyNumericCode = "ISO4217-currency_numeric_code";
            public static final String isIndependent = "is_independent";
            public static final String capital = "Capital";
            public static final String continent = "Continent";
            public static final String topLevelDomain = "TLD";
            public static final String languages = "Languages";
            public static final String geonameId = "Geoname ID";
            public static final String edgarCode = "EDGAR";
        }

    }

    private String name, officialNameEn, officialNameFr, ISO3166_1Alpha2,
    ISO3166_1Alpha3, internationalTelUnionCode, machineReadableCatalogingCode, worldMeterologicalOrgAbbr,
            distinguishableVehicleSign, dialingCode, fifaCode, fipsCode, globalAdminUnitLayersCode, internationalOlympicsCode,
    ISO4217CurrencyAlphabeticCode, ISO4217CurrencyCountryName, ISO4217CurrencyName, isIndependent, capital, continent, topLevelDomain, languages, edgarCode;

    private Integer ISO3166_1Numeric, geonameId, ISO4217CurrencyNumericCode, ISO4217CurrencyMinorUnit;

    public String getName() {
        return name;
    }

    public Locale setName(String name) {
        this.name = name;
        return this;
    }

    public String getOfficialNameEn() {
        return officialNameEn;
    }

    public Locale setOfficialNameEn(String officialNameEn) {
        this.officialNameEn = officialNameEn;
        return this;
    }

    public String getOfficialNameFr() {
        return officialNameFr;
    }

    public Locale setOfficialNameFr(String officialNameFr) {
        this.officialNameFr = officialNameFr;
        return this;
    }

    public String getISO3166_1Alpha2() {
        return ISO3166_1Alpha2;
    }

    public Locale setISO3166_1Alpha2(String ISO3166_1Alpha2) {
        this.ISO3166_1Alpha2 = ISO3166_1Alpha2;
        return this;
    }

    public String getISO3166_1Alpha3() {
        return ISO3166_1Alpha3;
    }

    public Locale setISO3166_1Alpha3(String ISO3166_1Alpha3) {
        this.ISO3166_1Alpha3 = ISO3166_1Alpha3;
        return this;
    }

    public String getInternationalTelUnionCode() {
        return internationalTelUnionCode;
    }

    public Locale setInternationalTelUnionCode(String internationalTelUnionCode) {
        this.internationalTelUnionCode = internationalTelUnionCode;
        return this;
    }

    public String getMachineReadableCatalogingCode() {
        return machineReadableCatalogingCode;
    }

    public Locale setMachineReadableCatalogingCode(String machineReadableCatalogingCode) {
        this.machineReadableCatalogingCode = machineReadableCatalogingCode;
        return this;
    }

    public String getWorldMeterologicalOrgAbbr() {
        return worldMeterologicalOrgAbbr;
    }

    public Locale setWorldMeterologicalOrgAbbr(String worldMeterologicalOrgAbbr) {
        this.worldMeterologicalOrgAbbr = worldMeterologicalOrgAbbr;
        return this;
    }

    public String getDistinguishableVehicleSign() {
        return distinguishableVehicleSign;
    }

    public Locale setDistinguishableVehicleSign(String distinguishableVehicleSign) {
        this.distinguishableVehicleSign = distinguishableVehicleSign;
        return this;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public Locale setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
        return this;
    }

    public String getFifaCode() {
        return fifaCode;
    }

    public Locale setFifaCode(String fifaCode) {
        this.fifaCode = fifaCode;
        return this;
    }

    public String getFipsCode() {
        return fipsCode;
    }

    public Locale setFipsCode(String fipsCode) {
        this.fipsCode = fipsCode;
        return this;
    }

    public String getGlobalAdminUnitLayersCode() {
        return globalAdminUnitLayersCode;
    }

    public Locale setGlobalAdminUnitLayersCode(String globalAdminUnitLayersCode) {
        this.globalAdminUnitLayersCode = globalAdminUnitLayersCode;
        return this;
    }

    public String getInternationalOlympicsCode() {
        return internationalOlympicsCode;
    }

    public Locale setInternationalOlympicsCode(String internationalOlympicsCode) {
        this.internationalOlympicsCode = internationalOlympicsCode;
        return this;
    }

    public String getISO4217CurrencyAlphabeticCode() {
        return ISO4217CurrencyAlphabeticCode;
    }

    public Locale setISO4217CurrencyAlphabeticCode(String ISO4217CurrencyAlphabeticCode) {
        this.ISO4217CurrencyAlphabeticCode = ISO4217CurrencyAlphabeticCode;
        return this;
    }

    public String getISO4217CurrencyCountryName() {
        return ISO4217CurrencyCountryName;
    }

    public Locale setISO4217CurrencyCountryName(String ISO4217CurrencyCountryName) {
        this.ISO4217CurrencyCountryName = ISO4217CurrencyCountryName;
        return this;
    }

    public String getISO4217CurrencyName() {
        return ISO4217CurrencyName;
    }

    public Locale setISO4217CurrencyName(String ISO4217CurrencyName) {
        this.ISO4217CurrencyName = ISO4217CurrencyName;
        return this;
    }

    public String getIsIndependent() {
        return isIndependent;
    }

    public Locale setIsIndependent(String isIndependent) {
        this.isIndependent = isIndependent;
        return this;
    }

    public String getCapital() {
        return capital;
    }

    public Locale setCapital(String capital) {
        this.capital = capital;
        return this;
    }

    public String getContinent() {
        return continent;
    }

    public Locale setContinent(String continent) {
        this.continent = continent;
        return this;
    }

    public String getTopLevelDomain() {
        return topLevelDomain;
    }

    public Locale setTopLevelDomain(String topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
        return this;
    }

    public String getLanguages() {
        return languages;
    }

    public Locale setLanguages(String languages) {
        this.languages = languages;
        return this;
    }

    public Integer getGeonameId() {
        return geonameId;
    }

    public Locale setGeonameId(Integer geonameId) {
        this.geonameId = geonameId;
        return this;
    }

    public String getEdgarCode() {
        return edgarCode;
    }

    public Locale setEdgarCode(String edgarCode) {
        this.edgarCode = edgarCode;
        return this;
    }

    public Integer getISO3166_1Numeric() {
        return ISO3166_1Numeric;
    }

    public Locale setISO3166_1Numeric(Integer ISO3166_1Numeric) {
        this.ISO3166_1Numeric = ISO3166_1Numeric;
        return this;
    }

    public Integer getISO4217CurrencyNumericCode() {
        return ISO4217CurrencyNumericCode;
    }

    public Locale setISO4217CurrencyNumericCode(Integer ISO4217CurrencyNumericCode) {
        this.ISO4217CurrencyNumericCode = ISO4217CurrencyNumericCode;
        return this;
    }

    public Integer getISO4217CurrencyMinorUnit() {
        return ISO4217CurrencyMinorUnit;
    }

    public Locale setISO4217CurrencyMinorUnit(Integer ISO4217CurrencyMinorUnit) {
        this.ISO4217CurrencyMinorUnit = ISO4217CurrencyMinorUnit;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtils.getBuilder().create().toJson(this);
    }

    public static class LocaleSerializer implements JsonSerializer<Locale>{
        @Override
        public JsonElement serialize(Locale src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject o = new JsonObject();
            o.addProperty(Constants.Fields.name, src.getName());
            o.addProperty(Constants.Fields.officialNameEn, src.getOfficialNameEn());
            o.addProperty(Constants.Fields.officialNameFr, src.getOfficialNameFr());
            o.addProperty(Constants.Fields.ISO3166_1Alpha2, src.getISO3166_1Alpha2());
            o.addProperty(Constants.Fields.ISO3166_1Alpha3, src.getISO3166_1Alpha3());
            o.addProperty(Constants.Fields.ISO3166_1Numeric, src.getISO3166_1Numeric());
            o.addProperty(Constants.Fields.internationalTelUnionCode, src.getInternationalTelUnionCode());
            o.addProperty(Constants.Fields.machineReadableCatalogingCode, src.getMachineReadableCatalogingCode());
            o.addProperty(Constants.Fields.worldMeterologicalOrgAbbr, src.getWorldMeterologicalOrgAbbr());
            o.addProperty(Constants.Fields.distinguishableVehicleSign, src.getDistinguishableVehicleSign());
            o.addProperty(Constants.Fields.dialingCode, src.getDialingCode());
            o.addProperty(Constants.Fields.fifaCode, src.getFifaCode());
            o.addProperty(Constants.Fields.fipsCode, src.getFipsCode());
            o.addProperty(Constants.Fields.globalAdminUnitLayersCode, src.getGlobalAdminUnitLayersCode());
            o.addProperty(Constants.Fields.internationalOlympicsCode, src.getInternationalOlympicsCode());
            o.addProperty(Constants.Fields.ISO4217CurrencyAlphabeticCode, src.getISO4217CurrencyAlphabeticCode());
            o.addProperty(Constants.Fields.ISO4217CurrencyCountryName, src.getISO4217CurrencyCountryName());
            o.addProperty(Constants.Fields.ISO4217CurrencyName, src.getISO4217CurrencyName());
            o.addProperty(Constants.Fields.ISO4217CurrencyNumericCode, src.getISO4217CurrencyNumericCode());
            o.addProperty(Constants.Fields.isIndependent, src.getIsIndependent());
            o.addProperty(Constants.Fields.capital, src.getCapital());
            o.addProperty(Constants.Fields.continent, src.getContinent());
            o.addProperty(Constants.Fields.topLevelDomain, src.getTopLevelDomain());
            o.addProperty(Constants.Fields.languages, src.getLanguages());
            o.addProperty(Constants.Fields.geonameId, src.getGeonameId());
            o.addProperty(Constants.Fields.edgarCode, src.getEdgarCode());
            return o;
        }
    }

    public static class LocaleDeserializer implements JsonDeserializer<Locale>{
        @Override
        public Locale deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject o = json.getAsJsonObject();
            return deserialize(o);
        }

        public static Locale deserialize(JsonObject o){
            return new Locale()
                    .setName(Value.TO.stringValue(Constants.Fields.name, o))
                    .setOfficialNameEn(Value.TO.stringValue(Constants.Fields.officialNameEn, o))
                    .setOfficialNameFr(Value.TO.stringValue(Constants.Fields.officialNameFr, o))
                    .setISO3166_1Alpha2(Value.TO.stringValue(Constants.Fields.ISO3166_1Alpha2, o))
                    .setISO3166_1Alpha3(Value.TO.stringValue(Constants.Fields.ISO3166_1Alpha3, o))
                    .setISO3166_1Numeric(Value.TO.integerValue(Constants.Fields.ISO3166_1Numeric, o))
                    .setInternationalTelUnionCode(Value.TO.stringValue(Constants.Fields.internationalTelUnionCode, o))
                    .setMachineReadableCatalogingCode(Value.TO.stringValue(Constants.Fields.machineReadableCatalogingCode, o))
                    .setWorldMeterologicalOrgAbbr(Value.TO.stringValue(Constants.Fields.worldMeterologicalOrgAbbr, o))
                    .setDistinguishableVehicleSign(Value.TO.stringValue(Constants.Fields.distinguishableVehicleSign, o))
                    .setDialingCode(Value.TO.stringValue(Constants.Fields.dialingCode, o))
                    .setFifaCode(Value.TO.stringValue(Constants.Fields.fifaCode, o))
                    .setFipsCode(Value.TO.stringValue(Constants.Fields.fipsCode, o))
                    .setGlobalAdminUnitLayersCode(Value.TO.stringValue(Constants.Fields.globalAdminUnitLayersCode, o))
                    .setInternationalOlympicsCode(Value.TO.stringValue(Constants.Fields.internationalOlympicsCode, o))
                    .setISO4217CurrencyAlphabeticCode(Value.TO.stringValue(Constants.Fields.ISO4217CurrencyAlphabeticCode, o))
                    .setISO4217CurrencyCountryName(Value.TO.stringValue(Constants.Fields.ISO4217CurrencyCountryName, o))
                    .setISO4217CurrencyNumericCode(Value.TO.integerValue(Constants.Fields.ISO4217CurrencyNumericCode, o))
                    .setIsIndependent(Value.TO.stringValue(Constants.Fields.isIndependent, o))
                    .setCapital(Value.TO.stringValue(Constants.Fields.capital, o))
                    .setContinent(Value.TO.stringValue(Constants.Fields.continent, o))
                    .setTopLevelDomain(Value.TO.stringValue(Constants.Fields.topLevelDomain, o))
                    .setLanguages(Value.TO.stringValue(Constants.Fields.languages, o))
                    .setGeonameId(Value.TO.integerValue(Constants.Fields.geonameId, o))
                    .setEdgarCode(Value.TO.stringValue(Constants.Fields.edgarCode, o));
        }

    }

    public static List<Locale> getLocales() throws IOException{
        String filePath = "/locales.txt";
        try {
            List<Locale> locales = new ArrayList<>();
            JsonParser parser = new JsonParser();
            InputStream str = Locale.class.getResourceAsStream(filePath);
            JsonArray array = null;
            InputStreamReader reader = new InputStreamReader(str);
            array = (JsonArray) parser.parse(reader);
            for (Object o : array) {
                Locale locale = LocaleDeserializer.deserialize((JsonObject) o);
                locales.add(locale);
            }
            return locales;
        }catch (Exception e){
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

}
