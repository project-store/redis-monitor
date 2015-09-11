package com.bee.redisflag.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author weiwei
 * 
 * 业务异常，用于捕获异常时做特殊处理
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -5739943861988445443L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}
}
