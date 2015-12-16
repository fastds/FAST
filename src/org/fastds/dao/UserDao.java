package org.fastds.dao;

import java.util.List;

import org.fastds.dbutil.HibernateUtil;
import org.fastds.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

public class UserDao {
    @Test
    public void save() {
	User user = new User();
	user.setUserName("fast");
	user.setPassword("12345");
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	session.save(user);
	session.getTransaction().commit();
    }

    @Test
    public void get() {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	User user = (User) session.get(User.class, 1);
	//user.setUserName("123");
	session.getTransaction().commit();
	System.out.println(user.getUserName());
    }
    @Test
    public void test()
    {
    	User user = findByUsername("fast");
    	System.out.print(user);
    }
    @SuppressWarnings("unchecked")
    public User findByUsername(String name) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	Query query = session.createQuery("from User where username=?");
	query.setString(0, name);
	List<User> list = query.list();
	if (list.size() == 0) {
	    return null;
	}
	User user = list.get(0);
	session.getTransaction().commit();

	return user;
    }

    @SuppressWarnings("unchecked")
    public boolean checkName(String name) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	Query query = session.createQuery("from User where userName=?");
	query.setString(0, name);
	List<User> list = query.list();
	session.getTransaction().commit();
	if (list.isEmpty()) {
	    return true;
	} else {
	    return false;
	}
    }

    public User findById(int id) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	User user = (User) session.get(User.class, id);
	session.getTransaction().commit();
	return user;
    }

    public void add(User user) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	session.save(user);
	session.getTransaction().commit();

    }

    public void update(User user) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	session.update(user);
	session.getTransaction().commit();
    }

    public void updatePsw(User user) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	User user1 = (User) session.get(User.class, user.getId());
	user1.setPassword(user.getPassword());
	session.getTransaction().commit();

    }

    public void updatePassword(User user) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	User user1 = (User) session.get(User.class, user.getId());
	user1.setPassword(user.getPassword());
	session.getTransaction().commit();

    }

    public void deleteUser(User user) {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	session.delete(user);
	session.getTransaction().commit();
    }

}
