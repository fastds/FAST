package org.fastds.service;

import org.fastds.dao.UserDao;
import org.fastds.model.User;

public class UserService {
    private UserDao dao = new UserDao();

    public User findByUsername(String username) {
	return dao.findByUsername(username);

    }

    public void add(User user) {
	dao.add(user);
    }

    public void update(User user) {
	dao.update(user);

    }

    public void updatePassword(User user) {
	dao.updatePassword(user);

    }

    public void updatePsw(User user) {
	dao.updatePsw(user);
    }

    public User findById(int id) {
	return dao.findById(id);
    }

    public boolean checkName(String name) {
	return dao.checkName(name);
    }

}
