package com.ChatWeb.ChatWeb.controller;

import com.ChatWeb.ChatWeb.dao.CassandraConnexion;
import com.ChatWeb.ChatWeb.dao.User;
import com.datastax.driver.core.Row;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserMid extends CassandraConnexion {

    synchronized public String login(String email, String pass){
        User u = new User(email,pass);
        JSONObject json = u.login();
        int code = (int)json.get("msg");
        if(code == 0) {
            String token = UUID.randomUUID().toString();
            Creattoken(email,token);
            json.put("token",token);
        }
        return json.toString();
    }

    public void logout(String token) {
        this.getSession().execute("delete from tokens where id = ? ;",token);
    }

    public String getinfoUser(String token) {
        User u = getUser(token);
        if(u!=null)
            return u.getinfo().toString();
        else
            return "nothing to do";
    }

    public User getUser(String token){
        List<Row> result = this.getSession().execute("SELECT * FROM tokens where id = ? ;",token).all();
        if(!result.isEmpty())
            return new User(result.get(0).getString("email"));
        else
            return null;
    }
    public void Creattoken(String email,String token)
    {
        this.getSession().execute("insert into tokens(id,email) values(?,?) ;",token,email);
    }


    public String creatpost(String token,String text) {
        User u = getUser(token);
        if(u!=null) {
            u.pub(text);
        }
            return "";
    }
}
