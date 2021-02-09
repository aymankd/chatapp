package com.ChatWeb.ChatWeb;
import com.datastax.driver.core.Row;
import org.json.JSONObject;
import java.util.List;
public class User extends CassandraConnexion {
    String email;
    String psd;
    public User(String e,String p)
    {
        email = e;
        psd = p;
    }
    public String login()
    {
        JSONObject obj = new JSONObject();
        List<Row> result = this.getSession().execute("SELECT * FROM user where email = ? ;",email).all();
        if(result.isEmpty())
            obj.put("msg", "email n'existe pas");
        else
        {
            String password = result.get(0).getString("password");
            if(password.equals(psd))
                obj.put("msg","connexion etablie avec succé");
            else
                obj.put("msg","mot de pass erroné");
        }
        return obj.toString();
    }
    public String register()
    {
        JSONObject obj = new JSONObject();
        Boolean emailexestence = this.getSession().execute("SELECT * FROM user where email = ? ;",email).all().isEmpty();
        if(emailexestence)
        {
            this.getSession().execute("insert into user(email,password) values(?,?);",email,psd);
            obj.put("msg","compte crée avec succé");
        }else
            obj.put("msg","email déja existant");
        return obj.toString();
    }
}