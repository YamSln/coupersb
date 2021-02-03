package local.coupersb.exceptions.types;

/**
 * The class {@code DuplicationException} provides information of a duplication
 * case in the coupons system
 * 
 * @author YAM
 *
 */
public class DuplicationException extends CouperException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new duplication exception
	 */
	public DuplicationException() 
	{
		super();
	}
	
	/**
	 * Creates a new duplication exception with a given message
	 * 
	 * @param message the message to display
	 */
	public DuplicationException(String message) 
	{
		super(message);
	}
	
	/**
	 * Crates a new duplication exception with a message based of a given class
	 * 
	 * @param entityClass the class to display in the message
	 */
	public DuplicationException(Class<?> entityClass) 
	{
		super(entityClass.getName() + " specified is already exists");
	}

}