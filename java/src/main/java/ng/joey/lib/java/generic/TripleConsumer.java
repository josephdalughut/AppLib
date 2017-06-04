package ng.joey.lib.java.generic;

/**
 * A simple generic interface that returns a value by consuming three different data types
 * @param <T> the data type to be returned
 * @param <U> the first data type to be consumed
 * @param <V> the second data type to be consumed
 * @param <W> the third data type to be consumed
 */
public interface TripleConsumer<T, U, V, W> {
    T onConsume(U u, V v, W w);
    T onConsume1(U u);
    T onConsume2(V v);
    T onConsume3(W w);
}
