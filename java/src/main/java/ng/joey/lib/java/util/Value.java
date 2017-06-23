package ng.joey.lib.java.util;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Joey Dalu
 */
public class Value {

    public static final String HYPHEN = "-";
    public static final String UNDERSCORE = "_";

    /**
     * Utility class for value comparison
     */
    public static class IS {

        /**
         * Check if an object of type {@param <T>} is emptyValue.
         * This method wokrs for strings, collections, maps and arrays of object types.
         * @param t the object to be checked
         * @param <T> the type parameter of the object
         * @return true if the object is emptyValue or is null
         */
        public static <T> boolean emptyValue(T t){
            if(nullValue(t)) return true;
            if(t instanceof String)
                return ((String)t).replaceAll("\\s+", "").isEmpty();
            if(t instanceof Collection)
                return emptyValue((Collection) t);
            if(t instanceof Map)
                return emptyValue((Map) t);
            if(t instanceof Object[])
                return emptyValue((Object[]) t);
            return false;
        }

        /**
         * Check if a array of objects of type {@param <T>} is emptyValue.
         * @param ts the array of objects to check
         * @param <T> the type parameter of the objects
         * @return true if the array of objects is emptyValue or null
         */
        public static <T> boolean emptyValue(T... ts){
            return nullValue(ts) || ts.length < 1;
        }

        /**
         * Check if a string is emptyValue
         * @param string the string to check
         * @return true if the string is emptyValue or null
         */
        public static boolean emptyValue(String string){
            return nullValue(string) || string.replaceAll("\\s+", "").isEmpty();
        }

        /**
         * Check if a collection of objects of type {@param <T>} is emptyValue
         * @param ts the collection of objects to check
         * @param <T> the type parameter of the objects
         * @return true if the collection is emptyValue or null
         */
        public static <T> boolean emptyValue(Collection<T> ts){
            return nullValue(ts) || ts.isEmpty();
        }

        /**
         * Check if a map of objects of key type {@param <K>} and value type {@param <V>}
         * @param map the map to check
         * @param <K> the key type parameter
         * @param <V> the value type parameter
         * @return true if the map is emptyValue or null
         */
        public static <K, V> boolean emptyValue(Map<K, V> map){
            return nullValue(map) || map.isEmpty();
        }

        /**
         * Check if an object is null
         * @param t the object to check
         * @param <T> the type parameter of the object
         * @return true if the object is null
         */
        public static <T> boolean nullValue(T t){
            return t == null;
        }

        /**
         * Utility class for comparing sameness
         */
        public static class SAME {

            /**
             * Check if a list of objects are all same
             * @param ts the array of objects to check
             * @param <T> the type parameter of the objects
             * @return true if all of objects contained in {@param ts} are same
             */
            public static <T> boolean value(T... ts){
                if(emptyValue(ts))
                    return false;
                T current = ts[0];
                for(T i : ts){
                    if(!(nullValue(current) ? nullValue(i) : current.equals(i))) return false;
                }
                return true;
            }

            /**
             * Check is a list of {@link String} are all same
             * @param strings the array of {@link String} to check
             * @return true if all the {@link String}'s in {@param strings} are same
             */
            public static boolean stringValue(String... strings){
                if(emptyValue(strings)) return false;
                boolean same = false;
                String current = strings[0];
                for(String i : strings){
                    if(!(nullValue(current) ? nullValue(i) : current.equals(i))) return false;
                }
                return true;
            }

            /**
             * Check is a list of {@link Long} are all same
             * @param longs the array of {@link Long} to check
             * @return true if all the {@link Long}'s in {@param longs} are same
             */
            public static boolean longValue(Long... longs){
                if(emptyValue(longs)) return false;
                Long current = longs[0];
                for(Long i : longs){
                    if(!(nullValue(current) ? nullValue(i) : current.equals(i))) return false;
                }
                return true;
            }

            /**
             * Check is a list of {@link Integer} are all same
             * @param integers the array of {@link Integer} to check
             * @return true if all the {@link Integer}'s in {@param integers} are same
             */
            public static boolean integerValue(Integer... integers){
                if(emptyValue(integers)) return false;
                Integer current = integers[0];
                for(Integer i : integers){
                    if(!(nullValue(current) ? nullValue(i) : current.equals(i))) return false;
                }
                return true;
            }

