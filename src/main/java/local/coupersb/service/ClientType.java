package local.coupersb.service;

/**
 * Available client types in the coupons system
 * 
 * @author YAM
 *
 */
public enum ClientType 
{
	ADMINISTRATOR("Admin"),
	COMPANY("Company"),
	CUSTOMER("Customer");
	
	String name;

	ClientType(String name) 
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
}