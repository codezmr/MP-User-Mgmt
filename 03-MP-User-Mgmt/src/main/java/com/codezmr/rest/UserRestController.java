package com.codezmr.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codezmr.bindings.ActivateAccount;
import com.codezmr.bindings.Login;
import com.codezmr.bindings.User;
import com.codezmr.services.UserMgmtService;

@RestController
public class UserRestController {

	@Autowired
	private UserMgmtService service;

	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user) {

		boolean isSaved = service.saveUser(user);

		if (isSaved) {
			String successMsg = "Registration Success :)";
			return new ResponseEntity<String>(successMsg, HttpStatus.CREATED);
		} else {
			String failureMsg = "Opps!! Registration Failed ):";
			return new ResponseEntity<String>(failureMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount acctivateAccount) {

		boolean isAccActivated = service.activateUserAcc(acctivateAccount);

		if (isAccActivated) {
			String successMsg = "Account Activated Success.";
			return new ResponseEntity<String>(successMsg, HttpStatus.OK);
		} else {
			String failureMsg = "Oops!! Invalid Temporary Password.";
			return new ResponseEntity<String>(failureMsg, HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUser() {

		List<User> allUsers = service.getAllUsers();

		return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);

	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {

		User user = service.getUserById(userId);
		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {

		boolean isDeleted = service.deleteUserById(userId);

		if (isDeleted) {
			String successMsg = "User Account Deleted Success.";
			return new ResponseEntity<String>(successMsg, HttpStatus.OK);
		} else {
			String failureMsg = "Oops!! User Account Deletion Failed.";
			return new ResponseEntity<String>(failureMsg, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/user/{userId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId, @PathVariable String status) {

		boolean isStatusChnaged = service.changeAccountStatus(userId, status);

		if (isStatusChnaged) {
			String successMsg = "User Status Changed Success.";
			return new ResponseEntity<String>(successMsg, HttpStatus.OK);
		} else {
			String failureMsg = "Oops!! Status Changing Failed.";
			return new ResponseEntity<String>(failureMsg, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {

		String status = service.login(login);
		return new ResponseEntity<String>(status, HttpStatus.OK);

	}

}
