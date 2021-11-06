package com.pinch.org.login.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinch.org.login.containers.EditProfileDto;
import com.pinch.org.login.containers.HandleForgotPasswordDto;
import com.pinch.org.login.containers.LoginRequestDto;
import com.pinch.org.login.containers.LogoutRequestDto;
import com.pinch.org.login.containers.signUpRequestDto;
import com.pinch.org.login.entity.User;
import com.pinch.org.login.services.LoginService;
import com.pinch.org.login.util.Response;

@RestController
@RequestMapping("/api/")
public class LoginController {

	@Autowired
	LoginService loginService;

	// ping call to see if the api services are working!!
	@GetMapping("ping")
	public String ping() {
		return "PONG !!!";
	}

	@PostMapping("signUp")
	public Response<User> signUp(@RequestBody signUpRequestDto request) throws MessagingException {
		return loginService.signUp(request);
	}

	@GetMapping("availability")
	public Response<User> userNameAvailability(@RequestParam String userName) {
		return loginService.userNameAvailability(userName);
	}

	@PostMapping("login")
	public Response<String> login(@RequestBody LoginRequestDto request) {
		return loginService.login(request);
	}

	@PostMapping("logout")
	public Response<String> logout(@RequestHeader String token, @RequestBody LogoutRequestDto request) {
		request.setToken(token);
		return loginService.logout(request);
	}

	// forgot password request by entering email
	@GetMapping("forgotPasswordEmail")
	public Response<String> forgotPasswordEmail(@RequestParam String email) throws MessagingException {
		return loginService.forgotPasswordEmail(email);
	}

	// forgot password request by entering userName
	@GetMapping("forgotPasswordUserName")
	public Response<String> forgotPasswordUsername(@RequestParam String userName) throws MessagingException {
		return loginService.forgotPasswordUsername(userName);
	}

	@PutMapping("forgotPassword")
	public Response<String> handleForgotPassword(@RequestBody HandleForgotPasswordDto request) {
		return loginService.handleForgotPassowrd(request);
	}

	@PutMapping("editProfile")
	public Response<String> editProfile(@RequestHeader String token, @RequestBody EditProfileDto request) {

		request.setToken(token);
		return loginService.editProfile(request);
	}

}
