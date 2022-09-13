package com.codezmr.services;

import java.util.List;

import com.codezmr.bindings.ActivateAccount;
import com.codezmr.bindings.Login;
import com.codezmr.bindings.User;

public interface UserMgmtService {

	public boolean saveUser(User user);

	public boolean activateUserAcc(ActivateAccount activateAccount);
	
	public List<User> getAllUsers(); 
	
	public User getUserById(Integer userId);
	
	public boolean deleteUserById(Integer userId);
	
	public boolean changeAccountStatus(Integer userId, String accStatus);
	
	public String login(Login login);
	
	public String forgetPwd(String email);
}