            /**
             * Check is a list of {@link Float} are all same
             * @param floats the array of {@link Float} to check
             * @return true if all the {@link Float}'s in {@param floats} are same
             */
            public static boolean floatValue(Float... floats){
                if(emptyValue(floats)) return false;
                Float current = floats[0];
                for(Float i : floats){
                    if(!(nullValue(current) ? nullValue(i) : current.equals(i))) return false;
                }
                return true;
            }

            /**
             * Check is a list of {@link Double} are all same
             * @param doubles the array of {@link Double} to check
             * @return true if all the {@link Double}'s in {@param doubles} are same
             */
            public static boolean doubleValue(Double... doubles){
                if(emptyValue(doubles)) return false;
                Double current = doubles[0];
                for(Double i : doubles){
                    if(!(nullValue(current) ? nullValue(i) : current.equals(i))) return false;
                }
                return true;
            }

        }

        public static  class VALID {

            public static  class Patterns {
                public static final Pattern EMAIL = Pattern.compile(
                        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                                "\\@" +
                                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                                "(" +
                                "\\." +
                                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                                ")+"
                );

                public static final Pattern PASSWORD_DEFAULT = Pattern.compile("^[^\\s+]([\\w]{5,19})$");

            }

            /**
             * Check if a string is a valid word (if it includes only alphanumeric characters
             * @param word the word to be checked
             * @param min the minimum amount of characters needed
             * @param max the maximum amount of characters needed
             * @return true if {@param word} is valid
             */
            public static boolean word(String word, int min, int max){
                Pattern spacePattern = Pattern.compile("[\\s]{1,}");
                Pattern wordPattern = Pattern.compile("([\\w]{"+(min)+","+(max)+"})$");
                return word != null && !spacePattern.matcher(word).matches() && wordPattern.matcher(word).matches();
            }

            /**
             * Check if a string is a valid email address
             * @param emailAddress the email address to check
             * @return true if {@param emailAddress} is valid
             */
            public static boolean emailAddress(String emailAddress){
                return !IS.emptyValue(emailAddress) && Patterns.EMAIL.matcher(emailAddress).matches();
            }


        }

        /**
         * Utility class for checking if any object in a group conforms to a condition
         */
        public static  class ANY {

            /**
             * Check if a list of objects contains any null value
             * @param ts the list of objects to check
             * @param <T> the type parameter of the objects
             * @return true if the list {@param ts} contains a null value
             */
            public static <T> boolean nullValue(T... ts){
                if(Value.IS.emptyValue(ts)) return true;
                for(T t : ts)
                    if(Value.IS.<T>nullValue(t)) return true;
                return false;
            }

            /**
             * Check if a list of objects contains any empty value
             * @param ts the list of objects to check
             * @param <T> the type parameter of the objects
             * @return true if the list {@param ts} contains an empty value
             */
            public static <T> boolean emptyValue(T... ts){
                if(Value.IS.emptyValue(ts)) return true;
                for(T t : ts)
                    if(Value.IS.<T>emptyValue(t)) return true;
                return false;
            }

            /**
             * Check if a list of {@link String} contains any empty value
             * @param strings the list of {@link String}'s to check
             * @return true if the list {@param strings} contains an empty value
             */
            public static boolean emptyValue(String... strings){
                if(Value.IS.emptyValue(strings)) return true;
                for(String s : strings)
                    if(Value.IS.emptyValue(s)) return true;
                return false;
            }

            /**
             * Utility class for checking if any object in a group doesn't conform to a condition
             */
            public static  class NOT {

                /**
                 * Check if a list of objects contains at least a non null value
                 * @param ts the list of objects to check
                 * @param <T> the type parameter of the objects
                 * @return true if at list an object is non null
                 */
                public static <T> boolean nullValue(T... ts){
                    if(Value.IS.emptyValue(ts)) return false;
                    for(T t : ts)
                        if(!Value.IS.<T>nullValue(t)) return true;
                    return false;
                }

                /**
                 * Check if a list of objects contains at least a value that isn't empty
                 * @param ts the list of objects to check
                 * @param <T> the type parameter of the objects
                 * @return true if at list an object is not empty
                 */
                public static <T> boolean emptyValue(T... ts){
                    if(Value.IS.emptyValue(ts)) return false;
                    for(T t : ts)
                        if(!Value.IS.<T>emptyValue(t)) return true;
                    return false;
                }

