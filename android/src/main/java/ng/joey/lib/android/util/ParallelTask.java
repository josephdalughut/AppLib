package ng.joey.lib.android.util;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

public abstract class ParallelTask<T, U, V> extends AsyncTask<T, U, V> {

    public ParallelTask execute(){
        AsyncTaskCompat.executeParallel(this);
        return this;
    }

}
