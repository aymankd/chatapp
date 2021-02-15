package com.ChatWeb.ChatWeb.dao;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class User extends CassandraConnexion {
    final Session session = this.getSession();

    public JSONObject login(String email, String pass) {
        JSONObject obj = new JSONObject();
        List<Row> result = session.execute("SELECT * FROM user where email = ? ;", email).all();
        if(result.isEmpty())
            obj.put("msg", 1);
        else {
            String password = result.get(0).getString("password");
            if(password.equals(pass))
                obj.put("msg",0);
            else
                obj.put("msg",2);
        }
        return obj;
    }
    public JSONObject register(String email,String psd) {
        JSONObject obj = new JSONObject();
        Boolean emailexestence = session.execute("SELECT * FROM user where email = ? ;",email).all().isEmpty();
        if(emailexestence) {
            session.execute("insert into user(email,password) values(?,?);",email,psd);
            obj.put("msg","compte crée avec succée");
        }else
            obj.put("msg","email déja existant");
        return obj;
    }
    public JSONObject getinfo(String email) {
        JSONObject obj = new JSONObject();
        List<Row> result = session.execute("SELECT * FROM user where email =? ;",email).all();
        if(!result.isEmpty()) {
            obj.put("username", result.get(0).getString("username"));
            obj.put("name", result.get(0).getString("nom")+" "+result.get(0).getString("prenom"));
        }
        return obj;
    }

    public boolean isfollowing(String following,String email){
        JSONObject obj = new JSONObject();
        List<Row> result = session.execute("SELECT * FROM follows where follower = ? and following = ? ;",email,following).all();
        return !result.isEmpty();
    }

     public JSONObject Search(String keyword, String email){
        JSONObject obj = new JSONObject();
        List<Row> result = session.execute("SELECT email,username,nom,prenom FROM user where username like ?;",keyword+"%").all();
        if(!result.isEmpty()) {
            obj.put("msg", 0);
            JSONArray arr = new JSONArray();
            for (Row row:result)
            if(!row.getString(0).equals(email)) {
                JSONObject r = new JSONObject();
                r.put("email", row.getString(0));
                r.put("username", row.getString(1));
                r.put("nom", row.getString(2));
                r.put("prenom", row.getString(3));
                r.put("follow",isfollowing(row.getString(0),email));
                arr.put(r);
            }

            obj.put("result",arr);
        }else
            obj.put("msg", 1);
        return obj;
    }

    public JSONObject follow(String following, String email)
    {
        JSONObject obj = new JSONObject();
        List<Row> result = session.execute("SELECT * FROM follows where follower = ? and following = ?  ;",email,following).all();
        if(result.isEmpty())
        {
            session.execute("insert into follows(follower,following) values(?,?);",email,following);
            obj.put("msg", 0);
        }else {
            obj.put("msg", 1);
        }
        return obj;
    }

    public JSONObject unfollow(String following, String email) {
        JSONObject obj = new JSONObject();
        List<Row> result = session.execute("SELECT * FROM follows where follower = ? and following = ? ;",email,following).all();
        if(!result.isEmpty())
        {
            session.execute("delete from follows where follower = ? and following = ? ;",email,following);
            obj.put("msg", 0);
        }else {
            obj.put("msg", 1);
        }
        return obj;
    }

    public void pub(String text, String email) {
        this.getSession().execute("insert into pub(email,text,date,id) values(?,?,toTimestamp(now()),now());",email,text);
    }

    public JSONObject GetNews(String email){
        JSONObject obj = new JSONObject();
        List<Row> result = this.getSession().execute("SELECT following FROM follows where follower = ? ALLOW FILTERING ;",email).all();
        if(!result.isEmpty()){
            obj.put("msg", 0);
            List<String> l = new ArrayList();
            for (Row row:result)
                l.add(row.getString(0));
            l.add(email);
            JSONArray arr = new JSONArray();
            if(!l.isEmpty()) {
                Statement statement = new SimpleStatement("SELECT * FROM pub where email in ? order by date desc limit 5 ; ", l);
                statement.setFetchSize(Integer.MAX_VALUE);
                List<Row> result2 = this.getSession().execute(statement).all();
                for (Row row:result2){
                    JSONObject r = new JSONObject();
                    r.put("poster", getinfo(row.getString(0)).get("username"));
                    r.put("date", row.getTimestamp(1).getTime());
                    r.put("dislike", row.getList(2,String.class));
                    r.put("id", row.getUUID(3));
                    r.put("like", row.getList(4,String.class));
                    r.put("text",row.getString(5));
                    UUID pubid = row.getUUID(3);
                    JSONArray cmtarr = new JSONArray();
                    List<Row> cmtres = this.getSession().execute("select text,writer from comments where pubid = ? ;",pubid).all();
                    for (Row comment:cmtres){
                        JSONObject com = new JSONObject();
                        com.put("text",comment.getString(0));
                        com.put("writer",comment.getString(1));
                        cmtarr.put(com);
                    }
                    r.put("comments",cmtarr);
                    arr.put(r);
                }
            }
            obj.put("result",arr);
        }else
            obj.put("msg", 1);
        return obj;
    }

    public JSONObject comment(String pubeid,String date,String text,String email){
        JSONObject obj = new JSONObject();
        this.getSession().execute("insert into comments(pubeid,date,id,text,writer) values(?,toTimestamp(now()),uuid(),?,?);",pubeid,text,email);
        obj.put("msg", "comment creation succeed");
        return obj;
    }
}