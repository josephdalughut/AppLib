package ng.joey.lib.rest.service;

import com.google.appengine.api.ThreadManager;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Joey Dalughut on 8/9/16 at 1:36 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class EntityRegistrar implements ServletContextListener {

    private static Logger logger = Logger.getLogger(ServletContextListener.class.getName());

    private final Set<Class<?>> entities;

    public EntityRegistrar()
    {
        this.entities = new HashSet<>();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUrls(ClasspathHelper.forPackage(""));
        final ExecutorService es = Executors.newCachedThreadPool(ThreadManager.currentRequestThreadFactory());
        cb.setExecutorService(es);
        final Reflections r = new Reflections(cb);
        this.entities.addAll(r.getTypesAnnotatedWith(Entity.class));
        es.shutdown();
        for (final Class<?> cls : this.entities)
        {
            ObjectifyService.register(cls);
            logger.info("Registered "+ cls.getName()+" class with objectify");
        }
        //AdminAPI.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
