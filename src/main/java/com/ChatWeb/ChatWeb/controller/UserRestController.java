package com.ChatWeb.ChatWeb.controller;
import com.ChatWeb.ChatWeb.dao.CassandraConnexion;
import com.ChatWeb.ChatWeb.dao.User;
import org.json.JSONException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/User")
public class UserRestController extends CassandraConnexion {
    private final UserMid mid = new UserMid();
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String Login(@FormParam("email") String email , @FormParam("psd") String pass) throws JSONException
    {
        return  mid.login(email,pass);
    }
    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/logout")
    public String logout(@FormParam("token") String token) throws JSONException
    {
        mid.logout(token);
        return "";
    }
    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/infoUser")
    public String infoUser(@FormParam("token") String token) throws JSONException
    {
        return mid.getinfoUser(token);
    }
    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/CreatPost")
    public String CreatPost(@FormParam("token") String token, @FormParam("text") String text) throws JSONException
    {
        return mid.creatpost(token,text);
    }









    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/register")
    public String Register(@FormParam("email") String email, @FormParam("psd") String pass) throws JSONException
    {
        User u = new User(email,pass);
        return u.register().toString();
    }
    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/follow")
    public String follow(@FormParam("email") String email, @FormParam("following") String following) throws JSONException
    {
        User u = new User(email);
        return u.follow(following).toString();
    }
    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/unfollow")
    public String unfollow(@FormParam("email") String email, @FormParam("following") String following) throws JSONException
    {
        User u = new User(email);
        return u.unfollow(following).toString();
    }

    @POST @Produces(MediaType.APPLICATION_JSON) @Path("/comment")
    public String comment(@FormParam("pubemail") String pubemail, @FormParam("date") String date, @FormParam("email") String email, @FormParam("text") String text) throws JSONException
    {
        User u = new User(email);
        return u.comment(pubemail,date,text).toString();
    }
}