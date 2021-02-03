package local.coupersb.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import local.coupersb.model.Company;
import local.coupersb.model.search.CompanySearchResult;

public interface CompanyRepository extends JpaRepository<Company, Integer>
{
	boolean existsByEmailAndPassword(String email, String password);
	
	Optional<Company> findByName(String name);
	
	Optional<Company> findByNameIgnoreCase(String name);
	
	Optional<Company> findByEmail(String email);
	
	Optional<Company> findByEmailIgnoreCase(String email);
	
	Optional<Company> findByIdAndName(int id, String name);
	
	Optional<Company> findByEmailAndPassword(String email, String password);
	
	List<CompanySearchResult> findAllByNameContainingIgnoreCase(String nameExample, Pageable pagable);
}