package local.coupersb.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import local.coupersb.validation.ValidEmail;

/**
 * A client of the coupons system
 * 
 * @author YAM
 *
 */

@MappedSuperclass
public class Client 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "password")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@ValidEmail
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	/**
	 * Creates an empty client
	 */
	public Client() 
	{
		
	}
	
	/**
	 * Crates a new client with a given password and email address
	 * 
	 * @param password password of the client
	 * @param email email of the client
	 */
	public Client(String password, String email)
	{
		this.password = password;
		setEmail(email);
	}
	
	/**
	 * Crates a new client with a given id, password and email address
	 * 
	 * @param id id of the client
	 * @param password password of the client
	 * @param email email of the client
	 */
	public Client(int id, String password, String email)
	{
		setId(id);
		this.password = password;
		setEmail(email);
	}
	
	/**
	 * Returns the clients id
	 * 
	 * @return the clients id
	 */
	public int getId() 
	{
		return id;
	}
	
	/**
	 * Sets the id of the client
	 * 
	 * @param id the id to set to the client
	 */
	public void setId(int id)
	{
		if(id > 0)
			this.id = id;
	}
	
	/**
	 * Returns the clients password
	 * 
	 * @return the clients password
	 */
	public String getPassword() 
	{
		return password;
	}
	
	/**
	 * Sets the password of the client
	 *  
	 * @param password the password to set to the client
	 */
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	/**
	 * Returns the clients email address
	 * 
	 * @return the clients email address
	 */
	public String getEmail() 
	{
		return email;
	}
	
	/**
	 * Sets the email address of the client
	 * 
	 * @param email the email address to set to the client
	 */
	public void setEmail(String email) 
	{
		if(email.contains("@") && email.contains("."))
			this.email = email;
	}

	@Override
	public String toString() 
	{
		return "Client [id=" + id + ", password=" + password + ", email=" + email + ", couponsList=" + "]";
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
		Client other = (Client) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}