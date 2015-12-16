package org.fastds.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fastds.dbutil.HibernateUtil;
import org.fastds.dbutil.UUIDUtil;
import org.fastds.model.UserQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

public class UserQueryDao {
	public ArrayList<UserQuery> findByUserName(String username) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("FROM userquery WHERE username=?");
		query.setString(0, username);
		ArrayList<UserQuery> list = (ArrayList<UserQuery>) query.list();
		session.getTransaction().commit();
		return list;
	}
	@Test
	public void testAdd()
	{
		UserQuery u = new UserQuery();
		u.setQueryId(UUIDUtil.generateUUID());
		u.setQueryString("test");
		u.setQueryTime("1988-08-06");
		u.setUsername("fast");
		add(u);
		
	}
	@Test
	public void testFindByUserName()
	{
		ArrayList<UserQuery> u = findByUserName("fast");
		System.out.print(u.size()+":"+u.get(0));
		
	}
	@Test
	public void testDeleteByID()
	{
		delete("ed8baa929d864f178e1ad732b2d00f2e");
		
	}
	public void add(UserQuery userQuery) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(userQuery);
		session.getTransaction().commit();
	}
	public UserQuery findByQueryId(String queryId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		UserQuery userQuery = (UserQuery) session.get(UserQuery.class, queryId);
		//user.setUserName("123");
		session.getTransaction().commit();
		return userQuery;
	}
	public void delete(String queryId) {
		UserQuery userQuery = findByQueryId(queryId);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(userQuery);
		session.getTransaction().commit();
	}
	
}
