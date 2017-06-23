package ng.joey.lib.rest.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by root on 5/18/17.
 */

public abstract class EndpointServlet extends HttpServlet {

    public String getApiMethodName(HttpServletRequest request){
        String  url = request.getRequestURL().toString().replace(getServletUrl(), "");
        if(url.endsWith("/"))
            url = url.substring(url.length()-1);
        return url;
    }

    public abstract String getServletUrl();

}
