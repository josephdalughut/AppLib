package ng.joey.lib.java.generic;

/**
 * Created by joey on 6/16/17.
 */

public interface BiReceiver<T, U> {

    public void onReceive1(T t);
    public void onReceive2(U u);
}
