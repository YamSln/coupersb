package local.coupersb.controller;

import local.coupersb.security.payload.LoginRequest;

public abstract class ClientController 
{
	protected static final String PAGE_INDEX = "pageIndex";
	protected static final String PAGE_SIZE = "pageSize";
	protected static final String SORT_BY = "sortBy";
	protected static final String SORT_ORDER = "asc";
	protected static final String RESULTS_COUNT = "resultsCount";
	protected static final String NAME_EXAMPLE = "nameExample";
	protected static final String MAX_PRICE = "maxPrice";
	
	protected static final String DEFAULT_PAGE_INDEX = "0";
	protected static final String DEFAULT_PAGE_SIZE = "5";
	protected static final String DEFAULT_PRICE_FILTER = "0";
	
	protected static final String DEFAULT_SORT_ORDER = "true";
	protected static final String DEFAULT_COMPANY_SORT = "name";
	protected static final String DEFAULT_CUSTOMER_SORT = "firstName";
	protected static final String DEFAULT_COUPON_SORT = "title";
	
	public abstract boolean login(LoginRequest loginRequest);
}
