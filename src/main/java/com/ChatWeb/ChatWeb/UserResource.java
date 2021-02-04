package com.ChatWeb.ChatWeb;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/User")
public class UserResource extends CassandraConnexion {
    private final Session session;

    public UserResource(){
        session = this.getSession();
    }
    @GET @Produces("text/plain") @Path("/test")
    public String hello() {
        return "Hello, World!";
    }


    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/login")
    public String UserLogin(@HeaderParam("email") String email ,@HeaderParam("psd") String pass)
    {
        List<Row> result = session.execute("SELECT * FROM user where email = ? ;",email).all();
        if(result.isEmpty())
            return "email n'existe pas";
        else
        {
            String password = result.get(0).getString("password");
            if(password.equals(pass))
                return "connexion etablie avec succé";
            return "mot de pass erroné ";
        }
    }


    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/register")
    public String UserRegister(@HeaderParam("email") String email,@HeaderParam("psd") String pass)
    {
        Boolean emailexestence = session.execute("SELECT * FROM user where email = ? ;",email).all().isEmpty();
        if(emailexestence)
        {
            session.execute("insert into user(email,password) values(?,?);",email,pass);
            return "compte crée avec succé";
        }
        else
            return "email déja existant";
    }

}