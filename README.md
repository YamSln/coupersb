# coupersb

## Part of the couper project, my final project during college studies :mortar_board:
Spring boot version of the couper project.

Intoruduces learned technologies and skills:

- System overall design.
- DAO implementation using Spring Data JPA.
- Service layer with Spring Service.
- Rest Controllers for web endpoints.

With an addition of several more features and technologies:

- Security and session management using Spring AOP.
```java
@Before("isAdministrator()")
public void handleAdminRequest()
{ // Handels admin request using session data
  if(handleRequest(this.httpSession, ClientType.ADMINISTRATOR))
    return;
  throw new UnauthorizedException("User is not admin");
}
```
- Business logic and bean validation using custom functional style validators.
```java
public interface CouponValidator extends Function<Coupon, CouponValidationResult>
{
	public static CouponValidator isAvailable()
	{
		return coupon -> coupon.isAvailable() ? SUCCESS : OUT_OF_STOCK;
	}
  ...
}
```
- Pagination and sorting mechanisms.
```java
@GetMapping(COUPON_GET_URL + "pagedSorted")
public Page<Coupon> getCustomerCoupons( // All of the customer's coupons paged
    @RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
    @RequestParam(name = SORT_BY, defaultValue = DEFAULT_COUPON_SORT) String sortBy,
    @RequestParam(name = SORT_ORDER, defaultValue = DEFAULT_SORT_ORDER) boolean asc)
{
  return this.customerService.getCustomerCoupons(pageIndex, pageSize, sortBy, asc);
}
```
- Final deployment to Heroku Cloud Platform.
