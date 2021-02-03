package local.coupersb.validation;

public class ValidationUtils 
{
	// -- Validation error messages --
	
	// Category
	
	public static final String CATEGORY_NAME_BLANK_MESSAGE = 
			"Category name musnot be null or empty";
	public static final String CATEGORY_NAME_SIZE_MESSAGE = 
			"Category name must consist at least 2 charachters and maximum of 25";
	
	// Client
	
	public static final String EMAIL_VALIDATION_MESSAGE = "Email is not valid";
	public static final String EMAIL_NULL_MESSAGE = "Email address musnot be null";
	
	// Company
	
	public static final String COMPANY_NAME_SIZE_MESSAGE = 
			"Comapny name must consist at least 2 charachters and maximum of 45";
	public static final String COMPANY_NAME_BLANK_MESSAGE = "Company name musnot be null or empty";
	
	// Customer
	
	public static final String FIRST_NAME_SIZE_MESSAGE = 
			"Customer first must contain at least 2 characters and maximum of 25";
	public static final String LAST_NAME_SIZE_MESSAGE = 
			"Customer last must contain at least 2 characters and maximum of 25";
	public static final String FIRST_NAME_BLANK_MESSAGE = "Customer first name musnot be null or empty";
	public static final String LAST_NAME_BLANK_MESSAGE = "Customer last name musnot be null or empty";
	
	// Coupon
	
	public static final int MAX_COUPON_PRICE = 999;
	public static final int MAX_COUPON_QUANTITY = 999;
	
	public static final String COUPON_TITLE_SIZE_MESSAGE = "Coupon title must consis at least 2 charachters";
	public static final String COUPON_TITLE_BLANK_MESSAGE = "Coupon title musnot be null or empty";
	public static final String COUPON_START_DATE_NULL_MESSAGE = "Coupon start date musnot be null";
	public static final String COUPON_END_DATE_NULL_MESSAGE = "Coupon end date musnot be null";
	public static final String COUPON_NEGATIVE_QUANTITY_MESSAGE = "Coupon quantity cannot be negative";
	public static final String COUPON_MAX_PRICE_MESSAGE = "Coupon price cannot exceed " + MAX_COUPON_PRICE;
	public static final String COUPON_MAX_QUANTITY_MESSAGE = "Coupon quantity cannot exceed " + MAX_COUPON_QUANTITY;
	
	// Email regex pattern
	
	public static final String EMAIL_REGEX_PATTERN = ".+@.+\\..+";
}