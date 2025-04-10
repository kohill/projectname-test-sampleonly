package com.companyname.common.data.login;

import com.companyname.common.data.DataList;

import java.util.List;

public class UserList implements DataList {

    List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public User get(String user) {
        return users.stream().filter(u -> u.getUser().equals(user)).findFirst().orElse(null);
    }
}
