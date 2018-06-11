package com.yourwelcome.czirjak.firebasefriends;

public class User {

    private String name;
    private String profession;

    public User(String name, String profession) {
        this.name = name;
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
