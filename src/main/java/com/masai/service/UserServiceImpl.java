package com.masai.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.EmailException;
import com.masai.exception.LoginException;
import com.masai.exception.UserException;
import com.masai.model.Email;
import com.masai.model.Login;
import com.masai.model.User;
import com.masai.model.UserDTO;
import com.masai.model.UserUpdateDTO;
import com.masai.repository.LoginRepo;
import com.masai.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private LoginRepo loginRepo;

	// checking user login validation
	public void checkLoginStatus(String email) throws LoginException {

		Login loginData = loginRepo.findByEmail(email);
		if (loginData == null)
			throw new LoginException("User login required!");
	}

	// get user by email
	public User getuser(String userEmail) throws UserException {

		User existsingUser = userRepo.findByEmail(userEmail);
		if (existsingUser == null)
			throw new UserException("User not found with email : " + userEmail);
		return existsingUser;
	}

	@Override
	public User registerUser(UserDTO userDTO) throws UserException {

		User existsingUser = userRepo.findByEmail(userDTO.getEmail());
		if (existsingUser != null)
			throw new UserException("User already exists with email : " + userDTO.getEmail());

		User newUser = new User();
		newUser.setEmail(userDTO.getEmail());
		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setMobileNumber(userDTO.getMobileNumber());
		newUser.setDateOfBirth(userDTO.getDateOfBirth());
		newUser.setPassword(userDTO.getPassword());

		return userRepo.save(newUser);
	}

	@Override
	public List<Email> checkAllMail(String userEmail) throws UserException, EmailException, LoginException {

		// checking user login status
		checkLoginStatus(userEmail);

		// get user from database by email
		User user = getuser(userEmail);

		List<Email> emailList = user.getEmailList();
		if (emailList.isEmpty())
			throw new EmailException("Empty email list");

		return emailList;
	}

	@Override
	public List<Email> checkAllStarredMail(String userEmail) throws UserException, EmailException, LoginException {

		// checking user login status
		checkLoginStatus(userEmail);

		// get user from database by email
		User user = getuser(userEmail);

		List<Email> starredEmails = new ArrayList<>();
		for (Email email : user.getEmailList()) {
			if (email.getStar())
				starredEmails.add(email);
		}

		if (starredEmails.isEmpty())
			throw new EmailException("User doesn't have any starred email");

		return starredEmails;
	}

	@Override
	public User updateUser(UserUpdateDTO userUpdateDTO, String userEmail) throws UserException, LoginException {

		// get user from database by email
		User existingUser = getuser(userEmail);

		existingUser.setEmail(userEmail);
		existingUser.setFirstName(userUpdateDTO.getFirstName());
		existingUser.setLastName(userUpdateDTO.getLastName());
		existingUser.setMobileNumber(userUpdateDTO.getMobileNumber());
		existingUser.setDateOfBirth(userUpdateDTO.getDateOfBirth());
		existingUser.setPassword(userUpdateDTO.getPassword());

		return userRepo.save(existingUser);
	}

}
