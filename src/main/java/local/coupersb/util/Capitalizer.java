package local.coupersb.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A utility class that features strings letter capitalizing methods
 * 
 * @author YAM
 *
 */
public class Capitalizer 
{
	/**
	 * Returns a given string with its first letter capitalized
	 * @param toCapitalize string to capitalize its first word
	 * @return a given string with its first letter capitalized
	 */
	public static String capitalize(String toCapitalize)
	{
		return capitalize(toCapitalize, false);
	}
	
	/**
	 * Returns a given string with its first letter capitalized, and the others uncapitalized
	 * @param toCapitalize string to capitalize its first word and uncapitalize the others
	 * @return a given string with its first letter capitalized, and the others uncapitalized
	 */
	public static String capitalizeFormat(String toCapitalize)
	{
		return capitalize(toCapitalize, true);
	}
	
	/**
	 * Returns a given string with the first letter of each character sequence divided by space ("word") capitalized
	 * @param toCapitalizeFully string to capitalize the first letter of each word in it
	 * @return a given string with the first letter of each character sequence divided by space ("word") capitalized
	 */
	public static String capitalizeFully(String toCapitalizeFully)
	{
		return streamAndFilter(toCapitalizeFully, false);
	}
	
	/**
	 * Returns a given string with the first letter of each character sequence divided by space ("word") capitalized, <br>
	 * And the other letters uncapitalized
	 * @param toCapitalizeFully string to capitalize the first letter of each word in it
	 * @return a given string with the first letter of each character sequence divided by space ("word") capitalized, <br>
	 * And the other letters uncapitalized
	 */
	public static String capitalizeFullyFormat(String toCapitalizeFully)
	{
		return streamAndFilter(toCapitalizeFully, true);
	}
	
	private static String capitalize(String toCapitalize, boolean format)
	{
		return (toCapitalize.substring(0, 1).toUpperCase() + 
				(format ? toCapitalize.substring(1).toLowerCase() : toCapitalize.substring(1))).trim();
	}
	
	private static String streamAndFilter(String toStreamAndFilter, boolean format)
	{
		return Stream.of(toStreamAndFilter.trim().split("\\s")) // Trim and split by space
				.filter(word -> word.length() > 0) // Filter multiple spaces
				.map(word -> format ? capitalizeFormat(word) : capitalize(word)) // Capitalize each word
				.collect(Collectors.joining(" ")); // Join all words back to one string
	}
	
}
