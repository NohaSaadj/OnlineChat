package com.example.onlinechat;

public class User {
    String id;
    String name;
    String email;
    String password;
    String cPassword;
    private  User(){}
    User(String id, String name, String email , String password , String cPassword ){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cPassword = cPassword;


    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCPassword() {
        return cPassword;
    }

}
