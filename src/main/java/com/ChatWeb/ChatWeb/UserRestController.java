package com.ChatWeb.ChatWeb;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/User")
public class UserRestController extends CassandraConnexion {
    private final Session session;
    public UserRestController(){
        session = this.getSession();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String UserLogin(@FormParam("email") String email ,@FormParam("psd") String pass) throws JSONException
    {
        User u = new User(email,pass);
        return u.login();
    }

    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/register")
    public String UserRegister(@FormParam("email") String email, @FormParam("psd") String pass) throws JSONException
    {
        User u = new User(email,pass);
        return u.register();
    }
   
}