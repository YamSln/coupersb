package local.coupersb.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import local.coupersb.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>
{
	Optional<Category> findByName(String name);
	
	Optional<Category> findByNameIgnoreCase(String name);
}