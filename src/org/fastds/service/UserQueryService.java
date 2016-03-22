package org.fastds.service;

import java.util.ArrayList;
import java.util.List;

import org.fastds.dao.UserQueryDao;
import org.fastds.model.DBColumns;
import org.fastds.model.User;
import org.fastds.model.UserQuery;

public class UserQueryService {
	private UserQueryDao dao = new UserQueryDao();

	public ArrayList<UserQuery> findAll(User user) {
		String username = user.getUserName();
		return dao.findByUserName(username);
	}

	public void add(UserQuery userQuery) {
		dao.add(userQuery);
		
	}

	public UserQuery findByQueryId(String queryId) {
		return dao.findByQueryId(queryId);
	}

	public void delete(UserQuery userQuery) {
		dao.delete(userQuery.getQueryId());
	}

	public List<String> getArrays() {
		return dao.getArrays();
	}

	public DBColumns getColumnsByArrayName(String name) {
		return dao.getColumnsByArrayName(name);
	}


}
