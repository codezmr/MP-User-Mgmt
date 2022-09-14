package com.codezmr.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.codezmr.bindings.ActivateAccount;
import com.codezmr.bindings.Login;
import com.codezmr.bindings.User;
import com.codezmr.entity.UserMaster;
import com.codezmr.repo.UserMasterRepo;

public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Override
	public boolean saveUser(User user) {

		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);

		entity.setPassword(tempPassGenrator());
		entity.setAccStatus("In-Active");

		UserMaster isSaved = userMasterRepo.save(entity);

		// TODO: Send Registration Email.
		
		return isSaved.getUserId() != null;
	}

	private String tempPassGenrator() {

		final String ch = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		String tempPasswd = "";

		for (int i = 0; i < 8; i++) {

			tempPasswd = tempPasswd + ch.charAt((int) (Math.random() * (ch.length() - 0 + 1) + 0));

		}
		return tempPasswd;

	}

	@Override
	public boolean activateUserAcc(ActivateAccount activateAccount) {
		
		
		
		return false;
	}

	@Override
	public List<User> getAllUsers() {
		return null;
	}

	@Override
	public User getUserById(Integer userId) {
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		return false;
	}

	@Override
	public String login(Login login) {
		return null;
	}

	@Override
	public String forgetPwd(String email) {
		return null;
	}

}
