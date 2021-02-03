package local.coupersb.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import local.coupersb.model.Coupon;
import local.coupersb.model.Customer;
import local.coupersb.model.search.CustomerSearchResult;

public interface CustomerRepository extends JpaRepository<Customer, Integer>
{
	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByEmailIgnoreCase(String email);
	
	Optional<Customer> findByEmailAndPassword(String email, String password);
	
	Optional<Customer> findByIdAndPurchasedCoupons(int id, Coupon coupon);
	
	List<CustomerSearchResult> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
			String firstNameExample, String lastNameExample, Pageable pageable);
}