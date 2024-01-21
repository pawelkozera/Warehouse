package org.client.Authorization;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String password;
    private String position;
    private String name;
    private String surname;
    private int id;
    private UserSession() {
    }
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    public String getUsername() {
        return username;
    }
    public int getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public String getPosition() {
        return position;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }

    public void setUsername(String username) {
        this.username=username;
    }
    public void setId(int id) {
        this.id=id;
    }
    public void setPosition(String position) {
        this.position=position;
    }
    public void setPassword(String password) {
        this.password=password;
    }
    public void setName(String name) {
        this.name=name;
    }
    public void setSurname(String surname) {
        this.surname=surname;
    }
}
