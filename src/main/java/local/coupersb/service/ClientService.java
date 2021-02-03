package local.coupersb.service;

import org.springframework.stereotype.Service;

import local.coupersb.dao.CategoryRepository;
import local.coupersb.dao.CompanyRepository;
import local.coupersb.dao.CouponRepository;
import local.coupersb.dao.CustomerRepository;
import local.coupersb.security.AuthenticationManager;

@Service
public abstract class ClientService 
{
	protected final static String NAME_SORT_KEY = "name";
	protected final static String FIRST_NAME_SORT_KEY = "firstName";
	protected final static String LAST_NAME_SORT_KEY = "lastName";
	protected final static String COUPON_TITLE_SORT_KEY = "title";
	
	protected CompanyRepository companyRepository;
	protected CustomerRepository customerRepository;
	protected CouponRepository couponRepository;
	protected CategoryRepository categoryRepository;
	
	protected AuthenticationManager authenticationManager;
	
	public abstract boolean login(String email, String password);
}