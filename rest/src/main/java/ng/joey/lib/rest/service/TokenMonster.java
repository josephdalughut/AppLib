package ng.joey.lib.rest.service;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.QueryKeys;
import ng.joey.lib.java.util.Time;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Joey Dalughut on 8/9/16 at 1:14 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class TokenMonster extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) {
        if("clear".equals(request.getQueryString())) {
            this.clearAll(response);
        }else{
            postBadRequest(response);
        }
    }

    private void clearAll(HttpServletResponse response) {
        Long now = Time.now();
        int clearCount = 0;
        boolean more = true;
        while (more){
            QueryKeys<ng.joey.lib.rest.entity.Token> queryKeys = Persistence.ofy().load().type(ng.joey.lib.rest.entity.Token.class)
                    .filter(ng.joey.lib.rest.entity.Token.Constants.Fields.EXPIRES_AT +" <", now)
                    .limit(100).keys();
            QueryResultIterator<Key<ng.joey.lib.rest.entity.Token>> keyIterator = queryKeys.iterator();
            List<Key<ng.joey.lib.rest.entity.Token>> keyList = new ArrayList<>();
            while (keyIterator.hasNext())
                keyList.add(keyIterator.next());
            more = (keyList.size() == 100);
            clearCount+=keyList.size();
            Persistence.ofy().delete().keys(keyList).now();
        }
        postSuccess(response, clearCount);
    }

    private void postBadRequest(HttpServletResponse response){
        try {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            writer.println("<h1>400 : Bad request</h1>");
            writer.close();
            writer.flush();
        }catch (IOException e){

        }
    }

    private void postSuccess(HttpServletResponse response, int count){
        try {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = response.getWriter();
            writer.println("<h1>Cleared "+count+" tokens! </h1>");
            writer.close();
            writer.flush();
        }catch (IOException e){

        }

    }

}
