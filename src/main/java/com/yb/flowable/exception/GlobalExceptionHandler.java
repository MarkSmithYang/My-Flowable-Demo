package com.yb.flowable.exception;

import com.yb.flowable.common.ResponseResult;
import org.apache.ibatis.exceptions.PersistenceException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;

/**
 * @author Duhuafei
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseResult accessDeniedExceptionHandler(AccessDeniedException e) {
		log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.FORBIDDEN.value())
				.message("权限不足");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseResult constraintViolationExceptionHandler(ConstraintViolationException e) {
		log.error(e.getMessage(), e);
		final String message = e.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.reduce((s, s2) -> s + ", " + s2)
				.orElse("");
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(message);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		FieldError fieldError = e.getBindingResult().getFieldError();
		String message;
		if (fieldError != null) {
			message = fieldError.getDefaultMessage();
		} else {
			message = e.getMessage();
		}
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(message);
	}

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ParameterErrorException.class)
    public ResponseResult parameterErrorExceptionHandler(ParameterErrorException e) {
        log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult methodArgumentTypeMismatchExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage());
    }

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class})
	public ResponseResult methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
		log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message("参数类型不匹配");
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage());
	}

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResult missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseResult httpMessageNotWritableExceptionHandler(HttpMessageNotWritableException e) {
		log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult exceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message("网络异常");
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
		return ResponseResult.statusCode(HttpStatus.BAD_REQUEST.value())
				.message("网络异常");
    }

}
