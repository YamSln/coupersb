package local.coupersb.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import local.coupersb.model.Coupon;
import local.coupersb.model.search.CouponSearchResult;

public interface CouponRepository extends JpaRepository<Coupon, Integer>
{
	@Modifying
	@Transactional
	@Query("delete from coupon c where c.endDate < current_timestamp")
	void deleteExpieredCoupons();
	
	Optional<Coupon> findByCategoryId(int categoryId);
	
	Optional<Coupon> findByIdAndOwnerCompanyId(int id, int ownerCompanyId);

	Optional<Coupon> findByOwnerCompanyIdAndTitle(int ownerCompanyId, String title);
	
	Optional<Coupon> findByIdAndPurchasingCustomersId(int couponId, int customerId);
	
	List<Coupon> findAllByShoppingCustomersId(int customerId);
	
	List<Coupon> findAllByOwnerCompanyId(int ownerCompanyId);
	
	List<Coupon> findAllByPurchasingCustomersId(int customerId);
	
	Page<Coupon> findAllByOwnerCompanyId(int ownerCompanyId, Pageable pageable);
	
	Page<Coupon> findAllByPurchasingCustomersId(int customerId, Pageable pageable);
	
	Page<Coupon> findAllByPurchasingCustomersNullOrPurchasingCustomersIdNot(int customerId, Pageable pagable);
	
	Page<Coupon> findAllByPurchasingCustomersNullOrPurchasingCustomersIdNotAndPriceLessThanEqual(
			int customerId, double maxPrice, Pageable pageable);
	
	List<CouponSearchResult> findAllByPurchasingCustomersNullAndTitleContainingIgnoreCaseOrPurchasingCustomersIdNotAndTitleContainingIgnoreCase(
			String example, int customerId, String titleExample, Pageable pageable);
	
	List<CouponSearchResult> findAllByOwnerCompanyIdAndTitleContainingIgnoreCase(
			int ownerCompanyId, String titleExample, Pageable pageable);
	
	List<CouponSearchResult> findAllByPurchasingCustomersIdAndTitleContainingIgnoreCase(
			int customerId, String titleExample, Pageable pageable);
}