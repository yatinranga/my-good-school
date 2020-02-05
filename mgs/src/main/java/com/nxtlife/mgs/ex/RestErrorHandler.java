package com.nxtlife.mgs.ex;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler
{

  private static Logger logger = LoggerFactory.getLogger(RestErrorHandler.class);

  @ExceptionHandler({ AuthException.class })
  public ResponseEntity<Object> handleAuthenticationError(Exception exception, WebRequest request)
  {
    logger.warn("Auth error:" + exception.getMessage());
    final ApiError apierror = message(HttpStatus.UNAUTHORIZED, exception);
    return handleExceptionInternal(exception, apierror, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler({ AccessDeniedException.class })
  public ResponseEntity<Object> handleAuthorisationError(Exception exception, WebRequest request)
  {
    logger.warn("Authorisation error:" + exception.getMessage());
    final ApiError apierror = message(HttpStatus.FORBIDDEN, exception);
    return handleExceptionInternal(exception, apierror, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler({ NotFoundException.class })
  public ResponseEntity<Object> handleMissing(NotFoundException exception, WebRequest request)
  {
    logger.error("NOT FOUND:" + exception.getMessage());
    final ApiError apierror = message(HttpStatus.NOT_FOUND, exception);
    return handleExceptionInternal(exception, apierror, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler({ ValidationException.class })
  public ResponseEntity<Object> handleLogical(ValidationException exception, WebRequest request)
  {
    logger.error("ERROR:" + exception.getMessage());
    final ApiError apierror = message(HttpStatus.BAD_REQUEST, exception);
    return handleExceptionInternal(exception, apierror, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleOther(RuntimeException exception, WebRequest request)
  {
    logger.error("ERROR:", exception);
    final ApiError apierror = message(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    return handleExceptionInternal(exception, apierror, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  private ApiError message(final HttpStatus httpStatus, final Exception ex)
  {
    final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
    return new ApiError(httpStatus.value(), message, message);
  }
}
