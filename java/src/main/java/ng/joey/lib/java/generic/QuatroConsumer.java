package ng.joey.lib.java.generic;

/**
 * A simple generic interface able to collect four different data types and return one
 * @param <T> the data type to be returned
 * @param <U> the first data type to be consumed
 * @param <V> the second data type to be consumed
 * @param <W> the third data type to be consumed
 * @param <X> the fourth data type to be consumed
 */
public interface QuatroConsumer<T, U, V, W, X> {
    T consume(U u, V v, W w, X x);
    T consume1(U u);
    T consume2(V v);
    T consume3(W w);
    T consume4(X x);
}
