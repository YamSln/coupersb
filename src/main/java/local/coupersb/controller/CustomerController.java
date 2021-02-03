package local.coupersb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import local.coupersb.model.Coupon;
import local.coupersb.model.Customer;
import local.coupersb.model.search.CouponSearchResult;
import local.coupersb.security.annotations.CustomerRequest;
import local.coupersb.security.annotations.UnAuthenticated;
import local.coupersb.security.payload.LoginRequest;
import local.coupersb.service.CustomerService;

import static local.coupersb.util.UrlUtils.*;

@CustomerRequest
@RestController
@RequestMapping("${application.url}customer")
public class CustomerController extends ClientController
{
	private final CustomerService customerService;
	
	@Autowired
	public CustomerController(CustomerService customerService) 
	{
		this.customerService = customerService;
	}
	
	@PostMapping("login")
	@UnAuthenticated
	@Override
	public boolean login(@Valid @RequestBody LoginRequest loginRequest) 
	{
		return this.customerService.login(loginRequest.getEmail(), loginRequest.getPassword());
	}
	
	@PostMapping(COUPON_URL + "purchase")
	public void purchaseCoupons(@Valid @RequestBody List<Coupon> coupons)
	{
		this.customerService.purchaseCoupons(coupons);
	}
	
	@PostMapping(COUPON_URL + "addToCart")
	public void addToCart(@Valid @RequestBody Coupon coupon)
	{
		this.customerService.addToCart(coupon);
	}
	
	@PostMapping(COUPON_URL + "removeFromCart")
	public void removeFromCart(@Valid @RequestBody Coupon coupon)
	{
		this.customerService.removeFromCart(coupon);
	}
	
	@PostMapping(COUPON_URL + "purchaseCart")
	public void purchaseCart()
	{
		this.customerService.purchaseCart();
	}
	
	@GetMapping(COUPON_GET_URL + "{couponId}")
	public Coupon getCustomerCoupon(@PathVariable("couponId") int couponId)
	{
		return this.customerService.getCustomerCoupon(couponId);
	}
	
	@GetMapping(COUPON_GET_URL + "all")
	public List<Coupon> getCustomerCoupons()
	{
		return this.customerService.getCustomerCoupons();
	}
	
	@GetMapping(COUPON_GET_URL + "cart")
	public List<Coupon> getCart()
	{
		return this.customerService.getCustomerCart();
	}
	
	@GetMapping(COUPON_GET_URL + "paged")
	public Page<Coupon> getCustomerCoupons( // All of the customer's coupons paged
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
	{
		return this.customerService.getCustomerCoupons(pageIndex, pageSize);
	}
	
	@GetMapping(COUPON_GET_URL + "pagedSorted")
	public Page<Coupon> getCustomerCoupons( // All of the customer's coupons paged
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = DEFAULT_COUPON_SORT) String sortBy,
			@RequestParam(name = SORT_ORDER, defaultValue = DEFAULT_SORT_ORDER) boolean asc)
	{
		return this.customerService.getCustomerCoupons(pageIndex, pageSize, sortBy, asc);
	}
	
	@GetMapping(COUPON_GET_URL + "example")
	public List<CouponSearchResult> getCustomerCoupons( // All of the customer's coupons by name example, used mainly for searching
			@RequestParam(name = RESULTS_COUNT, defaultValue = DEFAULT_PAGE_SIZE) int resultsCount,
			@RequestParam(name = NAME_EXAMPLE) String nameExample)
	{
		return this.customerService.getCustomerCoupons(resultsCount, nameExample);
	}
	
	@GetMapping(COUPON_GET_URL + "shop/{couponId}")
	public Coupon getCoupon(@PathVariable("couponId") int couponId)
	{
		return this.customerService.getCoupon(couponId);
	}
	
	@GetMapping(ALL_COUPONS_GET_URL + "paged")
	public Page<Coupon> getAllCoupons( // All coupons paged (price filter is optional)
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(
					name = MAX_PRICE, 
					defaultValue = DEFAULT_PRICE_FILTER, 
					required = false) double maxPrice)
	{
		return this.customerService.getAllCoupons(pageIndex, pageSize, maxPrice);
	}
	
	@GetMapping(ALL_COUPONS_GET_URL + "pagedSorted")
	public Page<Coupon> getAllCoupons( // All coupons, paged and sorted (price filter is optional)
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = DEFAULT_COUPON_SORT) String sortBy,
			@RequestParam(name = SORT_ORDER, defaultValue = DEFAULT_SORT_ORDER) boolean asc,
			@RequestParam(
					name = MAX_PRICE, 
					defaultValue = DEFAULT_PRICE_FILTER, 
					required = false) double maxPrice)
	{
		return this.customerService.getAllCoupons(pageIndex, pageSize, sortBy, asc, maxPrice);
	}
	
	@GetMapping(ALL_COUPONS_GET_URL + "example")
	public List<CouponSearchResult> getAllCoupons( // All coupons by name example, used mainly for searching
			@RequestParam(name = RESULTS_COUNT, defaultValue = DEFAULT_PAGE_SIZE) int resultsCount,
			@RequestParam(name = NAME_EXAMPLE) String nameExample)
	{
		return this.customerService.getAllCoupons(resultsCount, nameExample);
	}
	
	@GetMapping("getDetails")
	public Customer getDetails()
	{
		return this.customerService.getDetails();
	}

}