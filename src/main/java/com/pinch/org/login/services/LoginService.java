package com.pinch.org.login.services;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pinch.org.login.containers.ForgotPasswordTemplateDto;
import com.pinch.org.login.containers.LoginRequestDto;
import com.pinch.org.login.containers.signUpRequestDto;
import com.pinch.org.login.entity.ForgotPasswordToken;
import com.pinch.org.login.entity.LoginAuthToken;
import com.pinch.org.login.entity.User;
import com.pinch.org.login.repository.ForgotPasswordRepo;
import com.pinch.org.login.repository.LoginAuthTokenRepo;
import com.pinch.org.login.repository.LoginRepo;
import com.pinch.org.login.util.LoginConstants;
import com.pinch.org.login.util.PasswordEncoder;
import com.pinch.org.login.util.RandomStringGenerator;
import com.pinch.org.login.util.Response;

@Service
public class LoginService {

	@Autowired
	LoginRepo loginRepo;

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	@Autowired
	LoginAuthTokenRepo loginAuthTokenRepo;

	@Autowired
	ForgotPasswordRepo forgotPasswordRepo;

	@Autowired
	Environment environment;

	public Response<User> signUp(signUpRequestDto request) throws MessagingException {
		Response<User> response = new Response<>();
		Optional<User> optional = loginRepo.findByUserName(request.getUserName());
		if (optional.isPresent()) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.USERNAME_ALREADY_PRESENT);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return response;
		}
		optional = loginRepo.findByEmail(request.getEmail());
		if (optional.isPresent()) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.EMAIL_ALREADY_PRESENT);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return response;
		}

		// validate email format
		if (!isValidEmail(request.getEmail())) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.INVALID_EMAIL_FORMAT);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return response;
		}

		User user = new User();
		user.setCreatedTime(System.currentTimeMillis());
		user.setEmail(request.getEmail());
		user.setIsActive(true);
		user.setModifiedTime(System.currentTimeMillis());
		user.setUserName(request.getUserName());
		user.setIsVerifiedEmail(false);
		user.setPassword(PasswordEncoder.passwordEncode(request.getPassword()));
		user.setUserId(generateUserId());
		loginRepo.save(user);

		sendHtmlMail(request, "SignupWelcome", LoginConstants.SIGNUP_EMAIL_SUBJECT, request.getEmail());

		response.setMessage(LoginConstants.USER_ADDED_SUCCESSFULLY);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setSuccess(true);
		return response;
	}

	// method to check if the user entered userName is available or not
	public Response<User> userNameAvailability(String userName) {
		Response<User> response = new Response<User>();
		Optional<User> optional = loginRepo.findByUserName(userName);
		if (optional.isPresent()) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.USERNAME_ALREADY_PRESENT);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return response;
		}
		response.setSuccess(true);
		response.setMessage(LoginConstants.USERNAME_IS_AVAILABLE);
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		return response;
	}

	public Response<String> login(LoginRequestDto request) {
		Response<String> response = new Response<>();
		Optional<User> optional = loginRepo.findByUserName(request.getUserName());
		if (!optional.isPresent()) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.INVALID_USERNAME);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return response;
		}

		User user = optional.get();

		if (!user.getIsActive()) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.INVALID_USER);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return response;
		}

		if (!PasswordEncoder.passwordValidate(request.getPassword(), user.getPassword())) {
			response.setSuccess(false);
			response.setMessage(LoginConstants.INVALID_PASSWORD);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return response;
		}
		String token = RandomStringGenerator.generateRandomString(100);// generate random string of 100 chars

		LoginAuthToken loginAuthToken = new LoginAuthToken();
		loginAuthToken.setCreatedDate(System.currentTimeMillis());
		loginAuthToken.setToken(token);
		loginAuthToken.setUserName(request.getUserName());

		loginAuthTokenRepo.save(loginAuthToken);

		response.setSuccess(true);
		response.setMessage(LoginConstants.LOGIN_SUCCESSFULL);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setData(token);
		return response;
	}

	public Response<String> forgotPasswordEmail(String email) throws MessagingException {
		Optional<User> optional = loginRepo.findByEmail(email);
		if (optional.isEmpty()) {
			Response<String> response = new Response<String>();
			response.setSuccess(false);
			response.setMessage(LoginConstants.INVALID_EMAIL);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return response;
		}
		return forgotPassword(optional.get());
	}

	public Response<String> forgotPasswordUsername(String userName) throws MessagingException {
		Optional<User> optional = loginRepo.findByUserName(userName);
		if (optional.isEmpty()) {
			Response<String> response = new Response<String>();
			response.setSuccess(false);
			response.setMessage(LoginConstants.INVALID_USERNAME);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return response;
		}
		return forgotPassword(optional.get());
	}

	private Response<String> forgotPassword(User user) throws MessagingException {
		String token = RandomStringGenerator.generateRandomString(100);

		ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
		forgotPasswordToken.setUserName(user.getUserName());
		forgotPasswordToken.setCreatedDate(System.currentTimeMillis());
		forgotPasswordToken.setToken(token);
		forgotPasswordRepo.save(forgotPasswordToken);

		ForgotPasswordTemplateDto templateVariable = new ForgotPasswordTemplateDto();
		templateVariable.setToken(environment.getProperty("forgot.password.url") + token);
		templateVariable.setUserName(user.getUserName());

		sendHtmlMail(templateVariable, "ForgotPassword", LoginConstants.FORGOT_PASSWORD_EMAIL_SUBJECT, user.getEmail());

		Response<String> response = new Response<String>();
		response.setSuccess(true);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setMessage(LoginConstants.FORGOT_PASSWORD_CHECK_EMAIL);
		return response;
	}

	public void sendHtmlMail(Object variables, String template, String subject, String email)
			throws MessagingException {
		Context context = new Context();
		context.setVariable("variable", variables);// data to be injected into the HTML file

		String processedString = templateEngine.process(template, context);// injecting data into html file
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject(subject);
		helper.setText(processedString, true);
		helper.setTo(email);
		mailSender.send(mimeMessage);
	}

	static boolean isValidEmail(String email) {
		// validating the email format thats being provided "xxxx @ xx . xx"
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	public String generateUserId() {
		Long userCount = loginRepo.count();
		String userId;

		if (userCount == 0)
			userId = LoginConstants.USERID_PREFIX + "1"; // first user
		else
			userId = LoginConstants.USERID_PREFIX + ++userCount;// userid is one plus total number of users.
		return userId;
	}

}