package com.masai.service;

import com.masai.exception.LoginException;
import com.masai.exception.EmailException;
import com.masai.exception.UserException;
import com.masai.model.Email;
import com.masai.model.EmailDTO;

public interface EmailService {

	public Email sendEmail(EmailDTO email, String fromEmail, String toEmail)
			throws EmailException, UserException, LoginException;

	public Email starredEmail(Integer emailId) throws EmailException, LoginException;

	public Email deleteEmail(Integer emailId) throws EmailException, LoginException, UserException;

}
