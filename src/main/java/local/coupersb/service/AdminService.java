package local.coupersb.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import local.coupersb.dao.CategoryRepository;
import local.coupersb.dao.CompanyRepository;
import local.coupersb.dao.CouponRepository;
import local.coupersb.dao.CustomerRepository;
import local.coupersb.exceptions.types.DuplicationException;
import local.coupersb.exceptions.types.NoSuchElementException;
import local.coupersb.exceptions.types.UpdateViolationException;
import local.coupersb.model.Category;
import local.coupersb.model.Company;
import local.coupersb.model.Coupon;
import local.coupersb.model.Customer;
import local.coupersb.model.search.CompanySearchResult;
import local.coupersb.model.search.CustomerSearchResult;
import local.coupersb.security.AuthenticationManager;
import local.coupersb.util.Capitalizer;
import local.coupersb.util.PageBuilder;

@Service
@Transactional
public class AdminService extends ClientService
{	
	@Autowired
	AdminService(
			CouponRepository couponRepository, 
			CategoryRepository categoryRepository, 
			CompanyRepository companyRepository, 
			CustomerRepository customerRepository,
			AuthenticationManager authenticationManager)
	{
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
		this.customerRepository = customerRepository;
		this.categoryRepository = categoryRepository;
		this.authenticationManager = authenticationManager;
	}
	
	public Category createCategory(Category category)
	{
		this.categoryRepository
			.findByName(category.getName())
			.ifPresent(this::throwCategoryExistsException);
		category.setName(Capitalizer.capitalizeFullyFormat(category.getName()));
		return this.categoryRepository.save(category);
	}
	
	public Category getCategory(int categoryId)
	{
		return this.categoryRepository
				.findById(categoryId)
				.orElseThrow(() -> new NoSuchElementException("Category specified not found"));
	}
	
	public List<Category> getAllCategories()
	{
		return this.categoryRepository.findAll();
	}
	
	public void updateCategory(Category category)
	{
		Category categoryToUpdate = this.categoryRepository
			.findById(category.getId())
			.orElseThrow(() -> new UpdateViolationException("Cannot update id"));
		// Check if there is a category with the same name
		if(!categoryToUpdate.getName().equalsIgnoreCase(category.getName()))
			this.categoryRepository
			.findByNameIgnoreCase(category.getName())
			.ifPresent(this::throwCategoryExistsException);
		
		categoryToUpdate.setName(Capitalizer.capitalizeFullyFormat(category.getName()));
		this.categoryRepository.save(categoryToUpdate);
	}
	
	public void deleteCategory(int categoryId)
	{
		this.categoryRepository
			.findById(categoryId)
			.orElseThrow(() -> new NoSuchElementException("Category specified not found"));
		// Check if category is in use by a coupon
		this.couponRepository.findByCategoryId(categoryId).ifPresent(this::throwCategoryInUseException);
		
		this.categoryRepository.deleteById(categoryId);
	}
	
	public Company createCompany(Company company)
	{
		validateCompany(company);
		company.setName(Capitalizer.capitalizeFullyFormat(company.getName()));
		company.setEmail(company.getEmail().toLowerCase());
		return this.companyRepository.save(company);		
	}
	
	public Company getCompany(int companyId)
	{
		return this.companyRepository
				.findById(companyId)
				.orElseThrow(() -> new NoSuchElementException("Company specified not found"));
	}
	
	public List<Company> getAllCompanies()
	{
		return this.companyRepository.findAll();
	}
	
