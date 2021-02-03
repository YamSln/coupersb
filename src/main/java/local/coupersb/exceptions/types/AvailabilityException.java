package local.coupersb.exceptions.types;

/**
 * The class {@code AvailabilityException} allows to create an exception that
 * provides information about the availability status of a coupons system database
 * element (mostly when a given element does not logically exists)
 * 
 * @author YAM
 *
 */
public class AvailabilityException extends CouperException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crate a new {@code AvailabilityException}
	 */
	public AvailabilityException() 
	{
		super();
	}
	
	/**
	 * Creates a new {@code AvailabilityException} with a given message
	 * 
	 * @param message a message to be displayed when the exception is displayed
	 */
	public AvailabilityException(String message) 
	{
		super(message);
	}
	
}