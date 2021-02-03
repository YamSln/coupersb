package local.coupersb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import local.coupersb.security.AuthenticationManager;
import local.coupersb.security.annotations.ClientRequest;
import local.coupersb.security.payload.AuthenticationResponse;

@ClientRequest
@RestController
@RequestMapping("${application.url}")
public class AuthController 
{
	private final AuthenticationManager authenticationManager;
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager) 
	{
		this.authenticationManager = authenticationManager;
	}
	
	@GetMapping("authenticated")
	public AuthenticationResponse isAuthenticated()
	{
		return this.authenticationManager.isLoggedIn();
	}
	
	@PostMapping("logout")
	public void Logout()
	{
		this.authenticationManager.logout();
	}
}