	public Page<Company> getAllCompanies(int pageIndex, int pageSize)
	{ // Page request of companies
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
		// Get all companies from database as a page
		Page<Company> pageResult = this.companyRepository.findAll(pageRequest);
		// Build and return the page result
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public Page<Company> getAllCompanies(int pageIndex, int pageSize, String sortBy, boolean asc)
	{ // Page request of companies - sorted
		Sort sort = Sort.by(sortBy);
		sort = asc ? sort.ascending() : sort.descending();
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
		// Get all companies from database as a page
		Page<Company> pageResult = this.companyRepository.findAll(pageRequest);
		// Build and return the page result
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public List<CompanySearchResult> getAllCompanies(int resultsCount, String nameExample)
	{ // Page request of companies, sorted by name
		Sort sort = Sort.by(NAME_SORT_KEY).ascending();
		PageRequest pageRequest = PageRequest.of(0, resultsCount, sort);
		// Get all companies matching the name example
		return this.companyRepository.findAllByNameContainingIgnoreCase(nameExample, pageRequest);
	}
	
	public void updateCompany(Company company)
	{
		Company companyToUpdate = this.companyRepository
			.findById(company.getId())
			.orElseThrow(() -> new UpdateViolationException("Cannot update id"));
		
		if(!companyToUpdate.getName().equalsIgnoreCase(company.getName()))
			validateCompanyName(company); // Company name is updated
		if(!companyToUpdate.getEmail().equalsIgnoreCase(company.getEmail()))
			validateCompanyEmail(company); // Company email is updated
		if(!Strings.isNullOrEmpty(company.getPassword())) // Password changed
			companyToUpdate.setPassword(company.getPassword());
		
		companyToUpdate.setName(Capitalizer.capitalizeFullyFormat(company.getName()));
		companyToUpdate.setEmail(company.getEmail().toLowerCase());
		
		this.companyRepository.save(companyToUpdate);
	}
	
	public void deleteCompany(int companyId)
	{
		this.companyRepository
			.findById(companyId)
			.orElseThrow(() -> new NoSuchElementException("Company specified not found"));
		
		this.companyRepository.deleteById(companyId);
	}
	
	public Customer createCustomer(Customer customer)
	{
		this.customerRepository.findByEmail(customer.getEmail())
			.ifPresent(this::throwCustomerExistsByEmailException);
		customer.setFirstName(Capitalizer.capitalizeFullyFormat(customer.getFirstName()));
		customer.setLastName(Capitalizer.capitalizeFullyFormat(customer.getLastName()));
		customer.setEmail(customer.getEmail().toLowerCase());
		return this.customerRepository.save(customer);
	}
	
	public Customer getCustomer(int customerId)
	{
		return this.customerRepository
				.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer specified not found"));
	}
	
	public List<Customer> getAllCustomers()
	{
		return this.customerRepository.findAll();
	}
	
	public Page<Customer> getAllCustoemrs(int pageIndex, int pageSize)
	{ // Page request for customers
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
		// Get all customers from database as a page
		Page<Customer> pageResult = this.customerRepository.findAll(pageRequest);
		// Build and return the page result
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public Page<Customer> getAllCustoemrs(int pageIndex, int pageSize, String sortBy, boolean asc)
	{ // Page request for customers - sorted
		Sort sort = Sort.by(sortBy);
		sort = asc ? sort.ascending() : sort.descending();
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
		// Get all customers from database as a page
		Page<Customer> pageResult = this.customerRepository.findAll(pageRequest);
		// Build and return the page result
		return PageBuilder.buildPageResult(pageResult, pageRequest);
	}
	
	public List<CustomerSearchResult> getAllCustomers(int resultsCount, String nameExample)
	{ // Page request of customers, sorted by first name and last name
		Sort sort = Sort.by(FIRST_NAME_SORT_KEY, LAST_NAME_SORT_KEY).descending();
		PageRequest pageRequest = PageRequest.of(0, resultsCount, sort);
		// Get all customers matching the name example
		return this.customerRepository
				.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
						nameExample, nameExample, pageRequest);
	}
	
	public void updateCustomer(Customer customer)
	{
		Customer customerToUpdate = this.customerRepository
			.findById(customer.getId())
			.orElseThrow(() -> new UpdateViolationException("Cannot update id"));
		
		if(!customerToUpdate.getEmail().equalsIgnoreCase(customer.getEmail()))
			validateCustomerEmail(customer); // Customer email is updated
		if(!Strings.isNullOrEmpty(customer.getPassword())) // Password changed
			customerToUpdate.setPassword(customer.getPassword());
	
		customerToUpdate.setFirstName(Capitalizer.capitalizeFullyFormat(customer.getFirstName()));
		customerToUpdate.setLastName(Capitalizer.capitalizeFullyFormat(customer.getLastName()));
		customerToUpdate.setEmail(customer.getEmail().toLowerCase());
		
		this.customerRepository.save(customerToUpdate);
	}
	
	public void deleteCustomer(int customerId)
	{
		this.customerRepository
			.findById(customerId)
			.orElseThrow(() -> new NoSuchElementException("Customer specified not found"));
		
		this.customerRepository.deleteById(customerId);
	}

	@Override
	public boolean login(String email, String password) 
	{
		return this.authenticationManager.login(email, password, ClientType.ADMINISTRATOR);
	}
	
	private void validateCompany(Company company)
	{
		validateCompanyName(company);
		validateCompanyEmail(company);		
	}
	
	private void validateCompanyName(Company company)
	{
		this.companyRepository.findByNameIgnoreCase(company.getName())
			.ifPresent(this::throwCompanyExistsByNameException);
	}
	
	private void validateCompanyEmail(Company company)
	{
		this.companyRepository.findByEmailIgnoreCase(company.getEmail())
			.ifPresent(this::throwCompanyExistsByEmailException);
	}
	
	private void validateCustomerEmail(Customer customer)
	{
		this.customerRepository
			.findByEmailIgnoreCase(customer.getEmail())
			.ifPresent(this::throwCustomerExistsByEmailException);
	}
	
	private void throwCategoryExistsException(Category exceptionCategory)
	{
		throw new DuplicationException(
				String.format("Category with the name %s already exists", exceptionCategory.getName()));
	}
	
	private void throwCategoryInUseException(Coupon coupon)
	{
		throw new UpdateViolationException(
				String.format("Category %s is used by at least one coupon in the system", coupon.getCategory().getName()));
	}
	
	private void throwCompanyExistsByNameException(Company exceptionComapny)
	{
		throw new DuplicationException(
				String.format("Company with the name %s already exists", exceptionComapny.getName()));
	}
	
	private void throwCompanyExistsByEmailException(Company exceptionComapny)
	{
		throw new DuplicationException(
				String.format("Company with the email %s already exists", exceptionComapny.getEmail()));
	}
	
	private void throwCustomerExistsByEmailException(Customer exceptionCustomer)
	{
		throw new DuplicationException(
				String.format("Customer with the email %s already exists", exceptionCustomer.getEmail()));
	}

}