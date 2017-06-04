package ng.joey.lib.java.generic;

/**
 * A simple generic interface that retrieves three different data types
 * @param <T> the first data type to be retrieved
 * @param <U> the second data type to be retrieved
 * @param <V> the thrid data type to be retrieved
 */
public interface TripleRetriever<T, U, V>{
    Object[] onRetrieve();
    T onRetrieve1();
    U onRetrieve2();
    V onRetrieve3();
}
