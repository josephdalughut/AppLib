package ng.joey.lib.java.generic;

/**
 * Created by joey on 6/16/17.
 */

public interface TriReceiver<T, U, V> {

    public void onReceive1(T t);
    public void onReceive2(U u);
    public void onReceive3(V v);
}
