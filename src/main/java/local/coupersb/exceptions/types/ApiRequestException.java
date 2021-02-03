package local.coupersb.exceptions.types;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ApiRequestException 
{
	private final String message;
	private final HttpStatus httpStatus;
	private final Date timeStamp;
	
	public ApiRequestException(String message, HttpStatus httpStatus, Date timeStamp) 
	{
		this.message = message;
		this.httpStatus = httpStatus;
		this.timeStamp = timeStamp;
	}

	public String getMessage() 
	{
		return message;
	}

	public HttpStatus getHttpStatus() 
	{
		return httpStatus;
	}

	public Date getTimeStamp() 
	{
		return timeStamp;
	}
}