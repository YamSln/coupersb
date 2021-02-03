package local.coupersb.model;

import static local.coupersb.validation.ValidationUtils.COMPANY_NAME_BLANK_MESSAGE;
import static local.coupersb.validation.ValidationUtils.COMPANY_NAME_SIZE_MESSAGE;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A company of the coupons system - one type of its clients
 * 
 * @author YAM
 *
 */

@Entity(name = "company")
@Table(name = "companies")
public class Company extends Client
{
	@Size(min = 2, max = 45, message = COMPANY_NAME_SIZE_MESSAGE)
	@NotBlank(message = COMPANY_NAME_BLANK_MESSAGE)
	@Column(
			name = "name", 
			nullable = false, 
			unique = true, 
			length = 45)
	private String name;
	
	@OneToMany(
			mappedBy = "ownerCompany", 
			cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, 
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Coupon> ownedCoupons;
	
	public Company()
	{
		super();
	}
	
	/**
	 * Creates a company with the given password, email address and name
	 * 
	 * @param password password of the company
	 * @param email email of the company
	 * @param name name of the company
	 */
	public Company(String password, String email, String name)
	{
		super(password, email);
		this.name = name;
	}
	
	/**
	 * Creates a company with the given id, password, email address and name
	 * 
	 * @param id id of the company
	 * @param password password of the company
	 * @param email email of the company
	 * @param name name of the company
	 */
	public Company(int id, String password, String email, String name)
	{
		super(id, password, email);
		this.name = name;
	}
	
	/**
	 * Creates a company with the given id, password, email address, coupons list and name
	 * 
	 * @param id id of the company
	 * @param password password of the company
	 * @param email email of the company
	 * @param couponsList list of coupons that the company owns
	 * @param name name of the company
	 */
	public Company(int id, String password, String email, List<Coupon> ownedCoupons, String name)
	{
		super(id, password, email);
		this.setOwnedCoupons(ownedCoupons);
		this.name = name;
	}
	
	/**
	 * Returns the name of the company
	 * 
	 * @return the name of the company
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * Sets the name of the company
	 * 
	 * @param name the name to set to the company
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public List<Coupon> getOwnedCoupons() 
	{
		return ownedCoupons;
	}

	public void setOwnedCoupons(List<Coupon> ownedCoupons) 
	{
		this.ownedCoupons = ownedCoupons;
	}
	
	public boolean createCoupon(Coupon coupon)
	{
		return this.ownedCoupons.add(coupon);
	}
	
	public void deleteCoupon(Coupon coupon)
	{
		this.ownedCoupons.remove(coupon);
	}

	@Override
	public String toString() {
		return "Company [name=" + name + super.toString() + "]";
	}	
	
}