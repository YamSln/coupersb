package local.coupersb.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

import local.coupersb.model.Coupon;

import static local.coupersb.validation.CouponValidationResult.*;

/**
 * CouponPurchaseValidator is a functional interface that allows chained coupons
 * validation with customer results.
 * 
 * @author YAM
 *
 */
public interface CouponValidator extends Function<Coupon, CouponValidationResult>
{
	/**
	 * Returns {@code CouponValidationResult.SUCCESS} if the coupon is in stock
	 * 
	 * @return {@code CouponValidationResult.SUCCESS} if the coupon is in stock
	 */
	public static CouponValidator isAvailable()
	{
		return coupon -> coupon.isAvailable() ? SUCCESS : OUT_OF_STOCK;
	}
	
	/**
	 * Returns {@code CouponValidationResult.SUCCESS} if the coupon is not expired
	 * 
	 * @return {@code CouponValidationResult.SUCCESS} if the coupon is not expired
	 */
	public static CouponValidator isNotExpiered()
	{
		return coupon -> !coupon.isExpiered() ? SUCCESS : OUTDATED;
	}
	
	/**
	 * Returns {@code CouponValidationResult.SUCCESS} if the coupon price is valid (greater than 0)
	 *  
	 * @return {@code CouponValidationResult.SUCCESS} if the coupon price is valid (greater than 0)
	 */
	public static CouponValidator isPriceValid()
	{
		return coupon -> coupon.getPrice() > 0 ? SUCCESS : INVALID_PRICE;
	}
	
	/**
	 * Returns {@code CouponValidationResult.SUCCESS} if the coupon start date
	 * is valid (not before current date)
	 * 
	 * @return {@code CouponValidationResult.SUCCESS} if the coupon start date
	 * is valid (not before current date)
	 */
	public static CouponValidator isStartDateValid()
	{
		ZonedDateTime today = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		Date now = new Date(today.toEpochSecond() * 1000);
		return coupon -> !coupon.getStartDate().before(now) &&
				!coupon.getStartDate().after(coupon.getEndDate()) ? 
						SUCCESS : INVALID_START_DATE;
	}
	
	/**
	 * Returns {@code CouponValidationResult.SUCCESS} if the coupon end date
	 * is valid (not before now and not before start date)
	 * 
	 * @return Returns {@code CouponValidationResult.SUCCESS} if the coupon end date
	 * is valid (not before now and not before start date)
	 */
	public static CouponValidator isEndDateValid()
	{
		return coupon -> !coupon.isExpiered() &&
				!coupon.getEndDate().before(coupon.getStartDate()) ?
						SUCCESS : INVALID_END_DATE;
	}
	
	/**
	 * Returns {@code CouponPurchaseValidator} if the result from apply method on this
	 * is {@code CouponValidationResult.SUCCESS}, otherwise returns the custom result
	 * 
	 * @param other the next {@code CouponPurchaseValidator} to validated in the chain
	 * 
	 * @return {@code CouponPurchaseValidator} if the result from apply method on this
	 * is {@code CouponValidationResult.SUCCESS}, otherwise returns the custom result
	 */
	public default CouponValidator and(CouponValidator other)
	{
		return coupon -> 
		{ // Result from this validation
			CouponValidationResult result = this.apply(coupon);
			return result == SUCCESS ? // Successful validation?
					other.apply(coupon) : // Validation success!, continue chain
					result; // Validation failure..., return custom result
		};
	}
}
