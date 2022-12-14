package com.codezmr.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.codezmr.bindings.ActivateAccount;
import com.codezmr.bindings.Login;
import com.codezmr.bindings.User;
import com.codezmr.entity.UserMaster;
import com.codezmr.repo.UserMasterRepo;
import com.codezmr.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {

		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);

		entity.setPassword(tempPassGenrator());
		entity.setAccStatus("In-Active");

		UserMaster isSaved = userMasterRepo.save(entity);

		// Send Registration Email.
		String subject = "Your Registrtion is Success.";
		String fileName = "REG_EMAIL_BODY.txt";

		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		emailUtils.sendEmail(user.getEmail(), subject, body);

		return isSaved.getUserId() != null;
	}
	
	@Override
	public String forgetPwd(String email) {

		UserMaster entity = userMasterRepo.findByEmail(email);

		if (entity == null) {

			return "Invalid Email";
		}

		// Send password to user mail.

		String forgetPwdBody = "RECOVER_PWD_BODY.txt";
		
		String subject = "Get Your Forgot Password .";
		String fileName= "RECOVER_PWD_BODY.txt";
		
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		boolean isSendEmail = emailUtils.sendEmail(email, subject, body);
		
		if(isSendEmail) {
			return "Password sent to your register email: "+email;
		}

		return null;
	}

	private String tempPassGenrator() {

		final String ch = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		String tempPasswd = "";

		for (int i = 0; i < 8; i++) {

			tempPasswd = tempPasswd + ch.charAt((int) (Math.random() * (ch.length() - 0 + 1) + 0));

		}
		return tempPasswd;

	}

	private String readEmailBody(String fullName, String pwd, String fileName) {

		String url = "https://www.google.com";
		String loginUrl = "https://www.google.com";

		FileReader fr;

		String mailBody = null;
		try {
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			StringBuffer buffer = new StringBuffer();
			String line = br.readLine();

			while (line != null) {

				buffer.append(line);
				line = br.readLine();
			}
			br.close();

			mailBody = buffer.toString();
			mailBody = mailBody.replace("{Fullname}", fullName);
			mailBody = mailBody.replace("{TempPwd}", pwd);
			mailBody = mailBody.replace("{URL}", url);

			// forget password file
			mailBody = mailBody.replace("{LoginURL}", loginUrl);
			mailBody = mailBody.replace("{UserPwd}", pwd);

			return mailBody;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;

	}

	@Override
	public boolean activateUserAcc(ActivateAccount activateAccount) {

		UserMaster entity = new UserMaster();
		entity.setEmail(activateAccount.getEmail());
		entity.setPassword(activateAccount.getTempPwd());

		// SELECT * FROM USER_MASTER WHERE email=? AND password=?;
		Example<UserMaster> of = Example.of(entity);

		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activateAccount.getNewPwd());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}

	}

	@Override
	public List<User> getAllUsers() {

		List<UserMaster> findAll = userMasterRepo.findAll();

		List<User> users = new ArrayList<>();

		for (UserMaster entity : findAll) {

			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}

		return users;
	}

	@Override
	public User getUserById(Integer userId) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);

		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();

			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;

	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();

		}

		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);

		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(accStatus);
			userMasterRepo.save(userMaster);
			return true;
		}

		return false;
	}

	@Override
	public String login(Login login) {

		UserMaster entity = new UserMaster();
		entity.setEmail(login.getEmail());
		entity.setPassword(login.getPassword());

		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty()) {
			return "Invalid Credentials";
		} else {
			UserMaster userMaster = findAll.get(0);
			if (userMaster.getAccStatus().equals("Active")) {
				return "SUCCESS";
			} else {
				return "Account not activated";
			}
		}

	}



}