                /**
                 * Check if a list of {@link String} contains at least a value that isn't empty
                 * @param strings the list of {@link String} to check
                 * @return true if at least a {@link String} is not empty
                 */
                public static boolean emptyValue(String... strings){
                    if(Value.IS.emptyValue(strings)) return false;
                    for(String s : strings)
                        if(!Value.IS.emptyValue(s)) return true;
                    return false;
                }
            }

            /**
             * Utility class for checking individual containment
             */
            public static  class HAS {

                /**
                 * Check if any in a list of {@link String} starts with a prefix
                 * @param prefix the prefix to check for
                 * @param strings the list of strings to be checked
                 * @return <code>true</code> if any of the strings starts with {@param prefix}
                 */
                public static boolean start(String prefix, String... strings){
                    for(String string : strings){
                        if(Value.HAS.start(prefix, string)) return true;
                    }
                    return false;
                }

                /**
                 * Check if any in a list of {@link String} ends with a prefix
                 * @param suffix the suffix to check for
                 * @param strings the list of strings to be checked
                 * @return <code>true</code> if any of the strings ends with {@param prefix}
                 */
                public static boolean end(String suffix, String... strings){
                    for(String string : strings){
                        if(Value.HAS.end(suffix, string)) return true;
                    }
                    return false;
                }

            }

        }

    }

    /**
     * Utility class for checking containment
     */
    public static  class HAS {

        /**
         * Check if a {@link String} starts with a prefix
         * @param prefix the prefix to check for
         * @param string the string to be checked
         * @return <code>true</code> if the {@link String} {@param string} starts with {@param prefix}
         */
        public static boolean start(String prefix, String string){
            if(Value.IS.emptyValue(prefix) || Value.IS.emptyValue(string)) return false;
            return string.startsWith(prefix);
        }

        /**
         * Check if a {@link String} ends with a suffix
         * @param suffix the suffix to check for
         * @param string the string to be checked
         * @return <code>true</code> if the {@link String} {@param string} ends with {@param suffix}
         */
        public static boolean end(String suffix, String string){
            if(Value.IS.emptyValue(suffix) || Value.IS.emptyValue(string)) return false;
            return string.endsWith(suffix);
        }

    }

    /**
     * Utility class for value conversion
     */
    public static  class TO {

        /**
         * Get the {@link String} value of an object
         * @param t the object to convert to a {@link String}
         * @param <T> the type parameter of the object
         * @return the {@link String} value of {@param t}
         */
        public static <T> String stringValue(T t){
            return IS.nullValue(t) ? null : String.valueOf(t);
        }

        /**
         * Join a list of items using a delimiter to a string
         * @param delimiter
         * @param tokens
         * @return
         */
        public static String stringValue(CharSequence delimiter, Iterable tokens) {
            StringBuilder sb = new StringBuilder();
            boolean firstTime = true;
            for (Object token: tokens) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(token);
            }
            return sb.toString();
        }

        /**
         * Get the {@link Character} value of an object
         *  Note that this method would only return the first {@link Character} if more than one is found
         * @param t the object to convert to a {@link Character}
         * @param <T> the type parameter of the object
         * @return the {@link Character} value of {@param t}
         */
        public static <T> Character charValue(T t){
            return IS.nullValue(t) ? null : stringValue(t).toCharArray()[0];
        }

        /**
         * Get the {@link Long} value of an object
         * @param t the object to convert to a {@link Long}
         * @param <T> the type parameter of the object
         * @return the {@link Long} value of {@param t}
         */
        public static <T> Long longValue(T t){
            try {
                return IS.nullValue(t) ? null : t instanceof String ? (Long.valueOf((String)t)) :  ((Number) t).longValue();
            }catch (NumberFormatException e){
                return null;
            }
        }

        /**
         * Get the {@link Float} value of an object
         * @param t the object to convert to a {@link Float}
         * @param <T> the type parameter of the object
         * @return the {@link Float} value of {@param t}
         */
        public static <T> Float floatValue(T t){
            try {
                return IS.nullValue(t) ? null : t instanceof String ? (Float.valueOf((String)t)) :  ((Number) t).floatValue();
            }catch (NumberFormatException e){
                return null;
            }
        }

        /**
         * Get the {@link Double} value of an object
         * @param t the object to convert to a {@link Double}
         * @param <T> the type parameter of the object
         * @return the {@link Double} value of {@param t}
         */
        public static <T> Double doubleValue(T t){
            try {
                return IS.nullValue(t) ? null : t instanceof String ? (Double.valueOf((String)t)) :  ((Number) t).doubleValue();
            }catch (NumberFormatException e){
                return null;
            }
        }

