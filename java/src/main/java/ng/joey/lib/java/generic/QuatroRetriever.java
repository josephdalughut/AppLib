package ng.joey.lib.java.generic;

/**
 * A simple generic interface that's able to collect up to four different data types
 * @param <T> the first data type
 * @param <U> the second data type
 * @param <V> the third data type
 * @param <W> the fourth data type
 */
public interface QuatroRetriever<T, U, V, W> {
    Object[] onRetrieve();
    T onRetrieve1();
    U onRetrieve2();
    V onRetrieve3();
    W onRetrieve4();

}
