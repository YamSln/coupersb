package local.coupersb.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import local.coupersb.exceptions.types.NoSuchElementException;

public class PageBuilder 
{
	private PageBuilder()
	{ 
		
	}
	
	/**
	 * Returns a page result of a given page and page request
	 * @param <T> Element type of the of the page content
	 * @param page page result containing page data of the element
	 * @param pageRequest page request requested in order to retrieve the page result
	 * @return Returns a page result of a given page and page request
	 */
	public static <T> Page<T> buildPageResult(Page<T> page, Pageable pageRequest)
	{
		if(page != null)
		{ // Page index is out of bounds
			if(page.getNumber() > page.getTotalPages())
				throw new NoSuchElementException("Page index exceeds number of total pages");
			// Extract a list of elements from the page
			List<T> elements = page.stream().collect(Collectors.toList());
			// Return a page implementation of the elements list, page request and total number of elements
			return new PageImpl<>(elements, pageRequest, page.getTotalElements());
		} // Page is null
		return null;
	}
}