        /**
         * Get the {@link Integer} value of an object
         * @param t the object to convert to a {@link Integer}
         * @param <T> the type parameter of the object
         * @return the {@link Integer} value of {@param t}
         */
        public static <T> Integer integerValue(T t){
            try {
                return IS.nullValue(t) ? null : t instanceof String ? (Integer.valueOf((String)t)) :  ((Number) t).intValue();
            }catch (NumberFormatException e){
                return null;
            }
        }

        /**
         * Get the {@link String} value of a field in a {@link JsonObject}
         * @param fieldName the name of the field to check
         * @param o the {@link JsonObject} to check
         * @return the {@link String} value of {@param fieldName} in the {@JsonObject} {@param o}
         */
        public static String stringValue(String fieldName, JsonObject o){
            return IS.nullValue(o.get(fieldName)) || o.get(fieldName).isJsonNull() ? null : o.get(fieldName).getAsString();
        }

        /**
         * Get the {@link Long} value of a field in a {@link JsonObject}
         * @param fieldName the name of the field to check
         * @param o the {@link JsonObject} to check
         * @return the {@link Long} value of {@param fieldName} in the {@JsonObject} {@param o}
         */
        public static Long longValue(String fieldName, JsonObject o){
            return IS.nullValue(o.get(fieldName)) || o.get(fieldName).isJsonNull() ? null : Value.TO.longValue(o.get(fieldName).getAsString());
        }

        /**
         * Get the {@link Double} value of a field in a {@link JsonObject}
         * @param fieldName the name of the field to check
         * @param o the {@link JsonObject} to check
         * @return the {@link Double} value of {@param fieldName} in the {@JsonObject} {@param o}
         */
        public static Double doubleValue(String fieldName, JsonObject o){
            return IS.nullValue(o.get(fieldName)) || o.get(fieldName).isJsonNull() ? null : Value.TO.doubleValue(o.get(fieldName).getAsString());
        }

        /**
         * Get the {@link Float} value of a field in a {@link JsonObject}
         * @param fieldName the name of the field to check
         * @param o the {@link JsonObject} to check
         * @return the {@link Float} value of {@param fieldName} in the {@JsonObject} {@param o}
         */
        public static Float floatValue(String fieldName, JsonObject o){
            return IS.nullValue(o.get(fieldName)) || o.get(fieldName).isJsonNull() ? null : Value.TO.floatValue(o.get(fieldName).getAsString());
        }

        /**
         * Get the {@link Integer} value of a field in a {@link JsonObject}
         * @param fieldName the name of the field to check
         * @param o the {@link JsonObject} to check
         * @return the {@link Integer} value of {@param fieldName} in the {@JsonObject} {@param o}
         */
        public static Integer integerValue(String fieldName, JsonObject o){
            return IS.nullValue(o.get(fieldName)) || o.get(fieldName).isJsonNull() ? null : Value.TO.integerValue(o.get(fieldName).getAsString());
        }

        /**
         * Split a list of objects into smaller lists
         * @param list the list to be split
         * @param limit the number of elements to be in each list.
         * @param <T> the type parameter of the objects in the list
         * @return a list of {@link List}'s each containing objects from {@param list} up to {@param limit}
         */
        public static <T> List<List<T>> splitList(List<T> list, int limit){
            if(Value.IS.emptyValue(list)) return null;
            List<List<T>> lists = new ArrayList<>();
            if(list.size() < limit){
                lists.add(list);
                return lists;
            }
            int remains = list.size() % limit;
            int start = 0;
            int perfectSize = list.size();
            if(remains != 0){
                perfectSize = list.size() - remains;
                lists.add(list.subList(0, remains));
                start = remains;
            }
            int multiples = perfectSize / limit;
            int i = 0;
            while (i < multiples){
                lists.add(list.subList(start, start+=limit));
                i++;
            }
            return lists;
        }

        /**
         * Create a {@link Set} of items from an array of items
         * @param ts the array of items to convert to a {@link Set}
         * @param <T> the type parameter of the items to make a {@link Set} from
         * @return a {@link Set} containing the items from {@param ts}
         */
        public static <T> Set<T> set(T... ts){
            Set<T> set = new HashSet<>(ts.length);
            for(T t : ts)
                set.add(t);
            return set;
        }

