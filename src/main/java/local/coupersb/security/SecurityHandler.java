package local.coupersb.security;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import local.coupersb.exceptions.types.AuthenticationException;
import local.coupersb.exceptions.types.UnauthorizedException;
import local.coupersb.service.ClientType;

import static local.coupersb.security.SecurityUtils.*;

/**
 * The SecurityHandler acts as an aspect that invokes security handling advises (certain methods) at particular join points
 * (for example, execution of methods annotated with security annotations)
 * 
 * @author YAM
 *
 */
@Aspect
@Component
public class SecurityHandler 
{
	private final HttpSession httpSession;
	
	@Autowired
	public SecurityHandler(HttpSession httpSession) 
	{
		this.httpSession = httpSession;
	}
	
	/**
	 * Handles requests to functions that annotated with {@code @AdminRequest} annotation <br>
	 * Acts as a "before" advice (invoked before the annotated method) and validates the following: <br>
	 * 
	 * 1. The requesting user is authenticated (logged in) <br>
	 * 2. The requesting user is authorized (has a role of {@code ClientType.ADMIN})
	 */
	@Before("isAdministrator()")
	public void handleAdminRequest()
	{
		if(handleRequest(this.httpSession, ClientType.ADMINISTRATOR))
			return;
		throw new UnauthorizedException("User is not admin");
	}
	
	/**
	 * Handles requests to functions that annotated with {@code @CompanyRequest} annotation <br>
	 * Acts as a "before" advice (invoked before the annotated method) and validates the following: <br>
	 * 
	 * 1. The requesting user is authenticated (logged in) <br>
	 * 2. The requesting user is authorized (has a role of {@code ClientType.COMPANY})
	 */
	@Before("isCompany()")
	public void handleCompanyRequest()
	{
		if(handleRequest(this.httpSession, ClientType.COMPANY))
			return;
		throw new UnauthorizedException("User is not company");
	}
	
	/**
	 * Handles requests to functions that annotated with {@code @CustomerRequest} annotation <br>
	 * Acts as a "before" advice (invoked before the annotated method) and validates the following: <br>
	 * 
	 * 1. The requesting user is authenticated (logged in) <br>
	 * 2. The requesting user is authorized (has a role of {@code ClientType.CUSTOMER})
	 */
	@Before("isCustomer()")
	public void handleCustomerRequest()
	{
		if(handleRequest(this.httpSession, ClientType.CUSTOMER))
			return;
		throw new UnauthorizedException("User is not customer");
	}
	
	/**
	 * Handles requests to functions that annotated with {@code @ClientRequest} annotation <br>
	 * Acts as a "before" advice (invoked before the annotated method) and validates the following: <br>
	 * 
	 * 1. The requesting user is authenticated (logged in) <br>
	 * 2. The requesting user has a role set in his session (valid role)
	 */
	@Before("isLoggedIn()")
	public void handleClientRequest()
	{
		sessionValidation(this.httpSession);
	}
	
	@Pointcut("(@annotation(local.coupersb.security.annotations.AdminRequest) || "
			+ "@target(local.coupersb.security.annotations.AdminRequest)) && !isUnAuthenticated() && inAppExecution()")
	private void isAdministrator() { }
	
	@Pointcut("(@annotation(local.coupersb.security.annotations.CompanyRequest) || "
			+ "@target(local.coupersb.security.annotations.CompanyRequest)) && !isUnAuthenticated() && inAppExecution()")
	private void isCompany() { }
	
	@Pointcut("(@annotation(local.coupersb.security.annotations.CustomerRequest) || "
			+ "@target(local.coupersb.security.annotations.CustomerRequest)) && !isUnAuthenticated() && inAppExecution()")
	private void isCustomer() { }
	
	@Pointcut("(@annotation(local.coupersb.security.annotations.ClientRequest) || "
			+ "@target(local.coupersb.security.annotations.ClientRequest)) && !isUnAuthenticated() && inAppExecution()")
	private void isLoggedIn() { }
	
	@Pointcut("@annotation(local.coupersb.security.annotations.UnAuthenticated) || "
			+ "@target(local.coupersb.security.annotations.UnAuthenticated)") // Annotated with @UnAuthenticated
	private void isUnAuthenticated() { }
	
	@Pointcut("execution(* local.coupersb..*(..))") // Executed inside the application
	private void inAppExecution() { }
	
	private boolean handleRequest(HttpSession httpSession, ClientType clientType)
	{
		sessionValidation(httpSession);
		return httpSession.getAttribute(SESSION_ROLE_KEY).equals(clientType.getName());
	}
	
	private void sessionValidation(HttpSession httpSession)
	{
		validateLogin(httpSession);
		validateRoleAttribute(httpSession);
	}
	
	private void validateLogin(HttpSession httpSession)
	{
		Boolean isLoggedIn = (Boolean) httpSession.getAttribute(SESSION_LOGIN_KEY);
		System.out.println(isLoggedIn);
		if(isLoggedIn != null && isLoggedIn)
			return;
		throw new AuthenticationException("User is not logged in");
	}
	
	private void validateRoleAttribute(HttpSession httpSession)
	{
		String requestRole = (String) httpSession.getAttribute(SESSION_ROLE_KEY);
		System.out.println(requestRole);
		if(requestRole != null)
			return;
		throw new AuthenticationException("User is not logged in");
	}
}
