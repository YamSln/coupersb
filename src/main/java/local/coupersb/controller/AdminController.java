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
import local.coupersb.model.Customer;
import local.coupersb.model.search.CompanySearchResult;
import local.coupersb.model.search.CustomerSearchResult;
import local.coupersb.security.annotations.AdminRequest;
import local.coupersb.security.annotations.UnAuthenticated;
import local.coupersb.security.payload.LoginRequest;
import local.coupersb.service.AdminService;
import static local.coupersb.util.UrlUtils.*;

@AdminRequest
@RestController
@RequestMapping("${application.url}admin")
public class AdminController extends ClientController
{	
	private final AdminService adminService;
	
	@Autowired
	public AdminController(AdminService adminService) 
	{
		this.adminService = adminService;
	}
	
	@PostMapping("login")
	@UnAuthenticated
	@Override
	public boolean login(@Valid @RequestBody LoginRequest loginRequest) 
	{
		return this.adminService.login(loginRequest.getEmail(), loginRequest.getPassword());
	}
	
	@PostMapping(CATEGORY_POST_URL)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Category addCategory(@Valid @RequestBody Category category)
	{
		return this.adminService.createCategory(category);
	}
	
	@GetMapping(CATEGORY_GET_URL + "{categoryId}")
	public Category getCategory(@PathVariable("categoryId") int categoryId)
	{
		return this.adminService.getCategory(categoryId);
	}
	
	@GetMapping(CATEGORY_GET_URL + "all")
	public List<Category> getAllCategories()
	{
		return this.adminService.getAllCategories();
	}
	
	@PutMapping(CATEGORY_PUT_URL)
	public void updateCategory(@Valid @RequestBody Category category)
	{
		this.adminService.updateCategory(category);
	}
	
	@DeleteMapping(CATEGORY_DELETE_URL + "{categoryId}")
	public void deleteCategory(@PathVariable("categoryId") int categoryId)
	{
		this.adminService.deleteCategory(categoryId);
	}
	
	@PostMapping(COMPANY_POST_URL)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Company addCompany(@Valid @RequestBody Company company)
	{
		return this.adminService.createCompany(company);
	}
	
	@GetMapping(COMPANY_GET_URL + "{companyId}")
	public Company getCompany(@PathVariable("companyId") int companyId)
	{
		return this.adminService.getCompany(companyId);
	}
	
	@GetMapping(COMPANY_GET_URL + "all")
	public List<Company> getAllCompanies()
	{
		return this.adminService.getAllCompanies();
	}
	
	@GetMapping(COMPANY_GET_URL + "paged")
	public Page<Company> getAllCompanies( // All companies paged
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
	{
		return this.adminService.getAllCompanies(pageIndex, pageSize);
	}
	
	@GetMapping(COMPANY_GET_URL + "pagedSorted")
	public Page<Company> getAllCompanies( // All companies, paged and sorted
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = DEFAULT_COMPANY_SORT) String sortBy,
			@RequestParam(name = SORT_ORDER, defaultValue = DEFAULT_SORT_ORDER) boolean asc)
	{
		return this.adminService.getAllCompanies(pageIndex, pageSize, sortBy, asc);
	}
	
	@GetMapping(COMPANY_GET_URL + "example")
	public List<CompanySearchResult> getAllCompanies( // Companies by name example, used mainly for searching
			@RequestParam(name = RESULTS_COUNT, defaultValue = DEFAULT_PAGE_SIZE) int resultsCount,
			@RequestParam(name = NAME_EXAMPLE) String nameExample)
	{
		return this.adminService.getAllCompanies(resultsCount, nameExample);
	}
	
	@PutMapping(COMPANY_PUT_URL)
	public void updateCompany(@Valid @RequestBody Company company)
	{
		this.adminService.updateCompany(company);
	}
	
	@DeleteMapping(COMPANY_DELETE_URL + "{companyId}")
	public void deleteCompany(@PathVariable("companyId") int companyId)
	{
		this.adminService.deleteCompany(companyId);
	}
	
	@PostMapping(CUSTOMER_POST_URL)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Customer addCustomer(@Valid @RequestBody Customer customer)
	{
		return this.adminService.createCustomer(customer);
	}
	
	@GetMapping(CUSTOMER_GET_URL + "{customerId}")
	public Customer getCustomer(@PathVariable("customerId") int customerId)
	{
		return this.adminService.getCustomer(customerId);
	}
	
	@GetMapping(CUSTOMER_GET_URL + "all")
	public List<Customer> getAllCustomers()
	{
		return this.adminService.getAllCustomers();
	}
	
	@GetMapping(CUSTOMER_GET_URL + "paged")
	public Page<Customer> getAllCustomers( // All customers paged
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
	{
		return this.adminService.getAllCustoemrs(pageIndex, pageSize);
	}
	
	@GetMapping(CUSTOMER_GET_URL + "pagedSorted")
	public Page<Customer> getAllCustomers( // All customers, paged and sorted
			@RequestParam(name = PAGE_INDEX, defaultValue = DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = DEFAULT_CUSTOMER_SORT) String sortBy,
			@RequestParam(name = SORT_ORDER, defaultValue = DEFAULT_SORT_ORDER) boolean asc)
	{
		return this.adminService.getAllCustoemrs(pageIndex, pageSize, sortBy, asc);
	}
	
	@GetMapping(CUSTOMER_GET_URL + "example")
	public List<CustomerSearchResult> getAllCustomers( // Customers by name example, used mainly for searching
			@RequestParam(name = RESULTS_COUNT, defaultValue = DEFAULT_PAGE_SIZE) int resultsCount,
			@RequestParam(name = NAME_EXAMPLE) String nameExample)
	{
		return this.adminService.getAllCustomers(resultsCount, nameExample);
	}
	
	@PutMapping(CUSTOMER_PUT_URL)
	public void updateCustomer(@Valid @RequestBody Customer customer)
	{
		this.adminService.updateCustomer(customer);
	}
	
	@DeleteMapping(CUSTOMER_DELETE_URL + "{customerId}")
	public void deleteCustomer(@PathVariable("customerId") int customerId)
	{
		this.adminService.deleteCustomer(customerId);
	}
	
}