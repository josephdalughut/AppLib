package ng.joey.lib.java.generic;

/**
 * A simple generic Consumer interface which can collect an infinite ammount of a data type and return another data type
 * @param <T> the data type to be returned
 * @param <V> the data type to be consumed
 */
public interface VariableConsumer<T, V> {
    T onConsume(V... vargs);
}
