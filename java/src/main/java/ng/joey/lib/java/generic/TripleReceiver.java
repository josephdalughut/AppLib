package ng.joey.lib.java.generic;

/**
 * A simple generic interface that retrieves three different data types
 * @param <T> the first data type to be retrieved
 * @param <U> the second data type to be retrieved
 * @param <V> the thrid data type to be retrieved
 */
public interface TripleReceiver<T, U, V> {
    void onReceive(T t, U u, V v);
    void onReceive1(T t);
    void onReceive2(U u);
    void onReceive3(V v);
}
