package ng.joey.lib.rest.util;

import com.google.api.server.spi.response.ServiceUnavailableException;
import com.googlecode.objectify.Work;
import ng.joey.lib.java.util.Value;
import ng.joey.lib.rest.service.Persistence;

import java.util.ConcurrentModificationException;
import java.util.logging.Logger;

/**
 * Created by Joey Dalughut on 8/9/16 at 6:54 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 *
 * A class used to simplify the use of Objectify transactions
 */
public class Transactor<T> {

    public static Logger logger = Logger.getLogger(Transactor.class.getName());

    public static interface Getter<T> {
        T onRetrieve();
    }
    public static interface Runner {
        void onConsume();
    }

    private Getter<T> workGetter;
    private Runner workRunner;
    private int workTries = 5, workTry = 0;

    /**
     * Set the interface through which the task to be run is outlined
     * @param workGetter the interface through which the task to be run is outlined
     * @return self
     * @see Getter
     */
    public Transactor<T> withWorkGetter(Getter<T> workGetter){
        this.workGetter = workGetter;
        return this;
    }

    /**
     * Set the interface through which the task to be run is outlined
     * @param workRunner the interface through which the task to be run is outlined
     * @return self
     * @see Runner
     */
    public Transactor<T> withWorkRunner(Runner workRunner){
        this.workRunner = workRunner;
        return this;
    }

    /**
     * Set the number of times this task would be retried upon concurrency issues
     * @param limit the number of times this task would be retried upon concurrency issues
     * @return self
     */
    public Transactor<T> withWorkLimit(int limit){
        this.workTries = limit;
        return this;
    }


    /**
     * Start this {@link Transactor}'s task.
     * @throws ConcurrentModificationException if an entity couldn't be modified by this task due to
     * concurrency issues.
     */
    public void run() throws ServiceUnavailableException {
        logger.info("Starting transactor work");
        incrementWorkTry();
        try {
            Persistence.ofy().transactNew(1, new Work<Object>() {
                @Override
                public Object run() {
                    logger.info("Transactor work has called run");
                    workRunner.onConsume();
                    return null;
                }
            });
        }catch (ConcurrentModificationException e){
            if(Value.IS.SAME.integerValue(getCurrentWorkTry(), getWorkTries())){
                throw new ServiceUnavailableException("service : unavailable");
            }else{
                run();
            }
        }
    }

    /**
     * Start this {@link Transactor}'s task and return it's defined return type.
     * @return the result from this task
     * @throws ConcurrentModificationException if an entity couldn't be modified by this task due to
     * concurrency issues
     */
    public T get() throws ServiceUnavailableException{
        incrementWorkTry();
        try{
            return Persistence.ofy().transactNew(1, new Work<T>() {
                @Override
                public T run() {
                    return workGetter.onRetrieve();
                }
            });
        }catch (ConcurrentModificationException e){
            if(Value.IS.SAME.integerValue(getCurrentWorkTry(), getWorkTries())){
                throw new ServiceUnavailableException("service : unavailable");
            }else{
                return get();
            }
        }
    }

    /**
     * Get the number of times this task would be retried
     * @return the number of times this work would be retried
     */
    private int getWorkTries(){
        return this.workTries;
    }

    /**
     * Get the current retry count for this task
     * @return the current retry count for this task
     */
    private int getCurrentWorkTry(){
        return this.workTry;
    }

    /**
     * Increase the current work try
     */
    private void incrementWorkTry(){
        workTry++;
    }
}

