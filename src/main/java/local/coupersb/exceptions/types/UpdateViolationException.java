package local.coupersb.exceptions.types;

/**
 * The class {@code UpdateViolationException} provides information about an update violation
 * based in the logic rules of the coupons system
 * 
 * @author YAM
 *
 */
public class UpdateViolationException extends CouperException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new {@code UpdateViolationException}
	 */
	public UpdateViolationException() 
	{
		super();
	}
	
	/**
	 * Creates a new {@code UpdateViolationException} with a given message
	 * 
	 * @param message the message to display
	 */
	public UpdateViolationException(String message) 
	{
		super(message);
	}

	
}
