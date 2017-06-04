package ng.joey.lib.java.generic;

/**
 * A simple generic interface that's able to collect up to four different data types
 * @param <T> the first data type
 * @param <U> the second data type
 * @param <V> the third data type
 * @param <W> the fourth data type
 */
public interface QuatroReceiver<T, U, V, W> {
    void onReceive(T t, U u, V v, W w);
    void onReceive1(T t);
    void onReceive2(U u);
    void onReceive3(V v);
    void onReceive4(W w);
}
