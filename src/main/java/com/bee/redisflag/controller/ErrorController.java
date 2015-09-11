package com.bee.redisflag.controller;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author weiwei 异常处理器，根据请求的类型（text\html、application\json），跳转至错误页或者返回json报文
 */
@Controller
public class ErrorController {

	// 页面请求异常，携带异常对象返回erro页面
	@RequestMapping(value = "/error", produces = { "text/html" })
	public Object error4Html(HttpServletRequest request) {
		HashMap<String, Object> data = getModel(request);
		return new ModelAndView("error").addAllObjects(data);
	}

	// ajax请求异常，返回json
	@ResponseBody
	@RequestMapping("/error")
	public Object error4Ajax(HttpServletRequest request) {
		return getModel(request);
	}

	private HashMap<String, Object> getModel(HttpServletRequest request) {
		HashMap<String, Object> data = new HashMap<>();
		Throwable ex = (Exception) request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
		if (ex == null) {
			ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		}
		Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
		data.put("status", status);
		data.put("timestamp", new Date());
		data.put("error", HttpStatus.valueOf(status).getReasonPhrase());
		String message = request.getAttribute("javax.servlet.error.message") == null ? null : request.getAttribute("javax.servlet.error.message").toString();
		if (StringUtils.isEmpty(message) && ex != null) {
			message = ex.getMessage();
		}
		data.put("message", message);
		return data;
	}

}
