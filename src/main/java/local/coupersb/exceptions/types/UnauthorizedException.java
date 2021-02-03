package local.coupersb.exceptions.types;

public class UnauthorizedException extends CouperException 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException()
	{
		super();
	}
	
	public UnauthorizedException(String message)
	{
		super(message);
	}
	
}
