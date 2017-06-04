package ng.joey.lib.java.generic;

/**
 * A simple generic interface that retrieves a value
 * @param <T> the data type to be retrieved
 */
public interface Retriever<T> {
    T OnRetrieve();
}
