package com.codezmr.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codezmr.bindings.ActivateAccount;
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

}
