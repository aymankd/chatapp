package com.ChatWeb.ChatWeb.controller;

import com.ChatWeb.ChatWeb.dao.CassandraConnexion;
import com.ChatWeb.ChatWeb.dao.User;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserMid extends CassandraConnexion {
    Session session = this.getSession();
    User u = new User();
    synchronized public String login(String email, String pass){
        JSONObject json = u.login(email,pass);
        int code = (int)json.get("msg");
        if(code == 0) {
            String token = UUID.randomUUID().toString();
            Creattoken(email,token);
            json.put("token",token);
        }
        return json.toString();
    }

    public void logout(String token) {
        session.execute("delete from tokens where id = ? ;",token);
    }

    public String getinfoUser(String token) {
        String email = getemail(token);
        if(email!=null)
            return u.getinfo(email).toString();
        else
            return "nothing to do";
    }

    public String getemail(String token){
        List<Row> result = session.execute("SELECT * FROM tokens where id = ? ;",token).all();
        if(!result.isEmpty())
            return result.get(0).getString("email");
        else
            return null;
    }
    public void Creattoken(String email,String token)
    {
        session.execute("insert into tokens(id,email) values(?,?) ;",token,email);
    }

    public String Search(String token,String keyword){
        String mail = getemail(token);
        if(mail!=null){
            return u.Search(keyword,mail).toString();
        }
            return "";
    }

    public String CreatPost(String token,String text) {
        String email = getemail(token);
        if(email!=null){
            u.pub(text,email);
        }
            return "";
    }

    public String getNews(String token) {
        String email = getemail(token);
        if(email!=null){
            return u.GetNews(email).toString();
        }
        return "";
    }

    public String follow(String token, String following) {
        String email = getemail(token);
        if(email!=null){
            return u.follow(following,email).toString();
        }
        return "";
    }

    public String unfollow(String token, String following) {
        String email = getemail(token);
        if(email!=null){
            return u.unfollow(following,email).toString();
        }
        return "";
    }
}
