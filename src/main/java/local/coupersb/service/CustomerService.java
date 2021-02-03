package local.coupersb.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import local.coupersb.dao.CouponRepository;
import local.coupersb.dao.CustomerRepository;
import local.coupersb.exceptions.types.AvailabilityException;
import local.coupersb.exceptions.types.DuplicationException;
import local.coupersb.exceptions.types.NoSuchElementException;
import local.coupersb.exceptions.types.OutdatedException;
import local.coupersb.model.Coupon;
import local.coupersb.model.Customer;
import local.coupersb.model.search.CouponSearchResult;
import local.coupersb.security.AuthenticationManager;
import local.coupersb.util.PageBuilder;
import local.coupersb.validation.CouponValidationResult;

import static local.coupersb.validation.CouponValidator.*;

@Service
@Transactional
public class CustomerService extends ClientService
{
	@Autowired
	CustomerService(
			CustomerRepository customerRepository, 
			CouponRepository couponRepository,
			AuthenticationManager authenticationManager) 
	{
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
		this.authenticationManager = authenticationManager;
	}
	
	public void addToCart(Coupon coupon)
	{
		Customer customer = this.validateCouponForCart(coupon);
		customer.addToCart(coupon);
		this.customerRepository.save(customer);
	}
	
	public void removeFromCart(Coupon coupon)
	{
		Customer customer = this.getDetails();
		if(!customer.removeFromCart(coupon))
			throw new NoSuchElementException(String.format("Coupon specified does not exist in %s's cart", customer.getName()));
		else
			this.customerRepository.save(customer);
	}
	
	public void purchaseCoupons(List<Coupon> coupons)
	{ // Validate all of the coupons intended for this purchase
		List<Coupon> couponsForPurchase = coupons.stream()
				.map(this::validateCouponForPurchase)
				.collect(Collectors.toList());
		// Get the purchasing customer
		Customer customer = this.getDetails();
		// Update the coupons to be purchased
		this.updateCouponsForPurchase(couponsForPurchase);
		customer.purchaseCoupons(couponsForPurchase);
		// Save the purchase action
		this.customerRepository.save(customer);	
	}
	
	public void purchaseCart()
	{
		Customer customer = this.getDetails();
		this.purchaseCoupons(customer.getCouponsInCart());
	}
	
	public Coupon getCustomerCoupon(int couponId)
	{
		return this.couponRepository
				.findByIdAndPurchasingCustomersId(couponId, this.authenticationManager.currentUserId())
				.orElseThrow(() -> new NoSuchElementException("Coupon specified not fond for customer"));
	}
	
	public List<Coupon> getCustomerCoupons()
	{
		return this.couponRepository
				.findAllByPurchasingCustomersId(this.authenticationManager.currentUserId());
	}
	
	public List<Coupon> getCustomerCart()
	{
		return this.couponRepository
				.findAllByShoppingCustomersId(this.authenticationManager.currentUserId());
	}
	
