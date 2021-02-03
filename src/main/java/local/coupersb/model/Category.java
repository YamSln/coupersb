package local.coupersb.model;

import static local.coupersb.validation.ValidationUtils.CATEGORY_NAME_BLANK_MESSAGE;
import static local.coupersb.validation.ValidationUtils.CATEGORY_NAME_SIZE_MESSAGE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;;

/**
 * A coupon category
 * 
 * @author YAM
 *
 */

@Entity(name = "category")
@Table(name = "categories")
public class Category 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@NotBlank(message = CATEGORY_NAME_BLANK_MESSAGE)
	@Size(min = 2, max = 25, message = CATEGORY_NAME_SIZE_MESSAGE)
	@Column(name = "name", nullable = false, unique = true, length = 25)
	private String name;
	
	public Category() 
	{
		
	}
	
	/**
	 * Crates a new category with a given name
	 * 
	 * @param categoryName name to set to the new category
	 */
	public Category(String categoryName)
	{
		this.name = categoryName;
	}
	
	/**
	 * Crates a category with a given id and name
	 * 
	 * @param id the id of the category
	 * @param categoryName the name of the category
	 */
	public Category(int id, String categoryName)
	{
		setId(id);
		this.name = categoryName;
	}
	
	/**
	 * Returns the category name
	 * 
	 * @return the category name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Sets the category name
	 * 
	 * @param name name to set to the category
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Returns the category id
	 * 
	 * @return the category id
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Sets the id of the category
	 * 
	 * @param id the id to set to the category
	 */
	public void setId(int id)
	{
		if(id > 0)
			this.id = id;
	}

	@Override
	public String toString() 
	{
		return "Category [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}