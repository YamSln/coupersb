package local.coupersb.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import local.coupersb.exceptions.types.ApiRequestException;
import local.coupersb.exceptions.types.AuthenticationException;
import local.coupersb.exceptions.types.AvailabilityException;
import local.coupersb.exceptions.types.CouperException;
import local.coupersb.exceptions.types.DateException;
import local.coupersb.exceptions.types.DuplicationException;
import local.coupersb.exceptions.types.NoSuchElementException;
import local.coupersb.exceptions.types.OutdatedException;
import local.coupersb.exceptions.types.PriceException;
import local.coupersb.exceptions.types.UnauthorizedException;
import local.coupersb.exceptions.types.UpdateViolationException;

@ControllerAdvice
public class CouperExceptionHandler 
{
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleNotFound(CouperException e)
	{
		return this.handleCouperException(e, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicationException.class)
	public ResponseEntity<Object> handleNotAcceptable(CouperException e)
	{
		return this.handleCouperException(e, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = {AvailabilityException.class,
			UpdateViolationException.class,
			OutdatedException.class,
			DateException.class,
			PriceException.class})
	public ResponseEntity<Object> handleBadRequest(CouperException e)
	{
		return this.handleCouperException(e, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleUnAuthorized(CouperException e)
	{
		return this.handleCouperException(e, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleForbidden(CouperException e)
	{
		return this.handleCouperException(e, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBeanValidationException(MethodArgumentNotValidException e) 
	{
		return handleValidationException(e);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleBeanConstrainViolationException(ConstraintViolationException e)
	{
		return handleValidationException(e);
	}
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<Object> handleUnsupportedSortTypeException(PropertyReferenceException e)
	{
		return new ResponseEntity<>(
				new ApiRequestException("Unsupported sort type", HttpStatus.BAD_REQUEST, new Date()), 
				HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<Object> handleValidationException(Exception e)
	{
		List<String> errors = null; // Validation errors list
		Map<String, Object> responseBody = new HashMap<>(); // Create response body
		// Determine validation exception type
		if(e instanceof MethodArgumentNotValidException)
			errors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());
		else if(e instanceof ConstraintViolationException)
			errors = ((ConstraintViolationException) e).getConstraintViolations()
				.stream()
				.map(error -> error.getMessage())
				.collect(Collectors.toList());
		else
			throw new IllegalStateException("Specified exception is not an acceptable validation exception");
		// Build the response body
		responseBody.put("message", "Error/s during parameters validation");
		responseBody.put("errors", errors);
		responseBody.put("httpStatus", HttpStatus.BAD_REQUEST);
		responseBody.put("timeStamp", new Date());
		// Return response body with status 400
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<Object> handleCouperException(RuntimeException e, HttpStatus httpStatus)
	{
		ApiRequestException apiRequestException = 
				new ApiRequestException(e.getMessage(), httpStatus, new Date());
		
		return new ResponseEntity<>(apiRequestException, httpStatus);
	}
	
}