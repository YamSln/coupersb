package local.coupersb.security.payload;

public class AuthenticationResponse 
{
	private final boolean authenticated;
	private final String clientType;
	
	public AuthenticationResponse(boolean authenticated, String clientType) 
	{
		this.authenticated = authenticated;
		this.clientType = clientType;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public String getClientType() {
		return clientType;
	}
	
}
