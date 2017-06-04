package ng.joey.lib.java.generic;

/**
 * A simple Generic Receiver interface which can be implemented to pass down two values of type {@param <T>} and {@param <V>}
 * @param <T> the first value type of this interface instance
 * @param <V> the second value type of this interface instance
 */
public interface DoubleReceiver<T, V> {

    void onReceive(T t, V v);
    void onReceive1(T t);
    void onReceive2(V v);

}
