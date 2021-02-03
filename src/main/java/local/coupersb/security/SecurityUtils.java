package local.coupersb.security;

import local.coupersb.service.ClientType;

public class SecurityUtils 
{
	public static final String SESSION_LOGIN_KEY = "isLoggedIn";
	public static final String SESSION_ROLE_KEY = "role";
	public static final String SESSION_ID_KEY = "id";
	
	public static final String ROLE_ADMIN = ClientType.ADMINISTRATOR.getName();
	public static final String ROLE_COMPANY = ClientType.COMPANY.getName();
	public static final String ROLE_CUSTOMER = ClientType.CUSTOMER.getName();
}