	public Page<Coupon> getCustomerCoupons(int pageIndex, int pageSize)
	{ // Page request of customer coupons
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
		// Get all coupons from database as a page
		Page<Coupon> pageResult = this.couponRepository
				.findAllByPurchasingCustomersId(authenticationManager.currentUserId(), pageRequest);
		// Build and return the page
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public Page<Coupon> getCustomerCoupons(int pageIndex, int pageSize, String sortBy, boolean asc)
	{ // Page request of customer coupons - sorted
		Sort sort = Sort.by(sortBy);
		sort = asc ? sort.ascending() : sort.descending();
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
		// Get all coupons from database as a page
		Page<Coupon> pageResult = this.couponRepository
				.findAllByPurchasingCustomersId(authenticationManager.currentUserId(), pageRequest);
		// Build and return the page
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public List<CouponSearchResult> getCustomerCoupons(int resultsCount, String nameExample)
	{ // Page request of customer coupons, sorted by title
		Sort sort = Sort.by(COUPON_TITLE_SORT_KEY).ascending();
		PageRequest pageRequest = PageRequest.of(0, resultsCount, sort);
		// Get all coupons matching the name example
		return this.couponRepository.findAllByPurchasingCustomersIdAndTitleContainingIgnoreCase(
				authenticationManager.currentUserId(), nameExample, pageRequest);
	}
	
	public Coupon getCoupon(int couponId)
	{
		return this.couponRepository
				.findById(couponId)
				.orElseThrow(() -> new NoSuchElementException("Coupon specified not found"));
	}
	
	public Page<Coupon> getAllCoupons(int pageIndex, int pageSize, double maxPrice)
	{ // Page request of all of the coupons (that this customer does not own)
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
		Page<Coupon> pageResult = null;
		if(maxPrice == 0) // Price filter not specified
			pageResult = this.couponRepository.findAllByPurchasingCustomersNullOrPurchasingCustomersIdNot(
					this.authenticationManager.currentUserId(), pageRequest);
		else // Price filter specified
			pageResult = this.couponRepository.findAllByPurchasingCustomersNullOrPurchasingCustomersIdNotAndPriceLessThanEqual(
					authenticationManager.currentUserId(), maxPrice, pageRequest);
		// Build and return the page
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public Page<Coupon> getAllCoupons(int pageIndex, int pageSize, String sortBy, boolean asc, double maxPrice)
	{ // Page request of all of the coupons (that this customer does not own) - sorted
		Sort sort = Sort.by(sortBy);
		sort = asc ? sort.ascending() : sort.descending();
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
		Page<Coupon> pageResult = null;
		if(maxPrice == 0) // Price filter not specified
			pageResult = this.couponRepository.findAllByPurchasingCustomersNullOrPurchasingCustomersIdNot(
					this.authenticationManager.currentUserId(), pageRequest);
		else // Price filter specified
			pageResult = this.couponRepository.findAllByPurchasingCustomersNullOrPurchasingCustomersIdNotAndPriceLessThanEqual(
					authenticationManager.currentUserId(), maxPrice, pageRequest);
		// Build and return the page
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public List<CouponSearchResult> getAllCoupons(int resultsCount, String nameExample)
	{ // Page request of all of the coupons (that this customer does not own), sorted by title
		Sort sort = Sort.by(COUPON_TITLE_SORT_KEY).ascending();
		PageRequest pageRequest = PageRequest.of(0, resultsCount, sort);
		// Get all coupons matching the name example
		return this.couponRepository.findAllByPurchasingCustomersNullAndTitleContainingIgnoreCaseOrPurchasingCustomersIdNotAndTitleContainingIgnoreCase(
				nameExample, authenticationManager.currentUserId(), nameExample, pageRequest);
	}
	
	public Customer getDetails()
	{
		return this.customerRepository
				.findById(this.authenticationManager.currentUserId())
				.orElseThrow(() -> new NoSuchElementException("Customer specified not found"));
	}
	
	private void updateCouponsForPurchase(List<Coupon> coupons)
	{
		coupons.forEach(coupon -> coupon.setQuantity(coupon.getQuantity() - 1));
	}
	
	private Coupon validateCouponForPurchase(Coupon coupon)
	{
		Coupon couponForPurchase = this.couponRepository
				.findById(coupon.getId())
				.orElseThrow(() -> new NoSuchElementException("Coupon specified does not exist"));
		
		this.customerRepository // Coupon already purchased by current customer?
			.findByIdAndPurchasedCoupons(authenticationManager.currentUserId(), couponForPurchase)
			.ifPresent(this::throwCustomerAlreadyOwnsCouponException);
		
		couponValidation(couponForPurchase); // Validate coupon before performing purchase transaction
		
		return couponForPurchase;
	}
	
	private Customer validateCouponForCart(Coupon coupon)
	{
		Customer customer = this.getDetails();
		if(customer.getPurchasedCoupons().contains(coupon))
			throw new DuplicationException(String.format("Coupon spesified already purchased by %s", customer.getName()));
		if(customer.getCouponsInCart().contains(coupon))
			throw new DuplicationException(String.format("Coupon spesified already exists in %s's cart", customer.getName()));
		return customer;
	}
	
	private void couponValidation(Coupon coupon)
	{
		CouponValidationResult result = isAvailable()
				.and(isNotExpiered())
				.apply(coupon);
		
		switch(result)
		{
			case OUT_OF_STOCK:
				throw new AvailabilityException(
						String.format("Coupon %s is out of stock", coupon.getTitle()));
			case OUTDATED:
				throw new OutdatedException(
						String.format("Coupon %s is expiered", coupon.getTitle()));
			case SUCCESS:
			default:
				return;
		}
	}
	
	private void throwCustomerAlreadyOwnsCouponException(Customer customer)
	{
		throw new DuplicationException(
				String.format("Customer %s already owns specified coupon", customer.getName()));
	}

	@Override
	public boolean login(String email, String password) 
	{
		return this.authenticationManager.login(email, password, ClientType.CUSTOMER);
	}

}