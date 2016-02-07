package com.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/greeting")
public class HelloWorldResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello(){
        return Response.ok().entity("\"Hello World\"").build();
    }
}
