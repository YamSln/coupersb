package local.coupersb.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static local.coupersb.validation.ValidationUtils.FIRST_NAME_SIZE_MESSAGE;
import static local.coupersb.validation.ValidationUtils.LAST_NAME_SIZE_MESSAGE;
import static local.coupersb.validation.ValidationUtils.FIRST_NAME_BLANK_MESSAGE;
import static local.coupersb.validation.ValidationUtils.LAST_NAME_BLANK_MESSAGE;

/**
 * A customer of the coupons system - one type of its clients
 * 
 * @author YAM
 *
 */

@Entity(name = "customer")
@Table(name = "customers")
public class Customer extends Client
{
	@Size(min = 2, max = 25, message = FIRST_NAME_SIZE_MESSAGE)
	@NotBlank(message = FIRST_NAME_BLANK_MESSAGE)
	@Column(name = "first_name", nullable = false, length = 25)
	private String firstName;
	
	@Size(min = 2, max = 25, message = LAST_NAME_SIZE_MESSAGE)
	@NotBlank(message = LAST_NAME_BLANK_MESSAGE)
	@Column(name = "last_name", nullable = false, length = 25)
	private String lastName;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "customers_vs_coupons")
	private List<Coupon> purchasedCoupons;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "customers_cart_coupons")
	private List<Coupon> couponsInCart;
	
	/**
	 * Creates an empty customer
	 */
	public Customer() 
	{
		super();
	}
	
	/**
	 * Creates a new customer the given password, email address, first name and last name
	 * 
	 * @param password password of the customer
	 * @param email email of the customer
	 * @param firstName first name of the customer
	 * @param lastName last name of the customer
	 */
	public Customer(String password, String email, String firstName, String lastName) 
	{
		super(password, email);
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * Creates a new customer the given id, password, email address, first name and last name
	 * 
	 * @param id id of the customer
	 * @param password password of the customer
	 * @param email email of the customer
	 * @param firstName first name of the customer
	 * @param lastName last name of the customer
	 */
	public Customer(int id, String password, String email, String firstName, String lastName) 
	{
		super(id, password, email);
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * Creates a new customer the given id, password, email address, coupons list
	 * , first name and last name
	 * 
	 * @param id id of the customer
	 * @param password password of the customer
	 * @param email email of the customer
	 * @param couponsList list of coupons that the customer owns
	 * @param firstName first name of the customer
	 * @param lastName last name of the customer
	 */
	public Customer(int id, String password, String email, List<Coupon> purchasedCoupons, String firstName, String lastName)
	{
		super(id, password, email);
		this.firstName = firstName;
		this.lastName = lastName;
		this.purchasedCoupons = purchasedCoupons;
	}
	
	/**
	 * Returns the full the of the customer
	 * 
	 * @return the full the of the customer
	 */
	public String getName()
	{
		return this.firstName + " " + this.lastName;
	}
	
	/**
	 * Returns the first name of the customer
	 * 
	 * @return the first name of the customer
	 */
	public String getFirstName() 
	{
		return firstName;
	}
	
	/**
	 * Sets the first name of the customer
	 * 
	 * @param firstName the first name to set to the customer
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	/**
	 * Returns the last name of the customer
	 * 
	 * @return the last name of the customer
	 */
	public String getLastName() 
	{
		return lastName;
	}
	
	public List<Coupon> getPurchasedCoupons() 
	{
		return purchasedCoupons;
	}

	public void setPurchasedCoupons(List<Coupon> purchasedCoupons) 
	{
		this.purchasedCoupons = purchasedCoupons;
	}
	
	/**
	 * Sets the last name of the customer
	 * 
	 * @param lastName the last name to set to the customer
	 */
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	
	/**
	 * Returns {@code true} if the customer owns a given coupon
	 * otherwise returns {@code false}
	 * @param coupon the coupon to search for
	 * @return {@code true} if the customer owns a given coupon
	 * otherwise returns {@code false}
	 */
	public boolean hasCoupon(Coupon coupon)
	{
		return this.purchasedCoupons.contains(coupon);
	}
	
	public List<Coupon> getCouponsInCart()
	{
		return this.couponsInCart;
	}
	
	public void addToCart(Coupon coupon)
	{
		this.couponsInCart.add(coupon);
	}
	
	public boolean removeFromCart(Coupon coupon)
	{
		return this.couponsInCart.remove(coupon);
	}
	
	public void purchaseCoupon(Coupon coupon)
	{
		this.purchasedCoupons.add(coupon);
	}
	
	public void purchaseCoupons(List<Coupon> coupons)
	{
		coupons.forEach(this::validateCoupon);
		this.purchasedCoupons.addAll(coupons);
	}
	
	private void validateCoupon(Coupon coupon)
	{
		this.couponsInCart.removeIf(this.couponsInCart::contains);
	}
	
	public void removePurchasedCoupon(Coupon coupon)
	{
		this.purchasedCoupons.remove(coupon);
	}
	
	public void removeCouponFromCart(Coupon coupon)
	{
		this.couponsInCart.remove(coupon);
	}
	
	@Override
	public String toString()
	{
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", toString()=" + super.toString() + "]";
	}
		
}