        /**
         * Create a {@link List} of {@link String} from an array of {@link String}
         * @param ts the array of {@link String} to make a {@link List} of {@link String} from
         * @return a {@link List} of {@link String} containing the items from {@param ts}
         */
        public static <T> List<String> stringList(T... ts){
            if(Value.IS.emptyValue(ts)) return null;
            List<String> list = new ArrayList<>(ts.length);
            for(T t : ts)
                list.add(Value.TO.stringValue(t));
            return list;
        }

        /**
         * Create a {@link List} of {@link Integer} from an array of {@link Integer}
         * @param ts the array of {@link Integer} to make a {@link List} of {@link Integer} from
         * @return a {@link List} of {@link Integer} containing the items from {@param ts}
         */
        public static <T> List<Integer> integerList(T... ts){
            if(Value.IS.emptyValue(ts)) return null;
            List<Integer> list = new ArrayList<>(ts.length);
            for(T t : ts)
                list.add(Value.TO.integerValue(t));
            return list;
        }

        /**
         * Create a {@link List} of {@link Long} from an array of {@link Long}
         * @param ts the array of {@link Long} to make a {@link List} of {@link Long} from
         * @return a {@link List} of {@link Long} containing the items from {@param ts}
         */
        public static <T> List<Long> longList(T... ts){
            if(Value.IS.emptyValue(ts)) return null;
            List<Long> list = new ArrayList<>(ts.length);
            for(T t : ts)
                list.add(Value.TO.longValue(t));
            return list;
        }

        /**
         * Create a {@link List} of {@link Double} from an array of {@link Double}
         * @param ts the array of {@link Double} to make a {@link List} of {@link Double} from
         * @return a {@link List} of {@link Double} containing the items from {@param ts}
         */
        public static <T> List<Double> doubleList(T... ts){
            if(Value.IS.emptyValue(ts)) return null;
            List<Double> list = new ArrayList<>(ts.length);
            for(T t : ts)
                list.add(Value.TO.doubleValue(t));
            return list;
        }

        /**
         * Create a {@link List} of {@link Float} from an array of {@link Float}
         * @param ts the array of {@link Float} to make a {@link List} of {@link Float} from
         * @return a {@link List} of {@link Float} containing the items from {@param ts}
         */
        public static <T> List<Float> floatList(T... ts){
            if(Value.IS.emptyValue(ts)) return null;
            List<Float> list = new ArrayList<>(ts.length);
            for(T t : ts)
                list.add(Value.TO.floatValue(t));
            return list;
        }

        /**
         * Create a {@link List} of objects from an array of objects
         * @param ts the array of objects to be made into a {@link List}
         * @param <T> the type parameter of the objects
         * @return a {@link List} of objects containing the items from {@param ts}
         */
        public static <T> List<T> list(T... ts){
            if(Value.IS.emptyValue(ts)) return null;
            List<T> list = new ArrayList<>(ts.length);
            for(T t : ts)
                list.add(t);
            return list;
        }

        /**
         * Create an array of objects from a {@link Collection} of objects
         * @param ts the {@link Collection} of objects to be made into an array
         * @param <T> the type parameter of the objects
         * @return an array of objects of type {@param <T>}
         */
        public static <T> T[] array(Collection<T> ts){
            if(Value.IS.emptyValue(ts)) return null;
            Object[] tArray = new Object[ts.size()];
            int i=0;
            Iterator<T> it = ts.iterator();
            while (it.hasNext() && i < ts.size()){
                tArray[i] = it.next();
            }
            return (T[]) tArray;
        }

        /**
         * Return the negative of an {@link Integer}
         * @param i the {@link Integer} value to be negated
         * @return the negative value of {@param i}
         */
        public static Integer negative(Integer i){
            if(i == Integer.MIN_VALUE)
                throw new ArithmeticException("integer overflow");
            return -i;
        }

        /**
         * Return the negative of an {@link Long}
         * @param i the {@link Long} value to be negated
         * @return the negative value of {@param i}
         */
        public static Long negative(Long i){
            if(i == Long.MIN_VALUE)
                throw new ArithmeticException("long overflow");
            return -i;
        }

        /**
         * Return the negative of an {@link Double}
         * @param i the {@link Double} value to be negated
         * @return the negative value of {@param i}
         */
        public static Double negative(Double i){
            if(i == Double.MIN_VALUE)
                throw new ArithmeticException("double overflow");
            return -i;
        }

