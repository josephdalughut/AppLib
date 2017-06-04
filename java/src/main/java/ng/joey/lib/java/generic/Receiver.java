package ng.joey.lib.java.generic;

/**
 * A simple Generic Receiver interface which can be implemented to pass down a value of type {@param <T>}
 * @param <T> the value type of this interface instance
 */
public interface Receiver<T> {

    /**
     * Passes down a value to a class which implements this interface
     * @param t the value to pass down
     */
    void onReceive(T t);

}
