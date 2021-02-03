package local.coupersb.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import local.coupersb.dao.CategoryRepository;
import local.coupersb.dao.CompanyRepository;
import local.coupersb.dao.CouponRepository;
import local.coupersb.exceptions.types.AvailabilityException;
import local.coupersb.exceptions.types.DateException;
import local.coupersb.exceptions.types.DuplicationException;
import local.coupersb.exceptions.types.NoSuchElementException;
import local.coupersb.exceptions.types.PriceException;
import local.coupersb.exceptions.types.UpdateViolationException;
import local.coupersb.model.Category;
import local.coupersb.model.Company;
import local.coupersb.model.Coupon;
import local.coupersb.model.search.CouponSearchResult;
import local.coupersb.security.AuthenticationManager;
import local.coupersb.util.Capitalizer;
import local.coupersb.util.PageBuilder;
import local.coupersb.validation.CouponValidationResult;

import static local.coupersb.validation.CouponValidator.*;

@Service
@Transactional
public class CompanyService extends ClientService
{
	@Autowired
	CompanyService(
			CategoryRepository categoryRepository,
			CompanyRepository companyRepository, 
			CouponRepository couponRepository,
			AuthenticationManager authenticationManager) 
	{
		this.categoryRepository = categoryRepository;
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
		this.authenticationManager = authenticationManager;
	}
	
	public Coupon addCoupon(Coupon coupon)
	{
		this.couponRepository // Coupon with same title exist?
			.findByOwnerCompanyIdAndTitle(authenticationManager.currentUserId(), coupon.getTitle())
			.ifPresent(this::throwCouponExistsForCompanyException);
		
		couponValidation(coupon, true); // Validate the coupon before saving
		coupon.setTitle(Capitalizer.capitalizeFullyFormat(coupon.getTitle()));
		coupon.setOwnerCompany(this.getDetails());
		
		return this.couponRepository.save(coupon);
	}
	
	public Coupon getCoupon(int couponId)
	{
		return this.couponRepository
				.findByIdAndOwnerCompanyId(couponId, this.authenticationManager.currentUserId())
				.orElseThrow(() -> new NoSuchElementException("Coupon does not exist for company"));
	}
	
	public List<Coupon> getCompanyCoupons()
	{
		return this.couponRepository.findAllByOwnerCompanyId(this.authenticationManager.currentUserId());
	}
	
	public Page<Coupon> getCompanyCoupons(int pageIndex, int pageSize)
	{ // Page request of company coupons
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
		// Get all coupons from database as a page
		Page<Coupon> pageResult = this.couponRepository
				.findAllByOwnerCompanyId(authenticationManager.currentUserId(), pageRequest);
		// Build and return the page
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public Page<Coupon> getCompanyCoupons(int pageIndex, int pageSize, String sortBy, boolean asc)
	{ // Page request of customer coupons - sorted
		Sort sort = Sort.by(sortBy);
		sort = asc ? sort.ascending() : sort.descending();
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
		// Get all coupons from database as a page
		Page<Coupon> pageResult = this.couponRepository
				.findAllByOwnerCompanyId(authenticationManager.currentUserId(), pageRequest);
		// Build and return the page
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public List<CouponSearchResult> getCompanyCoupon(int resultsCount, String nameExample)
	{ // Page request of customer coupons, sorted by title
		Sort sort = Sort.by(COUPON_TITLE_SORT_KEY).ascending();
		PageRequest pageRequest = PageRequest.of(0, resultsCount, sort);
		// Get all coupons matching the name example
		return this.couponRepository.findAllByOwnerCompanyIdAndTitleContainingIgnoreCase(
				this.authenticationManager.currentUserId(), nameExample, pageRequest);
	}
	
	public Company getDetails()
	{
		return this.companyRepository
				.findById(this.authenticationManager.currentUserId())
				.orElseThrow(() -> new NoSuchElementException("Company specified not found"));
	}
	
	public void updateCoupon(Coupon coupon)
	{
		Coupon couponToUpdate = this.couponRepository // Coupon exist?
			.findByIdAndOwnerCompanyId(coupon.getId(), authenticationManager.currentUserId())
			.orElseThrow(() -> new UpdateViolationException("Cannot update id"));
		
		if(!coupon.getTitle().equalsIgnoreCase(couponToUpdate.getTitle()))
			validateCouponTitle(coupon); // Coupon title is updated
		// Is start date updated?
		boolean startDateUpdated = !coupon.getStartDate().equals(couponToUpdate.getStartDate());
		couponValidation(coupon, startDateUpdated); // Validate the coupon before saving
		
		couponToUpdate.setTitle(Capitalizer.capitalizeFullyFormat(coupon.getTitle()));
		couponToUpdate.setDescription(coupon.getDescription());
		couponToUpdate.setStartDate(coupon.getStartDate());
		couponToUpdate.setEndDate(coupon.getEndDate());
		couponToUpdate.setCategory(coupon.getCategory());
		couponToUpdate.setQuantity(coupon.getQuantity());
		couponToUpdate.setPrice(coupon.getPrice());
		couponToUpdate.setImagePath(coupon.getImagePath());
		
		this.couponRepository.save(couponToUpdate);
	}
	
	public void deleteCoupon(int couponId)
	{
		this.couponRepository
				.findByIdAndOwnerCompanyId(couponId, this.authenticationManager.currentUserId())
				.orElseThrow(() -> new NoSuchElementException("Coupon specified does not exist"));
		
		this.couponRepository.deleteById(couponId);
	}
	
	public List<Category> getAllCategories()
	{
		return this.categoryRepository.findAll();
	}
	
	@Override
	public boolean login(String email, String password) 
	{
		return this.authenticationManager.login(email, password, ClientType.COMPANY);
	}
	
	private void couponValidation(Coupon coupon, boolean startDateUpdated)
	{ // Validation chain
		CouponValidationResult result = isAvailable()
				.and(isPriceValid())
				.and(isEndDateValid())
				.apply(coupon);
		// If start date did not get updated, do not validate it
		result = startDateUpdated ? isStartDateValid().apply(coupon) : CouponValidationResult.SUCCESS;
		
		switch(result)
		{ // Validation result handling
			case OUT_OF_STOCK:
				throw new AvailabilityException("New coupon's quantity cannot be set to negative value");
			case INVALID_PRICE:
				throw new PriceException("Coupons price cannot be negative");
			case INVALID_START_DATE:
				throw new DateException(
						"Start date of a coupon cannot be before current date or after its end date");
			case INVALID_END_DATE:
				throw new DateException(
						"End date of a coupon cannot be before current date and its start date");
			case SUCCESS:
			default:
				return;
		}
	}
	
	private void validateCouponTitle(Coupon coupon)
	{
		this.couponRepository // Coupon with same title exist?
			.findByOwnerCompanyIdAndTitle(authenticationManager.currentUserId(), coupon.getTitle())
			.ifPresent(this::throwCouponExistsForCompanyException);
	}
	
	private void throwCouponExistsForCompanyException(Coupon coupon)
	{
		throw new DuplicationException(
				String.format("Coupon with title %s alrady exists for company", coupon.getTitle()));
	}
	
}