        /**
         * Return the negative of an {@link Float}
         * @param i the {@link Float} value to be negated
         * @return the negative value of {@param i}
         */
        public static Float negative(Float i){
            if(i == Float.MIN_VALUE)
                throw new ArithmeticException("float overflow");
            return -i;
        }

        /**
         * Utility class for handling JSON items
         */
        public static class JSON {

            /**
             * Convert a list represented as a  json {@link String} to a normal {@link List} of
             * {@link Long}
             * @param string the json {@link String} to be converted
             * @return a new {@link List} containing {@link Long} items from the passed in json string.
             */
            public static List<Long> normalLongList(String string){
                if(IS.emptyValue(string)) return null;
                String[] splits = (string.replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("\\\\", "").replaceAll("\"", "")).split(",");
                if(IS.emptyValue(splits)) return null;
                List<Long> ts = new ArrayList<>();
                for(String item : splits){
                    if(!IS.emptyValue(item))
                        ts.add(Value.TO.longValue(item));
                }
                return ts;
            }

            /**
             * Convert a list represented as a  json {@link String} to a normal {@link List} of
             * {@link String}
             * @param string the json {@link String} to be converted
             * @return a new {@link List} containing {@link String} items from the passed in json string.
             */
            public static List<String> normalStringList(String string){
                if(IS.emptyValue(string)) return null;
                String[] splits = (string.replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("\\\\", "").replaceAll("\"", "")).split(",");
                if(IS.emptyValue(splits)) return null;
                List<String> ts = new ArrayList<>();
                for(String item : splits){
                    if(!IS.emptyValue(item))
                        ts.add(item);
                }
                return ts;
            }

            /**
             * Convert a list represented as a  json {@link String} to a normal {@link Set} of
             * {@link Long}
             * @param string the json {@link String} to be converted
             * @return a new {@link Set} containing {@link Long} items from the passed in json string.
             */
            public static Set<Long> normalLongSet(String string){
                if(IS.emptyValue(string)) return null;
                String[] splits = (string.replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("\\\\", "").replaceAll("\"", "")).split(",");
                if(IS.emptyValue(splits)) return null;
                java.util.Set<Long> ts = new HashSet<>();
                for(String item : splits){
                    if(!IS.emptyValue(item))
                        ts.add(TO.longValue(item));
                }
                return ts;
            }

