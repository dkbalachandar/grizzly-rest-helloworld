package com.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/greeter")
public class GreetingResource {

    private static final String HELLO_MESSAGE= "Welcome %s";

    private static final String BIRTHDAY_MESSAGE= "Happy Birthday %s";

    @Context
    private Request request;

    @GET
    @Path("welcome")
    @Produces(MediaType.TEXT_HTML)
    public Response sayWelcomeMessage(@QueryParam("name") String name) {
        System.out.println("sayWelcomeMessage:::::"+ System.currentTimeMillis());
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(60);
        cacheControl.setPrivate(true);
        //Dont store the content
        cacheControl.setNoStore(true);
        Response.ResponseBuilder builder = Response.ok(String.format(HELLO_MESSAGE, name));
        builder.cacheControl(cacheControl);
        return builder.build();
    }

    @GET
    @Path("bday")
    @Produces(MediaType.TEXT_HTML)
    public Response sayBdayWishesMessage(@QueryParam("name") String name) {

        System.out.println("sayGreetingMessage:::::"+ System.currentTimeMillis());
        CacheControl cacheControl = new CacheControl();
        //60 seconds
        cacheControl.setMaxAge(60);

        String message = String.format(BIRTHDAY_MESSAGE, name);
        EntityTag entityTag = new EntityTag(Integer.toString(message.hashCode()));
        //Browser sends ETag and If-Modified-Since headers. So we can use this and
        // find out whether the response is expired or not
        Response.ResponseBuilder builder = request.evaluatePreconditions(entityTag);
        // If the build is null, then the content dont match, so the response should be sent
        if (builder == null) {
            builder = Response.ok(message);
            builder.tag(entityTag);
        } else {
            builder.cacheControl(cacheControl).tag(entityTag);
        }
        return builder.build();
    }
}
