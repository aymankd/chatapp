package com.ChatWeb.ChatWeb.dao;
import com.ChatWeb.ChatWeb.dao.CassandraConnexion;
import com.datastax.driver.core.Row;
import org.json.JSONObject;
import java.util.List;
public class User extends CassandraConnexion {
    public String email;
    public String psd;

    public User(String e,String p)
    {
        email = e;
        psd = p;
    }
    public User(String e)
    {
        email = e;
    }
    public String getemail(){
        return email;
    }

    public JSONObject login()
    {
        JSONObject obj = new JSONObject();
        List<Row> result = this.getSession().execute("SELECT * FROM user where email = ? ;",email).all();
        if(result.isEmpty())
            obj.put("msg", 1);
        else
        {
            String password = result.get(0).getString("password");
            if(password.equals(psd))
                obj.put("msg",0);
            else
                obj.put("msg",2);
        }
        return obj;
    }
    public JSONObject register()
    {
        JSONObject obj = new JSONObject();
        Boolean emailexestence = this.getSession().execute("SELECT * FROM user where email = ? ;",email).all().isEmpty();
        if(emailexestence)
        {
            this.getSession().execute("insert into user(email,password) values(?,?);",email,psd);
            obj.put("msg","compte crée avec succée");
        }else
            obj.put("msg","email déja existant");
        return obj;
    }
    public JSONObject getinfo()
    {
        JSONObject obj = new JSONObject();
        List<Row> result = this.getSession().execute("SELECT * FROM user where email =? ;",email).all();
        if(!result.isEmpty())
        {
            obj.put("username", result.get(0).getString("username"));
            obj.put("name", result.get(0).getString("nom")+" "+result.get(0).getString("prenom"));
        }
        return obj;
    }
    public JSONObject follow(String following)
    {
        JSONObject obj = new JSONObject();
        List<Row> result = this.getSession().execute("SELECT * FROM follows where follower = ? and following = ?  ;",email,following).all();
        if(result.isEmpty())
        {
            this.getSession().execute("insert into follows(follower,following) values(?,?);",email,following);
            obj.put("msg", "follow succeed");
        }else {
            obj.put("msg", "you already follow this account");
        }
        return obj;
    }

    public JSONObject unfollow(String following) {
        JSONObject obj = new JSONObject();
        List<Row> result = this.getSession().execute("SELECT * FROM follows where follower = ? and following = ? ;",email,following).all();
        if(!result.isEmpty())
        {
            this.getSession().execute("delete from follows where follower = ? and following = ? ;",email,following);
            obj.put("msg", "unfollow succeed");
        }else {
            obj.put("msg", "you are not following this account");
        }
        return obj;
    }

    public void pub(String text)
    {
        this.getSession().execute("insert into pub(email,date,text) values(?,toTimestamp(now()),?);",email,text);
    }

    public JSONObject comment(String pubemail,String date,String text){
        JSONObject obj = new JSONObject();
        this.getSession().execute("insert into comments(pubemail,date,id,text,writer) values(?,'"+date+"',uuid(),?,?);",pubemail,text,email);
        obj.put("msg", "comment creation succeed");
        return obj;
    }

}