package local.coupersb.util;

public class UrlUtils 
{
	private UrlUtils() { }
	
	public static final String CATEGORY_URL = "category/";
	public static final String COMPANY_URL = "company/";
	public static final String CUSTOMER_URL = "customer/";
	public static final String COUPON_URL = "coupon/";
	public static final String ALL_COUPON_URL = "allCoupon/";
	
	public static final String POST_URL = "add";
	public static final String PUT_URL = "update";
	public static final String DELETE_URL = "delete/";
	public static final String GET_URL = "get/";
	
	public static final String CATEGORY_POST_URL = CATEGORY_URL + POST_URL;
	public static final String CATEGORY_PUT_URL = CATEGORY_URL + PUT_URL;
	public static final String CATEGORY_DELETE_URL = CATEGORY_URL + DELETE_URL;
	public static final String CATEGORY_GET_URL = CATEGORY_URL + GET_URL;
	
	public static final String COMPANY_POST_URL = COMPANY_URL + POST_URL;
	public static final String COMPANY_PUT_URL = COMPANY_URL + PUT_URL;
	public static final String COMPANY_DELETE_URL = COMPANY_URL + DELETE_URL;
	public static final String COMPANY_GET_URL = COMPANY_URL + GET_URL;
	
	public static final String CUSTOMER_POST_URL = CUSTOMER_URL + POST_URL;
	public static final String CUSTOMER_PUT_URL = CUSTOMER_URL + PUT_URL;
	public static final String CUSTOMER_DELETE_URL = CUSTOMER_URL + DELETE_URL;
	public static final String CUSTOMER_GET_URL = CUSTOMER_URL + GET_URL;
	
	public static final String COUPON_POST_URL = COUPON_URL + POST_URL;
	public static final String COUPON_PUT_URL = COUPON_URL + PUT_URL;
	public static final String COUPON_DELETE_URL = COUPON_URL + DELETE_URL;
	public static final String COUPON_GET_URL = COUPON_URL + GET_URL;
	public static final String ALL_COUPONS_GET_URL = ALL_COUPON_URL + GET_URL;
}