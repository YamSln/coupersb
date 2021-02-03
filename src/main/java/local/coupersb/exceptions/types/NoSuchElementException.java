package local.coupersb.exceptions.types;

/**
 * The class {@code NoSuchElementException} provides information of an element existence
 * in the coupons system
 * 
 * @author YAM
 *
 */
public class NoSuchElementException extends CouperException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new {@code NoSuchElementException}
	 */
	public NoSuchElementException() 
	{
		super();
	}
	
	/**
	 * Creates a new {@code NoSuchElementException} with a given message
	 * 
	 * @param message the message to display
	 */
	public NoSuchElementException(String message) 
	{
		super(message);
	}
	
	/**
	 * Creates a new {@code NoSuchElementException} with a message based of a given class
	 * 
	 * @param entityClass the class to display in the message
	 */
	public NoSuchElementException(Class<?> entityClass) 
	{
		super(entityClass.getName() + " specified does no exists");
	}

}