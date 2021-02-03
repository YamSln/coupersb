package local.coupersb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import local.coupersb.model.Category;
import local.coupersb.model.Company;
import local.coupersb.model.Coupon;
import local.coupersb.model.search.CouponSearchResult;
import local.coupersb.security.annotations.CompanyRequest;
import local.coupersb.security.annotations.UnAuthenticated;
import local.coupersb.security.payload.LoginRequest;
import local.coupersb.service.CompanyService;
import static local.coupersb.util.UrlUtils.*;

@CompanyRequest
@RestController
@RequestMapping("${application.url}company")
public class CompanyController extends ClientController
{
	private final CompanyService companyService;
	
	@Autowired
	public CompanyController(CompanyService companyService) 
	{
		this.companyService = companyService;
	}
	
	@PostMapping("login")
	@UnAuthenticated
	@Override
	public boolean login(@Valid @RequestBody LoginRequest loginRequest) 
	{
		return this.companyService.login(loginRequest.getEmail(), loginRequest.getPassword());
	}
	
	@GetMapping(CATEGORY_GET_URL + "all")
	public List<Category> getAllCategories()
	{
		return this.companyService.getAllCategories();
	}
	
	@PostMapping(COUPON_POST_URL)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Coupon addCoupon(@Valid @RequestBody Coupon coupon)
	{
		return this.companyService.addCoupon(coupon);
	}
	
	@GetMapping(COUPON_GET_URL + "{couponId}")
	public Coupon getCoupon(@PathVariable("couponId") int couponId)
	{
		return this.companyService.getCoupon(couponId);
	}
	
	@GetMapping(COUPON_GET_URL + "all")
	public List<Coupon> getCompanyCoupons()
	{
		return this.companyService.getCompanyCoupons();
	}
	
	@GetMapping(COUPON_GET_URL + "paged")
	public Page<Coupon> getCompanyCoupons( // All of the companie's coupons paged
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
	{
		return this.companyService.getCompanyCoupons(pageIndex, pageSize);
	}
	
	@GetMapping(COUPON_GET_URL + "pagedSorted")
	public Page<Coupon> getCompanyCoupons( // All of the companie's coupons, paged and sorted
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = DEFAULT_COUPON_SORT) String sortBy,
			@RequestParam(name = SORT_ORDER, defaultValue = DEFAULT_SORT_ORDER) boolean asc)
	{
		return this.companyService.getCompanyCoupons(pageIndex, pageSize, sortBy, asc);
	}
	
	@GetMapping(COUPON_GET_URL + "example")
	public List<CouponSearchResult> getCompanyCoupons( // All of the companie's coupons by name example, used mainly for searching
			@RequestParam(name = RESULTS_COUNT, defaultValue = DEFAULT_PAGE_SIZE) int resultsCount,
			@RequestParam(name = NAME_EXAMPLE) String nameExample)
	{
		return this.companyService.getCompanyCoupon(resultsCount, nameExample);
	}
	
	@GetMapping("getDetails")
	public Company getDetails()
	{
		return this.companyService.getDetails();
	}
	
	@PutMapping(COUPON_PUT_URL)
	public void updateCoupon(@Valid @RequestBody Coupon coupon)
	{
		this.companyService.updateCoupon(coupon);
	}
	
	@DeleteMapping(COUPON_DELETE_URL + "{couponId}")
	public void deleteCoupon(@PathVariable("couponId") int couponId)
	{
		this.companyService.deleteCoupon(couponId);
	}
	
}