            /**
             * Convert a list represented as a  json {@link String} to a normal {@link Set} of
             * {@link Long}
             * @param string the json {@link String} to be converted
             * @return a new {@link Set} containing {@link Long} items from the passed in json string.
             */
            public static Set<String> normalStringSet(String string){
                if(IS.emptyValue(string)) return null;
                String[] splits = (string.replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("\\\\", "").replaceAll("\"", "")).split(",");
                if(IS.emptyValue(splits)) return null;
                java.util.Set<String> ts = new HashSet<>();
                for(String item : splits){
                    if(!IS.emptyValue(item))
                        ts.add(item);
                }
                return ts;
            }


        }

    }

    public static  class OMIT {

        /**
         * Removes spaces from a string
         * @param string the string from which spaces should be omitted
         * @return {@param string} with spaces omitted
         */
        public static String space(String string){
            if(Value.IS.emptyValue(string)) return string;
            return string.replaceAll("\\s+", "");
        }

        /**
         * Removes a value from a string
         * @param string the string from which {@param valueToOmit} should be removed
         * @param valueToOmit the string to be ommited from {@param string}
         * @return
         */
        public static String value(String valueToOmit, String string){
            if(Value.IS.ANY.emptyValue(string, valueToOmit)) return string;
            return string.replaceAll(valueToOmit, "");
        }

        /**
         * Removes a value from an array of values of the same type
         * @param valueToOmit the object to be omit from the array
         * @param ts the array of objects to be checked. varargs are supported
         * @param <T> the type parameter of the objecrs
         * @return a new array of type {@param <T>} with value {@param valueToOmit} ommited
         */
        public static <T> T[] value(T valueToOmit, T... ts){
            if(Value.IS.emptyValue(ts)) return ts;
            List<T> l = new ArrayList<>();
            for(T t : ts)
                if(!Value.IS.SAME.value(t, valueToOmit))
                    l.add(t);
            return Value.TO.array(l);
        }

        /**
         * Removes a value from a {@link Collection} of objects
         * @param valueToOmit the object to omit from the list
         * @param ts the {@link Collection} of objects to be checked
         * @param <T> the type parameter of the objects
         * @return the same {@link Collection} of objects passed in excluding {@param valueToOmit}
         */
        public static <T> Collection<T> value(T valueToOmit, Collection<T> ts){
            if(Value.IS.emptyValue(ts)) return ts;
            for(T t : ts)
                if(Value.IS.SAME.value(t, valueToOmit))
                    ts.remove(t);
            return ts;
        }

        /**
         * Removes any null values from a {@link Collection} of objects
         * @param ts the {@link Collection} of objects to check
         * @param <T> the type parameter of the objects
         * @return the same {@link Collection} of objects passed with null values omitted
         */
        public static <T> Collection<T> nullValue(Collection<T> ts){
            return OMIT.value(null, ts);
        }

        /**
         * Removes any null values from an array of objects, varargs supported
         * @param ts the array of objects to check
         * @param <T> the type parameter of the objects
         * @return a new array from {@param ts} with null values omitted
         */
        public static <T> T[] nullValue(T... ts){
            return OMIT.value(null, ts);
        }

    }

    /**
     * Utility class to find the ith occurrence of a value in a string
     */
    public static  class FIND {

        /**
         * Find a {@link Integer} occurring at a specified position in a string. Position starts from
         * index 0
         * @param string the string to work with
         * @param positionOfInteger the position of the {@link Integer} to be found
         * @return
         */
        public static Integer integerValueOccuringAt(String string, int positionOfInteger){
            if(Value.IS.emptyValue(string)) return null;
            List<Integer> integers = TO.integerList(string.replaceAll("[^0-9]+", " ").trim().split(" "));
            return positionOfInteger < integers.size() ? integers.get(positionOfInteger) : null;
        }

        /**
         * Find a {@link Long} occurring at a specified position in a string. Position starts from
         * index 0
         * @param string the string to work with
         * @param positionOfLong the position of the {@link Long} to be found
         * @return
         */
        public static Long longValueOccurringAt(String string, int positionOfLong){
            if(Value.IS.emptyValue(string)) return null;
            List<Long> longs = TO.longList(string.replaceAll("[^0-9]+", " ").trim().split(" "));
            return positionOfLong < longs.size() ? longs.get(positionOfLong) : null;
        }

        /**
         * Find a {@link Float} occurring at a specified position in a string. Position starts from
         * index 0
         * @param string the string to work with
         * @param positionOfFloat the position of the {@link Float} to be found
         * @return
         */
        public static Float floatValueOccuringAt(String string, int positionOfFloat){
            if(Value.IS.emptyValue(string)) return null;
            List<Float> floats = TO.floatList(string.replaceAll("[^0-9]+", " ").trim().split(" "));
            return positionOfFloat < floats.size() ? floats.get(positionOfFloat) : null;
        }

        /**
         * Find a {@link Double} occurring at a specified position in a string. Position starts from
         * index 0
         * @param string the string to work with
         * @param positionOfDouble the position of the {@link Double} to be found
         * @return
         */
        public static Double doubleValueOccuringAt(String string, int positionOfDouble){
            if(Value.IS.emptyValue(string)) return null;
            List<Double> doubles = TO.doubleList(string.replaceAll("[^0-9]+", " ").trim().split(" "));
            return positionOfDouble < doubles.size() ? doubles.get(positionOfDouble) : null;
        }

    }

    /**
     * Class for replacing a number of elements in a string with a different string
     */
    public static class REPLACE{

        public static String fromStart(String value, String replaceWith, int count){
            if(Value.IS.emptyValue(value)) return value;
            String replacement = String.format("%0" + count + "d", 0).replace("0", replaceWith);
            return value.replaceFirst("[\\w+]{"+count+"}", replacement);
        }

        public static String fromEnd(String value, String replaceWith, int count){
            if(Value.IS.emptyValue(value)) return value;
            int rem = value.length() - count;
            String replacement = String.format("%0" + rem + "d", 0).replace("0",replaceWith);
            return value.replaceFirst("[\\w+]{"+rem+"}", replacement);
        }
    }

    public static class Random extends java.util.Random {

        public static String UUID(){
            return UUID.randomUUID().toString();
        }

        public static String UUIDClear(){
            return OMIT.value("-", UUID());
        }

    }

}