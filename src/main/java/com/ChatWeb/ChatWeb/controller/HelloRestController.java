package com.ChatWeb.ChatWeb.controller;
import com.ChatWeb.ChatWeb.dao.CassandraConnexion;
import com.datastax.driver.core.Session;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class HelloRestController extends CassandraConnexion {
    private final Session session;
    public HelloRestController(){
        session = this.getSession();
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!!!!!!!!!!";
    }

    @POST
    @Path("/test2")
    @Produces(MediaType.APPLICATION_JSON)
    public String hello2(@FormParam("email") String email , @FormParam("psd") String pass) {
        JSONObject obj = new JSONObject();
        obj.put("msg","email : "+email+" // pass : "+pass);
        return obj.toString();
    }


}