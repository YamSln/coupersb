package local.coupersb.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static local.coupersb.security.SecurityUtils.*;

import local.coupersb.dao.CompanyRepository;
import local.coupersb.dao.CustomerRepository;
import local.coupersb.model.Client;
import local.coupersb.model.Company;
import local.coupersb.model.Customer;
import local.coupersb.security.payload.AuthenticationResponse;
import local.coupersb.service.ClientType;

/**
 * Authentication manager performs the login process to the system
 * on successful login process, it initiates the current session with
 * users info
 * 
 * @author YAM
 *
 */
@Service
public class AuthenticationManager 
{
	private final CompanyRepository companyRepository;
	private final CustomerRepository customerRepository;
	
	private final HttpSession httpSession;
	
	@Autowired
	public AuthenticationManager(HttpSession httpSession,
			CustomerRepository customerRepository,
			CompanyRepository companyRepository) 
	{
		this.httpSession = httpSession;
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
	}
	
	public boolean login(String email, String password, ClientType clientType)
	{
		switch(clientType)
		{
			case ADMINISTRATOR:
				return handleAdminLogin(email, password, clientType);
				
			case COMPANY:
				return handleCompanyLogin(email, password, clientType);
					
			case CUSTOMER:
				return handleCustomerLogin(email, password, clientType);
			
			default:
				return false;
		}
	}
	
	/**
	 * Logs outs from the system by invalidating the current session
	 */
	public void logout()
	{
		 this.httpSession.invalidate();
	}
	
	/**
	 * Returns the id of the user associated with current session
	 * 
	 * @return the id of the user associated with current session
	 */
	public int currentUserId()
	{
		int userId = (int) this.httpSession.getAttribute(SESSION_ID_KEY);
		
		if(userId != 0)
			return userId;
		throw new IllegalStateException("User session not initialized properly");
	}
	
	/**
	 * Returns {@code true} if the current user is logged in
	 * 
	 * @return {@code true} if the current user is logged in
	 */
	public AuthenticationResponse isLoggedIn()
	{
		Boolean loggedIn = (Boolean) this.httpSession.getAttribute(SESSION_LOGIN_KEY);
		String clientType = (String) this.httpSession.getAttribute(SESSION_ROLE_KEY);
		
		if(loggedIn != null && clientType != null)
			return new AuthenticationResponse(loggedIn, clientType);
		return null;
	}
	
	private boolean handleAdminLogin(String email, String password, ClientType clientType)
	{
		if(email.equals("admin@admin.com") && password.equals("admin"))
		{
			initSession(clientType);
			return true;
		}
		return false;
	}
	
	private boolean handleCompanyLogin(String email, String password, ClientType clientType)
	{
		Company company = this.companyRepository
				.findByEmailAndPassword(email, password)
				.orElse(null); // Company does not exist in the system (or invalid credentials)
			
			if(company != null)
			{ // Valid credentials
				initSession(clientType);
				setSessionIdAttribute(company);
				return true;
			}
			return false;
	}
	
	private boolean handleCustomerLogin(String email, String password, ClientType clientType)
	{
		Customer customer = this.customerRepository
				.findByEmailAndPassword(email, password)
				.orElse(null); // Customer does not exist in the system (or invalid credentials)
			
			if(customer != null)
			{ // Valid credentials
				initSession(clientType);
				setSessionIdAttribute(customer);
				return true;
			}
			return false;
	}
	
	private void initSession(ClientType clientType)
	{
		this.httpSession.setAttribute(SESSION_LOGIN_KEY, true);
		this.httpSession.setAttribute(SESSION_ROLE_KEY, clientType.getName());
	}
	
	private void setSessionIdAttribute(Client client)
	{
		this.httpSession.setAttribute(SESSION_ID_KEY, client.getId());
	}
}
